package ti4.collection

import munit.ScalaCheckSuite
import org.scalacheck.Prop.forAll

class HexGridTest extends ScalaCheckSuite {

  property("A hexagon grid combined with itself should be the same as a single grid") {
    forAll(Generator.hexGridString) { case grid: HexGrid[String] =>
      grid.withHexes(grid.toList*) == grid
    }
  }

  property("When adding an existing hex coordinate to a hexagon grid, the value of the added hex coordinate should be kept") {
    forAll(Generator.hexGridString) { case grid: HexGrid[String] =>
      val newValue = "new value"
      val hexOpt = grid.headOption.map(_._1)
      val hexTileOpt = hexOpt.map(hex => hex -> newValue)
      val newGrid = grid.withHexes(hexTileOpt.toList*)

      hexOpt.flatMap(newGrid.getValue) == hexTileOpt.map(_._2)
      newGrid.getPosition(newValue) == hexOpt
    }
  }

  test("should ") {

  }

}
