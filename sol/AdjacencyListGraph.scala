package sol

import src.{Graph, Node}

/**
 * Class for the adjacency list representation of a Graph.
 *
 * @tparam T the type of the nodes' contents
 */
class AdjacencyListGraph[T] extends Graph[T] {
  private var allNodes: List[ALNode] = List()

  /**
   * An AdjacencyList implementation of a graph node
   * @param contents - the value stored at that node
   * @param getsTo - the list of nodes which this node gets to
   */
  class ALNode(val contents: T, var getsTo: List[ALNode]) extends Node[T] {
    @Override
    override def getContents(): T = contents

    @Override
    override def getNexts(): List[Node[T]] = getsTo

    @Override
    override def addEdge(toNode: Node[T]): Unit = {
      getsTo = toNode.asInstanceOf[ALNode] :: getsTo
    }

}
  @Override
  override def createNode(contents: T): Node[T] = {
    val newNode: ALNode = new ALNode(contents, List[ALNode]())
    allNodes = newNode :: allNodes
    newNode
  }

  @Override
  override def addEdge(fromNode: Node[T], toNode: Node[T]): Unit = fromNode.addEdge(toNode)

  @Override
  override def show(): Unit = {
    for (node <- allNodes) {
      var toPrint = node.getContents().toString + " gets to "
      if (node.getNexts().isEmpty) println(toPrint + "nothing.")
      else {
        for (to <- node.getNexts()) {
          toPrint += to.getContents().toString + ", "
        }
        println(toPrint.trim.stripSuffix(",") + ".")
      }
    }
  }
}
