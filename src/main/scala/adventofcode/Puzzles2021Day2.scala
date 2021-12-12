package adventofcode

import scala.io.Source

object Puzzles2021Day2 {
  def puzzle(
    fileName: String
  ) = {
    val operations = readData(fileName)
    val position = operations.foldLeft(Position(0, 0, 0)) {
      case (position, operation) => move(position, operation)
    }
    position.horizontal * position.depth
  }

  def puzzleTwo(
    fileName: String
  ) = {
    val operations = readData(fileName)
    val position = operations.foldLeft(Position(0, 0, 0)) {
      case (position, operation) => moveAim(position, operation)
    }
    position.horizontal * position.depth
  }

  sealed trait Operation
  case class Forward(x: Int) extends Operation
  case class Down(x: Int) extends Operation
  case class Up(x: Int) extends Operation

  case class Position(horizontal: Int, depth: Int, aim: Int)

  def move(position: Position, operation: Operation) = {
    operation match {
      case Forward(x) => position.copy(horizontal = position.horizontal + x)
      case Up(x) => position.copy(depth = position.depth - x)
      case Down(x) => position.copy(depth = position.depth + x)
    }
  }

  def moveAim(position: Position, operation: Operation) = {
    operation match {
      case Forward(x) => position.copy(
        horizontal = position.horizontal + x,
        depth = position.depth + position.aim * x
      )
      case Up(x) => position.copy(aim = position.aim - x)
      case Down(x) => position.copy(aim = position.aim + x)
    }
  }

  val operationPattern = "(\\w+)\\s+(\\d+)".r

  def readData(
    fileName: String
  ) = {
    Source.fromFile(fileName)
      .getLines
      .toList
      .map(_ match {
        case operationPattern("forward", x) => Forward(x.toInt)
        case operationPattern("down", x) => Down(x.toInt)
        case operationPattern("up", x) => Up(x.toInt)
      })
  }
}
