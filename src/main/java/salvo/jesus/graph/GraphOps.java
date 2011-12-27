/*
 * Created on May 5, 2004
 */
package salvo.jesus.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import salvo.jesus.graph.algorithm.GraphFilter;
import salvo.jesus.graph.algorithm.GraphMorphism;
import salvo.jesus.graph.algorithm.GraphVertexFold;

/**
 * A utility class implementing several generic operations on graphs.
 * 
 * @author nono
 * @version $Id: GraphOps.java 1403 2007-05-23 08:58:35Z /CN=nono $
 */
public class GraphOps {

	/*
	 * ensure no construction
	 * 
	 */
	private GraphOps() {
	};

	/**
	 * Update the given graph according to given filter.
	 * <p>
	 * The graph is modified to comply with filter : filtered out vertices are
	 * removed as well as edges between one or two filtered out vertices
	 * <p>
	 * BROKEN
	 * 
	 * @deprecated
	 */
	public static void filterUpdate(DirectedGraph graph, GraphFilter filter)
			throws Exception {
		Set delVertices = new HashSet();
		Set delEdges = new HashSet();
		/* filter vertices */
		Iterator it = graph.getVerticesIterator();
		while (it.hasNext()) {
			boolean remove = false;
			Object v = it.next();
			boolean f = filter.filter(v);
			/* dont care if filtered out */
			if (!f)
				remove = true;
			/* filter edges on this vertex */
			Iterator it2 = graph.getOutgoingEdges(v).iterator();
			while (it2.hasNext()) {
				Edge e = (Edge) it2.next();
				Object op = e.getOppositeVertex(v);
				/* do not add edges between filtered out vertices */
				if (remove || !filter.filter(op) || !filter.filter(e))
					delEdges.add(e);
			}
			if (remove)
				delVertices.add(v);
		}
		/* effectively remove edges and vertices */
		it = delEdges.iterator();
		while (it.hasNext())
			graph.removeEdge((Edge) it.next());
		it = delVertices.iterator();
		while (it.hasNext())
			graph.remove(it.next());

	}

	/**
	 * Construct a new Graph object from a given Graph and a filter. The
	 * resultant graph has edges and vertices from original graph which match
	 * filter in effect being a subgraph of <code>graph</code>
	 * 
	 * @param graph
	 *            graph object to filter. May not be null
	 * @param filter
	 * @return a subgraph of graph
	 */
	public static DirectedGraph filter(DirectedGraph graph, GraphFilter filter)
			throws GraphException {
		Map vmap = new HashMap(); /* store vertex */
		Map emap = new HashMap(); /* store edges to edges */
		DirectedGraph niou = (DirectedGraph) graph.same();
		GraphFactory fact = niou.getGraphFactory();
		/* filter vertices */
		Iterator it = graph.getVerticesIterator();
		while (it.hasNext()) {
			Object v = it.next();
			boolean f = filter.filter(v);
			/* dont care if filtered out */
			if (!f)
				continue;
			/* vertex may be already visited */
			Object nv = vmap.get(v);
			if (nv == null) {
				nv = v;
				vmap.put(v, nv);
				niou.add(nv);
			}
			/* filter edges on this vertex */
			Iterator it2 = graph.getOutgoingEdges(v).iterator();
			while (it2.hasNext()) {
				Edge e = (Edge) it2.next();
				Object op = e.getOppositeVertex(v);
				assert op != null : "(" + v + " - " + e + " - " + op + ")";
				/* do not add edges between filtered out vertices */
				if (!filter.filter(op))
					continue;
				/* check edge is not filtered out */
				if (!filter.filter(e))
					continue;
				Object nop = vmap.get(op);
				/* check edge not already added */
				if ((nop != null)) {
					if (emap.get(e) != null)
						continue;
				} else {
					nop = op;
					vmap.put(op, nop);
					niou.add(nop);
				}
				/* create and add new edge */
				Edge ne = fact.createEdgeWith(nv, nop, e.getData());
				emap.put(e, ne);
				niou.addEdge(ne);
			}
		}
		return niou;
	}

	/**
	 * Fold given graph according to given (vertex) fold. Folding of a graph is
	 * different from filtering (which really is subgraphing): It retains
	 * transitive links between non filtered vertices and edges. To create link
	 * between two unfiltered nodes, this method concatenates unfiltered edges
	 * into a list.
	 * <p>
	 * Folding comes in two flavors: vertex (this one) and edge (see
	 * {@link #fold(DirectedGraph, GraphEdgeFold)} folding.
	 * </p>
	 * 
	 * @param graph
	 *            the graph to fold.
	 * @param fold
	 *            the vertex fold to apply
	 * @return a new DirectedGraph representing folding of given graph.
	 * @throws GraphException
	 */
	public static DirectedGraph fold(DirectedGraphImpl graph,
			GraphVertexFold fold) throws GraphException {
		DirectedGraph niou = (DirectedGraph) graph.clone();
		// make a list of nodes to fold
		List<Object> remove = new ArrayList<Object>();
		for (Object o : niou.getAllVertices())
			if (fold.fold(o))
				remove.add(o);
		while (!remove.isEmpty()) {
			Object o = remove.remove(0);
			// get edges - we need a copy as the returned lists 
			// may be modified by removal operations
			List<DirectedEdge> in = new ArrayList<DirectedEdge>(niou
					.getIncomingEdges(o));
			List<DirectedEdge> out = new ArrayList<DirectedEdge>(niou
					.getOutgoingEdges(o));
			// remove those edges
			for (DirectedEdge de : in)
				niou.removeEdge(de);
			for (DirectedEdge de : out)
				niou.removeEdge(de);
			// add cartesian product of edges
			for (DirectedEdge din : in) {
				for (DirectedEdge dout : out) {
					niou.addEdge(niou.getGraphFactory().createEdgeWith(
							din.getSource(), dout.getSink(),
							fold.merge(din.getData(), dout.getData())));
				}
			}
			niou.remove(o);
		}
		return niou;
	}

	/**
	 * This method computes the image of a directed graph by a graph
	 * homomorphism.
	 * <p>
	 * 
	 * @param graph
	 *            the (directed)graph to compute image
	 * @param morphism
	 *            the morphism applied
	 * @return a new (directed) graph object resulting from successive calls to
	 *         morphism image methods.
	 */
	public static DirectedGraph morph(DirectedGraph graph,
			GraphMorphism morphism) throws Exception {
		DirectedGraph niou = (DirectedGraph) graph.same();
		morphism.target(graph);
		/* filter vertices */
		Iterator it = graph.getVerticesIterator();
		while (it.hasNext()) {
			Object v = it.next();
			Object nv = morphism.image(v);
			if (nv == null)
				continue;
			niou.add(nv);
			/* filter edges on this vertex */
			Iterator it2 = graph.getOutgoingEdges(v).iterator();
			while (it2.hasNext()) {
				Edge e = (Edge) it2.next();
				Object op = e.getOppositeVertex(v);
				Object nop = morphism.image(op);
				/* skip when opposite vertex is null */
				if (nop == null)
					continue;
				/* create and add new edge */
				Edge ne = morphism.imageOf(e);
				/*
				 * check homomorphism property
				 */
				if ((!ne.getVertexA().equals(nv) || (!ne.getVertexB().equals(
						nop))))
					throw new Exception(
							"Homomorphims property violated by image of " + e
									+ " between " + nv + " and " + nop);
				niou.addEdge(ne);
			}
		}
		return niou;

	}

	public static Graph createFrom(double matrix[][]) throws Exception {
		switch (properties(matrix)) {
		case 1: // '\001'
			return makeDirectedGraph(matrix);

		case 3: // '\003'
			return makeDirectedWeightedGraph(matrix);

		case 2: // '\002'
			return makeWeightedGraph(matrix);
		}
		return makeGraph(matrix);
	}

	private static Graph makeGraph(double matrix[][]) throws Exception {
		Graph ret = new GraphImpl();
		Object vs[] = new Object[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			Object from = vs[i];
			if (from == null) {
				from = Integer.toString(i);
				vs[i] = from;
				ret.add(from);
			}
			for (int j = i; j < matrix.length; j++) {
				if (matrix[i][j] != 1.0D)
					continue;
				Object to = vs[j];
				if (to == null) {
					to = Integer.toString(j);
					vs[j] = to;
					ret.add(to);
				}
				ret.addEdge(to, from);
			}

		}

		return ret;
	}

	private static Graph makeWeightedGraph(double matrix[][]) throws Exception {
		WeightedGraph ret = new WeightedGraphImpl();
		Object vs[] = new Object[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			Object from = vs[i];
			if (from == null) {
				from = Integer.toString(i);
				vs[i] = from;
				ret.add(from);
			}
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] == 0.0D
						|| matrix[i][j] == 1.7976931348623157E+308D)
					continue;
				Object to = vs[j];
				if (to == null) {
					to = Integer.toString(j);
					vs[j] = to;
					ret.add(to);
				}
				ret.addEdge(from, to, matrix[i][j]);
			}

		}

		return ret;
	}

	private static Graph makeDirectedWeightedGraph(double matrix[][])
			throws Exception {
		DirectedGraph ret = new DirectedGraphImpl();
		Object vs[] = new Object[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			Object from = vs[i];
			if (from == null) {
				from = new Integer(i);
				vs[i] = from;
				ret.add(from);
			}
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] == 0.0D
						|| matrix[i][j] == 1.7976931348623157E+308D)
					continue;
				Object to = vs[j];
				if (to == null) {
					to = new Integer(j);
					vs[j] = to;
					ret.add(to);
				}
				ret
						.addEdge(new DirectedWeightedEdgeImpl(from, to,
								matrix[i][j]));
			}

		}

		return ret;
	}

	private static Graph makeDirectedGraph(double matrix[][]) throws Exception {
		DirectedGraph ret = new DirectedGraphImpl();
		Object vs[] = new Object[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			Object from = vs[i];
			if (from == null) {
				from = new Integer(i);
				vs[i] = from;
				ret.add(from);
			}
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] != 1.0D)
					continue;
				Object to = vs[j];
				if (to == null) {
					to = new Integer(j);
					vs[j] = to;
					ret.add(to);
				}
				ret.addEdge(from, to);
			}

		}

		return ret;
	}

	private static int properties(double matrix[][]) {
		int ret = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = i; j < matrix.length; j++) {
				if (matrix[i][j] != matrix[j][i])
					ret = 1;
				if (matrix[i][j] == 1.7976931348623157E+308D
						|| matrix[i][j] != 0.0D && matrix[i][j] != 1.0D)
					ret |= 2;
				if (ret == 3)
					return ret;
			}

		}

		return ret;
	}

	public static final int DIRECTED = 1;

	public static final int WEIGHTED = 2;

	public static final int DIRECTED_WEIGHTED = 3;

}
