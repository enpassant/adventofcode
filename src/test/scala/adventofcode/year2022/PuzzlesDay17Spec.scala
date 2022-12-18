package adventofcode.year2022

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.year2022.PuzzlesDay17._

class PuzzlesDay17Spec extends AnyFunSpec with Matchers {
  val testData = "samples/2022/input_17_test.txt"
  val bigData = "samples/2022/input_17.txt"

  describe("Day 17.") {
    it("Part one with test data") {
      val result = puzzle(testData)
      result should equal (3068)
    }
    it("Part one with big data") {
      val result = puzzle(bigData)
      result should equal (3200)
    }
    it("Part two with test data") {
      val result = puzzleTwo(
        testData
      )
      result should equal (1514285714288L)
    }
    it("Part two with big data") {
      val result = puzzleTwo(
        bigData
      )
      result should equal (1584927536247L)
    }
  }
}
