package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day11._

class Puzzles2021Day11Spec extends AnyFunSpec with Matchers {
  val testData = "input_11_test.txt"
  val bigData = "input_11.txt"

  describe("Day 11.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (1656)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (1688)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (195)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (403)
    }
  }
}
