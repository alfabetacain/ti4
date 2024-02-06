package ti4.combat

import cats.data.OptionT
import cats.effect.IO
import cats.effect.std.Random
import cats.effect.std.SecureRandom
import cats.syntax.all._
import ti4.model.FactionId
import ti4.model.Unit.Ship
import ti4.model.Faction

object Combat {

  case class Fleet(owner: Faction, ships: List[Ship])

  sealed trait CombatOutcome

  object CombatOutcome {
    case object Draw                                                  extends CombatOutcome
    final case class Won(winner: Faction, remainingShips: List[Ship]) extends CombatOutcome

    val draw: CombatOutcome                                             = Draw
    def won(winner: Faction, remainingShips: List[Ship]): CombatOutcome = Won(winner, remainingShips)
  }

  final case class SpaceCombatRoundResult(attacker: Fleet, defender: Fleet)

  def resolveSpaceCombatRound(attacker: Fleet, defender: Fleet): IO[SpaceCombatRoundResult] = {
    for {
      newAttacker <- rollForHits(attacker).flatMap(assignHits(_, defender))
      newDefender <- rollForHits(defender).flatMap(assignHits(_, attacker))
    } yield SpaceCombatRoundResult(newAttacker, newDefender)
  }

  def isDeclaringRetreat(faction: Faction): IO[Boolean] = {
    false.pure[IO]
  }

  def antiFighterBarrage(attacker: Fleet, defender: Fleet): IO[SpaceCombatRoundResult] = {
    SpaceCombatRoundResult(attacker, defender).pure[IO]
  }

  def getNumberOfDice(owner: FactionId, ship: Ship): Int = {
    1
  }

  def rollDice(threshold: Int): IO[Boolean] = {
    SecureRandom.javaSecuritySecureRandom[IO].flatMap { random =>
      random.betweenInt(1, 11).map { _ >= threshold }
    }
  }

  def rollForHits(fleet: Fleet): IO[Int] = {
    fleet.ships.traverse { ship =>
      val stats = fleet.owner.getSpaceCombatStats(ship)
      val dice  = List.fill(stats.numberOfDice)(stats.threshold)
      dice.traverse { rollDice }.map(_.count(identity))
    }.map(_.sum)
  }

  def assignHits(hits: Int, fleet: Fleet): IO[Fleet] = {
    ???
  }

  def retreat(fleet: Fleet): IO[Unit] = {
    // probably needs access to the game board
    // side effect changing the board?
    ???
  }

  def spaceCombat(attacker: Fleet, defender: Fleet): IO[CombatOutcome] = {

    val bothDestroyed =
      OptionT.when[IO, CombatOutcome](attacker.ships.isEmpty && defender.ships.isEmpty)(CombatOutcome.draw)
    val attackerDestroyed =
      OptionT.when[IO, CombatOutcome](attacker.ships.isEmpty)(CombatOutcome.won(defender.owner, defender.ships))
    val defenderDestroyed =
      OptionT.when[IO, CombatOutcome](defender.ships.isEmpty)(CombatOutcome.won(attacker.owner, attacker.ships))

    val defenderRetreated =
      OptionT.liftF(isDeclaringRetreat(defender.owner)).flatMap(isRetreating =>
        OptionT.whenF(isRetreating)(retreat(defender).as(CombatOutcome.won(attacker.owner, attacker.ships)))
      )

    val attackerRetreated =
      OptionT.liftF(isDeclaringRetreat(attacker.owner)).flatMap(isRetreating =>
        OptionT.whenF(isRetreating)(retreat(attacker).as(CombatOutcome.won(defender.owner, defender.ships)))
      )

    val continueCombat =
      resolveSpaceCombatRound(attacker, defender).flatMap(result => spaceCombat(result.attacker, result.defender))

    bothDestroyed
      .orElse(attackerDestroyed)
      .orElse(defenderDestroyed)
      .orElse(defenderRetreated)
      .orElse(attackerRetreated)
      .getOrElseF(continueCombat)
  }

  def resolveSpaceCombat(attacker: Fleet, defender: Fleet): IO[CombatOutcome] = {
    antiFighterBarrage(attacker, defender).flatMap { case SpaceCombatRoundResult(attacker, defender) =>
      spaceCombat(attacker, defender)
    }
  }
}
