package ti4.collection

import munit.ScalaCheckSuite
import org.scalacheck.Prop.forAll

class HexTest extends ScalaCheckSuite {

  property("A hexagon coordinate should always be fulfill the constraint q + r + s = 0") {
    forAll(Generator.hexagonCoordinate) { (hex: Hex) =>
      hex.toVector.sum == 0
    }
  }

}
