package ti4.model

import ti4.model.Unit.Ship

object GameBoardTile {

  enum Wormhole {
    case Alpha, Beta, Delta, Gamma
  }

  trait Tile

  trait System extends Tile

  trait Hyperlane extends Tile

  // should the tile know of its adjacent tiles?
  // how should the board be represented?
  // going the direction of each tile knowing of adjacent tiles can potentially be used in determining the game board too
  // what about wormholes? How are they represented in a tile? They are part in determining adjacency.
  // Some kind of graph structure probably makes the most sense
  case class SystemTile(id: TileId,
                        planet: List[Planet],
                        anomalies: List[Anomaly],
                        ships: List[Ship],
                        wormholes: List[Wormhole]) extends System

  // list is not a good representation as we do not know anything about adjacency aka what is placed where.
  // A mix of hexagonal and weight graph is probably the best option as data structure
  // Is it necessary to know the position of each tile on the board? Maybe a layout that the graph is in could help?
  case class GameBoard(tiles: List[Tile])

}
