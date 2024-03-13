package ti4.identifiers

import munit.ScalaCheckSuite
import org.scalacheck.Prop.forAll
import org.scalacheck.Gen

class GameBoardSetupIdTest extends ScalaCheckSuite {

  val gameBoardIdGenerator: Gen[String] = for {
    boardGame <- Gen.oneOf("ti4", "ti4-pok")
    players <- Gen.chooseNum(3, 10)
    number <- Gen.chooseNum(0, Int.MaxValue)
  } yield s"$boardGame-game-board-setup-${players}p-$number"


  property("An invalid game board setup id should not be parsed") {
    forAll(Gen.alphaStr) { randomString =>
      GameBoardSetupId(randomString).left.map(_.getMessage) == Left(s"$randomString is not a valid game bord setup id")
    }
  }

  property("A valid game board setup id should be parsed") {
    forAll(gameBoardIdGenerator) { validId =>
      GameBoardSetupId(validId).isRight
    }
  }

//  TODO: Add below tests
//  "A game board setup id should always indicate which board game it belongs to"
//  "A game board setup id should always indicate a valid number of players"
//  "A game board setup id should always end on a number"

}
