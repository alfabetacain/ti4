package ti4.model

import ti4.collection.{ Hex, HexGrid }

/**
 * Either in gameboard or here, it should return the places where it is now possible to place systems.
 * So every time a system is placed it should return the remaining available spots for the next system.
 * That way anomalies and other aspects can be considered too in the placement part of the setup.
 */
final case class GameBoardSetup(id: String, name: String, grid: HexGrid[Tile], homePositions: Set[Hex])
