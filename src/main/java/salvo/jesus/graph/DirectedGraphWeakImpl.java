package salvo.jesus.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import salvo.jesus.graph.algorithm.GraphTraversal;

/**
 * A weak implementation of the DirectedGraph interface.
 * 
 * @author Jesus M. Salvo Jr.
 */

class DirectedGraphWeakImpl implements DirectedGraph {
	/**
	 * The GraphImpl object which has delegated the DirectedGraph interface
	 * implementation to this object - DirectedGraphWeakImpl.
	 */
	GraphImpl graph;

	/**
	 * List of outgoing edges in the graph. The index of a Object in the
	 * vertices List matches the Vertex's outgoing edges index in the List
	 * outgoingEdges.
	 * 
	 */
	List<List<DirectedEdge>> outgoingEdges;

	/**
	 * List of incoming edges in the graph. The index of a Object in the
	 * vertices List matches the Vertex's incoming edges index in the List
	 * incomingEdges.
	 * 
	 */
	List<List<DirectedEdge>> incomingEdges;

	/**
	 * Creates a new instance of DirectedGraphWeakImpl.
	 */
	public DirectedGraphWeakImpl(GraphImpl graph) {
		outgoingEdges = new ArrayList<List<DirectedEdge>>(10);
		incomingEdges = new ArrayList<List<DirectedEdge>>(10);
		this.graph = graph;
	}

	/**
	 * Returns the outgoing EdgeSets of the Graph. Each element in the return
	 * List is of type EdgeSet. The index of a Object in the vertices List
	 * matches the Vertex's EdgeSet index in the outgoingEdges List.
	 * 
	 * @return List containing the outgoing EdgeSets in the Graph. This simply
	 *         returns this.adjacentEdges.
	 */
	protected List<List<DirectedEdge>> getOutgoingEdges() {
		return outgoingEdges;
	}

	/**
	 * Returns the incoming EdgeSets of the Graph. Each element in the return
	 * List is of type EdgeSet. The index of a Object in the vertices List
	 * matches the Vertex's EdgeSet index in the incomingEdges List.
	 * 
	 * @return List containing the incoming EdgeSets in the Graph. This simply
	 *         returns this.adjacentEdges.
	 */
	protected List<List<DirectedEdge>> getIncomingEdges() {
		return incomingEdges;
	}

	/**
	 * Returns the outgoing edges of a particular Object in the Graph.
	 * 
	 * @param v
	 *            Object you want to determine its outgoing edges.
	 * @return List of outgoing edges of the specified Vertex.
	 */
	public List<DirectedEdge> getOutgoingEdges(Object v) {
		int indexObject;

		indexObject = this.graph.vertices.indexOf(v);
		if (indexObject == -1)
			throw new IllegalArgumentException("Object " + v
					+ " is not in this graph");
		return this.outgoingEdges.get(indexObject);
	}

	/**
	 * Returns the incoming edges of a particular Object in the Graph.
	 * 
	 * @param v
	 *            Object you want to determine its incoming edges.
	 * @return List of incoming edges of the specified Vertex.
	 */
	public List<DirectedEdge> getIncomingEdges(Object v) {
		int indexObject;

		indexObject = this.graph.vertices.indexOf(v);
		if (indexObject == -1)
			throw new IllegalArgumentException("Object " + v
					+ " is not in this graph");
		return this.incomingEdges.get(indexObject);
	}

	/**
	 * Returns the vertices that are adjacent to a specified Vertex, respecting
	 * the direction of the Edge from the specified Vertex.
	 * 
	 * @param v
	 *            Object you want to determine its outgoing adjacent vertices.
	 * @param outGoing
	 *            If true, method will return outgoing adjacent vertices. If
	 *            false, method will return incoming adjacent vertices.
	 * @return List of outgoing / incoming vertices adjacent to the specified
	 *         Vertex.
	 */
	private List getAdjacentVertices(Object v, boolean outGoing) {
		List adjacentVertices = new ArrayList(10);
		List incidentEdges;
		Iterator iterator;
		Edge edge;
		Object oppositeObject;

		if (outGoing)
			incidentEdges = this.getOutgoingEdges(v);
		else
			incidentEdges = this.getIncomingEdges(v);

		iterator = incidentEdges.iterator();
		while (iterator.hasNext()) {
			edge = (Edge) iterator.next();
			oppositeObject = edge.getOppositeVertex(v);
			if (oppositeObject != null)
				adjacentVertices.add(oppositeObject);
		}

		return adjacentVertices;
	}

	/**
	 * Returns the vertices that are adjacent to a specified Object where the
	 * Edge is outgoing from the specified Object to the adjacent vertex.
	 * 
	 * @param v
	 *            Object you want to determine its outgoing adjacent vertices.
	 * @return List of outgoing vertices adjacent to the specified Vertex.
	 */
	public List getOutgoingAdjacentVertices(Object v) {
		return this.getAdjacentVertices(v, true);
	}

	/**
	 * Returns the vertices that are adjacent to a specified Object where the
	 * Edge is incoming from the specified Object to the adjacent vertex.
	 * 
	 * @param v
	 *            Object you want to determine its incoming adjacent vertices.
	 * @return List of incoming vertices adjacent to the specified Vertex.
	 */
	public List getIncomingAdjacentVertices(Object v) {
		return this.getAdjacentVertices(v, false);
	}

	/**
	 * Returns an Edge in the Graph whose origin is fromObject and destination
	 * is toVertex. If there is more than one Edge that has the same origin and
	 * destination in the Graph, the first matching Edge is returned.
	 * 
	 * @param fromVertex
	 *            Object that is the origin of the directed Edge
	 * @param toVertex
	 *            Object that is the destination of the directed Edge
	 * @return Edge whose origin is fromObject and destination is toVertex
	 * @see salvo.jesus.graph.Edge
	 */
	public DirectedEdge getEdge(Object fromvertex, Object tovertex) {
		List outIncidentEdges;
		Iterator iterator;
		DirectedEdge edge;

		// Get the adjacent edge set of the from vertex
		outIncidentEdges = this.getOutgoingEdges(fromvertex);

		// Find the edge where the direction is to the tovertex
		iterator = outIncidentEdges.iterator();
		while (iterator.hasNext()) {
			edge = (DirectedEdge) iterator.next();
			if (edge.getSink() == tovertex) {
				// Edge is found.
				iterator = null;
				return edge;
			}
		}
		return null;
	}

	/**
	 * Determines if there is a path from Object fromObject to Object toVertex.
	 * This will not return true if the only path has at least one Edge pointing
	 * in the opposite direction of the path.
	 * 
	 * @param fromVertex
	 *            starting Object for the path
	 * @param toVertex
	 *            ending Object for the path
	 * @return true if there is a path from Object to toVertex. false otherwise.
	 */
	public boolean isPath(Object fromVertex, Object toVertex) {
		List visited = new ArrayList(10);

		this.graph.getTraversal().traverse(fromVertex, visited,
				new StopAtVisitor(toVertex));
		if (toVertex.equals(visited.get(visited.size() - 1)))
			return true;
		else
			return false;
	}

	/**
	 * Determines if there is a cycle from Object fromVertex. A cycle occurs
	 * when there is a path from the specified Object back to itself, taking
	 * into consideration that direction of the Edges along the path.
	 * 
	 * @param fromVertex
	 *            Object to be tested for a cycle path.
	 * @return true if there is a cycle path from fromObject to itself.
	 */
	public boolean isCycle(Object fromVertex) {
		List outedges = this.getOutgoingEdges(fromVertex);
		Iterator iterator = outedges.iterator();
		DirectedEdge dedge;
		Object adjacentVertex;

		// For each outgoing edge of the vertex ...
		while (iterator.hasNext()) {
			dedge = (DirectedEdge) iterator.next();
			// ... get the opposite vertex
			adjacentVertex = dedge.getOppositeVertex(fromVertex);
			// .. and check if there is a path from the opposite vertex back to
			// the vertex
			if (this.isPath(adjacentVertex, fromVertex))
				// There is a cycle
				return true;
		}

		// No cycle
		return false;
	}

	// --- NON-DELEGATION METHODS
	/**
	 * Adds a new List in both the incoming and outgoing List of Edges. The two
	 * new vectors added will hold the new vertex's incoming and outgoing edges.
	 * 
	 * @param newvertex
	 *            The Object object to be added to the graph.
	 */
	public void add(Object newvertex) throws GraphException {
		// Whenever a new vertex is added, we also need to create
		// a blank adjacenct edge list for the new vertex
		outgoingEdges.add(new ArrayList<DirectedEdge>(10));
		incomingEdges.add(new ArrayList<DirectedEdge>(10));
	}

	/**
	 * Removes the vertex's vectors of incoming and outgoing edges..
	 * 
	 * @param index
	 *            index of Object to be removed
	 */
	protected void remove(int index) {
		// Remove the adjacent edges entry of the vertex
		this.outgoingEdges.remove(index);
		this.incomingEdges.remove(index);
	}

	/**
	 * Adds the Edge created as an outgoing edge of one vertex and as an
	 * incoming edge of the other vertex.
	 * 
	 * @param fromVertex
	 *            Object that will be the source of the Edge
	 * @param toVertex
	 *            Object that will be the sink of the Edge
	 */
	protected void addEdge(DirectedEdge dedge) {
		List<DirectedEdge> v1outIncidentEdges;
		List<DirectedEdge> v2inIncidentEdges;
		Edge edge;

		// Now get the vector of outgoing edge of v1
		v1outIncidentEdges = this.getOutgoingEdges(dedge.getSource());
		// ... and the vector of incoming edge of v2
		v2inIncidentEdges = this.getIncomingEdges(dedge.getSink());

		// Add the edge as an outgoing edge of v1 and as an incoming edge of v2
		v1outIncidentEdges.add(dedge);
		v2inIncidentEdges.add(dedge);
	}

	/**
	 * Removes the Edge as the incoming and outgoing edge of the vertices at the
	 * ends of the Edge.
	 * 
	 * @param edge
	 *            Edge to be removed from the Graph
	 */
	protected void removeEdge(DirectedEdge dedge) {
		Object fromvertex;
		Object tovertex;
		List outIncidentEdges;
		List inIncidentEdges;

		// Get source and sink vertices of edge
		fromvertex = dedge.getSource();
		tovertex = dedge.getSink();

		// Get the vector of outgoing edge of the source and the
		// vector of incoming edges of the sink.
		outIncidentEdges = this.getOutgoingEdges(fromvertex);
		inIncidentEdges = this.getIncomingEdges(tovertex);

		// Remove the edge from the source's outgoing edges
		outIncidentEdges.remove(dedge);
		// Remove the edge from the sink's incoming edges
		inIncidentEdges.remove(dedge);
	}

	/**
	 * Empty method implementation. This method should never be called or
	 * delegated to for whatever reason.
	 */
	public void setGraphFactory(GraphFactory factory) {
	}

	/**
	 * Empty method implementation that returns null. This method should never
	 * be called or delegated to for whatever reason.
	 */
	public GraphFactory getGraphFactory() {
		return null;
	}

	/**
	 * Empty method implemetation that returns 0. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public int getVerticesCount() {
		return 0;
	}

	/*
	 * this method should never be called on a delegate
	 * 
	 * @see salvo.jesus.graph.Graph#getAllVertices()
	 */
	public List getAllVertices() {
		return null;
	}

	/**
	 * this method should never be called on a delegate
	 * 
	 * @see salvo.jesus.graph.Graph#getAllVertices()
	 */
	public List getAllEdges() {
		return null;
	}

	/**
	 * Empty method implemetation that returns 0. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public int getEdgesCount() {
		return 0;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void remove(Object v) throws GraphException {
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public Iterator getVerticesIterator() {
		return null;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public List cloneVertices() {
		return null;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 * 
	 * @deprecated
	 */
	public Edge createEdge(Object v1, Object v2) {
		return null;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public Edge addEdge(Object v1, Object v2) throws GraphException {
		return null;
	}

	/**
	 * Empty method implemetation that does nothing. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void addEdge(Edge edge) throws GraphException {
		return;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void removeEdge(Edge e) throws GraphException {
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void removeEdges(Object v) throws GraphException {
	}

	/**
	 * Empty method implemetation that returns 0. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public int getDegree() {
		return 0;
	}

	/**
	 * Empty method implemetation that returns 0. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public int getDegree(Object v) {
		return 0;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public Set getVertices(int degree) {
		return null;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public List getEdges(Object v) {
		return null;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public List getAdjacentVertices(Object v) {
		return null;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public HashSet getAdjacentVertices(List vertices) {
		return null;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public List getConnectedSet() {
		return null;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public List getConnectedSet(Object v) {
		return null;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void mergeconnectedSet(Object v1, Object v2) {
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public List traverse(Object startat) {
		return null;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public GraphTraversal getTraversal() {
		return null;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void setTraversal(GraphTraversal traversal) {
	}

	/**
	 * Empty method implemetation that returns false. This method should never
	 * be called or delegated to for whatever reason.
	 */
	public boolean isConnected(Object v1, Object v2) {
		return false;
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void addGraphAddVertexListener(GraphAddVertexListener listener) {
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void addGraphAddEdgeListener(GraphAddEdgeListener listener) {
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void addGraphRemoveEdgeListener(GraphRemoveEdgeListener listener) {
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void addGraphRemoveVertexListener(GraphRemoveVertexListener listener) {
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void removeGraphAddVertexListener(GraphAddVertexListener listener) {
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void removeGraphAddEdgeListener(GraphAddEdgeListener listener) {
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void removeGraphRemoveEdgeListener(GraphRemoveEdgeListener listener) {
	}

	/**
	 * Empty method implemetation that returns null. This method should never be
	 * called or delegated to for whatever reason.
	 */
	public void removeGraphRemoveVertexListener(
			GraphRemoveVertexListener listener) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see salvo.jesus.graph.Graph#same()
	 */
	public Graph same() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see salvo.jesus.graph.Graph#findVertex(java.lang.Object)
	 */
	public Object findVertex(Object o) {
		return null;
	}

}
