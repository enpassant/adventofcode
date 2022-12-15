package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay15._

class PuzzlesDay15Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_15_test.txt"
  val bigData = "samples/2022/input_15.txt"

  describe("Day 15.") {
    it("Part one with test data") {
      val result = puzzle(testData, 10)
      result should equal (26)
    }
    it("Part one with big data") {
      val result = puzzle(bigData, 2000000)
      result should equal (4582667)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (56000011)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (10961118625406L)
    }
  }
}
