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

final case class GameBoard(
    grid: Array[Array[Option[TileId]]],
    tiles: SMap[TileId, SystemTile],
    factions: SMap[FactionId, Faction],
) {
  def getTileIds: List[TileId]                   = tiles.keys.toList
  def neighbours(tileId: TileId): List[TileId]   = List.empty
  def getTile(id: TileId): Option[SystemTile]    = tiles.get(id)
  def getFaction(id: FactionId): Option[Faction] = factions.get(id)
}

object GameBoard {

  def init(): GameBoard = {
    GameBoard(Array.empty, SMap.empty, SMap.empty)
  }
}

enum Structure {
  case PlanetaryDefenseSystem, SpaceDock
}

final case class Planet(name: String, units: List[Unit.GroundForce], structures: List[Structure])

enum Anomaly {
  case GravityRift, Asteroids
}

final case class SystemTile(
    id: TileId = UUID.randomUUID().toString(),
    units: List[Unit] = List.empty,
    planets: List[Planet] = List.empty,
    anomaly: Option[Anomaly] = Option.empty,
) {

  def isOwnedBy(faction: FactionId): Boolean = {
    // TODO
    false
  }
}
