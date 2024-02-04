package ti4.collection

import munit.ScalaCheckSuite
import org.scalacheck.Prop.forAll

class ConverterTest extends ScalaCheckSuite {

  property("A hexagon coordinate converted to a point should convert back to the same hexagon coordinate") {
    forAll(Generator.layoutAndHex) { case (layout: Layout, originalHex: Hex) =>
      val point = Converter.toPoint(layout, originalHex)
      val newHex = Converter.toHex(layout, point)

      newHex == originalHex
    }
  }

  property("A point converted to a hexagon coordinate should convert back to the same point") {
    forAll(Generator.layoutAndPoint) { case (layout: Layout, originalPoint: Point) =>
      val hex = Converter.toHex(layout, originalPoint)
      val newPoint = Converter.toPoint(layout, hex)

      newPoint == originalPoint
    }
  }

}
