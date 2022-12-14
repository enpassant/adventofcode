package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay14._

class PuzzlesDay14Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_14_test.txt"
  val bigData = "samples/2022/input_14.txt"

  describe("Day 14.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (24)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (578)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (93)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (24377)
    }
  }
}
