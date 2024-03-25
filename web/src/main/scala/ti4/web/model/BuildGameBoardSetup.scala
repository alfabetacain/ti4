package ti4.web.model

case object BuildGameBoardSetup extends GameView {

  enum Transition extends GameTransition {
    case AddTile
  }

}
