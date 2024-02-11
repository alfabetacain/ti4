package ti4.model

import ti4.model.Unit.Ship

trait Tile {
  def id: TileId
  def ships: List[Ship] = List.empty
}

object Tile {

  val Empty: Tile = new Tile {
    override def id: TileId = ""
  }

  trait Hyperlane extends Tile

  case class System(
      id: TileId,
      planet: List[Planet],
      anomalies: List[Anomaly],
      override val ships: List[Ship],
      wormholes: List[Wormhole]
  ) extends Tile

}
