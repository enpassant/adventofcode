package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day15._

class Puzzles2021Day15Spec extends AnyFunSpec with Matchers {
  val testData = "samples/input_15_test.txt"
  val bigData = "samples/input_15.txt"
  val testData2 = "samples/input_15_test_2.txt"

  describe("Day 15.") {
    it("Part one with test data 2") {
      val result = puzzle(
        testData2
      )
      result should equal (8)
    }
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (40)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (702)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (315)
    }
    it("Part two with test data 2") {
      val result = puzzleTwo(
        testData2
      )
      result should equal (158)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (2967)
    }
  }
}
