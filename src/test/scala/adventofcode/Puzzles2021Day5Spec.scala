package adventofcode

import org.scalatest.funspec.AnyFunSpec
import adventofcode.Puzzles2021Day5._

class Puzzles2021Day5Spec extends AnyFunSpec {
  describe("Day 5.") {
    it("Part one with test data") {
      val result = p2021_5(
          "samples/input_5_test.txt",
          isNotDiagonal
        )
      assert(result == 5)
    }
    it("Part one with big data") {
      val result = p2021_5(
          "samples/input_5.txt",
          isNotDiagonal
        )
      assert(result == 6710)
    }
    it("Part two with test data") {
      val result = p2021_5(
          "samples/input_5_test.txt",
          every
        )
      assert(result == 12)
    }
    it("Part two with big data") {
      val result = p2021_5(
          "samples/input_5.txt",
          every
        )
      assert(result == 20121)
    }
  }
}
