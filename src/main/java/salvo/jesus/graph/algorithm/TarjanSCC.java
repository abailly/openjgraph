package salvo.jesus.graph.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.DirectedGraphImpl;
import salvo.jesus.graph.Edge;

/**
 * An implementation of the SCC algorithm based on Tarjan's algorithm
 * 
 * This implementation of SCC algorithm is based on the famous linear Tarjan's
 * algorithm.
 * 
 * @author Arnaud Bailly
 * @version $Id: TarjanSCC.java 1400 2007-05-14 11:17:22Z /CN=nono $
 */
public class TarjanSCC extends StronglyConnectedComponentsAlgorithm {

	// //////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	// //////////////////////////////////////////////////////////////////

	// hashtable of data from vertices
	private Map vertices = new HashMap();

	// / private structure for node info
	private class SCCData {
		// first seen
		int link;

		// last seen
		int number;

		// visited flag
		boolean visited;

		// associated vertex
		Object v;

		SCCData(Object v) {
			this.v = v;

		}
	}

	// dating of visited vertices
	private int datescc = 0;

	// stack for visit
	private Stack stack = new Stack();

	// array of list of vertices containing scc indexed by link number
	private DirectedGraph[] subgraphs;

	// //////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	// //////////////////////////////////////////////////////////////////

	/**
	 * constructs a SCC algorithm from the given directed graph
	 * 
	 * @param digraph
	 *            a DirectedGraph object
	 */
	public TarjanSCC(DirectedGraph digraph) {
		super(digraph);
		// set all vertices unvisited and put data for vertices in hashmap
		Iterator it = digraph.getAllVertices().iterator();
		while (it.hasNext()) {
			Object v = it.next();
			SCCData data = new SCCData(v);
			data.link = data.number = -1;
			data.visited = false;
			vertices.put(v, data);
		}
		subgraphs = new DirectedGraph[digraph.getVerticesCount()];
	}

	// //////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	// //////////////////////////////////////////////////////////////////

	/**
	 * Returns a Set of DirectedGraph objects, each graph being a subgraph of
	 * the initial graph with strongly connnected vertices
	 * 
	 * @return a Set of SCC
	 */
	public Set SCC() {
		Set sccs = new LinkedHashSet();
		// iterate over vertices
		Iterator it = vertices.keySet().iterator();
		// construct data for each vertices
		while (it.hasNext()) {
			Object v = it.next();
			SCCData data = (SCCData) vertices.get(v);
			if (!data.visited) // found an SCC root
				SCC(v);
		}
		/* add all subgraphs removing null elements */
		for (int i = 0; i < subgraphs.length; i++)
			if (subgraphs[i] != null)
				sccs.add(subgraphs[i]);
		return sccs;
	}

	// //////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	// //////////////////////////////////////////////////////////////////

	/**
	 * Returns the subgraph rooted at v containing all strongly connected
	 * vertices from v. This method updates the SCCData objects attached to
	 * visited vertices, which allows the calling procedure to reconstruct the
	 * SCC.
	 * 
	 * @param v
	 *            a Object object contained in the underlying directed graph
	 */
	private void SCC(Object v) {
		// update data
		SCCData data = (SCCData) vertices.get(v);
		data.visited = true;
		data.link = data.number = datescc++;
		stack.push(data);
		// retriev all outgoing edges
		Iterator it = digraph.getOutgoingEdges(v).iterator();
		while (it.hasNext()) {
			Edge e = (Edge) it.next();
			// visit connected nodes
			Object opp = e.getOppositeVertex(v);
			SCCData oppdata = (SCCData) vertices.get(opp);
			if (!oppdata.visited) {
				SCC(opp);
				data.link = oppdata.link < data.link ? oppdata.link : data.link;
			} else if (oppdata.number < data.number) // e is cross-link
				if (stack.contains(oppdata)) // back link
					data.link = oppdata.number < data.link ? oppdata.number
							: data.link;
		}
		// construct subgraph
		if (data.link == data.number) { // root of SCC
			DirectedGraph sgraph = new DirectedGraphImpl();
			subgraphs[data.link] = sgraph;
			try {
				sgraph.add(data.v);
				while (!stack.empty()
						&& ((SCCData) stack.peek()).number >= data.number) {
					SCCData scc = (SCCData) stack.pop();
					sgraph.add(scc.v);
					// add all opposite vertices with same link number
					Iterator oit = digraph.getOutgoingEdges(scc.v).iterator();
					while (oit.hasNext()) {
						Edge edge = (Edge) oit.next();
						if (((SCCData) vertices.get(edge
								.getOppositeVertex(scc.v))).link == data.link)
							sgraph.addEdge(edge);
					}
				}
			} catch (Exception ex) {
				System.err.println("Caught exception " + ex);
				ex.printStackTrace();

			}
		}
	}
}
