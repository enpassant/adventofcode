package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay5._

class PuzzlesDay5Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_5_test.txt"
  val bigData = "samples/2022/input_5.txt"

  describe("Day 5.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal ("CMZ")
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal ("TPGVQPFDH")
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal ("MCD")
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal ("DMRDFRHHH")
    }
  }
}
