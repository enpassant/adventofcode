package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay8._

class PuzzlesDay8Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_8_test.txt"
  val bigData = "samples/2022/input_8.txt"

  describe("Day 8.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (21)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (1538)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (8)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (496125)
    }
  }
}
