package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay13._

class PuzzlesDay13Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_13_test.txt"
  val bigData = "samples/2022/input_13.txt"

  describe("Day 13.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (13)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (5529)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (140)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (27690)
    }
  }
}
