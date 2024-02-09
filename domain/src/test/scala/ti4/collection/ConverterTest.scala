package ti4.collection

import munit.ScalaCheckSuite
import org.scalacheck.Prop.forAll

class ConverterTest extends ScalaCheckSuite {

  property("A hexagon coordinate converted to a point should convert back to the same hexagon coordinate") {
    forAll(Generator.layoutAndHex) { case (layout: Layout, originalHex: Hex) =>
      val point  = Converter.toPoint(layout, originalHex)
      val newHex = Converter.toHex(layout, point)

      newHex == originalHex
    }
  }

}
