package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day16._

class Puzzles2021Day16Spec extends AnyFunSpec with Matchers {
  val testData = "samples/input_16_test.txt"
  val bigData = "samples/input_16.txt"

  describe("Day 15.") {
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
      result should equal (971)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (54)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (831996589851L)
    }
  }
}
