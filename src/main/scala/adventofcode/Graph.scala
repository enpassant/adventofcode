package adventofcode

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
}
