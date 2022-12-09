package adventofcode

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import adventofcode.Puzzles2021Day22._

class Puzzles2021Day22Spec extends AnyFunSpec with Matchers {
  val testData = "samples/input_22_test.txt"
  val testData2 = "samples/input_22_test_2.txt"
  val bigData = "samples/input_22.txt"

  describe("Day 22.") {
    it("Part one with test data") {
      val result = puzzle(
        testData
      )
      result should equal (39)
    }
    it("Part one with test data 2") {
      val result = puzzle(
        testData2
      )
      result should equal (590784)
    }
    it("Part one with big data") {
      val result = puzzle(
        bigData
      )
      result should equal (615869)
    }
    //it("Part two with test data") {
      //val result = puzzleTwo(
        //testData
      //)
      //result should equal (3351)
    //}
    //it("Part two with big data") {
      //val result = puzzleTwo(
        //bigData
      //)
      //result should equal (22043)
    //}
  }
}
