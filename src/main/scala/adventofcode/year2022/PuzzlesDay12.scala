package adventofcode.year2022

import scala.io.Source
import scala.collection.immutable.Queue

/** https://en.wikipedia.org/wiki/Breadth-first_search */
object PuzzlesDay12 extends App {

  case class Pos(x: Int, y: Int)
  case class PosLength(pos: Pos, length: Int = 0)

  def puzzle(input: String) = {
    val map = read(input)
    val startPos = getPos(map)
    walk(map, startPos, _ == 26, (from, to) => to - from <= 1)
  }

  def puzzleTwo(input: String) = {
    val map = read(input)
    val endPos = getPos(map, 26)
    walk(map, endPos, _ == 0, (from, to) => from - to <= 1)
  }

  def walk(
    heightMap: Map[Pos, Int],
    beginPos: Pos,
    isGoal: Int => Boolean,
    canMove: (Int, Int) => Boolean
  ): Int = {
    def loop(visited: Set[Pos], queue: Queue[PosLength]): Int = {
      if (queue.isEmpty) {
        0
      } else {
        val (pathLength, newQueue) = queue.dequeue
        val pos = pathLength.pos
        val height = heightMap(pos)
        val neighbours = getNeighbours(pos)
          .filter(p => !visited.contains(pos))
          .filter(heightMap.contains)
          .filter(p => canMove(height, heightMap(p)))
        if (neighbours.exists(p => isGoal(heightMap(p)))) {
          pathLength.length + 1
        } else {
          val neighboursPath = neighbours.map(
            p => PosLength(p, pathLength.length + 1)
          )
          loop(
            visited + pos,
            newQueue.enqueue(neighboursPath)
          )
        }
      }
    }

    loop(Set(), Queue(PosLength(beginPos, 0)))
  }

  def getNeighbours(p: Pos) : List[Pos] = {
    Pos(p.x + 1, p.y)
      :: Pos(p.x, p.y + 1)
      :: Pos(p.x - 1, p.y)
      :: Pos(p.x, p.y - 1)
      :: Nil
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
