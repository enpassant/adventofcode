package adventofcode

import scala.io.Source

object Puzzles2021Day5 extends App {
  def p2021_5(
    fileName: String,
    filter: (Int, Int, Int, Int) => Boolean
  ) = {
    Common.readFileToSeq(fileName)
      .map(Common.parsePairToPair)
      .filter(filter.tupled)
      .flatMap(vectorToPixels.tupled)
      .groupMapReduce(identity)(_ => 1)(_ + _)
      .filter { case (k, v) => v > 1 }
      .size
  }

  val vectorToPixels = (x1: Int, y1: Int, x2: Int, y2:Int) =>
      Common.range(x1, x2)
        .zipAll(
          Common.range(y1, y2),
          x1,
          y1
        )

  def every(x1: Int, y1: Int, x2: Int, y2:Int) = true

  def isNotDiagonal(x1: Int, y1: Int, x2: Int, y2:Int) =
    (x1 == x2) || (y1 == y2)
}
