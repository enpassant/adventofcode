package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day9._

class Puzzles2021Day9Spec extends AnyFunSpec with Matchers {
  val testData = "input_9_test.txt"
  val bigData = "input_9.txt"

  describe("Day 9.") {
    it("Part one with test data") {
      val result = p2021_7(
        testData
      )
      result should equal (15)
    }
    it("Part one with big data") {
      val result = p2021_7(
        bigData
      )
      result should equal (514)
    }
    it("Part two with test data") {
      val result = p2021_7_2(
        testData
      )
      result should equal (1134)
    }
    it("Part two with big data") {
      val result = p2021_7_2(
        bigData
      )
      result should equal (1103130)
    }
  }
}
