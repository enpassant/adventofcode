package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay16._

class PuzzlesDay16Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_16_test.txt"
  val bigData = "samples/2022/input_16.txt"

  describe("Day 16.") {
    it("Part one with test data") {
      val result = puzzle(testData)
      result should equal (1651)
    }
    it("Part one with big data") {
      val result = puzzle(bigData)
      result should equal (1728)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (1707)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (2304)
    }
  }
}
