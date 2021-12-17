package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day17._

class Puzzles2021Day17Spec extends AnyFunSpec with Matchers {
  val testData = "samples/input_17_test.txt"
  val bigData = "samples/input_17.txt"

  describe("Day 15.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (45)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (8911)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (112)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (4748)
    }
  }
}
