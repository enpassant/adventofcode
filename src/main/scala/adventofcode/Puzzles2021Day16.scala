package adventofcode

import scala.io.Source
import java.io.PrintWriter

object Puzzles2021Day16 {
  def puzzle(
    fileName: String
  ) = {
    val stream = readData(fileName)
    val parser = StreamParser(stream, 1)
    val exp = readExp(parser).value
    sumVersions(exp)
  }

  def puzzleTwo(
    fileName: String
  ) = {
    val stream = readData(fileName)
    val parser = StreamParser(stream, 1)
    val exp = readExp(parser).value
    evaluate(exp)
  }

  def readExp(parser: StreamParser[_]): StreamParser[Exp] = {
    val version = parser.parse(3)(binaryToInt)
    val packetType = version.parse(3)(binaryToInt)
    if (packetType.value == 4) {
      readLiteral(version.value, packetType)
    } else {
      readOperator(version.value, packetType.value, packetType)
    }
  }

  def readOperator(
    version: Int,
    typeId: Int,
    parser: StreamParser[_]
  ): StreamParser[Exp] = {
    val lengthType = parser.parse(1)(_ == "0")
    if (lengthType.value) {
      val length = lengthType.parse(15)(binaryToInt)
      val count = length.value
      val subParserStream = length.parse(count)(_.toStream)
      val subParser = StreamParser(subParserStream.value, 1)
      val subExps = readSubOperatorsByLength(subParser, Vector[Exp]())
      subParserStream.copy(value = Operator(version, typeId, subExps.value))
    } else {
      val count = lengthType.parse(11)(binaryToInt)
      val (result, vector) =
        (1 to count.value).foldLeft(
          (count.asInstanceOf[StreamParser[Exp]], Vector[Exp]())
        ) {
          case ((sp, vc), i) => {
            val exp = readExp(sp)
            (exp, vc :+ exp.value)
          }
      }
      result.copy(value = Operator(version, typeId, vector.toList))
    }
  }

  def readSubOperatorsByLength(parser: StreamParser[_], vector: Vector[Exp])
    : StreamParser[List[Exp]] =
  {
    if (parser.stream.length <= 0) {
      parser.copy(value = vector.toList)
    } else {
      val exp = readExp(parser)
      readSubOperatorsByLength(exp, vector :+ exp.value)
    }
  }

  def readLiteral(version: Int, parser: StreamParser[_], digits: String = "")
    : StreamParser[Exp] =
  {
    val continue = parser.parse(1)(_ == "1")
    val digit = continue.parse(4)(identity)
    val newDigits = digits + digit.value
    if (continue.value) {
      readLiteral(version, digit, newDigits)
    } else {
      digit.copy(value = Literal(version, binaryToLong(newDigits)))
    }
  }

  def sumVersions(exp: Exp): Int = exp match {
    case Literal(version, _) => version
    case Operator(version, _, subExp) => version + subExp.map(sumVersions).sum
  }

  def evaluate(exp: Exp): Long = exp match {
    case Literal(_, value) => value
    case Operator(_, typeId, subExp) => typeId match {
      case 0 => subExp.map(evaluate).sum
      case 1 => subExp.map(evaluate).product
      case 2 => subExp.map(evaluate).min
      case 3 => subExp.map(evaluate).max
      case 5 => {
        val exp1 = evaluate(subExp.head)
        val exp2 = evaluate(subExp.tail.head)
        if (exp1 > exp2) 1 else 0
      }
      case 6 => {
        val exp1 = evaluate(subExp.head)
        val exp2 = evaluate(subExp.tail.head)
        if (exp1 < exp2) 1 else 0
      }
      case 7 => {
        val exp1 = evaluate(subExp.head)
        val exp2 = evaluate(subExp.tail.head)
        if (exp1 == exp2) 1 else 0
      }
    }
  }

  sealed trait Exp {
    def version: Int
  }
  case class Literal(version: Int, value: Long) extends Exp
  case class Operator(version: Int, typeId: Int, subExp: List[Exp]) extends Exp

  val hexToBin = Map[Char, String](
    '0' -> "0000",
    '1' -> "0001",
    '2' -> "0010",
    '3' -> "0011",
    '4' -> "0100",
    '5' -> "0101",
    '6' -> "0110",
    '7' -> "0111",
    '8' -> "1000",
    '9' -> "1001",
    'A' -> "1010",
    'B' -> "1011",
    'C' -> "1100",
    'D' -> "1101",
    'E' -> "1110",
    'F' -> "1111"
  )

  def binaryToInt(binary: String) = Integer.parseInt(binary, 2)
  def binaryToLong(binary: String) = java.lang.Long.parseUnsignedLong(binary, 2)

  case class StreamParser[T](
    val stream: Stream[Char],
    value: T
  ) {
    def parse[A](length: Int)(fn: String => A): StreamParser[A] = {
      val (stream1, stream2) = stream.splitAt(length)
      StreamParser(stream2, fn(stream1.mkString))
    }
  }

  def readData(
    fileName: String
  ) = {
    Source.fromFile(fileName)
      .getLines
      .mkString
      .flatMap(hexToBin)
      .toStream
  }
}
