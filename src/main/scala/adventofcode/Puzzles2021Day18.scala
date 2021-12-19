package adventofcode

import scala.io.Source
import java.io.PrintWriter

object Puzzles2021Day18 {
  def puzzle(
    fileName: String
  ) = {
    val data = readData(fileName)
    val snFirst = data.head
    val sum = data.tail.foldLeft(snFirst) {
      case(sn1, sn2) => add(sn1, sn2)
    }
    magnitude(reduce(sum))
  }

  def puzzleTwo(
    fileName: String
  ) = {
    val data = readData(fileName).toVector
    val count = data.size
    (0 until count).map (i =>
      (0 until count).map(j =>
        if (i == j) 0
        else magnitude(reduce(add(data(i), data(j))))
      ).max
    ).max
  }

  def reduce(sn: SNumber): SNumber = {
    val psn = print(sn, 0)
    val sn2 = explode(sn)
    if (sn2 != sn) {
      reduce(sn2)
    } else {
      val sn3 = split(sn2)
      if (sn3 != sn2) {
        reduce(sn3)
      } else {
        sn
      }
    }
  }

  def magnitude(sn: SNumber): Long = sn match {
    case SPair(sn1, sn2) => magnitude(sn1) * 3 + magnitude(sn2) *2
    case SDigit(v) => v.toLong
  }

  def split(sn: SNumber): SNumber = sn match {
    case SPair(sn1, sn2) => {
      val ssn1 = split(sn1)
      val ssn2 = if (ssn1 == sn1) split(sn2) else sn2
      SPair(ssn1, ssn2)
    }
    case SDigit(v) =>
      if (v < 10) sn else SPair(SDigit(v / 2), SDigit((v + 1) / 2))
  }

  def explode(sn: SNumber): SNumber = {
    def loop(sn: SNumber, depth: Int): (SNumber, Option[SNumber]) = sn match {
      case SPair(SDigit(v1), SDigit(v2)) if depth >= 4 => {
        (SDigit(0), Some(sn))
      }
      case SPair(sp: SPair, SDigit(v)) => {
        loop(sp, depth + 1) match {
          case (snNew, Some(SPair(SDigit(v1), SDigit(v2)))) =>
            (SPair(snNew, SDigit(v + v2)), Some(SPair(SDigit(v1), SEmpty)))
          case (snNew, Some(SPair(SEmpty, SDigit(v2)))) =>
            (SPair(snNew, SDigit(v + v2)), None)
          case (snNew, opt) => (SPair(snNew, SDigit(v)), opt)
        }
      }
      case SPair(SDigit(v), sp: SPair) => {
        loop(sp, depth + 1) match {
          case (snNew, Some(SPair(SDigit(v1), SDigit(v2)))) =>
            (SPair(SDigit(v + v1), snNew), Some(SPair(SEmpty, SDigit(v2))))
          case (snNew, Some(SPair(SDigit(v1), SEmpty))) =>
            (SPair(SDigit(v + v1), snNew), None)
          case (snNew, opt) => (SPair(SDigit(v), snNew), opt)
        }
      }
      case SPair(sp: SPair, sp2: SPair) => {
        loop(sp, depth + 1) match {
          case (snNew, Some(SPair(SDigit(v1), SDigit(v2)))) =>
            (SPair(snNew, addRight(sp2, v2)), Some(SPair(SDigit(v1), SEmpty)))
          case (snNew, Some(SPair(SEmpty, SDigit(v2)))) =>
            (SPair(snNew, addRight(sp2, v2)), None)
          case (snNew, Some(sn3)) => (SPair(snNew, sp2), Some(sn3))
          case (spNew, None) if spNew != sp => (SPair(spNew, sp2), None)
          case (spNew, None) =>
            loop(sp2, depth + 1) match {
              case (snNew, Some(SPair(SDigit(v1), SDigit(v2)))) =>
                (SPair(addLeft(spNew, v1), snNew), Some(SPair(SEmpty, SDigit(v2))))
              case (snNew, Some(SPair(SDigit(v1), SEmpty))) =>
                (SPair(addLeft(spNew, v1), snNew), None)
              case (snNew, opt) => (SPair(spNew, snNew), opt)
            }
        }
      }
      case _ => (sn, None)
    }

    def addRight(sn: SNumber, v: Int): SNumber = sn match {
      case SPair(SDigit(v2), sn2) => SPair(SDigit(v2 + v), sn2)
      case SPair(SPair(sn1, sn2), sn3) => SPair(SPair(addRight(sn1, v), sn2), sn3)
      case SDigit(v2) => SDigit(v + v2)
    }

    def addLeft(sn: SNumber, v: Int): SNumber = sn match {
      case SPair(sn2, SDigit(v2)) => SPair(sn2, SDigit(v2 + v))
      case SPair(sn1, SPair(sn2, sn3)) => SPair(sn1, SPair(sn2, addLeft(sn3, v)))
      case SDigit(v2) => SDigit(v + v2)
    }

    loop(sn, 0)._1
  }

  def add(sn1: SNumber, sn2: SNumber): SNumber = {
    val result = reduce(SPair(sn1, sn2))
    result
  }

  trait SNumber
  case object SEmpty extends SNumber
  case class SDigit(v: Int) extends SNumber
  case class SPair(left: SNumber, right: SNumber) extends SNumber

  def print(sn: SNumber, depth: Int): String = sn match {
    case SPair(sn1, sn2) => {
      val psn1 = print(sn1, depth + 1)
      val psn2 = print(sn2, depth + 1)
      s"[$psn1,$psn2]"
    }
    case SDigit(v) => v.toString()
  }

  def readSNumber(s: String): (SNumber, String) = s.toList match {
    case '[' :: tail => {
      val (snl, stl) = readSNumber(tail.mkString)
      val (snr, str) = readSNumber(stl.tail)
      (SPair(snl, snr), str.tail)
    }
    case sd :: tail => (SDigit(sd.toInt - 48), tail.mkString)
    case _ => (SEmpty, "")
  }

  def readData(
    fileName: String
  ) = {
    val pattern = raw"x=(\d+)\.\.(\d+), y=(-?\d+)\.\.(-?\d+)".r.unanchored
    Source.fromFile(fileName)
      .getLines
      .toList
      .map(readSNumber)
      .map(_._1)
  }
}
