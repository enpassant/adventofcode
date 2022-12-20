package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay20._

class PuzzlesDay20Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_20_test.txt"
  val bigData = "samples/2022/input_20.txt"

  describe("Day 20.") {
    it("Part one with test data") {
      val result = puzzle(testData)
      result should equal (3)
    }
    it("Part one with big data") {
      val result = puzzle(bigData)
      result should equal (9866)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (1623178306L)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (12374299815791L)
    }
  }
}
