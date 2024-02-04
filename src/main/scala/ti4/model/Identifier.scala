package ti4.model

object Identifier {

  // ti4 - Twilight Imperium 4th Edition
  // ti4-pok - Twilight Imperium 4th Edition: Prophecy of Kings
  private val gameNameRegex = "^ti4(-pok)?"

  // maybe just do numbers for the id of a setup?
  private val setupIdRegex = gameNameRegex + "-setup-([0-9]+)$".r

  // <game_name>-<type>-<id>
  // ti4
  // ti4-pok




}
