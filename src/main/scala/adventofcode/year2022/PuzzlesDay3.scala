package adventofcode.year2022

import scala.io.Source

object PuzzlesDay3 extends App {

  def puzzle(
    input: String
  ) = {
    val map = read(input)
    map.map(line => {
      val size = line.size
      (line.slice(0, size / 2), line.slice(size / 2, size))
    })
    .map(v => v._1.intersect(v._2).head)
    .map(calcPriority)
    .sum
  }

  def puzzleTwo(
    input: String
  ) = {
    val map = read(input)
    map.grouped(3)
      .map(seq =>
          seq(0).intersect(seq(1))
            .intersect(seq(2))
            .head
      )
      .map(calcPriority)
      .sum
  }

  def calcPriority(ch: Char) = {
    if (ch.isLower) ch - 'a' + 1 else ch - 'A' + 27
  }

  def read(
    input: String
  ) = {
    Source.fromFile(input)
        .getLines
  }
}
