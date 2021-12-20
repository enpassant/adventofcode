package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day20._

class Puzzles2021Day20Spec extends AnyFunSpec with Matchers {
  val testData = "samples/input_20_test.txt"
  val bigData = "samples/input_20.txt"

  describe("Day 20.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (35)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (5884)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (3351)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (19043)
    }
  }
}
