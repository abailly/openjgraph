package salvo.jesus.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import salvo.jesus.graph.algorithm.DepthFirstGraphTraversal;
import salvo.jesus.graph.algorithm.GraphTraversal;

/**
 * An implementation of the Graph interface. A Graph object represents a graph
 * data structure, which are vertices connected by edges, where the edges are
 * non-directional.
 */
public class GraphImpl implements Graph {
	/**
	 * Reference to the instance of <tt>GraphFactory</tt> responsible for
	 * creating Vertices and Edges.
	 */
	protected GraphFactory factory;

	/**
	 * List of vertices in the graph.
	 */
	protected List vertices;

	/**
	 * List of edges in the graph. Each element in the List is a List in itself,
	 * such that each element is are the incident edges of a Vertex. The index
	 * of the Object in vertices is the same index as that incident edges of the
	 * Object in edges.
	 */
	protected List edges;

	/**
	 * List of all edges in the graph regardless of the vertices.
	 * 
	 */
	protected Set<Edge> allEdges;

	/**
	 * List of Lists (no, that was not a typo!) of vertices that are connected.
	 * The term connected here means that, if there is an Edge from Object A to
	 * B, then Object A is connected to Object B and vice-versa, regardless of
	 * the direction of the Edge connecting the two vertices.
	 * 
	 * Therefore, if not all vertices are connected to each other, then you have
	 * a forest of graphs. The simplest forest of graphs contains two vertices
	 * and no edges, in which case you have a one forest that has only one
	 * Vertex and another forest also with only one Vertex. That simplest forest
	 * of graphs will be represented here as one List containing two Lists, each
	 * of which has only one Vertex.
	 * 
	 */
	protected List connectedSet;

	/**
	 * List of GraphAddVertexListeners that are interested in listening when new
	 * vertices are added to the Graph.
	 * 
	 * @see salvo.jesus.graph.GraphAddVertexListener
	 */
	protected List addvertexlistener;

	/**
	 * List of GraphAddEdgeListeners that are interested in listening when new
	 * edges are added to the Graph.
	 * 
	 * @see salvo.jesus.graph.GraphAddEdgeListener
	 */
	protected List addedgelistener;

	/**
	 * List of GraphRemoveVertexListeners that are interested in listening when
	 * vertices are removed from the Graph.
	 * 
	 * @see salvo.jesus.graph.GraphRemoveVertexListener
	 */
	protected List removevertexlistener;

	/**
	 * List of GraphRemoveEdgeListeners that are interested in listening when
	 * edges are removed from the Graph.
	 * 
	 * @see salvo.jesus.graph.GraphRemoveEdgeListener
	 */
	protected List removeedgelistener;

	/**
	 * Delegate object for implementing graph traversal. The default
	 * implementation is DepthFirstGraphTraversal.
	 */
	protected GraphTraversal traversal;

	public GraphImpl() {
		// System.out.println("[GraphImpl] GraphImpl");
		initData();
	}

	protected void initData() {
		vertices = new ArrayList(10);
		edges = new ArrayList(10);
		connectedSet = new ArrayList(10);
		allEdges = new HashSet<Edge>();

		addvertexlistener = new ArrayList(10);
		addedgelistener = new ArrayList(10);

		removevertexlistener = new ArrayList(10);
		removeedgelistener = new ArrayList(10);

		this.factory = new GraphImplFactory();

		traversal = new DepthFirstGraphTraversal(this);
	}

	/**
	 * Returns the factory that will be responsible for creating Vertices and
	 * Edges in a Graph.
	 */
	public GraphFactory getGraphFactory() {
		// System.out.println("[GraphImpl] getGraphFactory ");
		return this.factory;
	}

	/**
	 * Sets the factory that will be responsible for creating Vertices and Edges
	 * in a Graph.
	 */
	public void setGraphFactory(GraphFactory factory) {
		// System.out.println("[GraphImpl] setGraphFactory");
		this.factory = factory;
	}

	/**
	 * Returns an iterator that iterates through the graph's vertices.
	 * 
	 * @return An itereator of List vertices.
	 */
	public Iterator getVerticesIterator() {
		// System.out.println("[GraphImpl] getVerticesIterator");
		return this.vertices.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see salvo.jesus.graph.Graph#getAllVertices()
	 */
	public List getAllVertices() {
		return new ArrayList(vertices);
	}

	/**
	 * Returns a List of edges of the specified vertex.
	 * 
	 * @param v
	 *            The vertex whose edges we want returned
	 * @return A List of Edges that are incident edges of the specified vertex.
	 */
	public List getEdges(Object v) {
		// System.out.println("[GraphImpl] getEdges");
		List incidentedges = null;
		int index = this.vertices.indexOf(v);

		if (index >= 0) {
			incidentedges = (List) this.edges.get(index);
		}
		return incidentedges;
	}

	/**
	 * Adds a Object into the Graph. This will also create a new entry in the
	 * edges List and add the newly added Object to its own connected set,
	 * thereby adding a new List in the connectedSet List. Finally, all
	 * GraphAddVertexListeners are informed of the event that a Object has been
	 * added to the Graph.
	 * 
	 * @param newvertex
	 *            Object to be added to the Graph
	 */
	public void add(Object newvertex) throws GraphException {
		// System.out.println("[GraphImpl] add ");
		ArrayList newconnectedSet;
		Iterator iterator;
		GraphAddVertexListener listener;

		if (vertices.contains(newvertex))
			return;
		// Add the vertex
		vertices.add(newvertex);

		// Whenever a new vertex is added, we also need to create
		// a blank adjacenct edge list for the new vertex
		edges.add(new ArrayList(10));

		// By default, add the new vertex to its own connected set
		newconnectedSet = new ArrayList(10);
		newconnectedSet.add(newvertex);
		connectedSet.add(newconnectedSet);

		// Notify all addvertexlisteners that a vertex was added
		iterator = addvertexlistener.iterator();
		while (iterator.hasNext()) {
			listener = (GraphAddVertexListener) iterator.next();
			listener.vertexAdded(new GraphAddVertexEvent(this, newvertex));
		}
	}

	/**
	 * Method to create the proper type of Edge class. This is now merely a
	 * shortcut to <tt>factory.crateEdge()</tt>.
	 * 
	 * @param v1
	 *            One endpoint of the edge
	 * @param v2
	 *            Other endpoint of the edge
	 * @deprecated
	 */
	public Edge createEdge(Object v1, Object v2) {
		// System.out.println("[GraphImpl] createEdge");
		return this.factory.createEdge(v1, v2);
	}

	/**
	 * Adds an Edge into the Graph. The vertices of the Edge need not already be
	 * existing in the Graph for this method to work properly. The vertices in
	 * both ends of the Edge are merged into one connected set, thereby possibly
	 * decreasing the number of Lists in the coonectedSet List. Finally, all
	 * GraphAddEdgeListeners are informed of the event that a Edge has been
	 * added to the Graph.
	 * 
	 * @param v1
	 *            One endpoint of the edge
	 * @param v2
	 *            Other endpoint of the edge
	 * @return The Edge object created and added to the Graph.
	 */
	public Edge addEdge(Object v1, Object v2) throws GraphException {
		// System.out.println("[GraphImpl] addEdge(Object v1, Object v2)");
		Edge edge;
		List v1edges, v2edges;
		Iterator iterator;
		GraphAddEdgeListener listener;
		if ((v1 == null) || (v2 == null))
			throw new GraphModificationException(
					"Cannot add null vertices to graph");
		// If the vertices of the edge are not in the Graph, add them.
		if (!this.vertices.contains(v1))
			this.add(v1);
		if (!this.vertices.contains(v2))
			this.add(v2);

		edge = this.factory.createEdge(v1, v2);

		v1edges = this.getEdges(v1);
		v2edges = this.getEdges(v2);

		// Add the edge as an incident edge of both vertices
		v1edges.add(edge);
		v2edges.add(edge);
		allEdges.add(edge);

		// Merge the vertices connected sets
		mergeconnectedSet(v1, v2);

		// Notify all addedgelisteners that a new edge was added
		iterator = this.addedgelistener.iterator();
		while (iterator.hasNext()) {
			listener = (GraphAddEdgeListener) iterator.next();
			listener.edgeAdded(new GraphAddEdgeEvent(this, edge));
		}

		return edge;
	}

	/**
	 * Adds an Edge into the Graph. The vertices of the Edge need not be
	 * existing in the Graph for this method to work properly. The vertices in
	 * both ends of the Edge are merged into one connected set, thereby possibly
	 * decreasing the number of Lists in the coonectedSet List. Finally, all
	 * GraphAddEdgeListeners are informed of the event that a Edge has been
	 * added to the Graph.
	 * <p>
	 * In the event that any one of the vertices are not existing in the Graph,
	 * they are added to the Graph.
	 * <p>
	 * <b>Note:</b> It is the caller's responsibility to make sure that the
	 * type of Edge being added is an EdgeImpl.
	 * 
	 * @param e
	 *            The edge to be added to the Graph.
	 */
	public void addEdge(Edge edge) throws GraphException {
		Object v1, v2;
		Iterator iterator;
		GraphAddEdgeListener listener;
		List v1edges, v2edges;

		v1 = edge.getVertexA();
		v2 = edge.getVertexB();

		// If the vertices of the edge are not in the Graph, add them.
		if (!this.vertices.contains(v1))
			this.add(v1);
		if (!this.vertices.contains(v2))
			this.add(v2);

		// Only then should we call getEdges, since add( Object ) will
		// initialise these edgelist of a vertex.
		v1edges = this.getEdges(v1);
		v2edges = this.getEdges(v2);

		// Add the edge as an incident edge of both vertices
		v1edges.add(edge);
		v2edges.add(edge);
		allEdges.add(edge);

		// Merge the vertices connected sets
		mergeconnectedSet(v1, v2);

		// Notify all addedgelisteners that a new edge was added
		iterator = this.addedgelistener.iterator();
		while (iterator.hasNext()) {
			listener = (GraphAddEdgeListener) iterator.next();
			listener.edgeAdded(new GraphAddEdgeEvent(this, edge));
		}
	}

	public void remove(Object v) throws GraphException {
		// System.out.println("[GraphImpl] remove");
		Iterator iterator;
		Edge edgetoremove;
		Edge adjacentedge;
		List connectedsetofvertex;

		GraphRemoveVertexListener listener;

		// Remove all the edges of the vertex.
		this.removeEdges(v);

		// Remove the vertex from its connected set
		connectedsetofvertex = this.getConnectedSet(v);
		connectedsetofvertex.remove(v);
		// If the connected set is now empty, remove the connectedset
		if (connectedsetofvertex.size() == 0)
			this.connectedSet.remove(connectedsetofvertex);

		// Remove the adjacent edges entry of the vertex
		this.edges.remove(this.vertices.indexOf(v));

		// Notify all interested listeners that we are about to remove a vertex
		iterator = this.removeedgelistener.iterator();
		while (iterator.hasNext()) {
			listener = (GraphRemoveVertexListener) iterator.next();
			listener.vertexRemoved(new GraphRemoveVertexEvent(this, v));
		}

		// Finally, remove the vertex
		this.vertices.remove(v);
	}

	/**
	 * Removes the specified Edge from the Graph.
	 * 
	 * @param edge
	 *            The Edge object to be removed.
	 */
	public void removeEdge(Edge edge) throws GraphException {
		// System.out.println("[GraphImpl] removeEdge");
		Iterator iterator;
		GraphRemoveEdgeListener listener;

		Object v1, v2;
		List v1edges, v2edges;

		// Notify all removeedgelisteners that an edge is to be removed.
		iterator = removeedgelistener.iterator();
		while (iterator.hasNext()) {
			listener = (GraphRemoveEdgeListener) iterator.next();
			listener.edgeRemoved(new GraphRemoveEdgeEvent(this, edge));
		}

		// Remove the edge from the vertices incident edges.
		v1 = edge.getVertexA();
		v1edges = this.getEdges(v1);
		v1edges.remove(edge);

		v2 = edge.getVertexB();
		v2edges = this.getEdges(v2);
		v2edges.remove(edge);

		allEdges.remove(edge);

		// Determine if we need to unmerge a connected set.
		// If there is no path from fromvertex to tovertex in either
		// direction...
		if (!this.isConnected(v1, v2)) {
			// ... unmerge the connected sets. Do this by creating a new
			// connected set starting with the tovertex ....
			List newconnectedset = new ArrayList(10);
			List v1connectedset;

			newconnectedset = this.traverse(v2);

			// ... and removing elements from the existing set that are in the
			// new set.
			v1connectedset = this.getConnectedSet(v1);
			v1connectedset.removeAll(newconnectedset);

			// .. and finally adding the new set to the set of connected sets.
			connectedSet.add(newconnectedset);
		}
	}

	/**
	 * Removes incident Edges of a Vertex. The Edges removed are those whose
	 * either endpoints has the specified vertex. This method is usually called
	 * just prior to removing a Object from a Graph.
	 * 
	 * @param v
	 *            Object whose Edges are to be removed
	 */
	public void removeEdges(Object v) throws GraphException {
		// System.out.println("[GraphImpl] removeEdges");
		List vedges;
		Iterator iterator;
		Edge edgetoremove;

		// Remove incident edges of vertex
		vedges = this.getEdges(v);
		iterator = vedges.iterator();
		while (iterator.hasNext()) {
			edgetoremove = (Edge) iterator.next();
			this.removeEdge(edgetoremove);
			// Reinitialize the iterator as the removeEdge() method would have
			// modified the List.
			iterator = vedges.iterator();
		}
	}

	/**
	 * Returns the number of vertices in the graph
	 * 
	 * @return The number of vertices in the graph.
	 */
	public int getVerticesCount() {
		// System.out.println("[GraphImpl] getVerticesCount");
		return this.vertices.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see salvo.jesus.graph.Graph#getEdgesCount()
	 */
	public int getEdgesCount() {
		return allEdges.size();
	}

	/**
	 * Returns all vertices with the specified degree.
	 * 
	 * @param degree
	 *            The degree of the vertex to be returned.
	 * @return A collection of vertices with the above specified degree.
	 */
	public Set getVertices(int degree) {
		// System.out.println("[GraphImpl] getVertices");
		Set verticesofsamedegree = new HashSet();
		Iterator iterator;
		Object vertex;

		iterator = this.vertices.iterator();
		while (iterator.hasNext()) {
			vertex = iterator.next();
			if (this.getAdjacentVertices(vertex).size() == degree)
				verticesofsamedegree.add(vertex);
		}

		return verticesofsamedegree;
	}

	/**
	 * Returns the vertices adjacent to the specified vertex.
	 * 
	 * @param v
	 *            The Object you want to determine its adjacent vertices.
	 * @return List of vertices adjacent to the specified vertex v.
	 */
	public List getAdjacentVertices(Object v) {
		// System.out.println("[GraphImpl] getAdjacentVertices ");
		List adjacentVertices = new ArrayList(10);
		List incidentEdges = this.getEdges(v);
		Iterator iterator;
		Edge edge;
		Object oppositeObject;

		if (incidentEdges != null) {
			iterator = incidentEdges.iterator();
			while (iterator.hasNext()) {
				edge = (Edge) iterator.next();
				oppositeObject = edge.getOppositeVertex(v);
				if (oppositeObject != null)
					adjacentVertices.add(oppositeObject);
			}
		}

		return adjacentVertices;
	}

	/**
	 * Returns the vertices adjacent to all the vertices in the given
	 * collection.
	 * 
	 * @param vertices
	 *            List of Object where each vertex in the returned Set must be
	 *            adjacent to.
	 * @return Set of vertices adjacent to all the vertices in the supplied
	 *         List.
	 */
	public HashSet getAdjacentVertices(List vertices) {
		// System.out.println("[GraphImpl] getAdjacentVertices");
		HashSet adjacentVertices = new HashSet(this
				.getAdjacentVertices(vertices.get(0)));
		int i, size = vertices.size();

		for (i = 1; i < size; i++) {
			adjacentVertices.retainAll(this
					.getAdjacentVertices(vertices.get(i)));
		}

		return adjacentVertices;
	}

	/**
	 * Returns the connected sets in the Graph. Each List in the return List is
	 * a List of vertices that are connected to each other, regardless of the
	 * direction of the Edge conneting them together.
	 * 
	 * @return List of List of connected vertices.
	 */
	public List getConnectedSet() {
		// System.out.println("[GraphImpl] getConnectedSet");
		return connectedSet;
	}

	/**
	 * Returns the connected set to which the specified vertex belongs.
	 * 
	 * @param v
	 *            Object to which you want the connected set returned.
	 * @return List of connected vertices where the specified vertex belongs.
	 */
	public List getConnectedSet(Object v) {
		// System.out.println("[GraphImpl] getConnectedSet");
		Iterator iterator;
		List currentconnectedSet;

		iterator = connectedSet.iterator();
		while (iterator.hasNext()) {
			currentconnectedSet = (List) iterator.next();
			if (currentconnectedSet.contains(v))
				return currentconnectedSet;
		}

		return null;
	}

	/**
	 * Merges the connected sets to which Object v1 and Object v2 belongs, if
	 * they are not yet connected. This ma result in decreasing the number of
	 * Lists in the connectedSet List.
	 * 
	 * @param v1
	 *            Object whose connected set you want merged with the connected
	 *            set of Object v2.
	 * @param v2
	 *            Object whose connected set you want merged with the connected
	 *            set of Object v1.
	 */
	public void mergeconnectedSet(Object v1, Object v2) {
		// System.out.println("[GraphImpl] mergeconnectedSet");
		Iterator iterator;
		List connectedSetv1;
		List connectedSetv2;

		// First, find to which connected component set vertices v1 and v2
		// belong
		connectedSetv1 = this.getConnectedSet(v1);
		connectedSetv2 = this.getConnectedSet(v2);

		// Then merge one to the other. Merge the smaller set to the bigger set
		// Then reduce the size of the connectedSet vector by 1.
		if (connectedSetv1 == connectedSetv2)
			return;
		else if (connectedSetv1.size() < connectedSetv2.size()) {
			connectedSetv2.addAll(connectedSetv1);
			connectedSet.remove(connectedSet.indexOf(connectedSetv1));
		} else {
			connectedSetv1.addAll(connectedSetv2);
			connectedSet.remove(connectedSet.indexOf(connectedSetv2));
		}
	}

	/**
	 * Traverses the Graph starting at startat Vertex. Only the connected
	 * components to which startat belongs to will be traversed.
	 * 
	 */
	public List traverse(Object startat) {
		// System.out.println("[GraphImpl] traverse");
		return traversal.traverse(startat);
	}

	/**
	 * Gets the traversal algorithm used by the Graph.
	 * 
	 * @return GraphTraversal object performing traversal for the Graph.
	 */
	public GraphTraversal getTraversal() {
		// System.out.println("[GraphImpl] getTraversal");
		return this.traversal;
	}

	/**
	 * Sets the graph traversal algorithm to be used
	 * 
	 * @param traversal
	 *            A concrete implementation of the GraphTraversal object.
	 */
	public void setTraversal(GraphTraversal traversal) {
		// System.out.println("[GraphImpl] setTraversal");
		this.traversal = traversal;
	}

	/**
	 * Determines if two vertices are connected
	 * 
	 * @param v1
	 *            starting Object for the path
	 * @param v2
	 *            ending Object for the path
	 * @return true if v1 and v2 are connected.
	 */
	public boolean isConnected(Object v1, Object v2) {
		// System.out.println("[GraphImpl] isConnected");
		List connectedsetv1 = this.getConnectedSet(v1);

		if (connectedsetv1 != null && connectedsetv1.contains(v2))
			return true;
		else
			return false;
	}

	/**
	 * Returns the degree of the graph, which is simply the highest degree of
	 * all the graph's vertices.
	 * 
	 * @return An int indicating the degree of the graph.
	 */
	public int getDegree() {
		// System.out.println("[GraphImpl] getDegree");
		Object v;
		HashSet set;

		set = new HashSet(this.vertices);
		if (set.size() > 0) {
			v = Collections.max(set, new Comparator() {
				public int compare(Object obj1, Object obj2) {
					Object v1 = obj1, v2 = obj2;
					int countv1 = getDegree(v1);
					int countv2 = getDegree(v2);

					if (countv1 < countv2)
						return -1;
					if (countv1 > countv2)
						return 1;
					else
						return 0;
				}

				public boolean equals(Object objcomparator) {
					return objcomparator.equals(this);
				}
			});
			return this.getEdges(v).size();
		} else
			return 0;
	}

	/**
	 * Returns the degree of the vertex, which is simply the number of edges of
	 * the vertex.
	 * 
	 * @return The degree of the vertex.
	 */
	public int getDegree(Object v) {
		// System.out.println("[GraphImpl] getDegree ");
		return this.getEdges(v).size();
	}

	/**
	 * Adds a GraphAddVertexListener to the Graph's internal List of
	 * GraphAddVertexListeners so that when a new Object is added, all
	 * registered GraphAddVertedListeners are notified of the event.
	 * 
	 * @param listener
	 *            GraphAddVertexListener you want registered or be notified when
	 *            a new Object is added
	 * @see salvo.jesus.graph.GraphAddVertexListener
	 * @see #removeGraphAddVertexListener( GraphAddVertexListener )
	 */
	public void addGraphAddVertexListener(GraphAddVertexListener listener) {
		// System.out.println("[GraphImpl] addGraphAddVertexListener");
		addvertexlistener.add(listener);
	}

	/**
	 * Adds a GraphAddEdgeListener to the Graph's internal List of
	 * GraphAddEdgeListeners so that when a new Edge is added, all registered
	 * GraphAddEdgeListeners are notified of the event.
	 * 
	 * @param listener
	 *            GraphAddEdgeListener you want registered or be notified when a
	 *            new Edge is added
	 * @see salvo.jesus.graph.GraphAddEdgeListener
	 * @see #removeGraphAddEdgeListener( GraphAddEdgeListener )
	 */
	public void addGraphAddEdgeListener(GraphAddEdgeListener listener) {
		// System.out.println("[GraphImpl] addGraphAddEdgeListener");
		addedgelistener.add(listener);
	}

	/**
	 * Adds a GraphRemoveEdgeListener to the Graph's internal List of
	 * GraphRemoveEdgeListeners so that when an Edge is removed, all registered
	 * GraphRemoveEdgeListeners are notified of the event.
	 * 
	 * @param listener
	 *            GraphRemoveEdgeListener you want registered or be notified
	 *            when an Edge is removed
	 * @see salvo.jesus.graph.GraphRemoveEdgeListener
	 * @see #removeGraphRemoveEdgeListener( GraphRemoveEdgeListener )
	 */
	public void addGraphRemoveEdgeListener(GraphRemoveEdgeListener listener) {
		// System.out.println("[GraphImpl] addGraphRemoveEdgeListener");
		removeedgelistener.add(listener);
	}

	/**
	 * Adds a GraphRemoveVertexListener to the Graph's internal List of
	 * GraphRemoveVertexListeners so that when a Object is removed, all
	 * registered GraphRemoveVertexListeners are notified of the event.
	 * 
	 * @param listener
	 *            GraphRemoveVertexListener you want registered or be notified
	 *            when a Object is removed
	 * @see salvo.jesus.graph.GraphRemoveVertexListener
	 * @see #removeGraphRemoveVertexListener( GraphRemoveVertexListener )
	 */
	public void addGraphRemoveVertexListener(GraphRemoveVertexListener listener) {
		// System.out.println("[GraphImpl] addGraphRemoveVertexListener ");
		removevertexlistener.add(listener);
	}

	/**
	 * Removes a GraphAddVertexListener from the Graph's internal List of
	 * GraphAddVertexListeners.
	 * 
	 * @param listener
	 *            GraphAddVertexListener you no longer want registered or be
	 *            notified when a Object is added
	 * @see salvo.jesus.graph.GraphAddVertexListener
	 * @see #addGraphAddVertexListener( GraphAddVertexListener )
	 */
	public void removeGraphAddVertexListener(GraphAddVertexListener listener) {
		// System.out.println("[GraphImpl] removeGraphAddVertexListener");
		addvertexlistener.remove(listener);
	}

	/**
	 * Removes a GraphAddEdgeListener from the Graph's internal List of
	 * GraphAddEdgeListeners.
	 * 
	 * @param listener
	 *            GraphAddEdgeListener you no longer want registered or be
	 *            notified when an Edge is added
	 * @see salvo.jesus.graph.GraphAddEdgeListener
	 * @see #addGraphAddEdgeListener( GraphAddEdgeListener )
	 */
	public void removeGraphAddEdgeListener(GraphAddEdgeListener listener) {
		// System.out.println("[GraphImpl] removeGraphAddEdgeListener");
		addedgelistener.remove(listener);
	}

	/**
	 * Removes a GraphRemoveEdgeListener from the Graph's internal List of
	 * GraphRemoveEdgeListeners.
	 * 
	 * @param listener
	 *            GraphRemoveEdgeListener you no longer want registered or be
	 *            notified when an Edge is removed
	 * @see salvo.jesus.graph.GraphRemoveEdgeListener
	 * @see #addGraphRemoveEdgeListener( GraphRemoveEdgeListener )
	 */
	public void removeGraphRemoveEdgeListener(GraphRemoveEdgeListener listener) {
		// System.out.println("[GraphImpl] removeGraphRemoveEdgeListener");
		removeedgelistener.remove(listener);
	}

	/**
	 * Removes a GraphRemoveVertexListener from the Graph's internal List of
	 * GraphRemoveVertexListeners.
	 * 
	 * @param listener
	 *            GraphRemoveVertexListener you no longer want registered or be
	 *            notified when a Object is removed
	 * @see salvo.jesus.graph.GraphRemoveVertexListener
	 * @see #addGraphRemoveVertexListener( GraphRemoveVertexListener )
	 */
	public void removeGraphRemoveVertexListener(
			GraphRemoveVertexListener listener) {
		// System.out.println("[GraphImpl] removeGraphRemoveVertexListener");
		removevertexlistener.remove(listener);
	}

	/**
	 * Returns a String representation of the Graph. The string returned in the
	 * form: "Vertices: " + this.vertices.toString() + "\n " + "Edges: " +
	 * this.edges.toString()
	 * 
	 * @return String representation of the Graph
	 */
	public String toString() {
		// System.out.println("[GraphImpl] toString");
		return "Vertices= " + this.vertices.toString() + ", Edges= "
				+ this.getAllEdges().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see salvo.jesus.graph.Graph#same()
	 */
	public Graph same() {
		// System.out.println("[GraphImpl] same");
		Graph g = new GraphImpl();
		g.setGraphFactory(this.getGraphFactory());
		return g;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see salvo.jesus.graph.Graph#findVertex(java.lang.Object)
	 */
	public Object findVertex(Object o) {
		// System.out.println("[GraphImpl] findVertex");
		Iterator it = vertices.iterator();
		while (it.hasNext()) {
			Object v = it.next();
			Object data = v;
			if ((data == null)) {
				if (o == null)
					return v;
			} else {
				if (data.equals(o))
					return v;
			}
		}
		return null;
	}

	public Collection<Edge> getAllEdges() {
		return allEdges;
	}

	@Override
	public Object clone() {
		try {
			GraphImpl gi = (GraphImpl) super.clone();
			gi.initData();
			gi.setGraphFactory(getGraphFactory());
			for (Object o : getAllVertices()) {
				gi.add(o);
			}
			for (Edge e : getAllEdges())
				gi.addEdge(factory.createEdgeWith(e.getVertexA(), e
						.getVertexB(), e.getData()));
			return gi;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} catch (GraphException e) {
			e.printStackTrace();
		}
		assert false;
		return null;
	}
}
