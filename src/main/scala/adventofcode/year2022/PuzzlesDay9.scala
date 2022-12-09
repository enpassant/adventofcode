package adventofcode.year2022

import scala.io.Source

object PuzzlesDay9 extends App {
  trait Motion {
    def count: Int
  }

  case class Left(count: Int) extends Motion
  case class Right(count: Int) extends Motion
  case class Up(count: Int) extends Motion
  case class Down(count: Int) extends Motion

  case class Position(x: Int, y: Int)

  case class Config(head: Position, tail: Position, positions: Set[Position])
  case class Config2(heads: List[Position], positions: Set[Position])

  def puzzle(
    input: String
  ) = {
    val motions = read(input)
    calc(motions)
  }

  def puzzleTwo(
    input: String
  ) = {
    val motions = read(input)
    calc2(motions)
  }

  def calc(motions: List[Motion]): Int = {
    val head = Position(0, 0)
    val config = Config(head, head, Set[Position](head))
    val resultConfig = motions.foldLeft(config)(step)
    resultConfig.positions.size
  }

  def calc2(motions: List[Motion]): Int = {
    val head = Position(0, 0)
    val config = Config2(List.fill(10)(head), Set[Position](head))
    val resultConfig = motions.foldLeft(config)(step2)
    resultConfig.positions.size
  }

  def step(config: Config, motion: Motion): Config = {
    val (newHead, newMotion) = move(config.head, motion)
    val newTail = moveTail(newHead, config.tail)
    val newConfig = Config(newHead, newTail, config.positions + newTail)
    if (newMotion.count > 0) {
      step(newConfig, newMotion)
    } else {
      newConfig
    }
  }

  def step2(config: Config2, motion: Motion): Config2 = {
    val (newHead, newMotion) = move(config.heads.head, motion)
    val newHeads = config.heads.tail.scan(newHead)(moveTail)
    val newConfig = Config2(newHeads, config.positions + newHeads.last)
    if (newMotion.count > 0) {
      step2(newConfig, newMotion)
    } else {
      newConfig
    }
  }

  def move(position: Position, motion: Motion): (Position, Motion) = motion match {
    case Left(count) => (Position(position.x - 1, position.y), Left(count - 1))
    case Right(count) => (Position(position.x + 1, position.y), Right(count - 1))
    case Up(count) => (Position(position.x, position.y + 1), Up(count - 1))
    case Down(count) => (Position(position.x, position.y - 1), Down(count - 1))
  }

  def moveTail(head: Position, tail: Position): Position =
    (head.x - tail.x, head.y - tail.y) match
  {
    case (0, 2) => Position(tail.x, tail.y + 1)
    case (0, -2) => Position(tail.x, tail.y - 1)
    case (2, 0) => Position(tail.x + 1, tail.y)
    case (-2, 0) => Position(tail.x - 1, tail.y)
    case (1, 2) => Position(tail.x + 1, tail.y + 1)
    case (1, -2) => Position(tail.x + 1, tail.y - 1)
    case (-1, 2) => Position(tail.x - 1, tail.y + 1)
    case (-1, -2) => Position(tail.x - 1, tail.y - 1)
    case (-2, -1) => Position(tail.x - 1, tail.y - 1)
    case (-2, 1) => Position(tail.x - 1, tail.y + 1)
    case (2, -1) => Position(tail.x + 1, tail.y - 1)
    case (2, 1) => Position(tail.x + 1, tail.y + 1)
    case (2, 2) => Position(tail.x + 1, tail.y + 1)
    case (2, -2) => Position(tail.x + 1, tail.y - 1)
    case (-2, 2) => Position(tail.x - 1, tail.y + 1)
    case (-2, -2) => Position(tail.x - 1, tail.y - 1)
    case _ => tail
  }

  def read(
    input: String
  ) = {
    Source.fromFile(input)
        .getLines
        .map(line => line match {
          case s"R $count" => Some(Right(count.toInt))
          case s"L $count" => Some(Left(count.toInt))
          case s"U $count" => Some(Up(count.toInt))
          case s"D $count" => Some(Down(count.toInt))
        })
        .filter(_.isDefined)
        .map(_.get)
        .toList
  }
}
