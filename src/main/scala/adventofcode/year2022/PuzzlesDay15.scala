package adventofcode.year2022

import scala.io.Source

object PuzzlesDay15 extends App {
  case class Pos(x: Int, y: Int)
  case class Interval(x1: Int, x2: Int)
  case class Measurement(sensor: Pos, beacon: Pos) {
    val manhattan = calcManhattan(sensor, beacon)
  }

  def puzzle(input: String, targetRow: Int) = {
    val measurements = read(input)
    val concatenated = calcIntervals(measurements)(targetRow)
    concatenated._2.head.x2 - concatenated._2.head.x1
  }

  def puzzleTwo(input: String) = {
    val measurements = read(input)
    val (minPos, maxPos) = measurements
      .foldLeft((Pos(Int.MaxValue, Int.MaxValue), Pos(0, 0))) {
      (m, v) => (
        Pos(Math.min(m._1.x, v.sensor.x), Math.min(m._1.y, v.sensor.y)),
        Pos(Math.max(m._2.x, v.sensor.x), Math.max(m._2.y, v.sensor.y))
      )
    }
    val height = maxPos.y - minPos.y
    val intervals = Stream.range(height / 2, maxPos.y)
      .append(Stream.range(height / 2 - 1, minPos.y, -1))
      .map(calcIntervals(measurements))
    val interval = intervals.filter(_._2.size >= 2).head
    (interval._2.head.x1 - 1) * 4000000L + interval._1
  }

  def calcBlocks(y: Int, m: Measurement) = {
    val dif = m.manhattan - Math.abs(m.sensor.y - y)
    if (dif >= 0) {
      Some(Interval(m.sensor.x - dif, m.sensor.x + dif))
    } else {
      None
    }
  }

  def calcIntervals(measurements: List[Measurement])(targetRow: Int) = {
    val intervals = measurements.map(m => calcBlocks(targetRow, m))
      .filter(_.isDefined)
      .map(_.get)
      .sortBy(_.x1)
    (targetRow, intervals.tail
      .foldLeft(List(intervals.head)) {
        (ls, p) => if (ls.head.x2 < p.x1 - 1) {
          p :: ls
        } else if (ls.head.x2 >= p.x2) {
          ls
        } else {
          Interval(ls.head.x1, p.x2) :: ls.tail
        }
      })
  }

  def calcManhattan(a:Pos, b: Pos) = Math.abs(a.x - b.x) + Math.abs(a.y - b.y)

  def read(input: String) = {
    Source.fromFile(input)
      .getLines
      .filter(!_.isBlank())
      .map(_ match {
        case s"Sensor at x=$sx, y=$sy: closest beacon is at x=$bx, y=$by" =>
          Measurement(Pos(sx.toInt, sy.toInt), Pos(bx.toInt, by.toInt))
      })
      .toList
  }
}
