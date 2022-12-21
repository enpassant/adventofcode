package adventofcode

import scala.collection.immutable.Queue
import scala.io.Source

object Graph {

  def postorder[Node, Value](
    node: Node,
    getChildren: Node => Vector[Node],
    visit: (Node, Vector[Value]) => Value
  ): Value = {
    val children = getChildren(node)
    val values = children.map(postorder(_, getChildren, visit))
    visit(node, values)
  }

  def bfs[Node, Value](
    node: Node,
    getChildren: Node => Vector[Node],
    isGoal: (Node) => Boolean
  ): Option[Node] = {
    def loop(explored: Set[Node], queue: Queue[Node]): Option[Node] = {
      if (queue.isEmpty) {
        None
      } else {
        val (node, newQueue) = queue.dequeue
        if (isGoal(node)) {
          Some(node)
        } else {
          val children = getChildren(node)
            .filter(node => !explored.contains(node))
          loop(
            (explored + node) ++ children,
            newQueue ++ children
          )
        }
      }
    }

    loop(Set(), Queue(node))
  }
}
