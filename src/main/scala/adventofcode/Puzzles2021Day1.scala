package adventofcode

import scala.io.Source

object Puzzles2021Day1 extends App {
  def p2021_1(
    fileName: String
  ) = {
    val depths = Source.fromFile(fileName)
        .getLines
        .map(_.toInt)
        .toSeq

    depths.zip(depths.tail)
      .filter(_ < _)
      .length
  }

  def p2021_1_2(
    fileName: String
  ) = {
    val depths = Source.fromFile(fileName)
        .getLines
        .map(_.toInt)
        .toSeq

    val sums = depths.zip(depths.tail)
      .zip(depths.tail.tail)
      .map(v => v._1._1 + v._1._2 + v._2)

    sums.zip(sums.tail)
      .filter(_ < _)
      .length
  }
}
