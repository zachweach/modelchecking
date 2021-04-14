package sol

import tester.Tester

object ModelCheckingTestSuite {

  def testShow(t: Tester): Unit = {
    val testALGraph: AdjacencyListGraph[String] = new AdjacencyListGraph
    val one = testALGraph.createNode("one")
    val two = testALGraph.createNode("two")
    testALGraph.addEdge(one, two)
    testALGraph.addEdge(one, two)

    val testMGraph: MatrixGraph[String] = new MatrixGraph[String](2)
    val three = testMGraph.createNode("three")
    val four = testMGraph.createNode("four")
    testMGraph.addEdge(three, four)
    testMGraph.addEdge(three, four)

    //testALGraph.show()
    //testMGraph.show()
  }

  def main(args: Array[String]): Unit = {
    Tester.run(ModelCheckingTestSuite);
  }

}
