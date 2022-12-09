package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay7._

class PuzzlesDay7Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_7_test.txt"
  val bigData = "samples/2022/input_7.txt"

  describe("Day 7.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (95437)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (1501149)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (24933642)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (10096985)
    }
  }
}
