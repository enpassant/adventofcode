package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay2._

class PuzzlesDay2Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_2_test.txt"
  val bigData = "samples/2022/input_2.txt"

  describe("Day 2.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (15)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (13484)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (12)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (13433)
    }
  }
}
