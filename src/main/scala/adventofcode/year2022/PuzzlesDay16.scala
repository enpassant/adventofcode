package adventofcode.year2022

import scala.collection.immutable.Queue
import scala.io.Source

object PuzzlesDay16 extends App {
  case class Valve(name: String, rate: Int, valves: List[String])
  case class ValveDest(valve: Valve, distance: Int)
  case class Node(valve: Valve, rate: Int, others: Map[String, ValveDest])

  def puzzle(input: String) = {
    val data = read(input)
    val valveMap = data.map(v => (v.name, v)).toMap
    val allNodes = distance(valveMap)
    val nodeAA = allNodes.find(_.valve.name == "AA").get
    val nodes = allNodes.filter(_.valve.rate > 0) :+ nodeAA
    println(nodes.map(n => s"${n.valve.name}(${n.valve.rate})"))
    solve(calcFlowRate)(1, 0, nodes, Vector.range(0, nodes.size - 1))
  }

  def puzzleTwo(input: String) = {
    val data = read(input)
    val valveMap = data.map(v => (v.name, v)).toMap
    val allNodes = distance(valveMap)
    val nodeAA = allNodes.find(_.valve.name == "AA").get
    val nodes = allNodes.filter(_.valve.rate > 0) :+ nodeAA
    println(nodes.map(n => s"${n.valve.name}(${n.valve.rate})"))
    solve(calcFlowRate2)(1, 0, nodes, Vector.range(0, nodes.size - 1))
  }

  def getNextPermutation(order: Vector[Int]): Option[Vector[Int]] = {
    val zip = order.zip(order.tail).zipWithIndex
    val iOpt = zip.reverse.find(v => v._1._1 < v._1._2)
    if (iOpt.isEmpty) {
      None
    } else {
      val ((a, _), i) = iOpt.get
      val (prefix, suffix) = order.splitAt(i)
      val b = suffix.tail.filter(_ > a).sorted.head
      val end = suffix.filter(_ != b).sorted
      Some((prefix :+ b) ++ end)
    }
  }

  def cutPermutation(order: Vector[Int], pos: Int): Vector[Int] = {
    val (prefix, suffix) = order.splitAt(pos)
    prefix ++ suffix.sortBy(-_)
  }

  def permutationStream(order: Vector[Int]): Stream[Vector[Int]] = {
    val nextOrderOpt = getNextPermutation(order)
    if (nextOrderOpt.isEmpty) {
      order #:: Stream.empty
    } else {
      order #:: permutationStream(nextOrderOpt.get)
    }
  }

  def solve(calcFlowRate: (Vector[Node], Vector[Int]) => (Int, Long))
    (i: Int, max: Long, nodes: Vector[Node], order: Vector[Int]): Long =
  {
    val (o, result) = calcFlowRate(nodes, order)
    //if (i % 100000 == 0) {
      //println(s"i: $i, max: $max, result: $result, $order")
    //}
    val newMax = if (result > max) result else max
    val nextOrderOpt = if (o > 0) {
      getNextPermutation(
        cutPermutation(order, o + 1)
      )
    } else {
      getNextPermutation(order)
    }
    if (nextOrderOpt.isDefined) {
      solve(calcFlowRate)(i + 1, newMax, nodes, nextOrderOpt.get)
    } else {
      max
    }
  }

  def distance(valveMap: Map[String, Valve]): Vector[Node] = {
    def loop(
      visited: Set[Valve],
      result: Vector[ValveDest],
      queue: Queue[ValveDest]
    ): Vector[ValveDest]= {
      if (queue.isEmpty) {
        result
      } else {
        val (valveDest, newQueue) = queue.dequeue
        val valve = valveDest.valve
        val dist = valveDest.distance + 1
        val neighbours = valve.valves.map(
          name => ValveDest(valveMap(name), dist)
        ).filterNot(vd => visited.contains(vd.valve))
        loop(
          visited + valve,
          result :+ valveDest,
          newQueue.enqueue(neighbours)
        )
      }
    }

    val nodeValves = valveMap.values
      .toVector

    nodeValves.map(v => {
      val others = loop(Set(), Vector(), Queue(ValveDest(v, 0)))
        .filter(vd => (vd.valve != v) && nodeValves.contains(vd.valve))
        .map(vd => (vd.valve.name, vd))
        .toMap
      Node(v, 0, others)
    }).sortBy(_.valve.rate)
  }

  def toStream[A](iter: Iterator[A]) =
    Stream.unfold(iter)
      (i => if (i.hasNext) Some((i.next, i)) else None)

  def calcFlowRate(nodes: Vector[Node], order: Vector[Int]) = {
    def loop(rate: Long, time: Int, pos: Int, o: Int): (Int, Long) = {
      if (o >= order.size || time >= 29) {
          (o, rate)
      } else {
        val p = order(o)
        val node = nodes(p)
        val distance = nodes(pos).others(node.valve.name).distance
        val newTime = time + distance
        val newRate = rate + (30 - newTime) * node.valve.rate
        if (newTime >= 30) {
          (o, rate)
        } else {
          loop(newRate, newTime + 1, p, o + 1)
        }
      }
    }

    loop(0L, 1, nodes.size - 1, 0)
  }

  def calcFlowRate2(nodes: Vector[Node], order: Vector[Int]) = {
    def loop(rate: Long, time1: Int, time2: Int, pos1: Int, pos2: Int, o: Int): (Int, Long) = {
      if (o >= order.size || (time1 >= 25 && time2 >= 25)) {
          (o, rate)
      } else {
        val (o1, newRate, newTime1, p1) = if (time1 < 25) {
          val pi = order(o)
          val node1 = nodes(pi)
          val distance1 = nodes(pos1).others(node1.valve.name).distance
          val newTimei = time1 + distance1
          if (newTimei < 26) {
            (o + 1, rate + (26 - newTimei) * node1.valve.rate, newTimei, pi)
          } else {
            (o, rate, newTimei, pi)
          }
        } else {
          (o, rate, time1, pos1)
        }

        val (o2, newRate2, newTime2, p2) = if (time2 < 25) {
          val pj = order(o1)
          val node2 = nodes(pj)
          val distance2 = nodes(pos2).others(node2.valve.name).distance
          val newTimej = time2 + distance2
          if (newTimej < 26) {
            (o1 + 1, newRate + (26 - newTimej) * node2.valve.rate, newTimej, pj)
          } else {
            (o1, newRate, newTimej, pj)
          }
        } else {
            (o1, newRate, time2, pos2)
        }
        if (newTime1 >= 26 && newTime2 >= 26) {
          (o, rate)
        } else {
          loop(newRate2, newTime1 + 1, newTime2 + 1, p1, p2, o2)
        }
      }
    }

    loop(0L, 1, 1, nodes.size - 1, nodes.size - 1, 0)
  }

  def read(input: String) = {
    Source.fromFile(input)
      .getLines
      .filter(!_.isBlank())
      .map(_ match {
        case s"Valve $valve has flow rate=$rate; $tunnels $lead to $tr $valves" =>
          Valve(valve, rate.toInt, valves.split(", ").toList)
      })
      .toList
  }
}
