package adventofcode.year2022

import scala.io.Source

object PuzzlesDay17 extends App {
  type Rock = Vector[String]
  type Chamber = Array[Array[Char]]

  case class Pos(x: Int, y: Int)

  val rockHoriz = toRock("####")
  val rockPlus = toRock(".#.\n###\n.#.")
  val rockL = toRock("..#\n..#\n###")
  val rockVerical = toRock("#\n#\n#\n#")
  val rockSquare = toRock("##\n##")

  val rocks = Vector(rockHoriz, rockPlus, rockL, rockVerical, rockSquare)

  val chamberWidth = 7

  def puzzle(input: String) = {
    val data = read(input)
    simulate(2022, data)
  }

  def puzzleTwo(input: String) = {
    val data = read(input)
    simulate(1000000000000L, data)
  }

  case class Stat(
    map: Map[(Int, Int),(Int, Int)] = Map(),
    highests: Vector[Int] = Vector(),
    lastResult: Long = 0L
  ) {
    def addValue(rockNum: Int, gasIdx: Int, highest: Int) = {
      Stat(
         map + ((rockNum, gasIdx) -> (highests.size, highest)),
         highests :+ highest
      )
    }
    def getResult(rockNum: Int, gasIdx: Int, highest: Int) = {
      val count = highests.size
      val key = (rockNum, gasIdx)
      val highestOpt = map.get(key)
      highestOpt.map { case (c,h) =>
        val cd = count - c
        val hd = highest - h
        val interval = (1000000000000L - c)
        val modulo = interval % cd
        val height1 = highests(c)
        val height2 = highests(c + modulo.toInt) - height1
        val result = interval / cd * hd + height1 + height2
        copy(lastResult = result)
      }.getOrElse(this)
    }
  }

  def simulate(maxCount: Long, gas: Vector[Int]) = {
    val chamber: Chamber = Array.fill[Char](250000, chamberWidth)('.')

    def fallRock(rock: Rock, pos: Pos, gasIdx: Int): (Int, Pos) = {
      val gasPos = Pos(pos.x + gas(gasIdx), pos.y)
      val isWallColl = checkCollision(chamber, rock, gasPos)
      val nextPos = if (isWallColl) pos else gasPos
      val floorPos = Pos(nextPos.x, nextPos.y - 1)
      val isFloorColl = checkCollision(chamber, rock, floorPos)
      val endPos = if (isFloorColl) nextPos else floorPos
      if (isFloorColl) {
        ((gasIdx + 1) % gas.size, endPos)
      } else {
        fallRock(rock, endPos, (gasIdx + 1) % gas.size)
      }
    }

    def run(count: Int, highest: Int, rockNum: Int, gasIdx: Int, stat: Stat)
      : (Stat, Long) =
    {
      val resultStat = stat.getResult(rockNum, gasIdx, highest)
      if (count <= 0) {
        (resultStat, highest)
      } else {
        val rock = rocks(rockNum)
        val pos = Pos(2, highest + 3)

        val (nextGasIdx, endPos) = fallRock(rock, pos, gasIdx)
        putRockIntoChamber(chamber, rock, endPos)
        val nextHighest = Math.max(highest, endPos.y + rock.size)
        val newStat = resultStat.addValue(rockNum, gasIdx, highest)
        run(count - 1, nextHighest, (rockNum + 1) % rocks.size, nextGasIdx, newStat)
      }
    }

    if (maxCount < 3000) {
      run(2022, 0, 0, 0, Stat())._2
    } else {
      run(3000, 0, 0, 0, Stat())._1.lastResult
    }
  }

  def toRock(s: String): Rock = s.split("\n").toVector.reverse

  def putRockIntoChamber(chamber: Chamber, rock: Rock, pos: Pos) = {
    Range(0, rock.size).foreach(ry => {
      val row = rock(ry)
      Range(0, row.size).foreach(rx => {
        val chX = pos.x + rx
        val chY = pos.y + ry
        if (row(rx) == '#') {
          chamber(chY)(chX) = row(rx)
        }
      })
    })
  }

  def checkCollision(chamber: Chamber, rock: Rock, pos: Pos) = {
    Range(0, rock.size).exists(ry => {
      val row = rock(ry)
      Range(0, row.size).exists(rx => {
        val chX = pos.x + rx
        val chY = pos.y + ry
        chX < 0 ||
        chX >= chamberWidth ||
        chY < 0 ||
        (row(rx) == '#' && chamber(chY)(chX) == '#')
      })
    })
  }

  def printChamber(chamber: Chamber, from: Int, to: Int) = {
    Range.inclusive(to, from, -1).foreach(y => {
      Range(0, chamberWidth).foreach(x =>
        print(chamber(y)(x))
      )
      println
    })
  }

  def read(input: String) = {
    Source.fromFile(input)
      .getLines
      .filter(!_.isBlank())
      .toSeq
      .head
      .toVector
      .map(_ match {
        case '<' => -1
        case '>' => 1
      })
  }
}
