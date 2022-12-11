package adventofcode.year2022

import scala.io.Source

object PuzzlesDay11 extends App {

  case class State(monkeys: Vector[Monkey] = Vector())
  case class Monkey(
    number: Int,
    items: List[Long] = Nil,
    operation: String = "",
    test: Int = 0,
    throwIfTrue: Int = 0,
    throwIfFalse: Int = 0,
    inspectedElements: Int = 0
  )

  def puzzle(
    input: String
  ) = {
    val monkeys = read(input)
    (1 to 20).foldLeft(monkeys) {
      (monkeys, i) => evaluate(monkeys)
    }.map(_.inspectedElements)
    .sorted
    .reverse
    .take(2)
    .product
  }

  def puzzleTwo(
    input: String
  ) = {
    val monkeys = read(input)
    (1 to 10000).foldLeft(monkeys) {
      (monkeys, i) => evaluate2(monkeys)
    }.map(_.inspectedElements)
    .sorted
    .reverse
    .take(2)
    .map(_.toLong)
    .product
  }

  def evaluate(monkeys: Vector[Monkey]) = {
    //println(monkeys)
    (0 to monkeys.last.number)
      .foldLeft(monkeys) {
        (monkeys, i) => inspectElements(monkeys, monkeys(i))
      }
  }

  def inspectElements(monkeys: Vector[Monkey], monkey: Monkey): Vector[Monkey] =
  {
    if (monkey.items.isEmpty) {
      monkeys
    } else {
      val (newMonkeys, newMonkey) = inspectElement(monkeys, monkey)
      inspectElements(newMonkeys, newMonkey)
    }
  }

  def inspectElement(monkeys: Vector[Monkey], monkey: Monkey) = {
    val inspectedElement = monkey.items.head
    val worryLevel = runOperation(monkey.operation, inspectedElement)
    val wl = worryLevel / 3
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

  def evaluate2(monkeys: Vector[Monkey]) = {
    //println(monkeys)
    (0 to monkeys.last.number)
      .foldLeft(monkeys) {
        (monkeys, i) => inspectElements2(monkeys, monkeys(i))
      }
  }

  def inspectElements2(monkeys: Vector[Monkey], monkey: Monkey): Vector[Monkey] =
  {
    if (monkey.items.isEmpty) {
      monkeys
    } else {
      val (newMonkeys, newMonkey) = inspectElement2(monkeys, monkey)
      inspectElements2(newMonkeys, newMonkey)
    }
  }

  def inspectElement2(monkeys: Vector[Monkey], monkey: Monkey) = {
    val inspectedElement = monkey.items.head
    val worryLevel = runOperation(monkey.operation, inspectedElement)
    val test = (worryLevel % monkey.test) == 0
    val newMonkey = monkey.copy(
      items = monkey.items.tail,
      inspectedElements = monkey.inspectedElements + 1
    )

    val modulo = monkeys.map(_.test)
      .product
    val newMonkeys = monkeys.updated(monkey.number, newMonkey)
    val throwNumber = if (test) monkey.throwIfTrue else monkey.throwIfFalse
    val throwedMonkey = newMonkeys(throwNumber)
    val wl = worryLevel % modulo
    val newThrowedMonkey = throwedMonkey.copy(
      items = throwedMonkey.items :+ wl
    )
    (newMonkeys.updated(throwNumber, newThrowedMonkey), newMonkey)
  }

  def runOperation(operation: String, worryLevel: Long) = operation match {
    case s"old * old" => worryLevel * worryLevel
    case s"old * $value" => worryLevel * value.toLong
    case s"old + $value" => worryLevel + value.toLong
  }

  def read(
    input: String
  ) = {
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
          case OperationPattern(operation) => {
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
