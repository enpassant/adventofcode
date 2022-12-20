package adventofcode.year2022

import scala.collection.immutable.Queue
import scala.io.Source
import scala.annotation.tailrec

object PuzzlesDay19 extends App {
  enum Kind {
    case Ore, Clay, Obsidian, Geode
  }
  import Kind._

  case class Robot(kind: Kind, costs: Map[Kind, Int] = Map())
  case class Blueprint(num: Int, robots: Map[Kind, Robot])

  case class State(
    time: Int = 0,
    minerals: Map[Kind, Int] =
      Map(Ore -> 0, Clay -> 0, Obsidian -> 0, Geode -> 0),
    robots: Map[Kind, Int] =
      Map(Ore -> 1, Clay -> 0, Obsidian -> 0, Geode -> 0),
    builded: Seq[(Int, Kind)] = Seq()
  )

  case class Node(state: State, robot: Robot)

  def puzzle(input: String) = {
    val data = read(input)
    data.map(blueprint => {
        val geode = useBlueprint(blueprint, 24).get
        blueprint.num * geode
    }).sum
  }

  def puzzleTwo(input: String) = {
    val data = read(input)
    data.take(3).map(blueprint => {
        val geode = useBlueprint(blueprint, 32).get
        geode
    }).product
  }

  def useBlueprint(blueprint: Blueprint, maxTime: Int) = {
    @tailrec
    def loop(max: Int, visited: Set[Node], queue: Queue[Node]): Option[Int] = {
      if (queue.isEmpty) {
        Some(max)
      } else {
        val (node, newQueue) = queue.dequeue
        val newMax = if (node.state.time >= maxTime) {
          val geode = node.state.minerals(Geode)
          Math.max(max, node.state.minerals(Geode))
        } else {
          max
        }
        if (node.state.time < maxTime) {
          val neighbors = simulate(blueprint, maxTime)(node)
          loop(newMax, visited, newQueue ++ neighbors)
        } else {
          loop(newMax, visited, newQueue)
        }
      }
    }

    val nodes = Seq(
      Node(State(),  blueprint.robots(Ore)),
      Node(State(),  blueprint.robots(Clay))
    )
    loop(0, Set(), Queue() ++ nodes)
  }

  def simulate(blueprint: Blueprint, maxTime: Int)(node: Node): Seq[Node] = {
    @tailrec
    def loop(state: State, robot: Robot): Seq[Node] = {
      val newTime = state.time + 1
      if (newTime > maxTime) {
        Seq(Node(state.copy(time = newTime), Robot(Geode)))
      } else {
        val newState = if (isRobotCreatable(robot, state)) {
          val kind = robot.kind
          val robotCount = state.robots(kind)
          val newMinerals = robot.costs.foldLeft(state.minerals)(payRobot)
          State(
            newTime,
            newMinerals,
            robots = state.robots.updated(kind, robotCount + 1),
            state.builded :+ (newTime, robot.kind)
          )
        } else {
          state
        }
        val collectedMinerals =
          state.robots.foldLeft(newState.minerals)(collectMinerals)
        val finalState = State(
          newTime,
          collectedMinerals,
          newState.robots,
          newState.builded
        )
        if (newState == state) {
          loop(finalState, robot)
        } else {
          val newRobots = getCreatableRobots(blueprint, finalState)
          newRobots.map(robot => Node(finalState, robot))
        }
      }
    }

    loop(node.state, node.robot)
  }

  def collectMinerals(minerals: Map[Kind, Int], robot: (Kind, Int)) = {
    minerals.updated(robot._1, minerals(robot._1) + robot._2)
  }

  def payRobot(minerals: Map[Kind, Int], robot: (Kind, Int)) = {
    minerals.updated(robot._1, minerals(robot._1) - robot._2)
  }

  def isRobotCreatable(robot: Robot, state: State) = {
      robot.costs.forall { case (kind, cost) => cost <= state.minerals(kind) }
  }

  def getCreatableRobots(blueprint: Blueprint, state: State) = {
    val oreRobot = state.robots(Ore)
    val clayRobot = state.robots(Clay)
    val obsidianRobot = state.robots(Obsidian)
    val geodeRobot = state.robots(Geode)
    val time = state.time
    Seq(
      blueprint.robots(Clay),
      blueprint.robots(Obsidian),
      blueprint.robots(Geode),
      blueprint.robots(Ore)
    ).filter(_.kind match {
      case Ore => time < 10 &&
        oreRobot < 5 && clayRobot < 4 && obsidianRobot < 2 && geodeRobot < 1
      case Clay => time < 20 && clayRobot < 10 && obsidianRobot < 4 && geodeRobot < 2
      case Obsidian => clayRobot > 0 && obsidianRobot < 12 && geodeRobot < 4
      case Geode => obsidianRobot > 0
    })
  }

  def read(input: String) = {
    Source.fromFile(input)
      .getLines
      .filter(!_.isBlank())
      .map(line => line match {
        case s"Blueprint $num: $blueprint" => {
          val robots = blueprint.split("\\. ")
            .map(_ match {
              case s"Each ore robot costs $ore ore" =>
                Robot(Ore, Map(Ore -> ore.toInt))
              case s"Each clay robot costs $ore ore" =>
                Robot(Clay, Map(Ore -> ore.toInt))
              case s"Each obsidian robot costs $ore ore and $clay clay" =>
                Robot(Obsidian, Map((Ore -> ore.toInt), (Clay -> clay.toInt)))
              case s"Each geode robot costs $ore ore and $obsidian obsidian$_" =>
                Robot(Geode, Map(Ore -> ore.toInt, Obsidian -> obsidian.toInt))
            }).map(r => (r.kind -> r))
            .toMap
          Blueprint(num.toInt, robots)
        }
      })
      .toSeq
  }
}
