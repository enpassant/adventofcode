package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay12._

class PuzzlesDay12Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_12_test.txt"
  val bigData = "samples/2022/input_12.txt"

  describe("Day 12.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (31)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (380)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (29)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (375)
    }
  }
}
