package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay19._

class PuzzlesDay19Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_19_test.txt"
  val bigData = "samples/2022/input_19.txt"

  describe("Day 19.") {
    it("Part one with test data") {
      val result = puzzle(testData)
      result should equal (33)
    }
    it("Part one with big data") {
      val result = puzzle(bigData)
      result should equal (1009)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (3472)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (18816)
    }
  }
}
