package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day1._

class Puzzles2021Day1Spec extends AnyFunSpec with Matchers {
  val testData = "input_1_test.txt"
  val bigData = "input_1.txt"

  describe("Day 1.") {
    it("Part one with test data") {
      val result = p2021_1(
        testData
      )
      result should equal (7)
    }
    it("Part one with big data") {
      val result = p2021_1(
        bigData
      )
      result should equal (1316)
    }
    it("Part two with test data") {
      val result = p2021_1_2(
        testData
      )
      result should equal (5)
    }
    it("Part two with big data") {
      val result = p2021_1_2(
        bigData
      )
      result should equal (1344)
    }
  }
}
