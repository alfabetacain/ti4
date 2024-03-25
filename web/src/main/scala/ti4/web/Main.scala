package ti4.web

import monocle.syntax.all.*
import cats.syntax.all.*
import tyrian.*
import tyrian.Html.*
import tyrian.SVG.*
import tyrian.CSS
import org.scalajs.dom

import java.util.UUID
import ti4.model.{ Faction, GameBoard, SystemTile, TileId, Unit as TUnit }
import ti4.Movement
import cats.effect.IO
import ti4.web.model.GameView
import ti4.web.model.GameTransition
import ti4.web.model.ListOfGameBoardSetups
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("TyrianApp")
object Main extends TyrianIOApp[GameTransition, GameView] {

  def main(args: Array[String]): Unit = launch("app")

  override def init(flags: Map[String, String]): (GameView, Cmd[IO, GameTransition]) = {
    (ListOfGameBoardSetups, Cmd.None)
  }

  override def router: Location => GameTransition = {
    case _ => GameTransition.Noop
  }

  override def subscriptions(gameView: GameView): Sub[IO, GameTransition] = Sub.None

  override def update(gameView: GameView): GameTransition => (GameView, Cmd[IO, GameTransition]) = {
    case GameTransition.Noop => (gameView, Cmd.None)
  }

  val myStyles = style(CSS.`font-family`("Arial, Helvetica, sans-serif"))
  val topLine  = p(b(text("This is some HTML in bold.")))

  override def view(gameView: GameView): Html[GameTransition] = {
    div(id := "my-container")(
      div(myStyles)(
        topLine,
        p("Hello, world!"),
        button(onClick(ListOfGameBoardSetups.Transition.ChooseSetup))("Say hello!")
      )
    )
  }
}
