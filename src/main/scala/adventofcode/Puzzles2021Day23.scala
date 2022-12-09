package adventofcode

import scala.io.Source
import java.io.PrintWriter

object Puzzles2021Day23 {
  def puzzle(
    fileName: String
  ) = {
    val data = readData(fileName)
    data
  }

  def puzzleTwo(
    fileName: String
  ) = {
    val data = readData(fileName)
  }

  def readData(
    fileName: String
  ) = {
    val pattern = "--- scanner (\\d+) ---".r
    Source.fromFile(fileName)
      .getLines
      .drop(2)
      .take(2)
      .toList
  }
}
