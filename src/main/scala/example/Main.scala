package example

import tyrian.*
import tyrian.Html.*
import tyrian.SVG.*
import tyrian.CSS
import org.scalajs.dom
import java.util.UUID

final case class Model(map: Model.Map)

object Model {

  type TileId = String

  final case class Map(grid: Array[Array[TileId]]) {
    def tiles: List[TileId]                      = List.empty
    def neighbours(tileId: TileId): List[TileId] = List.empty
  }

  object Map {

    def init(): Map = {
      Map(Array.empty)
    }
  }

  final case class Tile(id: TileId = UUID.randomUUID().toString(), neighbours: List[TileId])

  def init(): Model = {
    Model(Map(Array(
      Array("", "", "", "1", "2", "", ""),
      Array("3", "4", "5", "6", "7", "8", ""),
      Array("9", "10", "11", "12", "13", "14", "15")
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
      fill   := (if (isBlank) "none" else "yellow"),
    )
  }

  private def renderMap(model: Model): Html[Msg] = {
    val baseX = 100
    val baseY = 100
    val polys = model.map.grid.zipWithIndex.map { case (row, columnIndex) =>
      val baseXOffset = baseX + (if (columnIndex % 2 == 0) 0 else polygonSize)
      val baseYOffset = (baseY + polygonSize * columnIndex * 1.5).toInt
      row.zipWithIndex.map {
        case (tile, rowIndex) if tile != "" =>
          Option(renderPolygon(
            (baseXOffset + polygonSize * rowIndex * 2).toInt,
            baseYOffset,
            tile == "",
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
