package adventofcode.year2022

import scala.io.Source
import scala.annotation.tailrec

import adventofcode.Graph

object PuzzlesDay21 extends App {

  sealed trait Node
  case class MonkeyNode(name: String, m1: String, m2: String, operation: Char)
    extends Node
  case class MonkeyValue(name: String, value: Long)
    extends Node

  type MonkeyTree = Map[String, Node]
  type Value = Long | (Long => Long)

  def puzzle(input: String) = {
    val data = read(input)
    val root = data("root")
    Graph.postorder(root, getChildren(data), visit1(data))
  }

  def puzzleTwo(input: String) = {
    val data = read(input)
    val root = data("root")
    Graph.postorder(root, getChildren(data), visit2(data))
  }

  def getChildren(tree: MonkeyTree)(node: Node) = {
    node match {
      case MonkeyNode(_, m1, m2, _) => Vector(tree(m1), tree(m2))
      case MonkeyValue(_, _) => Vector()
    }
  }

  def visit1(tree: MonkeyTree)(node: Node, values: Vector[Value]): Value = {
    node match {
      case MonkeyNode(_, _, _, operation) =>
        eval(values(0), values(1), operation)
      case MonkeyValue(_, value) => value
    }
  }

  def visit2(tree: MonkeyTree)(node: Node, values: Vector[Value]): Value = {
    node match {
      case MonkeyValue("humn", _) => v => v
      case MonkeyNode("root", _, _, _) => values match {
        case Vector(fn: (Long => Long), v: Long) => fn(v)
        case Vector(v: Long, fn: (Long => Long)) => fn(v)
        case _ => 0L
      }
      case MonkeyNode(_, _, _, operation) =>
        eval(values(0), values(1), operation)
      case MonkeyValue(_, value) => value
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
