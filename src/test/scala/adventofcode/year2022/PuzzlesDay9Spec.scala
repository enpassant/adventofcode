package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay9._

class PuzzlesDay9Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_9_test.txt"
  val test2Data = "samples/2022/input_9_test2.txt"
  val bigData = "samples/2022/input_9.txt"

  describe("Day 9.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (13)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (6243)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (1)
    }
    it("Part two with test 2 data") {
      val result = puzzleTwo(
        test2Data
      )
      result should equal (36)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (2630)
    }
  }
}
