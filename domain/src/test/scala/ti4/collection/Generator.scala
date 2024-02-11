package ti4.collection

import org.scalacheck.Gen

object Generator {

  private val intGen: Gen[Int]                  = Gen.choose(-10000, 10000)
  private val positiveIntGen: Gen[Int]          = Gen.choose(0, 10000)
  private val asciiPrintableString: Gen[String] = Gen.stringOf(Gen.asciiPrintableChar)

  val hexagonCoordinate: Gen[Hex] = for {
    q <- intGen
    r <- intGen
  } yield Hex(q, r)

  val point: Gen[Point] = for {
    x <- Gen.double
    y <- Gen.double
  } yield Point(x, y)

  private val layout: Gen[Layout] = for {
    size   <- point
    origin <- point
  } yield Layout(size, origin)

  val layoutAndHex: Gen[(Layout, Hex)] = for {
    layout <- layout
    hex    <- hexagonCoordinate
  } yield (layout, hex)

  private def makeHexGridGen[T](tGen: Gen[T]): Gen[HexGrid[T]] = {
    for {
      size   <- positiveIntGen
      hexes  <- Gen.listOfN(size, hexagonCoordinate)
      values <- Gen.listOfN(size, tGen)
      combined = hexes.zip(values)
    } yield HexGrid[T](combined*)
  }

  val hexGridInt: Gen[HexGrid[Int]]       = makeHexGridGen(intGen)
  val hexGridString: Gen[HexGrid[String]] = makeHexGridGen(asciiPrintableString)

}
