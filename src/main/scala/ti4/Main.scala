package ti4

import monocle.syntax.all.*
import cats.syntax.all.*
import tyrian.*
import tyrian.Html.*
import tyrian.SVG.*
import tyrian.CSS
import org.scalajs.dom

import java.util.UUID
import ti4.model.{Faction, Tile, TileId, GameBoard, Unit as TUnit}
import cats.effect.IO

final case class Model(map: GameBoard)

object Model {

  def init(): Model = {
    val faction1 = "faction1"
    Model(GameBoard(
      Array(
        Array(None, None, None, "1".some, "2".some, None, None),
        Array("3".some, "4".some, "5".some, "6".some, "7".some, "8".some, None),
        Array("9".some, "10".some, "11".some, "12".some, "13".some, "14".some, "15".some)
      ),
      Map(
        "1" -> Tile(
          id = "1",
          units = List(
            TUnit.Ship.Destroyer(faction1),
            TUnit.Ship.Destroyer(faction1),
            TUnit.Ship.Destroyer(faction1),
          )
        ),
        "2" -> Tile(
          id = "2",
        ),
        "3" -> Tile(
          id = "3",
        ),
        "4" -> Tile(
          id = "4",
        ),
        "5" -> Tile(
          id = "5",
        ),
        "6" -> Tile(
          id = "6",
        ),
        "7" -> Tile(
          id = "7",
        ),
        "8" -> Tile(
          id = "8",
        ),
        "9" -> Tile(
          id = "9",
        ),
        "10" -> Tile(
          id = "10",
        ),
        "11" -> Tile(
          id = "11",
        ),
        "12" -> Tile(
          id = "12",
        ),
        "13" -> Tile(
          id = "13",
        ),
        "14" -> Tile(
          id = "14",
        ),
        "15" -> Tile(
          id = "15",
        ),
      ),
      Map(faction1 -> Faction.generic(faction1)),
    ))
  }
}

enum Msg {
  case Noop
  case Move(from: List[(TileId, List[TUnit])], to: TileId)
}

object Main extends TyrianApp[Msg, Model] {

  object Colors {
    val gray = "gray"
  }

  def main(args: Array[String]): Unit = {
    launch("app")
  }

  override def init(flags: Map[String, String]): (Model, Cmd[cats.effect.IO, Msg]) = {
    (Model.init(), Cmd.Run(IO(Msg.Move(from = List(("1", List(TUnit.Ship.Destroyer("faction1")))), to = "2"))))
  }

  override def router: Location => Msg = {
    case _ => Msg.Noop
  }

  override def subscriptions(model: Model): Sub[cats.effect.IO, Msg] = Sub.None

  override def update(model: Model): Msg => (Model, Cmd[cats.effect.IO, Msg]) = {
    case Msg.Noop => (model, Cmd.None)
    case Msg.Move(from, to) =>
      (model.focus(_.map).modify(Movement.move(_, from, to)), Cmd.None)
  }

  private val polygonSize = 50

  private def renderPolygon(id: TileId, x: Int, y: Int, map: GameBoard): List[Html[Msg]] = {
    val size = polygonSize
    List(
      polygon(
        points := s"${x},${y} ${x},${y - size / 2} ${x + size},${y - size} ${x + size * 2},${y - size / 2} ${x + size * 2},${y + size / 2} ${x + size},${y + size} ${x},${y + size / 2}",
        stroke := "black",
        fill   := Colors.gray,
      ),
      textTag(
        fill         := "white",
        tyrian.SVG.x := (x + size).toString(),
        tyrian.SVG.y := y.toString,
      )(s"${id}${map.getTile(id).map(t => s"- (${t.units.size})").getOrElse("")}"),
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
          renderPolygon(
            tile,
            (baseXOffset + polygonSize * rowIndex * 2).toInt,
            baseYOffset,
            model.map,
          )
        case _ => List.empty
      }.toList.flatten
    }.toList.flatten
    svg(width := "2000", height := "2000")(
      polys,
    )
  }

  override def view(model: Model): Html[Msg] = {
    div(height := "100%", width := "100%")(
      div(width := "100%", height := "100%")(
        renderMap(model),
      )
    )
  }
}
