package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay18._

class PuzzlesDay18Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_18_test.txt"
  val bigData = "samples/2022/input_18.txt"

  describe("Day 18.") {
    it("Part one with test data") {
      val result = puzzle(testData)
      result should equal (64)
    }
    it("Part one with big data") {
      val result = puzzle(bigData)
      result should equal (4628)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (58)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (2582)
    }
  }
}
