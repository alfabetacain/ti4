package ti4.model

object HexLayout {

  final case class Hex(q: Int, r: Int, s: Int) {
    def +(other: Hex): Hex  = Hex(q + other.q, r + other.r, s + other.s)
    def -(other: Hex): Hex  = Hex(q - other.q, r - other.r, s - other.s)
    def *(scalar: Int): Hex = Hex(q * scalar, r * scalar, s * scalar)
    def length: Int         = (q.abs + r.abs + s.abs) / 2
  }

  object Hex {

    def apply(q: Int, r: Int): Hex = Hex(q, r, -q - r)

    /**
     * Reset the component with the largest change back to what the constraint q + r + s = 0 requires. For example,
     * if the r-change abs(r-frac.r) is larger than abs(q-frac.q) and abs(s-frac.s), then we reset r = -q-s. This
     * guarantees that q + r + s = 0.
     */
    def apply(fracQ: Double, fracR: Double): Hex = {
      val fracS = -fracQ - fracR

      var roundQ = fracQ.round
      var roundR = fracR.round
      var roundS = fracS.round
      val qDiff = (fracQ - roundQ).abs
      val rDiff = (fracR - roundR).abs
      val sDiff = (fracS - roundS).abs

      if (qDiff > rDiff && qDiff > sDiff) {
        roundQ = -roundR * - roundS
      } else if (rDiff > sDiff) {
        roundR = -roundQ * - roundS
      } else {
        roundS = -roundQ * - roundR
      }

      Hex(roundQ.toInt, roundR.toInt, roundS.toInt)
    }

    def directions: Seq[Hex] = Seq(
      Hex(1, 0, -1),
      Hex(1, -1, 0),
      Hex(0, -1, 1),
      Hex(-1, 0, 1),
      Hex(-1, 1, 0),
      Hex(0, 1, -1)
    )

  }

  final case class Point(x: Double, y: Double)

  /**
   * The layout of a hex grid.
   *
   * @param origin the center of the grid
   * @param size the size of the hexes
   */
  final case class Layout(origin: Point, size: Point)

  // we only use the flat top orientation
  // matrix for converting hex coordinates to pixel coordinates
  val f0 = 3.0 / 2.0
  val f1 = 0.0
  val f2 = Math.sqrt(3.0) / 2.0
  val f3 = Math.sqrt(3.0)

  // the inverse matrix, does the opposite of the above
  val b0 = 2.0 / 3.0
  val b1 = 0.0
  val b2 = -1.0 / 3.0
  val b3 = Math.sqrt(3.0) / 3.0

  val startAngle = 0.0

  /**
   * To go from a hex coordinate to a screen coordinate I first multiply by the matrix, then multiply by the size, then add the origin
   */
  def toPoint(layout: Layout, hex: Hex): Point = {
    val x = (f0 * hex.q + f1 * hex.r) * layout.size.x // scale transformation
    val y = (f2 * hex.q + f3 * hex.r) * layout.size.y
    Point(
      x = x + layout.origin.x, // translate transformation
      y = y + layout.origin.y
    )
  }

  /**
   * First undo the origin by subtracting it, then undo the size by dividing by it, then undo the matrix multiply by multiplying by the inverse matrix
   */
  def toHex(layout: Layout, point: Point): Hex = {
    val pointWithNoSizeOrOrigin = Point(
      x = (point.x - layout.origin.x) / layout.size.x,
      y = (point.y - layout.origin.y) / layout.size.y
    )
    Hex(
      fracQ = b0 * pointWithNoSizeOrOrigin.x + b1 * pointWithNoSizeOrOrigin.y,
      fracR = b2 * pointWithNoSizeOrOrigin.x + b3 * pointWithNoSizeOrOrigin.y
    )
  }

  def hexCornerOffset(layout: Layout, corner: Int): Point = {
    val angle = 2.0 * Math.PI * (startAngle + corner) / 6.0
    Point(
      x = layout.size.x * Math.cos(angle),
      y = layout.size.y * Math.sin(angle)
    )
  }

  def polygonCorners(layout: Layout, hex: Hex): Vector[Point] = {
    val center = toPoint(layout, hex)

    Vector(0, 1, 2, 3, 4, 5).map { i =>
      val offset = hexCornerOffset(layout, i)
      Point(
        x = center.x + offset.x,
        y = center.y + offset.y
      )
    }
  }

}
