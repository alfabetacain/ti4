package ti4.collection

final case class HexGrid[T] private (private val hexByValue: Map[Hex, T]) {
  private val valueByHex = hexByValue.map { case (hex, value) => value -> hex }

  /**
   * Creates a new grid where the new values will override existing values
   */
  def withHexes(entries: (Hex, T)*): HexGrid[T] = this.copy(hexByValue = hexByValue ++ entries)

  def getValue(hex: Hex): Option[T]      = hexByValue.get(hex)
  def getPosition(value: T): Option[Hex] = valueByHex.get(value)

  def filter(f: ((Hex, T)) => Boolean): HexGrid[T] = this.copy(hexByValue = hexByValue.filter(f))

  def toList: Seq[(Hex, T)] = hexByValue.toList

  def headOption: Option[(Hex, T)] = hexByValue.headOption

}

object HexGrid {

  def apply[T](entries: (Hex, T)*): HexGrid[T] = HexGrid(entries.toMap)

  private def generateRDimension(size: Int, q: Int) = Math.max(-size, -q - size) to Math.min(size, -q + size)

  def generate[T](size: Int, f: Hex => T): HexGrid[T] = {
    val hexes = for {
      q <- -size to size
      r <- generateRDimension(size, q)
      position = Hex(q, r)
    } yield position -> f(position)

    HexGrid(hexes: _*)
  }

}
