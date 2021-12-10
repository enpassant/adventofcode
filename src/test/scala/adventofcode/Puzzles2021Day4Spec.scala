package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day4._

class Puzzles2021Day4Spec extends AnyFunSpec with Matchers {
  val testData = "input_4_test.txt"
  val bigData = "input_4.txt"

  describe("Day 4.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (4512)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (8442)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (1924)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (4590)
    }
  }
}
