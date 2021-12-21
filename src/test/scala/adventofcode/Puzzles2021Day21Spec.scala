package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day21._

class Puzzles2021Day21Spec extends AnyFunSpec with Matchers {
  val testData = "samples/input_21_test.txt"
  val bigData = "samples/input_21.txt"

  describe("Day 21.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (739785)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (597600)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (444356092776315L)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (634769613696613L)
    }
  }
}
