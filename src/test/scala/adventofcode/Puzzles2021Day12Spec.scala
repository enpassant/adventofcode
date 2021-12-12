package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day12._

class Puzzles2021Day12Spec extends AnyFunSpec with Matchers {
  val testData = "samples/input_12_test.txt"
  val test2Data = "samples/input_12_test2.txt"
  val test3Data = "samples/input_12_test3.txt"
  val bigData = "samples/input_12.txt"

  describe("Day 12.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (10)
    }
    it("Part one with test 2 data") {
      val result = puzzle(
        test2Data
      )
      result should equal (19)
    }
    it("Part one with test 3 data") {
      val result = puzzle(
        test3Data
      )
      result should equal (226)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (3563)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (36)
    }
    it("Part two with test 2 data") {
      val result = puzzleTwo(
        test2Data
      )
      result should equal (103)
    }
    it("Part two with test 3 data") {
      val result = puzzleTwo(
        test3Data
      )
      result should equal (3509)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (105453)
    }
  }
}
