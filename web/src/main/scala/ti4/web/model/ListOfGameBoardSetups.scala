package ti4.web.model

case object ListOfGameBoardSetups extends GameView {

  enum Transition extends GameTransition {
    case ChooseSetup
  }

}
