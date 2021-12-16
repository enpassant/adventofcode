package adventofcode

import scala.io.Source
import java.io.PrintWriter

object Puzzles2021Day15 {
  def puzzle(
    fileName: String
  ) = {
    val data = readData(fileName)
    val maxX = data.keys.map((x, y) => x).max
    val maxY = data.keys.map((x, y) => y).max
    calcRiskLevel(data)(maxX, maxY)
  }

  def puzzleTwo(
    fileName: String
  ) = {
    val data = readData(fileName)
    val maxX = data.keys.map((x, y) => x).max + 1
    val maxY = data.keys.map((x, y) => y).max + 1
    val bigData = (0 to 4).flatMap(y =>
        (0 to 4).map(x =>
          (x, y)
        )
    ).foldLeft(Map[(Int, Int), Int]()) { case (dr, (x, y)) =>
      dr ++ data.map { case ((x1, y1), v) =>
        ((x * maxX + x1, y * maxY + y1) -> ((v + x + y - 1) % 9 + 1))
      }
    }
    calcRiskLevel(bigData)(5 * maxX - 1, 5 * maxY - 1)
  }

  def calcRiskLevel(dataRisk: Map[(Int, Int), Int]) = {
    val pMap = Map[(Int, Int), Int]((0, 0) -> 0)
    val uMap = Map[(Int, Int), Int]()
    step(dataRisk, pMap, uMap)
  }

  def step(
    dataRisk: Map[(Int, Int), Int],
    pMap: Map[(Int, Int), Int],
    uMap: Map[(Int, Int), Int]
  ): Map[(Int, Int), Int] =
  {
    val newPmap = pMap
      .filter { case ((x, y), v) => v < Int.MaxValue && !uMap.contains(x, y) }
    if (newPmap.isEmpty) {
      uMap
    } else {
      val ((piX, piY), piV) = newPmap
        .toList
        .sortBy { case ((x, y), v) => v }
        .head
      val newUmap = uMap + ((piX, piY) -> piV)

      val neighbors = getNeighbors(dataRisk, pMap, uMap, piX, piY)
      val nextPm = neighbors.foldLeft(pMap) { case (pM, ((x, y), v)) => {
        val p = piV + dataRisk(x, y)
        if (p < v) pM + ((x, y) -> p)
        else pM
      }}
      step(dataRisk, nextPm, newUmap)
    }
  }

  def getValue(
    dataRisk: Map[(Int, Int), Int],
    pMap: Map[(Int, Int), Int],
    uMap: Map[(Int, Int), Int],
    x: Int,
    y: Int
  ) = {
    dataRisk.get(x, y).map(_ =>
      uMap.getOrElse((x, y), pMap.getOrElse((x, y), Int.MaxValue))
    )
  }

  def calcPath(dataRisk: Map[(Int, Int), Int]) = {
    val maxX = dataRisk.keys.map((x, y) => x).max
    val maxY = dataRisk.keys.map((x, y) => y).max
    val risk = (maxY to 0 by -1).flatMap(y =>
        (maxX to 0 by -1).map(x =>
          (x, y)
        )
    ).foldLeft(dataRisk) { case (dr, (x, y)) =>
      dr + ((x, y) -> (dataRisk(x, y) + getRisk(dr, x, y)))
    }
    //risk(0, 0) - dataRisk(0, 0)
    risk
  }

  def getNeighbors(
    dataRisk: Map[(Int, Int), Int],
    pMap: Map[(Int, Int), Int],
    uMap: Map[(Int, Int), Int],
    x: Int,
    y: Int
  ) = {
    List(
      (x - 1, y),
      (x, y - 1),
      (x + 1, y),
      (x, y + 1)
    ).map { case (x, y) =>
      getValue(dataRisk, pMap, uMap, x, y).map(((x, y), _))
    }
    .filter(_.isDefined)
    .map(_.get)
  }

  def getRisk(dataRisk: Map[(Int, Int), Int], x: Int, y: Int) = {
    val ls = List(
      dataRisk.get(x - 1, y),
      dataRisk.get(x, y - 1),
      dataRisk.get(x + 1, y),
      dataRisk.get(x, y + 1)
    ).filter(_.isDefined)
    .map(_.get)
    if (ls.isEmpty) 0 else ls.min
  }

  def printRisk(map: Map[(Int, Int), Int]) = {
    val width = map.map { case ((x, y), v) => x }.max + 1
    val height = map.map { case ((x, y), v) => y }.max + 1
    val line = Vector.fill(width)(0)
    val vector = Vector.fill(height)(line)
    map.foldLeft(vector) {
      case (display, ((x, y), v)) =>
        display.updated(y, display(y).updated(x, v))
    }.mkString("\n")
  }

  def printMap(map: Map[(Int, Int), Int]) = {
    val width = map.map { case ((x, y), v) => x }.max + 1
    val height = map.map { case ((x, y), v) => y }.max + 1
    val line = "." * width
    val vector = Vector.fill(height)(line)
    map.foldLeft(vector) {
      case (display, ((x, y), v)) => display.updated(y, display(y).updated(x, (48 + v).toChar))
    }.mkString("\n")
  }

  def readData(
    fileName: String
  ) = {
    Source.fromFile(fileName)
      .getLines
      .zipWithIndex
      .map(v => (v._2, v._1))
      .toMap
      .flatMap((y, line) =>
        line.toCharArray
          .map(_.toInt - 48)
          .zipWithIndex
          .map(v => ((v._2, y), v._1))
          .toMap
      )
  }
}
