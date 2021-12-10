package adventofcode

import scala.io.Source

object Puzzles2021Day4 {
  private def puzzleInner(
    fileName: String,
    first: Boolean
  ) = {
    val bingo = readBingo(fileName)
    val (resultBingo, winner) = play(bingo, first)
    winner
    resultBingo.numbers.headOption.flatMap(
      lastNumber => winner.map(w => score(w) * lastNumber)
    ).getOrElse(0)
  }

  def puzzle(
    fileName: String
  ) = {
    puzzleInner(fileName, true)
  }

  def puzzleTwo(
    fileName: String
  ) = {
    puzzleInner(fileName, false)
  }

  type Table = Vector[Vector[Field]]

  case class Field(number: Int, marked: Boolean = false)
  case class Bingo(numbers: List[Int], tables: List[Table])

  def play(bingo: Bingo, first: Boolean)
    : (Bingo, Option[Table]) =
  {
    if (bingo.numbers.isEmpty) (bingo, None)
    else {
      val number = bingo.numbers.head
      val (tables, winner) = if (first) {
        val tables = bingo.tables.map(table => markTable(table, number))
        val winner = tables.find(isWin)
        (tables, winner)
      } else {
        val winners = bingo.tables.filter(!isWin(_))
        val tables = bingo.tables.map(table => markTable(table, number))
        val winner = if (winners.length == 1) {
          winners.map(markTable(_, number)).find(isWin)
        } else None
        (tables, winner)
      }
      if (winner.isDefined) (Bingo(bingo.numbers, tables), winner)
      else play(Bingo(bingo.numbers.tail, tables), first)
    }
  }

  def readBingo(
    input: String
  ) = {
    val lines = Source.fromFile(input)
      .getLines
      .toList

    val numbers = lines.head
      .split(",").map(_.trim.toInt)
      .toList

    val tables = lines.tail
      .map(_.split("\\s+")
        .filter(!_.isEmpty)
        .map(_.toInt)
        .map(Field(_))
        .toVector
      )
      .filter(_.size > 1)
      .grouped(5)
      .map(_.toVector)
      .toList

    Bingo(numbers, tables)
  }

  def score(table: Table) = {
    (0 to 4).map(i =>
      (0 to 4).map(j =>
        if (!table(i)(j).marked) table(i)(j).number
        else 0
      ).sum
    ).sum
  }

  def markTable(table: Table, number: Int) = {
    table.map(line =>
      line.map(field =>
        if (number == field.number) Field(field.number, true) else field
      )
    )
  }

  def isWin(table: Table) = {
    (0 to 4).exists(i =>
      (0 to 4).forall(j =>
        table(i)(j).marked
      ) ||
      (0 to 4).forall(j =>
        table(j)(i).marked
      )
    )
  }
}
