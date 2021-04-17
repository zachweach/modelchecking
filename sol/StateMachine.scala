package sol

import src.Graph
import src.Node

// ------------- DO NOT CHANGE --------------------
import org.apache.commons.csv._
import java.io.{File, FileReader, IOException}
import scala.collection.JavaConverters.iterableAsScalaIterableConverter
import scala.collection.mutable
// ------------------------------------------------

/**
 * Class of the State Machine.
 * @param stateGraph - an object which implements Graph stored inside a
 *                   stateMachine.
 */
class StateMachine(stateGraph: Graph[StateData]) {

  // field for the starting state of the state machine
  private var startState : Option[Node[StateData]] = None

  /**
   * Initializes a StateMachine from CSV files.
   * @param nodeCSV - the filename of the CSV containing node information
   * @param edgesCSV - the filename of the CSV containing edges information
   * @param startState - the starting state of the state machine (the id of the
   *                   node)
   */
  def initFromCSV(nodeCSV: String, edgesCSV: String, startState: String) {
    val format = CSVFormat.RFC4180.withHeader();

    // Parse nodeCSV
    val idMap = new mutable.HashMap[String, Node[StateData]]()
    try {
      val parser: CSVParser = new CSVParser(new FileReader(new File(nodeCSV)),
        format)
      val records = parser.getRecords().asScala.toList
      for (record <- records) {
        idMap(record.get(0)) = stateGraph.createNode(new StateData(record.get(1).split(";").toList))

        if(record.get(0).equals(startState)) {
          this.startState = Some(idMap(record.get(0)))
        }
      }
    }
    catch {
      case e: IOException => println("error reading " + nodeCSV)
    }

    if (this.startState.isEmpty) throw new RuntimeException("Start state was not listed in the nodeCSV")

    // Parse edgeCSV
    try {
      val parser: CSVParser = new CSVParser(new FileReader(new File(edgesCSV)), format)
      val records = parser.getRecords().asScala.toList
      for (record <- records) {
        val fromID = record.get(0)
        val toID = record.get(1)
        if (!idMap.contains(fromID) || !idMap.contains(toID)) {
          throw new IOException("edgesCSV tried to use a state that was not listed in the nodeCSV")
        }

        stateGraph.addEdge(idMap(fromID), idMap(toID))
      }
    }
    catch {
      case e: IOException => println("error reading" + edgesCSV)
    }
  }

  /**
   * Sets the starting node of the SateMachine
   * @param startNode Node of StataData that represents the new starting node
   */
  def setStart(startNode: Node[StateData]): Unit = {
    startState = Some(startNode)
  }

  /**
   * From the starting node, checks to make sure a certain StateData condition is not reachable
   * @param checkNode function that takes in a StateData and returns a Boolean representing if that
   *                  particular StataData meets a certain condition
   * @param failCond Boolean that represents the result of checkNode that should be considered a
   *                 "failure". This should be "false" if the condition should be met for every
   *                 reachable node and "true" is the condition should not be met.
   * @param checkedStates a Set of all of the Nodes of StateData that have already been checked
   * @return Option, which is None if the failCond is never reached or, if the failCond is reached,
   *         Some Node of StateData representing the node that failed
   */
  private def checkCond(checkNode: StateData => Boolean, failCond: Boolean, currState: Node[StateData],
                        checkedStates: Set[Node[StateData]]): Option[Node[StateData]] = {
    if (checkedStates.contains(currState)) return None
    val newChecked = checkedStates + currState

    if (checkNode(currState.getContents()) == failCond) return Some(currState)

    for (node <- currState.getNexts()) {
      val nodeCheck = checkCond(checkNode, failCond, node, newChecked)
      if (nodeCheck.isDefined) return nodeCheck
    }

    None
  }

  /**
   * Checks whether the checkNode function returns true on every state that is reachable from the startState
   * @param checkNode function that takes in a StateData and returns a Boolean representing if that
   *                  particular StataData meets a certain condition
   * @return Option, which is None if the function returns true on every state or Some Node of StateData
   *         representing the node that returned false from checkNode
   */
  def checkAlways(checkNode: StateData => Boolean): Option[Node[StateData]] = {
    checkCond(checkNode, false, startState.get, Set())
  }

  /**
   * Checks whether the checkNode function returns false on every state that is reachable from the startState
   * @param checkNode function that takes in a StateData and returns a Boolean representing if that
   *                  particular StataData meets a certain condition
   * @return Option, which is None if the function returns false on every state or Some Node of StateData
   *         representing the node that returned true from checkNode
   */
  def checkNever(checkNode: StateData => Boolean): Option[Node[StateData]] = {
    checkCond(checkNode, true, startState.get, Set())
  }
}