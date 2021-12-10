package adventofcode

import scala.io.Source

object Puzzles2021Day10 extends App {
  def p2021_10(
    fileName: String
  ) = {
    parseToState(fileName)
      .filterNot(isLegalState)
      .map(scoreState)
      .sum
  }

  def p2021_10_2(
    fileName: String
  ) = {
    val scores = parseToState(fileName)
    .filter(isLegalState)
    .map(scoreState)
    .filter(_ > 0)
    .sorted

    scores((scores.length) / 2)
  }

  def parseToState(
    fileName: String
  ) = {
    Source.fromFile(fileName)
        .getLines
        .toSeq
        .map(parseLine)
  }

  trait State {
    def chars: List[Char]
  }
  case class LegalState(chars: List[Char]) extends State
  case class IllagalState(chars: List[Char], illegal: Char) extends State

  val initState = LegalState(List())

  def parseLine(str: String) = {
    str.foldLeft[State](initState) {
      (state, char) => nextState(state, char)
    }
  }

  def scoreState(state: State) : BigInt = state match {
    case legalState: LegalState => scoreLegalState(legalState)
    case IllagalState(_, illegalChar) => errorScore.getOrElse(illegalChar, 0)
  }

  def scoreLegalState(state: LegalState) : Long = {
    state.chars.foldLeft(0L) {
      (sum, ch) => sum * 5 + legalScore(ch)
    }
  }

  val errorScore = Map(
    ')' -> 3,
    ']' -> 57,
    '}' -> 1197,
    '>' -> 25137
  )

  val legalScore = Map(
    '(' -> 1,
    '[' -> 2,
    '{' -> 3,
    '<' -> 4
  )

  val closeChars = Map(
    ')' -> '(',
    ']' -> '[',
    '}' -> '{',
    '>' -> '<'
  )

  def nextState(state: State, char: Char): State = state match {
    case LegalState(chars) => {
      val closeCharOpt = closeChars.get(char)
      val lastCloseCharEqual = chars.headOption
        .exists(ch => ch == closeCharOpt.getOrElse(' '))

      closeCharOpt.fold(
        LegalState(char :: chars)
      )(
        closeChar =>
          if (lastCloseCharEqual) LegalState(chars.tail)
          else IllagalState(state.chars, char)
      )
    }
    case IllagalState(_, _) => state
  }

  def isLegalState(state: State) = state match {
    case LegalState(_) => true
    case _ => false
  }
}
