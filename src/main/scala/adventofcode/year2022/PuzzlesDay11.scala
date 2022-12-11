package adventofcode.year2022

import scala.io.Source

object PuzzlesDay11 extends App {

  case class State(monkeys: Vector[Monkey] = Vector())
  case class Monkey(
    number: Int,
    items: List[Long] = Nil,
    operation: Long => Long = v => v,
    test: Int = 0,
    throwIfTrue: Int = 0,
    throwIfFalse: Int = 0,
    inspectedElements: Long = 0
  )

  def puzzle(input: String) = {
    val monkeys = read(input)
    solve(monkeys, 20, _ / 3)
  }

  def puzzleTwo(input: String) = {
    val monkeys = read(input)
    val modulo = monkeys.map(_.test)
      .product
    solve(monkeys, 10000, _ % modulo)
  }

  def solve(monkeys: Vector[Monkey], count: Int, adjustWL: Long => Long) = {
    (1 to count).foldLeft(monkeys) {
      (monkeys, i) => evaluate(monkeys, adjustWL)
    }.map(_.inspectedElements)
    .sorted
    .reverse
    .take(2)
    .product
  }

  def evaluate(monkeys: Vector[Monkey], adjustWL: Long => Long) = {
    (0 to monkeys.last.number)
      .foldLeft(monkeys) {
        (monkeys, i) => inspectElements(monkeys, monkeys(i), adjustWL)
      }
  }

  def inspectElements(
    monkeys: Vector[Monkey],
    monkey: Monkey,
    adjustWL: Long => Long
  ): Vector[Monkey] = {
    if (monkey.items.isEmpty) {
      monkeys
    } else {
      val (newMonkeys, newMonkey) = inspectElement(monkeys, monkey, adjustWL)
      inspectElements(newMonkeys, newMonkey, adjustWL)
    }
  }

  def inspectElement(
    monkeys: Vector[Monkey],
    monkey: Monkey,
    adjustWL: Long => Long
  ) = {
    val inspectedElement = monkey.items.head
    val worryLevel = monkey.operation(inspectedElement)
    val wl = adjustWL(worryLevel)
    val test = (wl % monkey.test) == 0
    val newMonkey = monkey.copy(
      items = monkey.items.tail,
      inspectedElements = monkey.inspectedElements + 1
    )

    val newMonkeys = monkeys.updated(monkey.number, newMonkey)
    val throwNumber = if (test) monkey.throwIfTrue else monkey.throwIfFalse
    val throwedMonkey = newMonkeys(throwNumber)
    val newThrowedMonkey = throwedMonkey.copy(
      items = throwedMonkey.items :+ wl
    )
    (newMonkeys.updated(throwNumber, newThrowedMonkey), newMonkey)
  }

  def read(input: String) = {
    case class State(monkeys: List[Monkey] = Nil)
    val ItemsPattern = "  Starting items: ([0-9, ]+)".r
    val OperationPattern = "  Operation: new = (.+)".r

    Source.fromFile(input)
      .getLines
      .foldLeft(State()) {
        (state, line) => line match {
          case s"Monkey $number:" => State(Monkey(number.toInt) :: state.monkeys)
          case ItemsPattern(itemsStr) => {
            val monkey = state.monkeys.head
            val items = itemsStr.split(", ")
              .map(_.toLong)
              .toList
            State(monkey.copy(items = items) :: state.monkeys.tail)
          }
          case OperationPattern(operationStr) => {
            val operation: Long => Long = operationStr match {
              case s"old * old" => v => v * v
              case s"old * $value" => v => v * value.toLong
              case s"old + $value" => v => v + value.toLong
            }
            val monkey = state.monkeys.head
            State(monkey.copy(operation = operation) :: state.monkeys.tail)
          }
          case s"  Test: divisible by $divisible" => {
            val monkey = state.monkeys.head
            State(monkey.copy(test = divisible.toInt) :: state.monkeys.tail)
          }
          case s"    If true: throw to monkey $number" => {
            val monkey = state.monkeys.head
            State(monkey.copy(throwIfTrue = number.toInt) :: state.monkeys.tail)
          }
          case s"    If false: throw to monkey $number" => {
            val monkey = state.monkeys.head
            State(monkey.copy(throwIfFalse = number.toInt) :: state.monkeys.tail)
          }
          case _ => state
        }
      }
      .monkeys
      .reverse
      .toVector
  }
}
