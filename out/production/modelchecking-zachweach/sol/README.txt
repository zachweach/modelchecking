-----------------------------------------------------------------------------------------------------------------------
PROJECT OUTLINE
This project contains the classes an methods required to represent graphs and state machines. It utalizes three
classes and several methods to do this.

AdjacencyListGraph:
This class serves as the representation of a graph. It contains a list of all of the nodes in the graph and can
create new nodes and add directional edges between existing nodes. It contains the sub-class ALNode, which stores
information about individual nodes.
Methods:
    addNode: adds a node to the graph with specified contents (does not create an edge automatically)
    addEdge: adds a directional (one way) edge that connects two nodes
    show: prints the contents of the graph

ALNode:
This is a sub-class of AdjacencyListGraph and stores information about individual nodes in a graph. It is constructed
with the contents of the node and all of the nodes it can get to.
Methods:
    getContents: returns the contents at this node
    getNexts: finds all nodes connected to this node by outgoing edges
    addEdge: adds an outgoing directional edge from this node to another node

StateMachine:
A state machine stores a graph, which represents all of the possible states that can exist within a program
and all of the ways that program can change between states. Each state is represented by a specific StateData.
Methods:
    initFromCSV: initializes a StateMachine from CSV files
    setStart: sets the starting node of the SateMachine
    checkAlways: checks whether some checkNode function returns true on every state that is reachable from the startState
    checkNever: checks whether the checkNode function returns false on every state that is reachable from the startState

StateData:
Stores the information about the current state of a program with specific "labels", which are represented as strings.
You are able to check if this current state contains a specific label with a method.
Methods:
    hasLabel: checks if this StateData object has a given label

-----------------------------------------------------------------------------------------------------------------------
Documentation of each test file that you created (if any).

I created one test file for this program and used the supplied "street-light" data files for testing. The files
that I created depict a graph where each node is labeled with a letter. It was created exclusively to test edge
cases in my implementation. These edge cases include: nodes that have no edges connected to it, nodes that have
multiple edges leading from it, cycles, and paths that do not lead directly to a failing node. Image depiction of
this graph can be found in testGraphVis.png in the data folder and the nodes and edges CSV files can also be
found in the data folder.

I also created three files (test-edges0.csv, test-edges1.csv, test-edges2.csv) that test different cases that
should throw exceptions when reading into using initFromCSV. These exceptions, however are caught and printed,
so they could only be tested by printing rather than checkException. As such, they are just included in the
sol file rather than the data file.
-----------------------------------------------------------------------------------------------------------------------
What is the worst-case running time of your checkNever method? State your answer and briefly justify it. Your
answer should explicitly state what the variable is (as in “quadratic in n where n is the number of turtles
in the marina”. Either terms like “quadratic” or formulas like n^2 are fine.)

The checkNever method takes linear O(n) time, where n is the number of nodes in the graph. This is because the
method, through the helper method checkCond, loops through every node accessible from the starting node and
performs constant time operations on that node. In the worst case, every node in the graph is accessible from
that start node and every node must be checked.

On the other hand, we must take into account the time complexity of whatever method is passed into
checkNever, as this method also must be run on every node. If this operation does not take constant time,
then checkNever also no longer takes constant time. For example, the hasLabel method for stateData takes linear
O(m) time, where m is the number of labels. In this case, the time complexity of checkNever becomes O(mn).

Overall, the total time complexity of checkNever is O(n) * O(?), where O(?) is the time complexity of
the checkNode method that is passed into checkNever.
-----------------------------------------------------------------------------------------------------------------------
Assume that you knew some state would violate checkNever, but you don’t know which one (so you still want to run
the checkNever algorithm to figure out where it is). Would that change the worst-case running time? Why or why not?

No, the worst-case running time will still remain the same. In the worst case, the node that violates checkNever
will be the last node in the graph that is checked, so the method will still have to run on every node in the graph.
The only thing that changes (compared to knowing that checkNever will return None) is the very last iteration will
not run the for-loop in checkCond. The for-loop in this instance, however, would always run over every node that
the failing node is connected to and instantly return None (since it has already been checked). This difference would
not effect overall runtime in terms of Big-O runtime, but is still worthwhile to mention.
-----------------------------------------------------------------------------------------------------------------------
Assume that we had asked you to modify checkNever and checkAlways to return not just an Option[Node[StateData]]
(indicating whether there is a state that passes/fails the check), but also the path from the start state showing
how to get to the error state (if one exists). How would this change the running time? Explain your answer.

Presumably, this would not affect the time complexity much, but is would affect the space complexity (since an
intermediate data structure would need to store this list). Just thinking about runtime for now, this only adds
constant time operations to the already existing methods. We would achieve this by creating a mutable list which
stores the path of nodes required to get to the node we are currently searching. We would do this by adding the
node we are searching to the end of the list (constant time) and then either 1) returning this list if we find
the violating node, 2) adding the next node that we search to the end of the list, or 3) removing the last node
from the list (constant time, which occurs when we determine that the current node does not have a path that
leads to a failing node). Since this functionality only adds constant time operations, it will not affect the
Big-O runtime of checkNever and checkAlways.
-----------------------------------------------------------------------------------------------------------------------
Continue with the previous question’s setup about returning the path to a violating state. What would you use as
the return type of checkNever and checkAlways in this situation?

Since we must return two things here (both the node that causes the violation and the path to get to that node),
I would probably change the return type to be a Option containing a Tuple that contains the violating node and
the path required to get to it, or None if such a node does not exist.
-----------------------------------------------------------------------------------------------------------------------
You implemented one of the two Graph representations (Adjacency List or Adjacency Matrix). Why did you implement
the one that you did (honesty here is just fine)? State one performance-related advantage to each representation
as a contrast the other. Briefly explain any programmer/coding-facing tradeoffs between them.

i. I actually decided to implement both graph representations. I chose to submit the Adjacency List implementation
because I found it easier when testing and visualizing to not worry about the limitations regarding the matrix
implementation (e.g. maxSize or the fact that you cannot have multiple directional edges between the same nodes).

ii. The matrix implementation is more space efficient, while the adjacency list is more time efficient. Since the
list implementation must store every node, which then stores a list of every node that it can get to, it must store
the nodes in the graph multiple times in multiple places. The matrix implementation, on the other hand, only has to
store the nodes once, since it stores the edges as an array of Booleans (which takes up much less space). This,
however, is exactly what makes its runtime worse. The getNexts methods for the list implementation runs in constant
time, since it only has to call the field from the constructor. The matrix implementation, on the other hand, takes
linear O(n) time for this method, where n is the maximum number of nodes (maxSize). This is because the method must
check the Boolean value of each slot in the array corresponding to the index of the node that it is being called on.

iii. As mentioned previously, the matrix implementation has certain limitations that are not present with the
adjacency list implementation. First, you must know exactly the maximum size of how may nodes you need to create
when initializing the matrix graph and you cannot add past the maximum. Additionally, you cannot have multiple
directional edges that point between the same nodes. For the state machine, this is not important, but for other
possible uses of graphs, it might be important to be able to do this.
-----------------------------------------------------------------------------------------------------------------------