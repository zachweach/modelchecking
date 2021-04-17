package sol

import tester.Tester

object ModelCheckingTestSuite {

  // Preliminary test to make sure the graphs were properly being created since I used show in nearly every other test
  def testShow(t: Tester): Unit = {
    val testALGraph = new AdjacencyListGraph[String]
    val one = testALGraph.createNode("one")
    val two = testALGraph.createNode("two")
    val three = testALGraph.createNode("three")
    testALGraph.addEdge(one, two)
    testALGraph.addEdge(one, two)
    testALGraph.addEdge(one, three)
    testALGraph.addEdge(three, one)

    /*val testMGraph = new MatrixGraph[String](2)
    val three = testMGraph.createNode("three")
    val four = testMGraph.createNode("four")
    testMGraph.addEdge(three, four)
    testMGraph.addEdge(three, four)*/

    //testALGraph.show()
    //testMGraph.show()
  }

  // Testing the methods (and constructor) for ALNode
  def testGetContents(t: Tester): Unit = {
    val testGraph1 = new AdjacencyListGraph[StateData]
    val node1 = testGraph1.createNode(new StateData(List("alpha", "beta", "gamma")))
    //testGraph1.show()

    t.checkExpect(node1.getContents().hasLabel("alpha"))
    t.checkExpect(node1.getContents().hasLabel("beta"))
    t.checkExpect(node1.getContents().hasLabel("gamma"))
    t.checkExpect(!node1.getContents().hasLabel("delta"))

    val testGraph2 = new AdjacencyListGraph[Double]
    val node2 = testGraph2.createNode(2.04)
    val node3 = testGraph2.createNode(5.45)
    val node4 = testGraph2.createNode(5.45)
    //testGraph2.show()

    t.checkExpect(node2.getContents() + node3.getContents() == 7.49)
    t.checkExpect(node2.getContents() != node4.getContents())
    t.checkExpect(node3.getContents() == node4.getContents())
  }

  def testNodeAddNexts(t: Tester): Unit = {
    val testGraph1 = new AdjacencyListGraph[StateData]
    val node1 = testGraph1.createNode(new StateData(List("a")))
    val node2 = testGraph1.createNode(new StateData(List("b")))
    val node3 = testGraph1.createNode(new StateData(List("c")))
    val node4 = testGraph1.createNode(new StateData(List("d")))

    node1.addEdge(node2)
    node1.addEdge(node3)
    //testGraph1.show()

    val actual = node1.getNexts()
    val expected = List(node2, node3)

    for (a <- actual) {
      t.checkExpect(expected.contains(a))
    }

    for (e <- expected) {
      t.checkExpect(actual.contains(e))
    }

    t.checkExpect(node2.getNexts().isEmpty)
    t.checkExpect(node3.getNexts().isEmpty)
    t.checkExpect(node4.getNexts().isEmpty)

    node2.addEdge(node4)
    t.checkExpect(node2.getNexts().contains(node4))
    t.checkExpect(node4.getNexts().isEmpty)
    //testGraph1.show()

    val testGraph2 = new AdjacencyListGraph[List[Int]]
    val node5 = testGraph2.createNode(List(5))
    val node6 = testGraph2.createNode(List(6))
    val node7 = testGraph2.createNode(List(7))

    node5.addEdge(node6)
    node6.addEdge(node7)
    node7.addEdge(node5)
    //testGraph2.show()

    t.checkExpect(node5.getNexts().contains(node6))
    t.checkExpect(!node5.getNexts().contains(node7))
    t.checkExpect(!node5.getNexts().contains(node5))
  }

  // Testing the methods (and constructor) for AdjacencyListGraph
  def testCreateNode(t: Tester): Unit = {
    val testGraph1 = new AdjacencyListGraph[StateData]
    val node1 = testGraph1.createNode(new StateData(List("1")))
    t.checkExpect(node1.getContents().hasLabel("1"))
    //testGraph1.show()

    val testGraph2 = new AdjacencyListGraph[Int]
    val node2 = testGraph2.createNode(1)
    t.checkExpect(node2.getContents() == 1)
    //testGraph2.show()
  }

  def testAddEdge(t: Tester): Unit = {
    val testGraph1 = new AdjacencyListGraph[StateData]
    val node1 = testGraph1.createNode(new StateData(List("red")))
    val node2 = testGraph1.createNode(new StateData(List("green")))
    val node3 = testGraph1.createNode(new StateData(List("blue")))
    val node4 = testGraph1.createNode(new StateData(List("orange")))
    val nodes = List(node1, node2, node3, node4)

    for (node <- nodes) testGraph1.addEdge(node1, node)
    testGraph1.addEdge(node4, node1)
    testGraph1.addEdge(node3, node4)
    //testGraph1.show()
    t.checkExpect(node4.getNexts().contains(node1))
    t.checkExpect(node3.getNexts().contains(node4))
    t.checkExpect(node1.getNexts().length == 4)

    val testGraph2 = new AdjacencyListGraph[String]
    val node5 = testGraph2.createNode("red")
    val node6 = testGraph2.createNode("yellow")
    val node7 = testGraph2.createNode("green")
    val allNodes = List(node5, node6, node7)

    for (node <- allNodes) {
      testGraph2.addEdge(node5, node)
      testGraph2.addEdge(node6, node)
      testGraph2.addEdge(node7, node)
    }
    //testGraph2.show()
    for (fNode <- allNodes)
      for (sNode <- allNodes)
        t.checkExpect(fNode.getNexts().contains(sNode))
  }

  // Testing the methods (and constructor) for StateData
  def testStateData(t: Tester): Unit = {
    val colorList = List("red", "green", "yellow")
    val data1 = new StateData(colorList)
    val data2 = new StateData(List())

    for (color <- colorList)
      t.checkExpect(data1.hasLabel(color))

    t.checkExpect(!data1.hasLabel("blue"))
    t.checkExpect(!data2.hasLabel("red"))
  }

  // Testing the methods (and constructor) for StateMachine
  def testCSVRead(t: Tester): Unit = {
    val machine1 = new StateMachine(new AdjacencyListGraph[StateData])
    machine1.initFromCSV("data\\street-light-nodes.csv", "data\\street-light-edges.csv", "0")
    // Created this method ".getStateGraph" temporarily to make sure the graph was being created
    // machine1.getStateGraph.show()

    val machine2 = new StateMachine(new AdjacencyListGraph[StateData])
    t.checkException(new RuntimeException("Start state was not listed in the nodeCSV"), machine2, "initFromCSV",
      "data\\street-light-nodes.csv", "data\\street-light-edges.csv", "15")

    // The following commented-out code tests the IOException that is caught when reading the edgesCSV
    // and finding a node that does not exist. Since this exception is caught and printed, I could not
    // run a checkException.
    /*val errorEdges = List("test-edges0.csv", "test-edges1.csv", "test-edges2.csv")
    for (edgeFile <- errorEdges) {
      machine2.initFromCSV("data\\street-light-nodes.csv", edgeFile, "0")
    }*/
  }

  def testNeverAlways(t: Tester): Unit = {
    val machine1 = new StateMachine(new AdjacencyListGraph[StateData])
    machine1.initFromCSV("data\\street-light-nodes.csv", "data\\street-light-edges.csv", "1")
    def twoGreen(s: StateData): Boolean = s.hasLabel("bigAveGreen") && s.hasLabel("smallStGreen")
    def oneRed(s: StateData): Boolean = s.hasLabel("bigAveRed") || s.hasLabel("smallStRed")

    t.checkExpect(machine1.checkNever(twoGreen).isEmpty)
    t.checkExpect(machine1.checkAlways(oneRed).isEmpty)

    val machine2 = new StateMachine(new AdjacencyListGraph[StateData])
    machine2.initFromCSV("data\\street-light-nodes.csv", "data\\street-light-edges.csv", "8")
    val failGreen = machine2.checkNever(twoGreen).get.getContents()
    val failRed = machine2.checkAlways(oneRed).get.getContents()
    t.checkExpect(failGreen.hasLabel("bigAveGreen") && failGreen.hasLabel("smallStGreen"))
    t.checkExpect(failRed.hasLabel("bigAveYellow") || failRed.hasLabel("smallStGreen"))

    val machine3 = new StateMachine(new AdjacencyListGraph[StateData])
    machine3.initFromCSV("data\\test-nodes.csv", "data\\test-edges.csv", "0")
    t.checkExpect(machine3.checkNever(data => data.hasLabel("F")).isDefined)
    t.checkExpect(machine3.checkNever(data => data.hasLabel("Z")).isEmpty)
    t.checkExpect(machine3.checkNever(data => data.hasLabel("I")).isEmpty)
    t.checkExpect(machine3.checkAlways(data => data.hasLabel("none")).get.getContents().hasLabel("A"))

    val machine4 = new StateMachine(new AdjacencyListGraph[StateData])
    machine4.initFromCSV("data\\test-nodes.csv", "data\\test-edges.csv", "8")
    t.checkExpect(machine4.checkAlways(data => data.hasLabel("none")).get.getContents().hasLabel("I"))
    t.checkExpect(machine4.checkAlways(data => data.hasLabel("I")).isEmpty)
    t.checkExpect(machine4.checkNever(data => data.hasLabel("C")).isEmpty)
  }

  def main(args: Array[String]): Unit = {
    Tester.run(ModelCheckingTestSuite)
  }

}
