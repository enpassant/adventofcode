package adventofcode.year2022

import scala.io.Source

object PuzzlesDay5 extends App {

  case class Store(stacks: Vector[List[Char]] = Vector.fill(20)(List()))
  case class Move(count: Int, from: Int, to: Int)

  def puzzle(
    input: String
  ) = {
    val map = read(input)
    rearrange(map._1, map._2)
      .stacks
      .filter(ls => !ls.isEmpty)
      .map(_.head)
      .mkString
  }

  def puzzleTwo(
    input: String
  ) = {
    val map = read(input)
    rearrange9001(map._1, map._2)
      .stacks
      .filter(ls => !ls.isEmpty)
      .map(_.head)
      .mkString
  }

  def rearrange(store: Store, moves: List[Move]) = {
    moves.foldLeft(store) {
      (st, move) => {
        val nst = (0 until move.count).foldLeft(st) {
          (s, i) => Store(s.stacks
            .updated(move.to, s.stacks(move.from).head :: s.stacks(move.to))
            .updated(move.from, s.stacks(move.from).tail)
          )
        }
        nst
      }
    }
  }

  def rearrange9001(store: Store, moves: List[Move]) = {
    moves.foldLeft(store) {
      (st, move) => {
        Store(st.stacks
          .updated(move.to, st.stacks(move.from).take(move.count) ++ st.stacks(move.to))
          .updated(move.from, st.stacks(move.from).drop(move.count))
        )
      }
    }
  }

  def read(
    input: String
  ) = {
    val (crates, operations) = Source.fromFile(input)
        .getLines
        .span(str => !str.trim().isEmpty)

    val store = crates.map(
      line => line.grouped(4)
        .map(_.mkString)
        .zipWithIndex
        .filter(v => !v._1.trim.isEmpty)
        .map(v => (v._1(1), v._2))
        .toList
    ).foldLeft(Store()) {
      (store, ls) =>
        ls.foldLeft(store) {
          (st, v) => Store(st.stacks.updated(v._2, st.stacks(v._2) :+ v._1))
        }
    }

    val moves = operations.drop(1)
      .map(line => line match {
        case s"move $count from $from to $to" =>
          Move(count.toInt, from.toInt - 1, to.toInt - 1)
      })
      .toList

    (store, moves)
  }
}
