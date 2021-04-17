package sol

import src.{Graph, Node}

import scala.collection.mutable

/**
 * Matrix implementation of a graph.
 * @param maxSize the maximum size of the graph, used for creating the array
 * @tparam T the type of the nodes' contents
 */
class MatrixGraph[T](val maxSize: Int) extends Graph[T] {
  if (maxSize < 0) throw new IllegalArgumentException("MaxSize cannot be negative.")

  val nodes: mutable.Map[Int, MNode] = mutable.Map()
  val graph: Array[Array[Boolean]] = Array.fill(maxSize)(new Array(maxSize))

  /**
   * Class for a node in adjacency matrix graph.
   * @param contents - the node's contents
   * @param index - the index in the matrix that corresponds to this node
   */
  class MNode(val contents: T, val index: Int) extends Node[T] {
    @Override
    override def getContents(): T = contents

    @Override
    override def getNexts(): List[Node[T]] = {
      var nexts: List[Node[T]] = List()
      var i = 0

      for (edge <- graph(index)) {
        if (edge) nexts = nodes(i) :: nexts
        i += 1
      }

      nexts
    }

    @Override
    override def addEdge(toNode: Node[T]): Unit =
      graph(index)(toNode.asInstanceOf[MNode].index) = true
  }

  private def nextID(): Int = {
    val maybeNext: Int = nodes.size
    if (maybeNext >= maxSize) throw new RuntimeException("Cannot exceed node capacity as set by maxSize")
    maybeNext
  }

  @Override
  override def createNode(contents: T): Node[T] = {
    val newNode: MNode = new MNode(contents, nextID())
    nodes.put(newNode.index, newNode)
    newNode
  }

  @Override
  override def addEdge(fromNode: Node[T], toNode: Node[T]): Unit = fromNode.addEdge(toNode)

  @Override
  override def show(): Unit = {
    for ((id, node) <- nodes) {
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
