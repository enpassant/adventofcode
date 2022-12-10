package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay10._

class PuzzlesDay10Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_10_test.txt"
  val bigData = "samples/2022/input_10.txt"

  describe("Day 10.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (13140)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (13680)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal ("""##..##..##..##..##..##..##..##..##..##..
###...###...###...###...###...###...###.
####....####....####....####....####....
#####.....#####.....#####.....#####.....
######......######......######......####
#######.......#######.......#######.....""")
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal ("""###..####..##..###..#..#.###..####.###..
#..#....#.#..#.#..#.#.#..#..#.#....#..#.
#..#...#..#....#..#.##...#..#.###..###..
###...#...#.##.###..#.#..###..#....#..#.
#....#....#..#.#....#.#..#....#....#..#.
#....####..###.#....#..#.#....####.###..""")
    }
  }
}
