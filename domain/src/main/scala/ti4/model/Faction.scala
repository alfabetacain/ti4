package ti4.model

import cats.syntax.all._
import ti4.model.{ Unit => TUnit }
import ti4.model.Unit.Ship
import cats.effect.IO
import ti4.combat.Combat.Fleet

type FactionId = String

trait Faction {
  def id: FactionId
  def movement(unit: TUnit): Int
  def getSpaceCombatStats(ship: Ship): TUnit.Ship.SpaceCombatStats
  def assignHits(numberOfHits: Int, ships: List[Ship]): IO[List[Ship]]
  def isRetreating(tile: TileId, attacker: Fleet, ownShips: List[Ship]): IO[Boolean]
  def chooseStrategyCard(cards: List[(StrategyCard, Int)]): IO[StrategyCard]
}

object Faction {

  def generic(givenId: FactionId): Faction = {
    new Faction {
      override val id: FactionId = givenId
      override def movement(unit: TUnit): Int = {
        unit match {
          case TUnit.Ship.Destroyer(`id`) => 1
          case TUnit.GroundForce(`id`)    => 0
          case _                          => 0
        }
      }

      override def getSpaceCombatStats(ship: TUnit.Ship): TUnit.Ship.SpaceCombatStats = {
        ship match {
          case _: TUnit.Ship.Destroyer =>
            TUnit.Ship.SpaceCombatStats(
              Option(TUnit.Ship.AntiFighterBarrageStats(numberOfDice = 2, threshold = 9)),
              numberOfDice = 1,
              threshold = 9
            )
        }
      }
      override def assignHits(numberOfHits: Int, ships: List[TUnit.Ship]): IO[List[TUnit.Ship]] = {
        ???
      }
      override def isRetreating(tile: TileId, attacker: Fleet, ownShips: List[TUnit.Ship]): IO[Boolean] = {
        false.pure[IO]
      }

      override def chooseStrategyCard(cards: List[(StrategyCard, Int)]): IO[StrategyCard] = {
        cards.head._1.pure[IO]
      }
    }
  }
}
