package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay11._

class PuzzlesDay11Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_11_test.txt"
  val bigData = "samples/2022/input_11.txt"

  describe("Day 11.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (10605)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (112815)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (2713310158L)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (25738411485L)
    }
  }
}
