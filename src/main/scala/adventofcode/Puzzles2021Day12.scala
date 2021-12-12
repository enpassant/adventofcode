package adventofcode

import scala.io.Source

object Puzzles2021Day12 {
  def puzzle(
    fileName: String
  ) = {
    val data = readData(fileName)

    val connects = data.groupBy(_._1)
      .map {
        case (k, v) => (k -> v.map(_._2))
      }
    val initStack = Stack(connects, List("start"), Nil)
    step(initStack, 1).paths.length
  }

  def puzzleTwo(
    fileName: String
  ) = {
    val data = readData(fileName)

    val connects = data.groupBy(_._1)
      .map {
        case (k, v) => (k -> v.map(_._2))
      }
    val initStack = Stack(connects, List("start"), Nil)
    step(initStack, 2).paths.length
  }

  type Graph = Map[String, List[String]]
  case class Stack(
    connects: Graph,
    path: List[String],
    paths: List[String]
  )

  def step(
    stack: Stack,
    maxVisitable: Int
  ): Stack = {
    stack.path match {
      case "end" :: tail =>
        stack.copy(paths = stack.path.reverse.mkString(",") :: stack.paths)
      case head :: tail => {
          val items = stack.connects.get(head)
            .getOrElse(Nil)
            .filter(isVisitable(maxVisitable, _, stack.path))
          if (items.isEmpty) stack
          else {
            val ls: List[String] = Nil
            val newPaths = items.foldLeft(ls) {
              case (paths, item) =>
                step(stack.copy(path = item :: stack.path,
                  paths = Nil
                ), maxVisitable).paths ++ paths
              }
            stack.copy(paths = stack.paths ++ newPaths)
          }
      }
      case _ => stack
    }
  }

  def isVisitable(maxVisitable: Int, item: String, path: List[String]) = {
    if (item == "start") false
    else if (item == "end") true
    else if (item == item.toUpperCase) true
    else if (maxVisitable == 1) {
      !path.contains(item)
    } else {
      val map = (item :: path)
        .filter(item => item == item.toLowerCase)
        .groupMapReduce(s => s)(s => 1)(_ + _)
      val maxItems = map.values
        .toList
        .filter(_ >= maxVisitable)
        .sorted
        .take(2)
      maxItems match {
          case 3 :: ls => false
          case 2 :: 2 :: ls => false
          case _ => true
        }
    }
  }

  def addExclude(item: String, maxVisitable: Int, exclude: Map[String, Int]) = {
    if (item != item.toLowerCase) {
      exclude
    } else {
      if (exclude.size > 1 || exclude.contains(item)) {
        exclude + (item -> 2)
      } else {
        exclude + (item -> 1)
      }
    }
  }

  def readData(
    fileName: String
  ) = {
    Source.fromFile(fileName)
      .getLines
      .map(_.split("-"))
      .flatMap(arr => List((arr(0) -> arr(1)), (arr(1) -> arr(0))))
      .toList
  }
}
