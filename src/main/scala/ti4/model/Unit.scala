package ti4.model

sealed trait Unit {
  def owningFaction: FactionId
}

object Unit {
  sealed trait Ship extends Unit

  object Ship {
    final case class AntiFighterBarrageStats(numberOfDice: Int, threshold: Int)

    final case class SpaceCombatStats(
        antiFighterBarrage: Option[AntiFighterBarrageStats],
        numberOfDice: Int,
        threshold: Int
    )
    final case class Destroyer(owningFaction: FactionId) extends Ship
  }

  final case class GroundForce(owningFaction: FactionId) extends Unit
}
