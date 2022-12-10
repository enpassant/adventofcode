package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay4._

class PuzzlesDay4Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_4_test.txt"
  val bigData = "samples/2022/input_4.txt"

  describe("Day 4.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (2)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (567)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (4)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (907)
    }
  }
}
