package ti4.model

import scala.collection.immutable.Map as SMap
import scala.collection.mutable.ArrayBuffer

object GameBoard {

  private type TileId = String
  private type Weight = Int
  private type Neighbours = SMap[TileId, Weight]
  opaque type HexTile = (TileId, Neighbours)
  opaque type Grid = SMap[TileId, Neighbours]
  opaque type Tiles = SMap[TileId, GameBoardTile.Tile]
  // option to is to ensure that is not empty as not all tiles will be set to make a valid hex board
  opaque type Layout = Array[Array[GameBoardTile.Tile]]

  ArrayBuffer
  Vector

  // what are my use cases?

  // Think of them and write them out
  // tiles can be everywhere on the board, a tile id does not tell us anything of the layout

  //

}
