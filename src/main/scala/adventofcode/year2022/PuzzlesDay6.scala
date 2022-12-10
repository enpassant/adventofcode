package adventofcode.year2022

import scala.io.Source

object PuzzlesDay6 extends App {

  def puzzle(
    input: String
  ) = {
    val map = read(input)
    map.map(searchDiffPos(4))
  }

  def puzzleTwo(
    input: String
  ) = {
    val map = read(input)
    map.map(searchDiffPos(14))
  }

  def searchDiffPos(length: Int)(str: String) = {
    searchSets(
        str,
        length,
        str.map(Set(_)).toList
      )
      .map(_.size)
      .zipWithIndex
      .filter(_._1 >= length)
      .head
      ._2 + length
  }

  def searchSets(str: String, length: Int, sets: List[Set[Char]])
    : List[Set[Char]] =
  {
    if (length <= 1) {
      sets
    } else {
      val newSets = sets.zip(str.tail)
        .map((set, ch) => set + ch)
      searchSets(str.tail, length - 1, newSets)
    }
  }

  def read(
    input: String
  ) = {
    Source.fromFile(input)
        .getLines
        .toList
  }
}
