package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day10._

class Puzzles2021Day10Spec extends AnyFunSpec with Matchers {
  val testData = "input_10_test.txt"
  val bigData = "input_10.txt"

  describe("Day 10.") {
    it("Part one with test data") {
      val result = p2021_10(
        testData
      )
      assert(result == 26397)
    }
    it("Part one with big data") {
      val result = p2021_10(
        bigData
      )
      assert(result == 345441)
    }
    it("Part two with test data") {
      val result = p2021_10_2(
        testData
      )
      result should equal (288957)
    }
    it("Part two with big data") {
      val result = p2021_10_2(
        bigData
      )
      result should equal (3235371166L)
    }
  }
}
