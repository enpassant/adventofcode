package adventofcode

import scala.io.Source

object Puzzles2021Day11 {
  def puzzle(
    fileName: String
  ) = {
    val data = readMap(fileName)

    (1 to 100).foldLeft((data, 0)) {
      case ((table, count), i) => {
        val newTable = step(table)
        (newTable, count + countFlashes(newTable))
      }
    }._2
  }

  def puzzleTwo(
    fileName: String
  ) = {
    val data = readMap(fileName)
    runToFirstFullFlash(data, 1)
  }

  def runToFirstFullFlash(data: Table, stepCount: Int): Int = {
    val newTable = step(data)
    if (countFlashes(newTable) >= 100) stepCount
    else runToFirstFullFlash(newTable, stepCount + 1)
  }

  type Table = Map[(Int, Int), Int]

  def readMap(fileName: String): Table = {
    Source.fromFile(fileName)
        .getLines
        .zipWithIndex
        .map(v => (v._2, v._1))
        .toMap
        .flatMap((x, line) =>
            line.toCharArray
              .map(_.toInt - 48)
              .zipWithIndex
              .map(v => ((x, v._2), v._1))
              .toMap
        )
  }

  def getAdjacents(map: Table, x: Int, y: Int) = {
    map.filter {
      case ((x1, y1), v) =>
        (v < 10) && (x1 != x || y1 != y) && (
          (Math.abs(x1 - x) <= 1) &&
          (Math.abs(y1 - y) <= 1)
        )
    }
  }

  def readData(
    fileName: String
  ) = {
    Source.fromFile(fileName)
      .getLines
      .map(_.toCharArray().toVector.map(_.toInt - 48))
      .toVector
  }

  def step(data: Table) = {
    increase
      .andThen(flash)
      .andThen(finishStep)
      (data)
  }

  def increase(data: Table) = {
    data.map {
      case ((x, y), v) => ((x, y), v + 1)
    }
  }

  def finishStep(data: Table) = {
    data.map {
      case ((x, y), v) => if (v > 9) ((x, y), 0) else ((x, y), v)
    }
  }

  def countFlashes(data: Table) = {
    data.foldLeft(0) {
      case (count, ((x, y), v)) => if (v == 0) count + 1 else count
    }
  }

  def flash(data: Table): Table = {
    val newData = data.foldLeft(data) {
      case (table, ((x, y), v)) =>
        if (v > 9 && v < 1000) {
          table ++ increase(getAdjacents(table, x, y)) + ((x, y) -> 1000)
        } else table
    }
    if (newData == data) newData
    else flash(newData)
  }

  def printTable(data: Table) = {
    (0 to 9).foreach(x =>
      println((0 to 9).map(y => data((x, y)))
        .map(v => if (v > 9) 0 else v))
    )
    println
  }
}
