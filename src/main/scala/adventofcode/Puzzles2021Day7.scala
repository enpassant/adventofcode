package adventofcode

import scala.io.Source

object Puzzles2021Day7 extends App {
  def p2021_7(
    input: String,
    calcFn: (Seq[Int], Int) => Int
  ) = {
    val population = Source.fromFile(input)
        .mkString
        .split(",")
        .map(_.trim.toInt)

    val startSum = calcFn(population, 0)
    val endPos = population.max

    def calc(minSum: Int, index: Int): Int = {
      if (index > endPos) {
        minSum
      } else {
          val sum = calcFn(population, index)
          if (sum > minSum) minSum
          else calc(sum, index + 1)
      }
    }
    calc(startSum, 1)
  }

  def calcFuelLineary(population: Seq[Int], index: Int) =
    population.map(pos => Math.abs(pos - index)).sum

  def calcFuelExponentally(population: Seq[Int], index: Int) =
    population.map(pos => calFuelDistance(Math.abs(pos - index))).sum

  def calFuelDistance(distance: Int) = distance * (distance + 1) / 2
}
