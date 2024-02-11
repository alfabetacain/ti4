package ti4.game

import fs2.Stream
import ti4.model.Faction
import cats.effect.IO
import cats.syntax.all._
import ti4.model.FactionId
import ti4.model.StrategyCard

object Loop {

  final case class ActionPhase(cards: Map[FactionId, List[StrategyCard]])

  final case class GameState(
      factions: List[Faction],
      points: Map[FactionId, Int],
      pointLimit: Int,
      speaker: FactionId,
      strategyCardsTradeGoods: Map[StrategyCard, Int],
      actionPhase: Option[ActionPhase],
  )

  private def chooseStrategyCards(
      factions: List[Faction],
      cards: Set[StrategyCard],
      tradeGoods: Map[StrategyCard, Int],
      acc: Map[FactionId, List[StrategyCard]]
  ): IO[Map[FactionId, List[StrategyCard]]] = {
    factions match {
      case Nil => Map.empty.pure[IO]
      case current :: rest =>
        current.chooseStrategyCard(cards.toList.map(card => (card, tradeGoods.getOrElse(card, 0))))
          .flatMap { chosenCard =>
            chooseStrategyCards(
              rest,
              cards - chosenCard,
              tradeGoods - chosenCard,
              acc.updatedWith(current.id) {
                case None                => Option(List(chosenCard))
                case Some(alreadyChosen) => Option(chosenCard :: alreadyChosen)
              }
            )
          }
    }
  }

  private def strategyPhase(state: GameState): IO[GameState] = {
    // choose strategy cards
    val speakerIndex = state.factions.indexWhere(_.id == state.speaker)
    val factionOrder =
      state.factions(speakerIndex) :: state.factions.drop(speakerIndex + 1) ++ state.factions.take(speakerIndex - 1)

    val strategyCards = StrategyCard.all
    chooseStrategyCards(factions = factionOrder, cards = strategyCards, state.strategyCardsTradeGoods, Map.empty)
      .flatMap { mappings =>
        if (factionOrder.size <= 4) {
          chooseStrategyCards(
            factionOrder,
            strategyCards -- mappings.values.flatten.toSet,
            state.strategyCardsTradeGoods,
            mappings
          )
        } else {
          mappings.pure[IO]
        }
      }.map { mappings =>
        // place trade goods on unchosen strategy cards
        val updatedTradeGoods = (strategyCards -- mappings.values.flatten.toSet).map { card =>
          card -> state.strategyCardsTradeGoods.get(card).map(_ + 1).getOrElse(1)
        }.toMap
        state.copy(
          strategyCardsTradeGoods = updatedTradeGoods,
          actionPhase = Option(ActionPhase(mappings))
        )
      }
  }

  private def takeAction(state: GameState, phaseState: ActionPhase, players: Stream[IO, FactionId]): IO[GameState] = {
    ???
  }

  private def actionPhase(state: GameState, phaseState: ActionPhase): IO[GameState] = {

    // consider 3.3.C in living rules
    val factionsInOrder = phaseState.cards.toList.map {
      case (faction, cards) => faction -> cards.map(_.initiative).sortBy(identity).head
    }.map(_._1)
    fs2.Stream(factionsInOrder: _*).repeat
    state.pure[IO]
  }
  private def statusPhase(state: GameState): IO[GameState] = { state.pure[IO] }
  private def agendaPhase(state: GameState): IO[GameState] = { state.pure[IO] }

  private def scorePoints(state: GameState, factionId: FactionId, points: Int): IO[GameState] = {
    state.copy(points =
      state.points.updatedWith(factionId) {
        case None          => None
        case Some(current) => Option(current + points)
      }
    ).pure[IO]
  }

  private def findWinner(state: GameState): Option[FactionId] = {
    state.points.collectFirst {
      case (faction, points) if points >= state.pointLimit => faction
    }
  }

  def loop(factions: List[Faction]): IO[Faction] = {
    IO.pure(factions.head)
  }
}
