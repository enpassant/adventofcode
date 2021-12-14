package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day14._

class Puzzles2021Day14Spec extends AnyFunSpec with Matchers {
  val testData = "samples/input_14_test.txt"
  val bigData = "samples/input_14.txt"

  describe("Day 14.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (1588)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (2509)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (2188189693529L)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (2827627697643L)
    }
  }
}
