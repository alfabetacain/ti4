package ti4.tech

import cats.effect.IO

final case class PreRequisites(red: Int, green: Int, blue: Int) {

  def satisfies(tech: Technology): Boolean = {
    val expected = tech.requires
    red >= expected.red && green >= expected.green && blue >= expected.blue
  }
}

sealed trait Technology {
  def requires: PreRequisites
  def color: Technology.Color
}

object Technology {

  enum Color {
    case RED, GREEN, BLUE
  }

  case object PlasmaScoring extends Technology {
    override val requires: PreRequisites = PreRequisites(0, 0, 0)
    override val color: Color            = Color.RED
  }
}

object Tech {

  def calculateCurrentPreRequisites(currentTech: Set[Technology]): PreRequisites = {
    PreRequisites(
      red = currentTech.count(_.color == Technology.Color.RED),
      green = currentTech.count(_.color == Technology.Color.GREEN),
      blue = currentTech.count(_.color == Technology.Color.BLUE),
    )
  }

  def research(currentPreRequisites: PreRequisites, wantedTech: Technology): IO[Option[Technology]] = {
    if (currentPreRequisites.satisfies(wantedTech)) {
      IO.pure(Option(wantedTech))
    } else {
      IO.pure(Option.empty)
    }
  }

}
