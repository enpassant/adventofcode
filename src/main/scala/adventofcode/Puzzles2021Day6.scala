package adventofcode

import scala.io.Source

object Puzzles2021Day6 extends App {
  def p2021_6(
    input: String,
    count: Int
  ) = {
    val population = input
      .split(",")
      .map(_.toInt)
      .foldLeft(Vector(0, 0, 0, 0, 0, 0, 0, 0, 0)) {
        case (vec, day) => vec.updated(day, vec(day) + 1)
      }
      .map(_.toLong)

    (1 to count)
      .foldRight(population) {
        (i, popu) =>
          (popu.drop(1) :+ popu(0)).updated(6, popu(7) + popu(0))
      }.sum
  }
}
