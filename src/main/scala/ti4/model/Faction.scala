package ti4.model

import ti4.model.{ Unit => TUnit }
import ti4.model.Unit.Ship

type FactionId = String

trait Faction {
  def id: FactionId
  def movement(unit: TUnit): Int
  def getSpaceCombatStats(ship: Ship): TUnit.Ship.SpaceCombatStats
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
    }
  }
}
