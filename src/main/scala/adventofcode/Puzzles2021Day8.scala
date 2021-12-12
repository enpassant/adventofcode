package adventofcode

import scala.io.Source
import scala.util.Random

object Puzzles2021Day8 {
  def p2021_8(
    fileName: String
  ) = {
    val population = readDigits(fileName)

    population
      .flatMap(arr => arr(1).toList.map(_.length))
      .filter(length => List(2,3,4,7).contains(length))
      .size
  }

  def p2021_8_2(
    fileName: String
  ) = {
    val population = readDigits(fileName)

    population
      .map(arr => convertToDigit(
        arr(0).toList,
        arr(1).toList,
        calcMapping(arr(0).toList)
    ))
    .sum
  }

  def calcMapping(digitWires: List[String]) = {
    val charMap = digitWires.flatMap(_.toList)
      .groupMapReduce(s => s)(s => 1)(_ + _)
      .foldLeft(Map[Char, Char]()) {
        case (map, (k, v)) =>
          if (v == 4) map + ('e' -> k)
          else if (v == 6) map + ('b' -> k)
          else if (v == 9) map + ('f' -> k)
          else map
      }
    val numberMap = digitWires.foldLeft(Map[Int, String]()) {
      case (map, v) =>
        if (v.length == 2) map + (1 -> v)
        else if (v.length == 3) map + (7 -> v)
        else if (v.length == 4) map + (4 -> v)
        else if (v.length == 7) map + (8 -> v)
        else map
    }

    val eMap = charMap('e')
    val bMap = charMap('b')
    val fMap = charMap('f')
    val cMap = (numberMap(1).toSet - fMap).head
    val aMap = (numberMap(7).toSet - cMap - fMap).head
    val dMap = (numberMap(4).toSet - bMap - cMap - fMap).head
    val gMap = (numberMap(8).toSet - aMap - bMap - cMap - dMap - eMap - fMap).head

    (charMap + ('c' -> cMap) + ('a' -> aMap) + ('d' -> dMap) + ('g' -> gMap))
      .map { case (k, v) => (v -> k) }
  }

  def convertToDigit(
    digitWires: List[String],
    numbers: List[String],
    charMap: Map[Char, Char]
  ) = {
    numbers.map(str => str.map(charMap))
      .map(str => baseMap.find {
        case(number, s) => str.toSet == s.toSet
      })
    .map {
      case Some((number, s)) => number
      case _ => 0
    }
    .foldLeft(0) {
      case (number, digit) => number * 10 + digit
    }
  }

  def readDigits(
    input: String
  ) = {
    Source.fromFile(input)
        .getLines
        .map(
          _.trim.split("\\|")
            .map(
              _.trim.split(" ")
              .map(_.trim)
            )
          )
        .toList
  }

  val baseMap = Map(
    0 -> "abcefg",
    1 -> "cf",
    2 -> "acdeg",
    3 -> "acdfg",
    4 -> "bcdf",
    5 -> "abdfg",
    6 -> "abdefg",
    7 -> "acf",
    8 -> "abcdefg",
    9 -> "abcdfg"
  )
}
