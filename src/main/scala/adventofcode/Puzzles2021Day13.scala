package adventofcode

import scala.io.Source

object Puzzles2021Day13 {
  def puzzle(
    fileName: String
  ) = {
    val (map, folds) = readData(fileName)
    fold(map, folds.head)
      .size
  }

  def puzzleTwo(
    fileName: String
  ) = {
    val (map, folds) = readData(fileName)
    val result = folds.foldLeft(map) { case (m, f) => fold(m, f) }
    printMap(result)
  }

  def fold(map: Set[(Int, Int)], fold: Fold) = fold match {
    case FoldX(x) => {
      map.foldLeft(Set[(Int, Int)]()) {
        case (m, (x1, y1)) =>
          if (x1 < x) m + ((x1, y1))
          else if (x1 > x) m + ((x + x - x1, y1))
          else m
      }
    }
    case FoldY(y) => {
      map.foldLeft(Set[(Int, Int)]()) {
        case (m, (x1, y1)) =>
          if (y1 < y) m + ((x1, y1))
          else if (y1 > y) m + ((x1, y + y - y1))
          else m
      }
    }
  }

  def printMap(map: Set[(Int, Int)]) = {
    val width = map.map { case (x, y) => x }.max + 1
    val height = map.map { case (x, y) => y }.max + 1
    val line = "." * width
    val vector = Vector.fill(height)(line)
    map.foldLeft(vector) {
      case (display, (x, y)) => display.updated(y, display(y).updated(x, '#'))
    }.mkString("\n")
  }

  sealed trait Fold
  case class FoldX(x: Int) extends Fold
  case class FoldY(y: Int) extends Fold

  def readData(
    fileName: String
  ) = {
    val input = Source.fromFile(fileName)
      .getLines

    val map = input
      .takeWhile(!_.isBlank())
      .map(_.split(",").map(_.trim.toInt)).map(arr => (arr(0), arr(1)))
      .map { case (x, y) => (x, y) }
      .toSet

    val foldPattern = "fold along (x|y)=(\\d+)".r
    val folds = input
      .map(s => s match {
        case foldPattern("x", x) => FoldX(x.toInt)
        case foldPattern("y", y) => FoldY(y.toInt)
      })
      .toList

    (map, folds)
  }
}
