package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay3._

class PuzzlesDay3Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_3_test.txt"
  val bigData = "samples/2022/input_3.txt"

  describe("Day 3.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (157)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (8085)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (70)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (2515)
    }
  }
}
