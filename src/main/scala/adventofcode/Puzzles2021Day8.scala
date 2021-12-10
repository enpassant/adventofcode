package adventofcode

import scala.io.Source
import scala.util.Random

object Puzzles2021Day8 {
  def p2021_8(
    input: String
  ) = {
    val population = readDigits(input)

    population
      .flatMap(arr => arr(1).toList.map(_.length))
      .filter(length => List(2,3,4,7).contains(length))
      .size
  }

  def p2021_8_2(
    input: String
  ) = {
    val population = readDigits(input)

    population
      .map(arr => calcWireMap(arr(0).toList, arr(1).toList))
      .sum
  }

  def toPermutationMap(permutation: String) = {
    ('a' to 'z').zip(permutation).toMap
  }

  def checkPermutation(digitWires: List[String], permutation: String) = {
    val wireMap = toPermutationMap(permutation)
    digitWires.forall(str => {
      val transformedStr = str.map(wireMap)
      baseMap.values.exists(transformedStr.toSet == _.toSet)
    })
  }

  def calcWireMap(digitWires: List[String], numbers: List[String]) = {
    allPermutations.find(permutation =>
        checkPermutation(digitWires, permutation)
    ).map(permutation => {
      numbers.map(str => str.map(toPermutationMap(permutation)))
        .map(str => baseMap.find {
          case(number, s) => str.toSet == s.toSet
        })
      .map {
        case Some((number, s)) => number
        case _ => 0
      }
    }).getOrElse(Nil)
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

  val wires = baseMap(8)
    .toSet

  def allPermutations = baseMap(8).permutations

  val possibleDigits = baseMap.toList.map {
    case (digit, str) => digit -> str.length
  }.groupMap {
    case (digit, length) => length
  } {
    case (digit, length) => digit
  }
}
