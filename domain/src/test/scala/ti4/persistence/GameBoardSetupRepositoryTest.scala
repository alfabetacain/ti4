package ti4.persistence

import ti4.collection.{Hex, HexGrid}
import ti4.identifiers.GameBoardSetupId
import ti4.model.{GameBoardSetup, Tile}

class GameBoardSetupRepositoryTest extends munit.FunSuite {

  test("retrieving a non-existent setup should fail") {
    val id = makeId("ti4-game-board-setup-1p-1231245")
    assertEquals(GameBoardSetupRepository.getGameBoardSetup(id), None)
  }

  test("retrieving a valid setup should be successful") {
    val topLeftCorner = Set(Hex(-3, 1), Hex(-3, 0), Hex(-2, -1))
    val topRightCorner = Set(Hex(2, -3), Hex(3, -3), Hex(3, -2))
    val bottom = Set(Hex(-1, 3), Hex(0, 3), Hex(1, 2))
    val extraTiles = (topLeftCorner ++ topRightCorner ++ bottom).map(hex => hex -> Tile.Empty).toSeq
    val id = makeId("ti4-game-board-setup-3p-0")
    val expected = GameBoardSetup(
      id = id,
      name = "Three-Player Setup",
      grid = HexGrid.generate(2, _ => Tile.Empty).withHexes(extraTiles:_*),
      homePositions = Set(Hex(-3, 0), Hex(3, -3), Hex(0, 3))
    )
    val result = GameBoardSetupRepository.getGameBoardSetup(id)

    assertEquals(result, Some(expected))
  }

  private def makeId(id: String) = GameBoardSetupId(id).toOption.getOrElse(fail(s"given invalid game board setup id '$id'"))

}
