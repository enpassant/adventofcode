package adventofcode.year2022

import scala.io.Source

object PuzzlesDay10 extends App {

  sealed trait Instruction

  case object Noop extends Instruction
  case class AddX(value: Int) extends Instruction

  case class CpuState(
    cycle: Int = 0,
    x: Int = 1,
    signalStrengths: List[Int] = Nil
  ) {
    val cycles = Set(20, 60, 100, 140, 180, 220)

    def tick() = {
      val nextCycle = cycle + 1
      if (cycles.contains(nextCycle)) {
        val signalStrength = nextCycle * x
        CpuState(nextCycle, x, signalStrength :: signalStrengths)
      } else {
        CpuState(nextCycle, x, signalStrengths)
      }
    }

    def addX(value: Int) = {
      val cpuState = tick().tick()
      CpuState(cpuState.cycle, cpuState.x + value, cpuState.signalStrengths)
    }
  }

  case class DisplayState(
    cycle: Int = 0,
    x: Int = 1,
    display: Vector[Char] = Vector()
  ) {
    def tick() = {
      val nextCycle = cycle + 1
      val dif = Math.abs(cycle % 40 - x)
      if (dif <= 1) {
        DisplayState(nextCycle, x, display :+ '#')
      } else {
        DisplayState(nextCycle, x, display :+ '.')
      }
    }

    def addX(value: Int) = {
      val cpuState = tick().tick()
      DisplayState(cpuState.cycle, cpuState.x + value, cpuState.display)
    }

    def show() = {
      display.grouped(40).map(_.mkString).mkString("\n")
    }
  }

  def puzzle(
    input: String
  ) = {
    val map = read(input)
    val cpuState = run(map)
    //println(cpuState)
    cpuState.signalStrengths.sum
  }

  def puzzleTwo(
    input: String
  ) = {
    val map = read(input)
    val displayState = display(map)
    displayState.show()
  }

  def run(program: List[Instruction]) = {
    program.foldLeft(CpuState())((cpuState, instruction) => instruction match {
      case Noop => cpuState.tick()
      case AddX(value) => cpuState.addX(value)
    })
  }

  def display(program: List[Instruction]) = {
    program.foldLeft(DisplayState())((state, instruction) => instruction match {
      case Noop => state.tick()
      case AddX(value) => state.addX(value)
    })
  }

  def read(
    input: String
  ) = {
    Source.fromFile(input)
        .getLines
        .map(line => line match {
          case s"noop" => Noop
          case s"addx $value" => AddX(value.toInt)
        })
        .toList
  }
}
