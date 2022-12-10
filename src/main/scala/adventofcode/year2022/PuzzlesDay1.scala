package adventofcode.year2022

import scala.io.Source

object PuzzlesDay1 extends App {

  def puzzle(
    input: String
  ) = {
    read(input)
      .max
  }

  def puzzleTwo(
    input: String
  ) = {
    read(input)
      .sorted
      .reverse
      .take(3)
      .sum
  }

  def read(
    input: String
  ) = {
    case class State(ls: List[Int] = Nil, calory: Int = 0)

    val state = Source.fromFile(input)
      .getLines
      .foldLeft(State()) {
        (state, line) => line match {
          case "" => State(state.calory :: state.ls, 0)
          case s"$calory" => State(state.ls, state.calory + calory.toInt)
        }
      }
    state.calory :: state.ls
  }
}
