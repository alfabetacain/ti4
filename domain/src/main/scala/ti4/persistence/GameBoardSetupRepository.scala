package ti4.persistence

import ti4.identifiers.GameBoardSetupId

object GameBoardSetupRepository {

  def getGameBoardSetup(id: GameBoardSetupId) = ???

  //  def threePlayerSetup(): GameBoardSetup = {
  //    val grid = HexGrid(3, 3)
  //    val homePositions = Set(grid(0, 0), grid(2, 0), grid(1, 2))
  //    GameBoardSetup("threePlayerSetup", "Three Player Setup", grid, homePositions)
  //  }

  // this should be how the game board is created
  // next is to figure out out to show the different options of board layouts
  // as they are limited. Should it be in an enum? Somewhere else? How to make it dynamic so it can something
  // we can read in and potentially other people can alter later?

  // make an API that separates the concern of the API. Save them to a database (file for now or whatever)
  // make a get API for now only. Should be returned by their id/key.
  
}
