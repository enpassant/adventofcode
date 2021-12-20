package adventofcode

import scala.io.Source
import java.io.PrintWriter

object Puzzles2021Day20 {
  def puzzle(
    fileName: String
  ) = {
    val data = readData(fileName)
    val (enhancement, image) = data
    val result = (1 to 2).foldLeft((image, 0)) {
      case ((img, default), i) => {
        val (step, d) = enhance(enhancement, default, img)
        //println(printMap(step))
        //println
        (step, d)
      }
    }
    lits(result._1)
  }

  def puzzleTwo(
    fileName: String
  ) = {
    val data = readData(fileName)
    val (enhancement, image) = data
    val result = (1 to 50).foldLeft((image, 0)) {
      case ((img, default), i) => {
        val (step, d) = enhance(enhancement, default, img)
        (step, d)
      }
    }
    lits(result._1)
  }

  def lits(image: Map[(Int, Int), Int]) = {
    image.map { case ((x, y), v) => v }.sum
  }

  def enhance(
    enhancement: Vector[Int],
    default: Int,
    image: Map[(Int, Int), Int]
  ) = {
    val w = image.map { case ((x, y), v) => x }.max + 1
    val h = image.map { case ((x, y), v) => y }.max + 1
    val s = 2

    val coords = for {
      x <- -s to (w + s)
      y <- -s to (h + s)
    } yield (x, y)

    val newImage = coords.map {
      case (x, y) => {
        ((x + s, y + s) -> enhancement(getCode(x, y, default, image)))
      }
    }.toMap

    (newImage, newImage(0, 0))
  }

  def getCode(xc: Int, yc: Int, default: Int, image: Map[(Int, Int), Int]) = {
    val binary = (0 to 2).map(y =>
      (0 to 2).map(x =>
        image.get((xc + x - 1, yc + y - 1))
          .getOrElse(default)
      )
    ).flatten
    .mkString
    Integer.parseInt(binary, 2)
  }

  def printMap(map: Map[(Int, Int), Int]) = {
    val width = map.map { case ((x, y), v) => x }.max + 1
    val height = map.map { case ((x, y), v) => y }.max + 1
    val line = "." * width
    val vector = Vector.fill(height)(line)
    map.foldLeft(vector) {
      case (display, ((x, y), v)) =>
        display.updated(y, display(y).updated(x, (48 + v).toChar))
    }.mkString("\n")
  }

  def readData(
    fileName: String
  ) = {
    val lines = Source.fromFile(fileName)
      .getLines

    val enhancement = lines.takeWhile(!_.isBlank())
      .mkString
      .map {
        case '#' => 1
        case _ => 0
      }
      .toVector

    val image = lines.takeWhile(!_.isBlank())
      .zipWithIndex
      .map(v => (v._2, v._1))
      .toMap
      .flatMap((y, line) =>
        line.toCharArray
          .map {
            case '#' => 1
            case _ => 0
          }
          .zipWithIndex
          .map(v => ((v._2, y), v._1))
          .toMap
      )

    (enhancement, image)
  }
}
