package ti4

import cats.syntax.all._
import tyrian.*
import tyrian.Html.*
import tyrian.SVG.*
import tyrian.CSS
import org.scalajs.dom
import java.util.UUID
import ti4.model.{ Map => TMap, Unit => TUnit }

final case class Model(map: TMap)

object Model {

  def init(): Model = {
    Model(TMap(Array(
      Array(None, None, None, "1".some, "2".some, None, None),
      Array("3".some, "4".some, "5".some, "6".some, "7".some, "8".some, None),
      Array("9".some, "10".some, "11".some, "12".some, "13".some, "14".some, "15".some)
    )))
  }
}

enum Msg {
  case Noop
}

object Main extends TyrianApp[Msg, Model] {

  object Colors {
    val gray = "gray"
  }

  def main(args: Array[String]): Unit = {
    launch("app")
  }

  override def init(flags: Map[String, String]): (Model, Cmd[cats.effect.IO, Msg]) = {
    (Model.init(), Cmd.None)
  }

  override def router: Location => Msg = {
    case _ => Msg.Noop
  }

  override def subscriptions(model: Model): Sub[cats.effect.IO, Msg] = Sub.None

  override def update(model: Model): Msg => (Model, Cmd[cats.effect.IO, Msg]) = {
    case Msg.Noop => (model, Cmd.None)
  }

  private val polygonSize = 50

  private def renderPolygon(x: Int, y: Int, isBlank: Boolean): Html[Msg] = {
    val size = polygonSize
    polygon(
      points := s"${x},${y} ${x},${y - size / 2} ${x + size},${y - size} ${x + size * 2},${y - size / 2} ${x + size * 2},${y + size / 2} ${x + size},${y + size} ${x},${y + size / 2}",
      stroke := "black",
      fill   := (if (isBlank) "none" else Colors.gray),
    )
  }

  private def renderMap(model: Model): Html[Msg] = {
    val baseX = 100
    val baseY = 100
    val polys = model.map.grid.zipWithIndex.map { case (row, columnIndex) =>
      val baseXOffset = baseX + (if (columnIndex % 2 == 0) 0 else polygonSize)
      val baseYOffset = (baseY + polygonSize * columnIndex * 1.5).toInt
      row.zipWithIndex.map {
        case (Some(tile), rowIndex) =>
          Option(renderPolygon(
            (baseXOffset + polygonSize * rowIndex * 2).toInt,
            baseYOffset,
            false,
          ))
        case _ => Option.empty
      }.toList.flatten
    }.toList.flatten
    svg(width := "2000", height := "2000")(
      polys,
    )
  }

  override def view(model: Model): Html[Msg] = {
    div(height := "100%", width := "100%")(
      h1("hello world"),
      div(width := "100%", height := "100%")(
        renderMap(model),
      )
    )
  }
}
