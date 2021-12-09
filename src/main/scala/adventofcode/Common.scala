package adventofcode

import scala.io.Source

object Common extends App {
  def readFileToSeq(
    fileName: String
  ) = {
    Source.fromFile(fileName)
      .getLines()
      .toSeq
  }

  def parseStringToSeqInt(
    input: String
  ) = {
    input
      .split(",")
      .map(_.toInt)
  }

  def parseFileToSeqInt(
    fileName: String
  ) = {
    Source.fromFile(fileName)
        .mkString
        .split(",")
        .map(_.trim.toInt)
  }

  val Pattern = "(\\d+),(\\d+) -> (\\d+),(\\d+)".r

  def parsePairToPair(str: String) = str match {
    case Pattern(x1, y1, x2, y2) =>
      (x1.toInt, y1.toInt, x2.toInt, y2.toInt)
  }

  def range(x1: Int, x2: Int) =
      if (x2 > x1) {
        (x1 to x2)
      } else {
        (x1 to x2 by -1)
      }
}
