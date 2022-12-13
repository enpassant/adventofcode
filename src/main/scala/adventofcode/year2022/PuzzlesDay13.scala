package adventofcode.year2022

import scala.io.Source

object PuzzlesDay13 extends App {
  sealed trait Item
  case class IntItem(v: Int) extends Item
  case class ListItem(ls: List[Item] = List()) extends Item {
    def add(item: Item) = ListItem(ls :+ item)
  }
  case class Packet(ls: ListItem) extends Item

  object PacketOrdering extends Ordering[Packet] {
    override def compare(left: Packet, right: Packet): Int = {
      compareLists(left.ls.ls, right.ls.ls)
    }
  }

  def puzzle(input: String) = {
    val map = read(input)
    map.map(v => PacketOrdering.compare(v(0), v(1)))
      .zipWithIndex
      .filter(_._1 <= 0)
      .map(_._2 + 1)
      .sum
  }

  def puzzleTwo(input: String) = {
    val map = read(input)
    val packets = List(
      Packet(ListItem(List(ListItem(List(IntItem(2)))))),
      Packet(ListItem(List(ListItem(List(IntItem(6))))))
    )
    map.flatMap(_.toList)
      .++(packets)
      .sorted(PacketOrdering)
      .zipWithIndex
      .filter(v => packets.contains(v._1))
      .map(_._2 + 1)
      .product
  }

  def compareItems(left: Item, right: Item): Int = (left, right) match {
    case (IntItem(intLeft), IntItem(intRight)) =>
      intLeft.compare(intRight)
    case (ListItem(lsLeft), ListItem(lsRight)) =>
      compareLists(lsLeft, lsRight)
    case (ListItem(lsLeft), IntItem(intRight)) =>
      compareLists(lsLeft, List(IntItem(intRight)))
    case (IntItem(intLeft), ListItem(lsRight)) =>
      compareLists(List(IntItem(intLeft)), lsRight)
    case _ => 0
  }

  def compareLists(left: List[Item], right: List[Item]) = {
    val zippedList = left.zip(right)
    zippedList.find((l, r) => compareItems(l, r) != 0)
      .map(compareItems)
      .getOrElse(left.size.compare(right.size))
  }

  def getList(start: Int, end: Int, open: Int, str: String): (String, String) =
  {
    if (open == 0) {
      (str.substring(start, end), str.substring(end))
    } else if (str(end) == '[') {
      getList(start, end + 1, open + 1, str)
    } else if (str(end) == ']') {
      getList(start, end + 1, open - 1, str)
    } else {
      getList(start, end + 1, open, str)
    }
  }

  def getInt(str: String): (IntItem, String) =
  {
    val intPattern = "([0-9]+).*".r
    val intPattern(number) = str
    val intNumber = number.toInt
    (IntItem(intNumber), str.substring(number.size))
  }

  def convertToLists(str: String, ls: ListItem): ListItem = {
    if (str.isBlank()) {
      ls
    } else if (str.head == ',' || str.head == ']') {
      convertToLists(str.tail, ls)
    } else if (str.head == '[') {
      val (lsStr, tailStr) = getList(1, 1, 1, str)
      val lsItem = convertToLists(lsStr, ListItem())
      convertToLists(tailStr, ls.add(lsItem))
    } else {
      val (intItem, tailStr) = getInt(str)
      convertToLists(tailStr, ls.add(intItem))
    }
  }

  def convertToPacket(str: String) = {
    if (str.head == '[') {
      val (lsStr, _) = getList(1, 1, 1, str)
      Packet(convertToLists(lsStr, ListItem()))
    } else {
      Packet(ListItem())
    }
  }

  def read(input: String) = {
    Source.fromFile(input)
      .getLines
      .filter(!_.isBlank())
      .map(convertToPacket)
      .grouped(2)
      .toList
  }
}
