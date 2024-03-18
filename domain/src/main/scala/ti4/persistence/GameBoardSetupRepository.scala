package ti4.persistence

import ti4.collection.{ Hex, HexGrid }
import ti4.identifiers.GameBoardSetupId
import ti4.model.{ GameBoardSetup, Tile }

/**
 * Represents persistence of the game board setup. Made it like this so it can be extended in the future, in case we'd
 * want to make possible to add game board setups and persist them in a database instead of in code.
 */
object GameBoardSetupRepository {

  private val setups = Map(
    threePlayerSetup(),
    fourPlayerSetup()
  )

//  "ti4-game-board-setup-5p-0"
//  "ti4-game-board-setup-5p-1"
//  "ti4-game-board-setup-6p-0"
//  "ti4-game-board-setup-6p-1"
//  "ti4-game-board-setup-7p-0"
//  "ti4-game-board-setup-7p-1"
//  "ti4-game-board-setup-8p-0"
//  "ti4-game-board-setup-8p-1"

  def getGameBoardSetup(id: GameBoardSetupId): Option[GameBoardSetup] = setups.get(id)

  private def threePlayerSetup(): (GameBoardSetupId, GameBoardSetup) = {
    val extraTiles =
      Seq(Hex(-3, 1), Hex(-3, 0), Hex(-2, -1), Hex(2, -3), Hex(3, -3), Hex(3, -2), Hex(-1, 3), Hex(0, 3), Hex(1, 2))
    val grid = HexGrid.generate(2, _ => Tile.Empty)
      .withHexes(extraTiles.map(_ -> Tile.Empty): _*)
    val id = GameBoardSetupId("ti4-game-board-setup-3p-0").toOption.get

    id -> GameBoardSetup(id, "Three-Player Setup", grid, Set(Hex(-3, 0), Hex(3, -3), Hex(0, 3)))
  }

  private def fourPlayerSetup(): (GameBoardSetupId, GameBoardSetup) = {
    val id   = GameBoardSetupId("ti4-game-board-setup-4p-0").toOption.get
    val grid = HexGrid.generate(3, _ => Tile.Empty)

    id -> GameBoardSetup(id, "Four-Player Setup", grid, Set(Hex(-1, -2), Hex(3, -2), Hex(1, 2), Hex(-3, 2)))
  }

  // TODO: Complete the five player setup, currently a copy of the fourPlayerSetup
  private def fivePlayerSetup(): (GameBoardSetupId, GameBoardSetup) = {
    val id   = GameBoardSetupId("ti4-game-board-setup-5p-0").toOption.get
    val grid = HexGrid.generate(3, _ => Tile.Empty)

    id -> GameBoardSetup(id, "Five-Player Setup", grid, Set(Hex(-1, -2), Hex(3, -2), Hex(1, 2), Hex(-3, 2)))
  }

}
