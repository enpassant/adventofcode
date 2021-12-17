package adventofcode

import scala.io.Source
import java.io.PrintWriter

object Puzzles2021Day17 {
  def puzzle(
    fileName: String
  ) = {
    val data = readData(fileName).get
    val x1 = calcX(data.x1)
    val x2 = calcX(data.x2)
    val y1 = calcY(data.y1)
    val y2 = calcY(data.y2)
    (x1, x2, y1, y2)
    (x1.toInt, x2.toInt, y1.toInt, y2.toInt)
    val y = Math.max(y1.toInt, y2.toInt)
    (y * y + y) / 2
  }

  def puzzleTwo(
    fileName: String
  ) = {
    val data = readData(fileName).get
    List.tabulate(data.x2 + 1, 1000 - data.y1) {
      (x, y) => simulate(x, y + data.y1, 0, 0, data)
    }.flatten
      .filter(_ == true)
      .map(_ => 1)
      .sum
  }

  def simulate(x: Int, y: Int, x1: Int, y1: Int, target: Target): Boolean = {
    if (x == 0 && x1 < target.x1) false
    else if (in(x1, target.x1, target.x2) && (in(y1, target.y1, target.y2))) true
    else if (in(x1, 0, target.x2) && y1 >= target.y1) {
      val newX = if (x > 0) x - 1 else 0
      simulate(newX, y - 1, x1 + x, y1 + y, target)
    } else false
  }

  def in(i: Int, i1: Int, i2: Int) = (i >= i1 && i <= i2)

  def calcX(x: Int) = {
    Math.sqrt(2.0 * x + 0.25) - 0.5
  }

  def calcY(y: Int) = {
    val y1 = 0 - y
    Math.sqrt(4.0 * (y1 * y1 + y1) + 1) / 2 - 1
  }

  case class Target(x1: Int, x2: Int, y1: Int, y2: Int)

  def readData(
    fileName: String
  ) = {
    val pattern = raw"x=(\d+)\.\.(\d+), y=(-?\d+)\.\.(-?\d+)".r.unanchored
    Source.fromFile(fileName)
      .mkString match {
        case pattern(x1, x2, y1, y2) =>
          Some(Target(x1.toInt, x2.toInt, y1.toInt, y2.toInt))
        case _ => None
      }
  }
}
