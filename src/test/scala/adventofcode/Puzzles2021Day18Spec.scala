package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day18._

class Puzzles2021Day18Spec extends AnyFunSpec with Matchers {
  val testData = "samples/input_18_test.txt"
  val bigData = "samples/input_18.txt"

  describe("Day 18.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (4140)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (3981)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (3993)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (4687)
    }
  }
}
