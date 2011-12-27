package salvo.jesus.graph.algorithm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import salvo.jesus.graph.DirectedAcyclicGraph;
import salvo.jesus.graph.DirectedAcyclicGraphImpl;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.DirectedGraphImpl;
import salvo.jesus.graph.Edge;


/**
 * An implementation of the SCC algorithm based on CLR classic
 * book on algorithms.
 *
 * This implementation of SCC algorithm is based on the one found in
 * "Introduction to Algorithms" by Cormen , Leiserson and Rivest. Basically,
 * this is two Depth-First traversal, one of the graph and the next on the
 * mirror graph with marking along the way.
 *
 * @author Arnaud Bailly
 * @version $Id: RivestSCC.java 1268 2006-08-14 13:25:12Z nono $
 */
public class RivestSCC extends StronglyConnectedComponentsAlgorithm {

	////////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	////////////////////////////////////////////////////////////////////

	// Set of visited vertices data, sorted in decreasing order
	// of end of visit
	private Set vertices = new TreeSet();

	private Set visited = new HashSet();

	/// private structure for node info
	private class SCCData implements Comparable {
		// start of visit
		int start;
		// end of visit
		int end;
		// associated vertex
		Object v;

		SCCData(Object v) {
			this.v = v;
		}

		public int compareTo(Object o) {
			SCCData other = (SCCData) o;
			if (other.end > end)
				return 1;
			else if (other.end < end)
				return -1;
			return 0;
		}

	}

	// dating of visited vertices
	private int datescc = 0;

	// mirror graph
	private DirectedGraph mirror;

	// sccs 
	private Set sccs = null;

	// cover graph
	private DirectedAcyclicGraph coverGraph = new DirectedAcyclicGraphImpl();

	////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	////////////////////////////////////////////////////////////////////

	/**
	 * constructs a SCC algorithm from the given directed graph
	 *
	 * @param digraph a DirectedGraph object
	 */
	public RivestSCC(DirectedGraph digraph) {
		super(digraph);
		mirror = new DirectedGraphImpl();
	}

	////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	////////////////////////////////////////////////////////////////////

	/**
	 * Returns a Set of DirectedGraph objects, each graph being a subgraph
	 * of the initial graph with strongly connnected vertices
	 *
	 * @return a Set of SCC
	 */
	public Set SCC() {
		try {
			sccs = new java.util.HashSet();
			// iterate over vertices
			Iterator it = digraph.getVerticesIterator();
			// depth-first traversal on digraph updates 
			// vertices set
			while (it.hasNext()) {
				Object v =  it.next();
				SCCData data = new SCCData(v);
				if (!visited.contains(v))
					visit1(data);
			}
			visited.clear();
			// depth first traversal on mirror graph ordered
			// on end of visit date
			it = vertices.iterator();
			while (it.hasNext()) {
				SCCData data = (SCCData) it.next();
				Object v = data.v;
				if (!visited.contains(v)) { // found an SCC root
					System.err.println(
						"Visit2 for "
							+ data.v
							+ " ("
							+ data.start
							+ ","
							+ data.end
							+ ")");
					DirectedGraph sgraph = new DirectedGraphImpl();
					visit2(v, sgraph);
					sccs.add(sgraph);
				}
			}
			return sccs;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns the covering graph for this SCC
	 * 
	 * The covering graph is obtained from the underlying graph by keeping one 
	 * vertex per SCC and only cover edges, that is edges which connects SCCs. The 
	 * resulting graph is a Directed Acyclic Graph.
	 *
	 * @return a DAG covering the SCCs
	 */
	public DirectedAcyclicGraph getCoverGraph() {
		return coverGraph;
	}

	////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	////////////////////////////////////////////////////////////////////

	/**
	 * Do a depth-first traversal from vertex in data, marking node along
	 * the way and constructing the mirror graph
	 *
	 * @param v an SCCData object contained in the underlying directed graph
	 */
	private void visit1(SCCData data) throws Exception {
		// update data
		visited.add(data.v);
		data.start = ++datescc;
		// update mirror graph
		mirror.add(data.v);
		// retriev all outgoing edges
		Iterator it = digraph.getOutgoingEdges(data.v).iterator();
		while (it.hasNext()) {
			Edge e = (Edge) it.next();
			// visit connected nodes	    
			Object opp = e.getOppositeVertex(data.v);
			// update mirror
			mirror.addEdge(opp, data.v);
			if (!visited.contains(opp))
				visit1(new SCCData(opp));
		}
		data.end = ++datescc;
		System.err.println(
			"Data for " + data.v + " (" + data.start + "," + data.end + ")");
		vertices.add(data);
	}

	/**
	 * Do a depth-first traversal from the vertex in data, constructing
	 * the subgraph along the way.
	 *
	 * @param data a vertex
	 * @param sgraph the subgraph to add data to
	 */
	private void visit2(Object v, DirectedGraph sgraph) throws Exception {
		// update data
		visited.add(v);
		// update mirror graph
		sgraph.add(v);
		System.err.println(
			"Subgraph "
				+ ((Object) sgraph).toString()
				+ " : added vertex "
				+ v);
		// retriev all outgoing edges
		Iterator it = mirror.getOutgoingEdges(v).iterator();
		while (it.hasNext()) {
			Edge e = (Edge) it.next();
			// visit connected nodes	    
			Object opp = e.getOppositeVertex(v);
			if (!visited.contains(opp)) {
				visit2(opp, sgraph);
			}
		}
	}
}
