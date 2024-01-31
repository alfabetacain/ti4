package ti4.collection

final case class HexGrid[T] private (private val hexByValue: Map[Hex, T]) {
  private val valueByHex = hexByValue.map { case (hex, value) => value -> hex }

  /**
   * Creates a new grid where the new values will override existing values
   */
  def withHexes(entries: (Hex, T)*): HexGrid[T] = this.copy(hexByValue = hexByValue ++ entries)

  def getValueAtPosition(hex: Hex): Option[T] = hexByValue.get(hex)
  def getThePositionOfValue(value: T): Option[Hex] = valueByHex.get(value)

  def filter(f: ((Hex, T)) => Boolean): HexGrid[T] = this.copy(hexByValue = hexByValue.filter(f))

}

object HexGrid {

  def apply[T](entries: (Hex, T)*): HexGrid[T] = HexGrid(entries.toMap)

  private def generateRDimension(size: Int, q: Int) = Math.max(-size, -q - size) to Math.min(size, -q + size)

  // That way we can make it possible in the future to make different board layouts
  // How to make it possible to ask for a specific game board? Should it have an id?
  // maybe this should just be called grid, since not all grids are hexagonal?
  def generate[T](size: Int, f: Hex => T): HexGrid[T] = {
    val hexes = for {
      q <- -size to size
      r <- generateRDimension(size, q)
      position = Hex(q, r)
    } yield position -> f(position)

    HexGrid(hexes: _*)
  }

  // this should be how the game board is created
  // next is to figure out out to show the different options of board layouts
  // as they are limited. Should it be in an enum? Somewhere else? How to make it dynamic so it can something
  // we can read in and potentially other people can alter later?

}
