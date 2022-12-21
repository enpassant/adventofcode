package adventofcode.year2022

import scala.io.Source
import scala.annotation.tailrec

object PuzzlesDay21 extends App {

  trait Node
  case class MonkeyNode(name: String, m1: String, m2: String, operation: Char)
    extends Node
  case class MonkeyValue(name: String, value: Long)
    extends Node

  type MonkeyTree = Map[String, Node]
  type Value = Long | (Long => Long)

  def puzzle(input: String) = {
    val data = read(input)
    val root = data("root")
    postorder1(data, root)
  }

  def puzzleTwo(input: String) = {
    val data = read(input)
    val root = data("root")
    postorder2(data, root)
  }

  def postorder1(values: MonkeyTree, node: Node): Value = {
    node match {
      case MonkeyNode(name, m1, m2, operation) => {
        val fn1 = postorder1(values, values(m1))
        val fn2 = postorder1(values, values(m2))
        eval(fn1, fn2, operation)
      }
      case MonkeyValue(name, value) => value
    }
  }

  def postorder2(values: MonkeyTree, node: Node): Value = {
    //println(node)
    node match {
      case MonkeyValue("humn", _) => v => v
      case MonkeyNode("root", m1, m2, operation) => {
        val fn1 = postorder2(values, values(m1))
        val fn2 = postorder2(values, values(m2))
        (fn1, fn2) match {
          case (fn: (Long => Long), v: Long) => fn(v)
          case (v: Long, fn: (Long => Long)) => fn(v)
          case _ => 0L
        }
      }
      case MonkeyNode(name, m1, m2, operation) => {
        val fn1 = postorder2(values, values(m1))
        val fn2 = postorder2(values, values(m2))
        eval(fn1, fn2, operation)
      }
      case MonkeyValue(name, value) => value
    }
  }

  def eval(fn1: Value, fn2: Value, oper: Char): Value = {
    (fn1, fn2, oper) match {
      case (v1: Long, v2: Long, '+') => v1 + v2
      case (v1: Long, v2: Long, '-') => v1 - v2
      case (v1: Long, v2: Long, '*') => v1 * v2
      case (v1: Long, v2: Long, '/') => v1 / v2
      case (fn: (Long => Long), v: Long, '+') => x => fn(x - v)
      case (fn: (Long => Long), v: Long, '-') => x => fn(x + v)
      case (fn: (Long => Long), v: Long, '*') => x => fn(x / v)
      case (fn: (Long => Long), v: Long, '/') => x => fn(x * v)
      case (v: Long, fn: (Long => Long), '+') => x => fn(x - v)
      case (v: Long, fn: (Long => Long), '-') => x => fn(v - x)
      case (v: Long, fn: (Long => Long), '*') => x => fn(x / v)
      case (v: Long, fn: (Long => Long), '/') => x => fn(v / x)
      case _ => 0L
    }
  }

  def read(input: String) = {
    Source.fromFile(input)
      .getLines
      .filter(!_.isBlank())
      .map(_ match {
        case s"$monkey: $m1 $oper $m2" =>
          (monkey -> MonkeyNode(monkey, m1, m2, oper(0)))
        case s"$monkey: $number" =>
          (monkey -> MonkeyValue(monkey, number.toLong))
      })
      .toMap
  }
}
