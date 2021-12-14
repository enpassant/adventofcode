package adventofcode

import scala.io.Source

object Puzzles2021Day14 {
  def puzzle(
    fileName: String
  ) = {
    val data = readData(fileName)
    val newData = (1 to 10).foldLeft(data) {
      case (d, i) => step(d)
    }
    calcScore(newData)
  }

  def puzzleTwo(
    fileName: String
  ) = {
    val data = readData(fileName)
    val newData = (1 to 40).foldLeft(data) {
      case (d, i) => step(d)
    }
    calcScore(newData)
  }

  def calcScore(data: Data) = {
    val counts = data.template.toList
      .flatMap((k, v) => List((k(0), v), (k(1), v)))
      .groupMapReduce(s => s._1)(s => s._2)(_ + _)
      .toList
      .map { case (s, count) => calcCount(s, count, data) }
      .sorted

    val min = counts.head
    counts.reverse.head - min
  }

  def calcCount(key: Char, value: Long, data: Data) = {
    if (key == data.first && key == data.last) value / 2 + 1
    else if (key == data.first || key == data.last) (value + 1) / 2
    else value / 2
  }

  def step(data: Data) = {
    val newTemplate = data.template
      .foldLeft(Map[String, Long]()) { case (t, (s, count)) => {
        val c = data.pairInsertion(s)
        val key1 = "" + s(0) + c
        val key2 = "" + c + s(1)
        t + (key1 -> (t.get(key1).getOrElse(0L) + count))
          + (key2 -> (t.get(key2).getOrElse(0L) + count))
      }}
    data.copy(template = newTemplate)
  }

  case class Data(
    template: Map[String, Long],
    pairInsertion: Map[String, String],
    first: Char,
    last: Char
  )

  def readData(
    fileName: String
  ) = {
    val input = Source.fromFile(fileName)
      .getLines

    val template = input
      .take(1)
      .toList
      .head

      val pattern = "(..) -> (.)".r
    val data = input
      .filter(!_.isBlank)
      .foldLeft(Map[String, String]()) {
        case (map, s) => s match {
          case pattern(pairs, insert) => map + (pairs -> insert)
        }
      }

    val templateMap = template.zip(template.tail)
      .map { case (a, b) => (("" + a + b) -> 1L) }
      .toMap

    Data(templateMap, data, template.head, template.last)
  }
}
