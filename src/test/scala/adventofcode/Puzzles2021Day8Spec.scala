package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day8._

class Puzzles2021Day8Spec extends AnyFunSpec with Matchers {
  val testData = "samples/input_8_test.txt"
  val test2Data = "samples/input_8_test2.txt"
  val bigData = "samples/input_8.txt"

  describe("Day 8.") {
    it("Part one with test data") {
      val result = p2021_8(
        test2Data
      )
      result should equal (26)
    }
    it("Part one with big data") {
      val result = p2021_8(
        bigData
      )
      result should equal (365)
    }
    it("Part two with test data") {
      val result = p2021_8_2(
        testData
      )
      result should equal (5353)
    }
    it("Part two with test 2 data") {
      val result = p2021_8_2(
        test2Data
      )
      result should equal (61229)
    }
    it("Part two with big data") {
      val result = p2021_8_2(
        bigData
      )
      result should equal (975706)
    }
  }
}
