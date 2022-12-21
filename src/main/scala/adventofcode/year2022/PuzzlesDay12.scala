package adventofcode.year2022

import scala.io.Source
import scala.collection.immutable.Queue

import adventofcode.Graph

/** https://en.wikipedia.org/wiki/Breadth-first_search */
object PuzzlesDay12 extends App {

  case class Pos(x: Int, y: Int)
  case class PosLength(pos: Pos, length: Int = 0)

  def puzzle(input: String) = {
    val map = read(input)
    val startPos = getPos(map)
    Graph.bfs(
      PosLength(startPos, 0),
      getChildren(map)((from, to) => to - from <= 1),
      v => map(v.pos) == 26
    ).map(_.length)
    .get
  }

  def puzzleTwo(input: String) = {
    val map = read(input)
    val endPos = getPos(map, 26)
    Graph.bfs(
      PosLength(endPos, 0),
      getChildren(map)((from, to) =>  from - to <= 1),
      v => map(v.pos) == 0
    ).map(_.length)
    .get
  }

  def getNeighbours(p: Pos) : Vector[Pos] = {
    Vector(
      Pos(p.x + 1, p.y),
      Pos(p.x, p.y + 1),
      Pos(p.x - 1, p.y),
      Pos(p.x, p.y - 1)
    )
  }

  def getChildren
    (heightMap: Map[Pos, Int])
    (canMove: (Int, Int) => Boolean)
    (node: PosLength): Vector[PosLength]=
  {
    getNeighbours(node.pos)
      .filter(heightMap.contains)
      .filter(p => canMove(heightMap(node.pos), heightMap(p)))
      .map(
        p => PosLength(p, node.length + 1)
      )
  }

  def getPos(map: Map[Pos, Int], value: Int = -1): Pos = {
    map.find((p, h) => h == value)
      .map(_._1)
      .head
  }

  def read(input: String) = {
    Source.fromFile(input)
      .getLines
      .zipWithIndex
      .flatMap(
        (line, y) => line.zipWithIndex.map(
          (ch, x) =>
            if (ch == 'S') (Pos(x, y), -1)
            else if (ch == 'E') (Pos(x, y), 26)
            else (Pos(x, y), ch - 'a')
        )
      )
      .toMap
  }
}
