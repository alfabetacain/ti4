package ti4.collection

final case class Hex(q: Int, r: Int, s: Int) {
  def +(other: Hex): Hex = Hex(q + other.q, r + other.r, s + other.s)

  def -(other: Hex): Hex = Hex(q - other.q, r - other.r, s - other.s)

  def *(scalar: Int): Hex = Hex(q * scalar, r * scalar, s * scalar)

  def length: Int = (q.abs + r.abs + s.abs) / 2
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
      roundQ = -roundR * -roundS
    } else if (rDiff > sDiff) {
      roundR = -roundQ * -roundS
    } else {
      roundS = -roundQ * -roundR
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
