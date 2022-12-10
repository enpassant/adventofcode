package adventofcode.year2022

import scala.io.Source

object PuzzlesDay2 extends App {

  sealed trait Shape
  case object Rock extends Shape
  case object Paper extends Shape
  case object Scissors extends Shape

  sealed trait Result
  case object Win extends Result
  case object Draw extends Result
  case object Lost extends Result

  def puzzle(
    input: String
  ) = {
    val map = read(input)
    map
      .map(line => line match {
        case s"$opp $my" => (getShape(opp), getShape(my))
      })
      .map(evaluate)
      .sum
  }

  def puzzleTwo(
    input: String
  ) = {
    val map = read(input)
    map
      .map(line => line match {
        case "A X" => (Rock, Scissors)
        case "A Y" => (Rock, Rock)
        case "A Z" => (Rock, Paper)
        case "B X" => (Paper, Rock)
        case "B Y" => (Paper, Paper)
        case "B Z" => (Paper, Scissors)
        case "C X" => (Scissors, Paper)
        case "C Y" => (Scissors, Scissors)
        case "C Z" => (Scissors, Rock)
      })
      .map(evaluate)
      .sum
  }

  def evaluate(play: (Shape, Shape)) = {
    val result = fight(play._2, play._1)
    getScore(result, play._2)
  }

  def getScore(result: Result, my: Shape) = {
    val score = result match {
      case Win => 6
      case Draw => 3
      case Lost => 0
    }
    score + getShapeScore(my)
  }

  def getShapeScore(shape: Shape) = shape match {
    case Rock => 1
    case Paper => 2
    case Scissors => 3
  }

  def getShape(ch: String) = ch match {
    case "A" => Rock
    case "X" => Rock
    case "B" => Paper
    case "Y" => Paper
    case "C" => Scissors
    case "Z" => Scissors
  }

  def fight(my: Shape, opp: Shape) = (my, opp) match {
    case (a, b) if (a == b) => Draw
    case (Paper, Rock) => Win
    case (Rock, Scissors) => Win
    case (Scissors, Paper) => Win
    case _ => Lost
  }

  def read(
    input: String
  ) = {
    Source.fromFile(input)
      .getLines
  }
}
