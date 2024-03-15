package ti4.identifiers

import SharedConstraints.{ GameNameRegex, PlayerRegex }

opaque type GameBoardSetupId = String

object GameBoardSetupId {

  private val gameBoardSetupIdRegex = s"$GameNameRegex-game-board-setup-$PlayerRegex-(\\d+)$$"

  def apply(id: String): Either[Throwable, GameBoardSetupId] = {
    Either.cond(
      test = id.matches(gameBoardSetupIdRegex),
      right = id,
      left = new IllegalArgumentException(s"$id is not a valid game bord setup id")
    )
  }

  implicit class GameBoardSetupIdExtension(id: GameBoardSetupId) {

    def value: String = id

  }
}
