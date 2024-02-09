package ti4.model

import ti4.model.Unit.Ship

import scala.collection.immutable.Map as SMap
import scala.collection.mutable.ArrayBuffer

object GameBoardNotes {

//  opaque type TileId     = String
//  private type Weight     = Int
//  private type Neighbours = SMap[TileId, Weight]
//  opaque type HexTile     = (TileId, Neighbours)
//  opaque type Grid        = SMap[TileId, Neighbours]
//  opaque type Tiles       = SMap[TileId, GameBoardTile.Tile]

  trait Feature
  final case class Path(targetTile: TileId, subsequentTiles: List[TileId])
  final case class ShipPath(ship: Ship, path: Path)

//  ArrayBuffer
//  Vector

  // Where to verify that movement is valid?
  def move(targetTile: TileId, ships: List[Ship]): SystemTile = ???

  /**
   * Returns the ships that can reach the target tile. The board will store ship's id and the tile it is on. So we know how far it needs to move.
   * @param targetTile destination tile
   * @param ships should know how much movement they have
   * @param features things that might change the movement of the ships
   */
  def shipsInRange(targetTile: TileId, ships: List[Ship], features: List[Feature]): List[ShipPath] = List.empty

  // tech points in the different types are the way to fulfill tech prerequisites

  // will need this for retreats
  def adjacentTiles(tile: TileId): List[SystemTile] = List.empty

}
