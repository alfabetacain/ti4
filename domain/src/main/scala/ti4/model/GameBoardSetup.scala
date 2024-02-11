package ti4.model

import ti4.collection.{ Hex, HexGrid }

/**
 * Either in game board or here, it should return the places where it is now possible to place systems.
 * So every time a system is placed it should return the remaining available spots for the next system.
 * That way anomalies and other aspects can be considered too in the placement part of the setup.
 */
final case class GameBoardSetup(id: String, name: String, grid: HexGrid[Tile], homePositions: Set[Hex])

object GameBoardSetup {

//  def threePlayerSetup(): GameBoardSetup = {
//    val grid = HexGrid(3, 3)
//    val homePositions = Set(grid(0, 0), grid(2, 0), grid(1, 2))
//    GameBoardSetup("threePlayerSetup", "Three Player Setup", grid, homePositions)
//  }

}
