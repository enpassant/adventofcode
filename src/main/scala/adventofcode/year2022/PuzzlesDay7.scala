package adventofcode.year2022

import scala.io.Source

object PuzzlesDay7 extends App {
  val ROOT = "/"
  val SPLIT = "#/#"

  sealed trait Tree {
    def name: String
  }

  case class Dir(
    name: String = ROOT,
    content: List[Tree] = Nil
  ) extends Tree
  case class File(name: String, size :Long) extends Tree

  case class Pwd(path: Vector[Dir])

  def puzzle(
    input: String
  ) = {
    val map = read(input)
    val (_, dirSizes) = calcDirSize(ROOT, map.path.head, Map())
    //showTree(0, map.path.head)
    dirSizes.map((k, v) => v)
      .filter(_ <= 100000)
      .sum
    //dirSizes
      //.filter(_._2 <= 100000)
    //println(map)
  }

  def puzzleTwo(
    input: String
  ) = {
    val map = read(input)
    val (sumSize, dirSizes) = calcDirSize(ROOT, map.path.head, Map())
    val unusedSize = 70000000 - sumSize
    val minFreeSize = 30000000 - unusedSize
    dirSizes.map((k, v) => v)
      .filter(_ >= minFreeSize)
      .min
  }

  def mkDir(pwd: Pwd, name: String): Pwd = {
    val newPwd = addContent(pwd, Dir(name))
    cd(newPwd, List(name))
  }

  def getPath(pwd: Pwd): String = {
    pwd.path
      .map(_.name)
      .mkString(SPLIT)
  }

  def cdUp(pwd: Pwd): Pwd = {
    Pwd(pwd.path.dropRight(1))
  }

  def cd(pwd: Pwd, path: List[String]): Pwd = {
    //println(s"pwd: $pwd, path: $path")
    if (path.isEmpty) {
      pwd
    } else {
      val dir = pwd.path.last
      val dirName = path.head
      //println(s"Dir: $dir, path: $path")
      if (dirName == ROOT) {
        cd(Pwd(pwd.path.take(1)), path.tail)
      } else {
        val newDir = dir.content.filter(_.name == path.head).head
        newDir match {
          case dir: Dir => cd(Pwd(pwd.path :+ dir), path.tail)
          case _ => pwd
        }
      }
    }
  }

  def addContent(pwd: Pwd, content: Tree): Pwd = {
    val dir = pwd.path.last
    val newDir = Dir(dir.name, content :: dir.content)
    val path = getPath(pwd)
    //println(s"newDir: $newDir")
    val newPwd = changeContent(Pwd(pwd.path.dropRight(1)), newDir)
    cd(newPwd, path.split(SPLIT).toList)
  }

  def changeContent(pwd: Pwd, newContent: Dir): Pwd = {
    //println(s"$pwd content: $newContent")
    if (pwd.path.isEmpty) {
      Pwd(Vector(newContent))
    } else {
      val newContentList = pwd.path.last.content.map {
        tree => {
          //println(s"$tree content: $newContent")
          if (tree.name == newContent.name) newContent else tree
        }
      }
      val newDir = Dir(pwd.path.last.name, newContentList)
      changeContent(Pwd(pwd.path.dropRight(1)), newDir)
    }
  }

  def showTree(depth: Int, tree: Tree): Unit = {
    val tab = List.fill(depth)(" ").mkString
    tree match {
      case Dir(name, content) => {
        println(s"$tab- $name (dir)")
        content.foreach(
            t => showTree(depth + 2, t)
        )
      }
      case File(name, size) => println(s"$tab- $name (file, size=$size)")
    }
  }

  def calcDirSize(path: String, tree: Tree, dirSizes: Map[String, Long])
    : (Long, Map[String, Long]) =
  {
    tree match {
      case Dir(name, content) => {
        val (dirSize, newDirSizes) = content.map(
            t => calcDirSize(path + SPLIT + name, t, dirSizes)
        ).reduce((a1, a2) => (a1._1 + a2._1, a1._2 ++ a2._2))
        (dirSize, newDirSizes + (path + SPLIT + name -> dirSize))
      }
      case File(name, size) => (size, dirSizes)
    }
  }

  def read(
    input: String
  ) = {
    val root: Dir = Dir()
    Source.fromFile(input)
        .getLines
        .foldLeft(Pwd(Vector(root)))(
          (pwd, line) => line match {
            case s"$$ cd /" => pwd
            case s"$$ cd .." => cdUp(pwd)
            case s"$$ cd $dir" => mkDir(pwd, dir)
            case s"$$ ls" => pwd
            case s"dir $dir" => pwd
            case s"$size $filename" => addContent(pwd, File(filename, size.toLong))
            case s => pwd
          }
        )
  }
}
