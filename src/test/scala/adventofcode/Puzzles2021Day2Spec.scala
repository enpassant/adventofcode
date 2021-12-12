package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day2._

class Puzzles2021Day2Spec extends AnyFunSpec with Matchers {
  val testData = "samples/input_2_test.txt"
  val bigData = "samples/input_2.txt"

  describe("Day 2.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (150)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (1654760)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (900)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (1956047400)
    }
  }
}
