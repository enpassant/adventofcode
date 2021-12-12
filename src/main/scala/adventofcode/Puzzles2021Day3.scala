package adventofcode

import scala.io.Source

object Puzzles2021Day3 {
  def puzzle(
    fileName: String
  ) = {
    val data = readData(fileName)
    val half = data.length / 2
    val oneBits = data.foldLeft(Vector.fill(data(0).length)(0)) {
      case (s, v) => s.zip(v).map(_ + _)
    }
    val gammaVec = oneBits.map(count => if (count >= half) 1 else 0)
    val epsilonVec = gammaVec.map(1 - _)
    val gamma = Integer.parseInt(gammaVec.mkString, 2)
    val epsilon = Integer.parseInt(epsilonVec.mkString, 2)
    gamma * epsilon
  }

  def puzzleTwo(
    fileName: String
  ) = {
    val data = readData(fileName)
    val oxigen = calcRate(data, 1)
    val co2 = calcRate(data, 0)
    oxigen * co2
  }

  def calcRate(data: Vector[Vector[Int]], searchBit: Int) = {
    val bitStr = Stream.iterate((data, 0)) {
      case (d, index) => (filterByIndex(index, d, searchBit), index + 1)
    }
    .find(_._1.length <= 1)
    .map(_._1(0))
    .getOrElse(Vector("0"))
    .mkString
    Integer.parseInt(bitStr, 2)
  }

  def filterByIndex(index: Int, data: Vector[Vector[Int]], searchBit: Int) = {
    val length = data.length
    val count = data.filter(_(index) > 0).length
    val bit = if (count * 2 >= length) searchBit else 1 - searchBit
    data.filter(_(index) == bit)
  }

  def readData(
    fileName: String
  ) = {
    Source.fromFile(fileName)
      .getLines
      .map(_.toCharArray().toVector.map(_.toInt - 48))
      .toVector
  }
}
