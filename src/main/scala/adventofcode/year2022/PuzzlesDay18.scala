package adventofcode.year2022

import scala.io.Source

object PuzzlesDay18 extends App {

  case class Pos(x: Int, y: Int, z: Int) {
    def move(pos: Pos) = Pos(x + pos.x, y + pos.y, z + pos.z)
  }
  type Lava = Set[Pos]

  val sides = Vector(
    Pos(-1, 0, 0), Pos(0, -1, 0), Pos(0, 0, -1),
    Pos(1, 0, 0), Pos(0, 1, 0), Pos(0, 0, 1)
  )

  def puzzle(input: String) = {
    val data = read(input)
    data.toSeq
      .map(calcFreeSize(data))
      .sum
  }

  def puzzleTwo(input: String) = {
    val data = read(input)
    val min = Pos(data.map(_.x).min, data.map(_.y).min, data.map(_.z).min)
    val max = Pos(data.map(_.x).max, data.map(_.y).max, data.map(_.z).max)
    val airMap = buildFreeAir(data)
    val airTRapped= for {
      x <- min.x to max.x
      y <- min.y to max.y
      z <- min.z to max.z
      if (!data.contains(Pos(x, y, z)) && !airMap(x)(y)(z))
    } yield Pos(x, y, z)
    data.toSeq
      .map(calcFreeSizeAirTrapped(data, airTRapped))
      .sum
  }

  def buildFreeAir(lava: Lava) = {
    val min = Pos(lava.map(_.x).min, lava.map(_.y).min, lava.map(_.z).min)
    val max = Pos(lava.map(_.x).max, lava.map(_.y).max, lava.map(_.z).max)
    val airMap = Array.fill(max.x + 1, max.y + 1, max.z + 1)(false)

    def checkFree(p: Pos) = {
      p.x == min.x || p.x == max.x ||
      p.y == min.y || p.y == max.y ||
      p.z == min.z || p.z == max.z ||
      sides.map(p.move(_))
        .filter(
          side => airMap(side.x)(side.y)(side.z)
        ).size > 0
    }

    def loop(): Int = {
      val test1 = for {
        x <- min.x to max.x
        y <- min.y to max.y
        z <- min.z to max.z
        if (!airMap(x)(y)(z))
        if (!lava.contains(Pos(x, y, z)) && checkFree(Pos(x, y, z)))
      } yield {
        airMap(x)(y)(z) = true
        true
      }

      val test2 = for {
        x <- Range(max.x, min.x, -1)
        y <- Range(max.y, min.y, -1)
        z <- Range(max.z, min.z, -1)
        if (!airMap(x)(y)(z))
        if (!lava.contains(Pos(x, y, z)) && checkFree(Pos(x, y, z)))
      } yield {
        airMap(x)(y)(z) = true
        true
      }

      if (test1.size > 0 || test2.size > 0) {
        loop()
      } else {
        0
      }
    }
    loop()

    airMap
  }

  def calcFreeSize(lava: Lava)(pos: Pos) = {
    sides.filter(
      side => !lava.contains(pos.move(side))
    ).size
  }

  def calcFreeSizeAirTrapped(lava: Lava, airTrapped: Seq[Pos])(pos: Pos) = {
    sides.map(pos.move(_))
      .filter(
        side => !lava.contains(side) && !airTrapped.contains(side)
      ).size
  }

  def read(input: String) = {
    //val lava = Array.fill(20, 20, 20)(false)
    Source.fromFile(input)
      .getLines
      .filter(!_.isBlank())
      .map(_.split(",").map(_.toInt))
      .map { case Array(x, y, z) => Pos(x, y, z) }
      .toSet
      //.foreach(a => lava(a(0))(a(1))(a(2)) = true)
    //lava
  }
}
