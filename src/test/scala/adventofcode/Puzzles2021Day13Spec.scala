package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day13._

class Puzzles2021Day13Spec extends AnyFunSpec with Matchers {
  val testData = "samples/input_13_test.txt"
  val bigData = "samples/input_13.txt"

  val testResult = """#####
                     |#...#
                     |#...#
                     |#...#
                     |#####""".stripMargin

  val bigResult = """#..#..##...##....##.###..####.#..#..##.
                    |#..#.#..#.#..#....#.#..#.#....#..#.#..#
                    |####.#....#..#....#.###..###..####.#...
                    |#..#.#.##.####....#.#..#.#....#..#.#...
                    |#..#.#..#.#..#.#..#.#..#.#....#..#.#..#
                    |#..#..###.#..#..##..###..####.#..#..##.""".stripMargin

  describe("Day 13.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (17)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (704)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (testResult)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (bigResult)
    }
  }
}
