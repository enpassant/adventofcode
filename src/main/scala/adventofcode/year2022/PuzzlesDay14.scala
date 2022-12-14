package adventofcode.year2022

import scala.io.Source

object PuzzlesDay14 extends App {
  val sand = 'o'
  val rock = '#'
  val air = '.'
  val source = '+'
  val floor = 2

  case class Pos(x: Int, y: Int)

  def puzzle(input: String) = {
    val lines = read(input)
    buildMap(lines, 1)
  }

  def puzzleTwo(input: String) = {
    val lines = read(input)
    buildMap(lines, 2)
  }

  def buildMap(lines: List[List[Pos]], puzzleMode: Int) = {
    val startPos = Pos(500, 0)
    val (mi, ma) = (List(startPos) :: lines)
      .flatten
      .foldLeft((Pos(Int.MaxValue, Int.MaxValue), Pos(0, 0))) {
      (m, v) => (
        Pos(Math.min(m._1.x, v.x), Math.min(m._1.y, v.y)),
        Pos(Math.max(m._2.x, v.x), Math.max(m._2.y, v.y))
      )
    }
    val height = ma.y - mi.y + floor + 1
    val minPos = Pos(Math.min(startPos.x - height, mi.x) - 1, mi.y)
    val maxPos = Pos(Math.max(startPos.x + height, mi.x) + 1, ma.y + floor)
    val width = maxPos.x - minPos.x + 1
    val emptyMap = Vector.fill(height)(Vector.fill(width)(air))

    def updateMap(pos: Pos, ch: Char, map: Vector[Vector[Char]]) = {
      map.updated(
        pos.y - minPos.y,
        map(pos.y - minPos.y).updated(pos.x - minPos.x, ch)
      )
    }

    def getMap(pos: Pos, map: Vector[Vector[Char]]) = {
      map(pos.y - minPos.y)(pos.x - minPos.x)
    }

    def isOut(pos: Pos) = {
      pos.y < minPos.y ||
      pos.y > maxPos.y ||
      pos.x < minPos.x ||
      pos.x > maxPos.x
    }

    def isBlocked(pos: Pos, map: Vector[Vector[Char]]) = {
      val ch = map(pos.y - minPos.y)(pos.x - minPos.x)
      ch == rock || ch == sand
    }

    val startMap = updateMap(startPos, source, emptyMap)

    //printMap(startMap)

    def drawLine(s: Pos, e: Pos, map: Vector[Vector[Char]]) = {
      if (s.x == e.x) {
        val st = if (e.y > s.y) 1 else -1
        Range.inclusive(s.y, e.y, st).foldLeft(map) { (m, y) =>
          updateMap(Pos(s.x, y), rock, m)
        }
      } else {
        val st = if (e.x > s.x) 1 else -1
        Range.inclusive(s.x, e.x, st).foldLeft(map) { (m, x) =>
          updateMap(Pos(x, s.y), rock, m)
        }
      }
    }

    val newLines = if (puzzleMode == 1) {
      lines
    } else {
      List(List(Pos(minPos.x, maxPos.y), Pos(maxPos.x, maxPos.y))) ::: lines
    }
    val map = newLines.foldLeft(startMap) { (m, positions) =>
      val s = positions.head
      positions.tail.foldLeft((m, s)) { case ((map, sp), pos) =>
        (drawLine(sp, pos, map), pos)
      }._1
    }

    //printMap(map)

    def countSand(map: Vector[Vector[Char]]) = {
      map.flatten.filter(_ == sand).size
    }

    def fallSand(pos: Pos, map: Vector[Vector[Char]]): Vector[Vector[Char]] = {
      val ch = getMap(pos, map)
      if (ch == sand || isOut(pos)) {
        map
      } else {
        val below = Pos(pos.x, pos.y + 1)
        if (isOut(below)) {
          map
        } else if (isBlocked(below, map)) {
          val left = Pos(pos.x - 1, pos.y + 1)
          if (isOut(left)) {
            map
          } else if (isBlocked(left, map)) {
            val right = Pos(pos.x + 1, pos.y + 1)
            if (isOut(right)) {
              map
            } else if (isBlocked(right, map)) {
              fallSand(startPos, updateMap(pos, sand, map))
            } else {
              fallSand(right, map)
            }
          } else {
            fallSand(left, map)
          }
        } else {
          fallSand(below, map)
        }
      }
    }

    val fullMap = fallSand(startPos, map)

    countSand(fullMap)
  }

  def printMap(map: Vector[Vector[Char]]) = {
    map.foreach(v => {
      v.foreach(ch => print(ch))
      println()
    })
    println()
  }

  def read(input: String) = {
    Source.fromFile(input)
      .getLines
      .filter(!_.isBlank())
      .map(_.split("->")
        .map(_.trim)
        .map(_.split(","))
        .map(a => Pos(a(0).toInt, a(1).toInt))
        .toList
      )
      .toList
  }
}
