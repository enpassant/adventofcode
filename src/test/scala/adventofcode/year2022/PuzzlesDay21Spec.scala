package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay21._

class PuzzlesDay21Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_21_test.txt"
  val bigData = "samples/2022/input_21.txt"

  describe("Day 21.") {
    it("Part one with test data") {
      val result = puzzle(testData)
      result should equal (152)
    }
    it("Part one with big data") {
      val result = puzzle(bigData)
      result should equal (158661812617812L)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (301)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (3352886133831L)
    }
  }
}
