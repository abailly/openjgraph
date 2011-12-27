package salvo.jesus.graph.algorithm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import salvo.jesus.graph.DirectedEdge;
import salvo.jesus.graph.DirectedEdgeImpl;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.DirectedGraphImpl;

/**
 * The base class for all algorithms extracting Strongly connected components
 * from a directed graph.
 * 
 * This class of algorithms retrieves the strongly connected components of a
 * directed graph as a Set of DirectedGraph objects. A strongly connected
 * component <emph>C</emph> of a graph <emph>G</emph> is a subgraph
 * <emph>(V,E)</emph> of <emph>G</emph> where the following property is true :
 * <br>
 * <emph>forall v,v' in V, exists path(v,v') and exists path(v',v)</emph><br>
 * 
 * @author Arnaud Bailly
 * @version $Id: StronglyConnectedComponentsAlgorithm.java 1268 2006-08-14
 *          13:25:12Z nono $
 */
public abstract class StronglyConnectedComponentsAlgorithm implements
		Serializable {

	protected DirectedGraph digraph;

	// //////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	// //////////////////////////////////////////////////////////////////

	/**
	 * constructs a SCC algorithm from the given directed graph
	 * 
	 * @param digraph
	 *            a DirectedGraph object
	 */
	public StronglyConnectedComponentsAlgorithm(DirectedGraph digraph) {
		this.digraph = digraph;
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
	public abstract Set SCC();

	/**
	 * Construct a new graph from given <code>graph</code> object with nodes
	 * representing strongly connected components keeping edges that comes out
	 * or go in node.
	 * <p>
	 * This method only works on directed graphs
	 * 
	 * @param graph
	 *            a graph to tranform
	 * @return a new graph representing scc from given parameter
	 */
	public DirectedGraph makeCollapsedSCC() throws Exception {
		Map vert2graph = new HashMap();
		/* map from graph vertices to new vertex */
		DirectedGraph collapsed = new DirectedGraphImpl();
		Iterator it = SCC().iterator();
		int i = 0;
		while (it.hasNext()) {
			DirectedGraph digraph = (DirectedGraph) it.next();
			if (digraph == null)
				continue;
			/* create new vertex */
			Object nv = null;
			if (digraph.getVerticesCount() == 1) {
				nv = digraph.getVerticesIterator().next();
			} else
				nv = "" + ++i;
			collapsed.add(nv);
			/* add all vertices from digraph to verts */
			Iterator vit = digraph.getVerticesIterator();
			while (vit.hasNext())
				vert2graph.put(vit.next(), nv);
		}
		/* add non-collapsed vertices */
		it = digraph.getVerticesIterator();
		while (it.hasNext()) {
			Object k = it.next();
			Object v = vert2graph.get(k);
			if (v == null) {
				v = k;
				collapsed.add(v);
				vert2graph.put(k, v);
			}
			Iterator it2 = digraph.getOutgoingEdges(k).iterator();
			while (it2.hasNext()) {
				DirectedEdge e = (DirectedEdge) it2.next();
				Object o = e.getOppositeVertex(k);
				Object no = vert2graph.get(o);
				if (no == null) {
					no = o;
					collapsed.add(no);
					vert2graph.put(o, no);
				}
				if (no != v)
					collapsed.addEdge(new DirectedEdgeImpl(v, no, e.getData()));
			}
		}
		/* return new graph */
		return collapsed;
	}

}
