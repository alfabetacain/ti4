package ti4.model

import cats.syntax.all._
import tyrian.*
import tyrian.Html.*
import tyrian.SVG.*
import tyrian.CSS
import org.scalajs.dom
import java.util.UUID
import scala.collection.immutable.{ Map => SMap }

type TileId = String

final case class Map(
                      grid: Array[Array[Option[TileId]]],
                      tiles: SMap[TileId, Tile],
                      factions: SMap[FactionId, Faction],
) {
  def getTileIds: List[TileId]                   = tiles.keys.toList
  def neighbours(tileId: TileId): List[TileId]   = List.empty
  def getTile(id: TileId): Option[Tile]          = tiles.get(id)
  def getFaction(id: FactionId): Option[Faction] = factions.get(id)
}

object Map {

  def init(): Map = {
    Map(Array.empty, SMap.empty, SMap.empty)
  }
}

sealed trait Unit {
  def owningFaction: FactionId
}

object Unit {
  sealed trait Ship extends Unit

  object Ship {
    final case class Destroyer(owningFaction: FactionId) extends Ship
  }

  final case class GroundForce(owningFaction: FactionId) extends Unit
}

final case class Planet(name: String, units: List[Unit.GroundForce])

enum Anomaly {
  case GravityRift, Asteroids
}

final case class Tile(
    id: TileId = UUID.randomUUID().toString(),
    units: List[Unit] = List.empty,
    planets: List[Planet] = List.empty,
    anomaly: Option[Anomaly] = Option.empty,
)
