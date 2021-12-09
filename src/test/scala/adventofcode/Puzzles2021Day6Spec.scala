package adventofcode

import org.scalatest.funspec.AnyFunSpec
import adventofcode.Puzzles2021Day6._

class Puzzles2021Day6Spec extends AnyFunSpec {
  val testData = "3,4,3,1,2"
  val bigData = "5,1,2,1,5,3,1,1,1,1,1,2,5,4,1,1,1,1,2,1,2,1,1,1,1,1,2,1,5,1,1,1,3,1,1,1,3,1,1,3,1,1,4,3,1,1,4,1,1,1,1,2,1,1,1,5,1,1,5,1,1,1,4,4,2,5,1,1,5,1,1,2,2,1,2,1,1,5,3,1,2,1,1,3,1,4,3,3,1,1,3,1,5,1,1,3,1,1,4,4,1,1,1,5,1,1,1,4,4,1,3,1,4,1,1,4,5,1,1,1,4,3,1,4,1,1,4,4,3,5,1,2,2,1,2,2,1,1,1,2,1,1,1,4,1,1,3,1,1,2,1,4,1,1,1,1,1,1,1,1,2,2,1,1,5,5,1,1,1,5,1,1,1,1,5,1,3,2,1,1,5,2,3,1,2,2,2,5,1,1,3,1,1,1,5,1,4,1,1,1,3,2,1,3,3,1,3,1,1,1,1,1,1,1,2,3,1,5,1,4,1,3,5,1,1,1,2,2,1,1,1,1,5,4,1,1,3,1,2,4,2,1,1,3,5,1,1,1,3,1,1,1,5,1,1,1,1,1,3,1,1,1,4,1,1,1,1,2,2,1,1,1,1,5,3,1,2,3,4,1,1,5,1,2,4,2,1,1,1,2,1,1,1,1,1,1,1,4,1,5"

  describe("Day 6.") {
    it("Part one with test data") {
      val result = p2021_6(testData, 80)
      assert(result == 5934)
    }
    it("Part one with big data") {
      val result = p2021_6(bigData, 80)
      assert(result == 383160)
    }
    it("Part two with test data") {
      val result = p2021_6(testData, 256)
      assert(result == 26984457539L)
    }
    it("Part two with big data") {
      val result = p2021_6(bigData, 256)
      assert(result == 1721148811504L)
    }
  }
}
