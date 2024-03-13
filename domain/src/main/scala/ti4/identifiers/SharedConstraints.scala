package ti4.identifiers

private[identifiers] object SharedConstraints {

  /**
   * Current valid game names and their mappings:
   *
   * ti4 -> Twilight Imperium 4th Edition
   *
   * ti4-pok -> Twilight Imperium 4th Edition: Prophecy of Kings
   */
  val GameNameRegex = "^ti4(-pok)?"

  /**
   * Number of players
   */
  val PlayerRegex = "\\d+p"

}
