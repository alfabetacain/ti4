package ti4.model

import ti4.collection.{Hex, HexGrid}

final case class GameBoardSetup(id: String, name: String, grid: HexGrid[Tile], homePositions: Set[Hex])
