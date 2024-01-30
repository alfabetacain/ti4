package ti4.collection

type HexGrid[+T] = Map[Hex, T]

object HexGrid {

  def apply[T](entries: (Hex, T)*): HexGrid[T] = Map(entries: _*)

  extension[T] (grid: HexGrid[T]) {
    /**
     * A similar hex with a different value will be overridden
     */
    def withHexes(entries: (Hex, T)*): HexGrid[T] = grid ++ entries
  }

  // think about how the API of the Hex grid should look like
  // position is easy to determine, the question is more how we choose
  // to represent the values that the grid should hold.
  private def generateRDimension(size: Int, q: Int) = {
    Math.max(-size, -q - size) to Math.min(size, -q + size)
  }

  // That way we can make it possible in the future to make different board layouts
  // How to make it possible to ask for a specific game board? Should it have an id?
  // maybe this should just be called grid, since not all grids are hexagonal?
  def generate[T](size: Int, empty: T): HexGrid[T] = {
    val hexes = for {
      q <- -size to size
      r <- generateRDimension(size, q)
    } yield Hex(q, r) -> empty

    hexes.toMap
  }

  // this should be how the game board is created
  // next is to figure out out to show the different options of board layouts
  // as they are limited. Should it be in an enum? Somewhere else? How to make it dynamic so it can something
  // we can read in and potentially other people can alter later?

}
