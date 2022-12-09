package adventofcode

import scala.io.Source
import java.io.PrintWriter

object Puzzles2021Day22 {
  def puzzle(
    fileName: String
  ) = {
    val data = readData(fileName)
    (-50 to 50).toStream.flatMap(x =>
      (-50 to 50).toStream.flatMap(y =>
        (-50 to 50).toStream.map(z =>
            (x, y, z)
        )
      )
    ).filter { case (x, y, z) => data.foldLeft(false) {
      case(b, operation) => operation match {
        case op: On => b || in(op, x, y, z)
        case op: Off => b && !in(op, x, y, z)
      }
    }}.length
  }

  def in(op: Op, x: Int, y: Int, z: Int) =
    (x >= op.x1 && x <= op.x2) &&
    (y >= op.y1 && y <= op.y2) &&
    (z >= op.z1 && z <= op.z2)

  sealed trait Op {
    def x1: Int
    def x2: Int
    def y1: Int
    def y2: Int
    def z1: Int
    def z2: Int
  }
  case class On(x1: Int, x2: Int, y1: Int, y2: Int, z1: Int, z2: Int)
    extends Op
  case class Off(x1: Int, x2: Int, y1: Int, y2: Int, z1: Int, z2: Int)
    extends Op

  def readData(
    fileName: String
  ) = {
    val pattern = "(on|off) x=(-*\\d+)..(-*\\d+),y=(-*\\d+)..(-*\\d+),z=(-*\\d+)..(-*\\d+)".r
    Source.fromFile(fileName)
      .getLines
      .map(s => s match {
        case pattern("on", x1, x2, y1, y2, z1, z2) =>
          On(x1.toInt, x2.toInt, y1.toInt, y2.toInt, z1.toInt, z2.toInt)
        case pattern("off", x1, x2, y1, y2, z1, z2) =>
          Off(x1.toInt, x2.toInt, y1.toInt, y2.toInt, z1.toInt, z2.toInt)
      }).toList
  }
}
