package adventofcode.year2022

import scala.io.Source

object PuzzlesDay8 extends App {
  case class Position(x: Int, y: Int)

  def puzzle(
    input: String
  ) = {
    val map = read(input)
    val visibles = calcVisible(map)
    visibles.size
  }

  def puzzleTwo(
    input: String
  ) = {
    val map = read(input)
    calcScenicScore(map)
  }

  def calcVisible(map: Vector[Vector[Int]]): Set[Position] = {
    val width = map(0).size
    val height = map.size
    val left = (0 until height).map(row =>
      calcVisibleRow(
        map,
        Position(0, row),
        p => Position(p.x + 1, p.y),
        _.x < width,
        -1,
        Set()
      )
    ).reduce(_ ++ _)
    val right = (0 until height).map(row =>
      calcVisibleRow(
        map,
        Position(width - 1, row),
        p => Position(p.x - 1, p.y),
        _.x >= 0,
        -1,
        Set()
      )
    ).reduce(_ ++ _)
    val top = (0 until width).map(col =>
      calcVisibleRow(
        map,
        Position(col, 0),
        p => Position(p.x, p.y + 1),
        _.y < height,
        -1,
        Set()
      )
    ).reduce(_ ++ _)
    val bottom = (0 until width).map(col =>
      calcVisibleRow(
        map,
        Position(col, height - 1),
        p => Position(p.x, p.y - 1),
        _.y >= 0,
        -1,
        Set()
      )
    ).reduce(_ ++ _)

    left ++ right ++ top ++ bottom
  }

  def calcVisibleRow(
    map: Vector[Vector[Int]],
    start: Position,
    increment: Position => Position,
    predicate: Position => Boolean,
    max: Int,
    visible: Set[Position]
  ) : Set[Position] = {
    if (predicate.apply(start)) {
      val treeHeight = map(start.y)(start.x)
      if (treeHeight > max) {
        calcVisibleRow(
          map,
          increment.apply(start),
          increment,
          predicate,
          treeHeight,
          visible + start
        )
      } else {
        calcVisibleRow(
          map,
          increment.apply(start),
          increment,
          predicate,
          max,
          visible
        )
      }
    } else {
      visible
    }
  }

  def calcScenicScore(map: Vector[Vector[Int]]): Int = {
    val width = map(0).size
    val height = map.size
    val scenicScores = for {
      row <- 0 until height
      col <- 0 until width
    } yield calcScenicScoreAtPos(map, Position(col, row))
    scenicScores.max
  }

  def calcScenicScoreAtPos(
    map: Vector[Vector[Int]],
    position: Position
  ): Int = {
    val width = map(0).size
    val height = map.size
    val treeHeight = map(position.y)(position.x)
    val right = calcVisibleTrees(
      map,
      position,
      p => Position(p.x + 1, p.y),
      _.x < width,
      treeHeight,
      0
    )
    val left = calcVisibleTrees(
      map,
      position,
      p => Position(p.x - 1, p.y),
      _.x >= 0,
      treeHeight,
      0
    )
    val top = calcVisibleTrees(
      map,
      position,
      p => Position(p.x, p.y - 1),
      _.y >= 0,
      treeHeight,
      0
    )
    val bottom = calcVisibleTrees(
      map,
      position,
      p => Position(p.x, p.y + 1),
      _.y < height,
      treeHeight,
      0
    )
    //println(s"$top * $left * $right * $bottom")
    left * right * top * bottom
  }

  def calcVisibleTrees(
    map: Vector[Vector[Int]],
    start: Position,
    increment: Position => Position,
    predicate: Position => Boolean,
    max: Int,
    count: Int
  ) : Int = {
    val nextPos = increment.apply(start)
    if (predicate.apply(nextPos)) {
      val treeHeight = map(nextPos.y)(nextPos.x)
      if (treeHeight < max) {
        calcVisibleTrees(
          map,
          increment.apply(start),
          increment,
          predicate,
          max,
          count + 1
        )
      } else {
        count + 1
      }
    } else {
      count
    }
  }

  def read(
    input: String
  ) = {
    Source.fromFile(input)
        .getLines
        .map(line => line.map(_.toInt - 48).toVector)
        .toVector
  }
}
