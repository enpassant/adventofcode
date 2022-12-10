package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay6._

class PuzzlesDay6Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_6_test.txt"
  val bigData = "samples/2022/input_6.txt"

  describe("Day 6.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (List(7,5,6,10,11))
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (List(1480))
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (List(19, 23, 23, 29, 26))
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (List(2746))
    }
  }
}
