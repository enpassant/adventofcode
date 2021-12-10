package adventofcode

import scala.io.Source

object Puzzles2021Day9 extends App {
  def p2021_7(
    input: String
  ) = {
    val heights = readMap(input)

    calcLowestPoints(heights)
      .values
      .map(_ + 1)
      .sum
  }

  def p2021_7_2(
    input: String
  ) = {
    val heights = readMap(input)

    val lowestPoints = calcLowestPoints(heights)

    lowestPoints
      .map(v => getBasin(heights, Map(v)))
      .map(_.size)
      .toSeq
      .sorted
      .reverse
      .take(3)
      .product
  }


  def readMap(
    input: String
  ) = {
    Source.fromFile(input)
        .getLines
        .zipWithIndex
        .map(v => (v._2, v._1))
        .toMap
        .flatMap((x, line) =>
            line.toCharArray
              .map(_.toInt - 48)
              .zipWithIndex
              .map(v => ((x, v._2), v._1))
              .toMap
        )
  }

  def calcLowestPoints(
    heights: Map[(Int, Int), Int]
  ) = {
      heights
        .filter { case ((x, y), v) =>
          v < getAdjacents(heights, x, y).min
        }
  }

  def getAdjacents(map: Map[(Int, Int), Int], x: Int, y: Int): List[Int] = {
    val ls =
      map.get((x-1, y)) ::
      map.get((x+1, y)) ::
      map.get((x, y-1)) ::
      map.get((x, y+1)) ::
      Nil

    ls.filter(_.isDefined)
      .map(_.get)
  }

  def getBasin(
    map: Map[(Int, Int), Int],
    basin: Map[(Int, Int), Int]
  ): Map[(Int, Int), Int] = {
    val newBasin = basin.foldLeft(basin) {
      case (basin, ((x, y), v)) => {
        val ls =
          (x-1, y) -> map.get((x-1, y)) ::
          (x+1, y) -> map.get((x+1, y)) ::
          (x, y-1) -> map.get((x, y-1)) ::
          (x, y+1) -> map.get((x, y+1)) ::
          Nil

        basin ++ ls.filter(_._2.isDefined)
          .map { case ((x, y), v) => ((x, y), v.get) }
          .toMap
          .filter { case ((x, y), v) => v < 9 && !basin.contains((x, y)) }
      }
    }
    if (newBasin.size > basin.size) getBasin(map, newBasin)
    else newBasin
  }
}
