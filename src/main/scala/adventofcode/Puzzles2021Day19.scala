package adventofcode

import scala.io.Source
import java.io.PrintWriter

object Puzzles2021Day19 {
  def puzzle(
    fileName: String
  ) = {
    val data = readData(fileName)
    val views = Vector(
      Vector(-1, -1, 1),
      Vector(-1, 1, -1),
      Vector(1, -1, -1),
      Vector(1, 1, 1)
    )
    val faces = Vector(0, 1, 2).permutations
      .toVector

    val orientations = for {
      v <- views
      f <- faces
    } yield (v, f)

    val scanner0 = data.head
    val scanner = data.tail.head
    transfer(orientations(1), scanner0, scanner)
  }

  type Orientation = (Vector[Int], Vector[Int])

  def transfer(
    orientation: Orientation,
    scanner0: Scanner,
    scanner: Scanner
  ) = {
    scanner.coords.map(c =>
      List(
        c(orientation._2(0)) * orientation._1(0),
        c(orientation._2(1)) * orientation._1(1),
        c(orientation._2(2)) * orientation._1(2)
      )
    )
  }

  def puzzleTwo(
    fileName: String
  ) = {
  }

  case class Scanner(id: Int, coords: List[List[Int]] = Nil)

  def readData(
    fileName: String
  ) = {
    val pattern = "--- scanner (\\d+) ---".r
    val noneScanner: List[Scanner] = Nil
    Source.fromFile(fileName)
      .getLines
      .foldLeft(noneScanner) {
        case (scannerOpt, s) => (scannerOpt, s) match {
          case (ls, pattern(id)) => Scanner(id.toInt) :: ls
          case (scanner :: ls, s) if (!s.isBlank) => {
            val coords = s.split(",").map(_.toInt).toList
            (scanner.copy(coords = coords :: scanner.coords)) :: ls
          }
          case (scannerOpt, _) => scannerOpt
        }
      }
  }
}
