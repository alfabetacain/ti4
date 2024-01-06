package ti4.model

import ti4.model.{ Unit => TUnit }

type FactionId = String

trait Faction {
  def id: FactionId
  def movement(unit: TUnit): Int
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
    }
  }
}
