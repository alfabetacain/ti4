package ti4.model

import cats.syntax.all._
import tyrian.*
import tyrian.Html.*
import tyrian.SVG.*
import tyrian.CSS
import org.scalajs.dom
import java.util.UUID

type TileId = String

final case class Map(grid: Array[Array[Option[TileId]]]) {
  def tiles: List[TileId]                      = List.empty
  def neighbours(tileId: TileId): List[TileId] = List.empty
}

object Map {

  def init(): Map = {
    Map(Array.empty)
  }
}

type FactionId

sealed trait Unit {
  def owningFaction: FactionId
}

object Unit {
  final case class Ship(owningFaction: FactionId)        extends Unit
  final case class GroundForce(owningFaction: FactionId) extends Unit
}

final case class Planet(name: String, units: List[Unit.GroundForce])

enum Anomaly {
  case GravityRift, Asteroids
}

final case class Tile(
    id: TileId = UUID.randomUUID().toString(),
    units: List[Unit],
    planets: List[Planet],
    anomaly: Option[Anomaly],
)
