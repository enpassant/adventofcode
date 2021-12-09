package adventofcode

import org.scalatest.funspec.AnyFunSpec
import adventofcode.Puzzles2021Day7._

class Puzzles2021Day7Spec extends AnyFunSpec {
  val testData = "input_7_test.txt"
  val bigData = "input_7.txt"

  describe("Day 7.") {
    it("Part one with test data") {
      val result = p2021_7(
        testData,
        calcFuelLineary
      )
      assert(result == 37)
    }
    it("Part one with big data") {
      val result = p2021_7(
        bigData,
        calcFuelLineary
      )
      assert(result == 328318)
    }
    it("Part two with test data") {
      val result = p2021_7(
        testData,
        calcFuelExponentally
      )
      assert(result == 168)
    }
    it("Part two with big data") {
      val result = p2021_7(
        bigData,
        calcFuelExponentally
      )
      assert(result == 89791146)
    }
  }
}
