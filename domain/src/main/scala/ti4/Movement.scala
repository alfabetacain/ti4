package ti4

import monocle.syntax.all._
import cats.syntax.all._
import ti4.model.TileId
import ti4.model.{ GameBoard => TMap, Unit => TUnit }
import cats.kernel.Eq

object Movement {

  private def distance(model: TMap, from: TileId, to: TileId): Int = {
    // TODO fancy A* implementation?
    1
  }

  def isLegalMove(model: TMap, from: TileId, to: TileId, unit: TUnit): Boolean = {
    // move distance
    val movement = model.getFaction(unit.owningFaction).map(_.movement(unit)).getOrElse(0)
    movement >= distance(model, from, to)
  }

  def move(model: TMap, from: List[(TileId, List[TUnit])], to: TileId): TMap = {
    model.getTile(to).map { toTile =>

      // TODO ensure uniqueness
      val withTiles = from.map { case (tileId, units) =>
        model.getTile(tileId)
          .map { tile =>
            val validUnits =
              tile.units.diff(tile.units.diff(units))
                .filter(isLegalMove(model, tileId, toTile.id, _))
            (
              tile,
              validUnits,
            )
          }
      }.flatten
      val (newModel, unitsToBeAdded) =
        withTiles.foldLeft((model, List.empty[TUnit])) { case (acc, (tile, unitsToRemove)) =>
          val nm = acc._1.focus(_.tiles.index(tile.id).units)
            .modify(_.diff(unitsToRemove))

          (nm, acc._2 ++ unitsToRemove)
        }

      newModel.focus(_.tiles.index(to).units)
        .modify(_ ++ unitsToBeAdded)
    }.getOrElse(model)
  }
}
