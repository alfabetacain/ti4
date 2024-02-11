package ti4.combat

import cats.data.OptionT
import cats.effect.IO
import cats.effect.std.Random
import cats.effect.std.SecureRandom
import cats.syntax.all._
import ti4.model.Faction
import ti4.model.FactionId
import ti4.model.TileId
import ti4.model.Unit.Ship
import ti4.model.Unit.Ship.SpaceCombatStats
import ti4.model.GameBoardNotes
import ti4.model.SystemTile
import ti4.model.Tile

trait Combat {
  def resolveSpaceCombat(tile: Tile, attacker: Faction, defender: Faction): IO[Combat.CombatOutcome]
}

object Combat {

  type Hits = Int

  case class Fleet(owner: Faction, ships: List[Ship])

  sealed trait CombatOutcome

  object CombatOutcome {
    case object Draw                                                  extends CombatOutcome
    final case class Won(winner: Faction, remainingShips: List[Ship]) extends CombatOutcome

    val draw: CombatOutcome                                             = Draw
    def won(winner: Faction, remainingShips: List[Ship]): CombatOutcome = Won(winner, remainingShips)
  }

  final case class SpaceCombatRoundResult(attacker: Fleet, defender: Fleet)

  def resolveSpaceCombatRound(random: Random[IO], attacker: Fleet, defender: Fleet): IO[SpaceCombatRoundResult] = {
    for {
      remainingAttackers <- rollCombat(random, defender).flatMap(attacker.owner.assignHits(_, attacker.ships))
      remainingDefenders <- rollCombat(random, attacker).flatMap(defender.owner.assignHits(_, defender.ships))
    } yield SpaceCombatRoundResult(
      attacker.copy(ships = remainingAttackers),
      attacker.copy(ships = remainingDefenders),
    )
  }

  private def rollAntiFighterBarrage(random: Random[IO], fleet: Fleet): IO[Hits] = {
    fleet.ships.map { fleet.owner.getSpaceCombatStats }
      .collect { case SpaceCombatStats(Some(antiFighterBarrage), _, _) => antiFighterBarrage }
      .traverse { barrageStats =>
        rollDice(random, barrageStats.numberOfDice, barrageStats.threshold)
      }.map(_.sum)
  }

  def antiFighterBarrage(random: Random[IO], attacker: Fleet, defender: Fleet): IO[SpaceCombatRoundResult] = {
    for {
      remainingAttackers <-
        rollAntiFighterBarrage(random, defender).flatMap(attacker.owner.assignHits(_, attacker.ships))
      remainingDefenders <-
        rollAntiFighterBarrage(random, attacker).flatMap(defender.owner.assignHits(_, defender.ships))
    } yield SpaceCombatRoundResult(
      attacker.copy(ships = remainingAttackers),
      defender.copy(ships = remainingDefenders),
    )
  }

  def rollDice(random: Random[IO], numberOfDice: Int, threshold: Int): IO[Int] = {
    // TODO instanciate once
    List.fill(numberOfDice)(threshold).traverse { threshold =>
      random.betweenInt(1, 11).map { _ >= threshold }
    }.map(_.count(identity))
  }

  def rollCombat(random: Random[IO], fleet: Fleet): IO[Int] = {
    fleet.ships.traverse { ship =>
      val stats = fleet.owner.getSpaceCombatStats(ship)
      rollDice(random, stats.numberOfDice, stats.threshold)
    }.map(_.sum)
  }

  private def canRetreat(tile: TileId, fleet: Fleet): IO[Boolean] = {
    GameBoardNotes.adjacentTiles(tile).exists(_.isOwnedBy(fleet.owner.id)).pure[IO]
  }

  def retreat(fleet: Fleet): IO[Unit] = {
    // probably needs access to the game board
    // side effect changing the board?
    // should probably be deferred to player
    // though not if it is impossible
    ???
  }

  enum DeclaredRetreat {
    case Attacker, Defender
  }

  private def determineRetreats(tile: TileId, attacker: Fleet, defender: Fleet): IO[Option[DeclaredRetreat]] = {
    val defenderRetreats =
      OptionT(
        defender.owner.isRetreating(tile, attacker, defender.ships).map(Option.when(_)(DeclaredRetreat.Defender))
      )
    val attackerRetreats =
      OptionT(
        attacker.owner.isRetreating(tile, defender, attacker.ships).map(Option.when(_)(DeclaredRetreat.Attacker))
      )
    defenderRetreats.orElse(attackerRetreats).value
  }

  def spaceCombat(
      random: Random[IO],
      tile: TileId,
      attacker: Fleet,
      defender: Fleet,
      declaredRetreat: Option[DeclaredRetreat] = None,
  ): IO[CombatOutcome] = {

    val bothDestroyed =
      OptionT.when[IO, CombatOutcome](attacker.ships.isEmpty && defender.ships.isEmpty)(CombatOutcome.draw)
    val attackerDestroyed =
      OptionT.when[IO, CombatOutcome](attacker.ships.isEmpty)(CombatOutcome.won(defender.owner, defender.ships))
    val defenderDestroyed =
      OptionT.when[IO, CombatOutcome](defender.ships.isEmpty)(CombatOutcome.won(attacker.owner, attacker.ships))

    val defenderRetreated =
      OptionT.whenF(
        declaredRetreat.contains(DeclaredRetreat.Defender)
      )(retreat(defender).as(CombatOutcome.won(attacker.owner, attacker.ships)))

    val attackerRetreated =
      OptionT.whenF(
        declaredRetreat.contains(DeclaredRetreat.Attacker)
      )(retreat(attacker).as(CombatOutcome.won(defender.owner, defender.ships)))

    val wantsToRetreat =
      determineRetreats(tile, attacker, defender)

    val continueCombat =
      for {
        declaredRetreat <- wantsToRetreat
        result <- resolveSpaceCombatRound(random, attacker, defender).flatMap(result =>
          spaceCombat(random, tile, result.attacker, result.defender, declaredRetreat)
        )
      } yield result

    bothDestroyed
      .orElse(attackerDestroyed)
      .orElse(defenderDestroyed)
      .orElse(defenderRetreated)
      .orElse(attackerRetreated)
      .getOrElseF(continueCombat)
  }

  def make(random: Random[IO]): Combat = {
    new Combat {
      override def resolveSpaceCombat(tile: Tile, attacker: Faction, defender: Faction): IO[CombatOutcome] = {
        val attackerFleet = Fleet(attacker, tile.ships.filter(_.owningFaction == attacker.id))
        val defenderFleet = Fleet(defender, tile.ships.filter(_.owningFaction == defender.id))
        antiFighterBarrage(random, attackerFleet, defenderFleet).flatMap {
          case SpaceCombatRoundResult(attacker, defender) =>
            spaceCombat(random, tile.id, attacker, defender)
        }
      }
    }
  }
}
