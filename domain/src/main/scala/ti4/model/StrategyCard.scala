package ti4.model

sealed trait StrategyCard {
  def initiative: Int
}

object StrategyCard {

  case object Leadership extends StrategyCard {
    override val initiative: Int = 1
  }

  case object Diplomacy extends StrategyCard {
    override val initiative: Int = 2
  }

  case object Politics extends StrategyCard {
    override val initiative: Int = 3
  }

  case object Construction extends StrategyCard {
    override val initiative: Int = 4
  }

  case object Trade extends StrategyCard {
    override val initiative: Int = 5
  }

  case object Warfare extends StrategyCard {
    override val initiative: Int = 6
  }

  case object Research extends StrategyCard {
    override val initiative: Int = 7
  }

  case object Imperial extends StrategyCard {
    override val initiative: Int = 8
  }

  val all: Set[StrategyCard] = Set(
    Leadership,
    Diplomacy,
    Politics,
    Construction,
    Trade,
    Warfare,
    Research,
    Imperial,
  )
}
