package adventofcode.year2022

import scala.io.Source

object PuzzlesDay4 extends App {

  def puzzle(
    input: String
  ) = {
    val map = read(input)
    map.filter(isContains)
      .size
  }

  def puzzleTwo(
    input: String
  ) = {
    val map = read(input)
    map.filter(isOverlap)
      .size
  }

  def isContains(ranges: (Range, Range)) = {
    ranges._1.containsSlice(ranges._2) ||
      ranges._2.containsSlice(ranges._1)
  }

  def isOverlap(ranges: (Range, Range)) = {
    ranges._1.start >= ranges._2.start && ranges._1.start <= ranges._2.end
      || ranges._2.start >= ranges._1.start && ranges._2.start <= ranges._1.end
  }

  def read(
    input: String
  ) = {
    Source.fromFile(input)
        .getLines
        .map(line => line match {
          case s"$from1-$to1,$from2-$to2" =>
            (from1.toInt to to1.toInt, from2.toInt to to2.toInt)
        })
        .toList
  }
}
