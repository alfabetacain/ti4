package ti4.collection

import org.scalacheck.Gen

object Generator {

  private val integerGenerator: Gen[Int] = Gen.choose(Int.MinValue, Int.MaxValue)

  val hexagonCoordinate: Gen[Hex] = for {
    q <- integerGenerator
    r <- integerGenerator
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

  val layoutAndPoint: Gen[(Layout, Point)] = for {
    layout <- layout
    point <- point
  } yield (layout, point)
}
