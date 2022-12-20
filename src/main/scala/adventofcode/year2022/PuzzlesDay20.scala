package adventofcode.year2022

import scala.io.Source
import scala.annotation.tailrec

object PuzzlesDay20 extends App {

  def puzzle(input: String) = {
    val data = read(input)
    val numbers = repeat(data.zipWithIndex, 1)
      .map(_._1)
    val size = numbers.size
    val pos = numbers.indexOf(0)
    List(1000, 2000, 3000).map(offset =>
      numbers((pos + offset) % size)
    ).sum
  }

  def puzzleTwo(input: String) = {
    val data = read(input)
    val encrypted = data.map(_ * 811589153)
    val numbers = repeat(encrypted.zipWithIndex, 10)
      .map(_._1)
    val size = numbers.size
    val pos = numbers.indexOf(0)
    List(1000, 2000, 3000).map(offset =>
      numbers((pos + offset) % size)
    ).sum
  }

  def repeat(init: Vector[(Long, Int)], count: Int) = {
    @tailrec
    def loop(numbers: Vector[(Long, Int)], count: Int): Vector[(Long, Int)] = {
      if (count <= 0) {
        numbers
      } else {
        val mixed = mixing(numbers)
        loop(mixed, count - 1)
      }
    }

    def mixing(mixingNumbers: Vector[(Long, Int)]) = {
      val size = mixingNumbers.size

      @tailrec
      def loop(numbers: Vector[(Long, Int)], i: Int): Vector[(Long, Int)] = {
        if (i >= size) {
          numbers
        } else {
          val item = init(i)
          val number = item._1
          if (number == 0) {
            loop(numbers, i + 1)
          } else {
            val pos = numbers.indexOf(item)
            val offset = pos + number
            val newPos = modulo(offset, size - 1)
            val deleted = numbers.patch(pos, Nil, 1)
            val inserted = deleted.patch(newPos, Vector(item), 0)
            loop(inserted, i + 1)
          }
        }
      }

      loop(mixingNumbers, 0)
    }

    loop(init, count)
  }

  @tailrec
  def modulo(number: Long, size: Int): Int = {
    if (number <= 0) {
      val mul = 1 - (number / size)
      modulo(number + mul * size, size)
    } else {
      val result = number % size
      if (result == 0L) size else result.toInt
    }
  }

  def read(input: String) = {
    Source.fromFile(input)
      .getLines
      .filter(!_.isBlank())
      .map(_.toLong)
      .toVector
  }
}
