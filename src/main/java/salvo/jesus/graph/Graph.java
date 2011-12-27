package salvo.jesus.graph;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import salvo.jesus.graph.algorithm.GraphTraversal;

/**
 * An interface for Graphs.
 */
public interface Graph extends Serializable, Cloneable {

	// ------------------- Informational methods
	/**
	 * Returns the number of vertices in the graph
	 * 
	 * @return The number of vertices in the graph.
	 */
	public int getVerticesCount();

	/**
	 * Returns a list view of all vertices in this graph. This list is
	 * independant of the actual way the vertices are stored in the Graph.
	 * 
	 * @return a List of vertices - maybe empty but not null
	 */
	public List getAllVertices();

	/**
	 * Returns the number of edges in the graph
	 * 
	 * @return The number of edges in the graph.
	 */
	public int getEdgesCount();

	/**
	 * Returns a list view of all edges in this graph. This list is independant
	 * of the actual way the vertices are stored in the Graph.
	 * 
	 * @return a List of edgees - maybe empty but not null
	 */
	public Collection<Edge> getAllEdges();

	// ------------------- Factory setup
	/**
	 * Returns the factory that will be responsible for creating Vertices and
	 * Edges in a Graph.
	 */
	public GraphFactory getGraphFactory();

	/**
	 * Sets the factory that will be responsible for creating Vertices and Edges
	 * in a Graph.
	 */
	public void setGraphFactory(GraphFactory factory);

	// ------------------- Object manipulation
	/**
	 * Adds a Object into the Graph. This will also create a new entry in the
	 * edges List and add the newly added Object to its own connected set,
	 * thereby adding a new List in the connectedSet List. Finally, all
	 * GraphAddVertexListeners are informed of the event that a Object has been
	 * added to the Graph.
	 * 
	 * @param v
	 *            Object to be added to the Graph
	 * @throws GraphException
	 *             TODO
	 */
	public void add(Object v) throws GraphException;

	/**
	 * Removes the specified Edge from the Graph.
	 * 
	 * @param edge
	 *            The Edge object to be removed.
	 * @throws GraphException
	 *             TODO
	 */
	public void remove(Object v) throws GraphException;

	/**
	 * Returns an iterator that iterates through the graph's vertices.
	 * 
	 * @return An iterator of List vertices.
	 */
	public Iterator getVerticesIterator();

	// ----------------------- Edge manipulation
	/**
	 * Method to create the proper type of Edge class.
	 * 
	 * @param v1
	 *            One endpoint of the edge
	 * @param v2
	 *            Other endpoint of the edge
	 * 
	 * @deprecated As of 0.9.0, this is replaced by
	 *             <tt>GraphFactory.createEdge()</tt>. Future releases will
	 *             have this method removed.
	 */
	public Edge createEdge(Object v1, Object v2);

	/**
	 * Adds an Edge into the Graph. The vertices of the Edge must already be
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
	 * @throws GraphException
	 *             TODO
	 */
	public Edge addEdge(Object v1, Object v2) throws GraphException;

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
	 * type of Edge being added to the Graph matches the Graph. For example,
	 * only a DirectedEdge must be added to a DirectedGraph.
	 * 
	 * @param e
	 *            The edge to be added to the Graph.
	 * @throws GraphException
	 *             TODO
	 */
	public void addEdge(Edge e) throws GraphException;

	/**
	 * Removes the specified Edge from the Graph.
	 * 
	 * @param e
	 *            The Edge object to be removed.
	 * @throws GraphException
	 *             TODO
	 */
	public void removeEdge(Edge e) throws GraphException;

	/**
	 * Removes incident Edges of a Vertex. The Edges removed are those whose
	 * either endpoints has the specified vertex. This method is usually called
	 * just prior to removing a Object from a Graph.
	 * 
	 * @param v
	 *            Object whose Edges are to be removed
	 * @throws GraphException
	 *             TODO
	 */
	public void removeEdges(Object v) throws GraphException;

	// --------------------------- Degree methods
	/**
	 * Returns the degree of the graph, which is simply the highest degree of
	 * all the graph's vertices.
	 * 
	 * @return An int indicating the degree of the graph.
	 */
	public int getDegree();

	/**
	 * Returns the degree of the vertex, which is simply the number of edges of
	 * the vertex.
	 * 
	 * @return The degree of the vertex.
	 */
	public int getDegree(Object v);

	/**
	 * Returns all vertices with the specified degree.
	 * 
	 * @param degree
	 *            The degree of the vertex to be returned.
	 * @return A collection of vertices with the above specified degree.
	 */
	public Set getVertices(int degree);

	// ---------------------- Adjacency methods
	/**
	 * Returns a List of edges of the specified vertex.
	 * 
	 * @param v
	 *            The vertex whose edges we want returned
	 * @return A List of Edges that are incident edges of the specified vertex.
	 */
	public List getEdges(Object v);

	/**
	 * Returns the vertices adjacent to the specified vertex.
	 * 
	 * @param v
	 *            The Object you want to determine its adjacent vertices.
	 * @return List of vertices adjacent to the specified vertex v.
	 */
	public List getAdjacentVertices(Object v);

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
	public HashSet getAdjacentVertices(List vertices);

	// ------------------------ Connected set methods
	/**
	 * Returns the connected sets in the Graph. Each List in the return List is
	 * a List of vertices that are connected to each other, regardless of the
	 * direction of the Edge conneting them together.
	 * 
	 * @return List of List of connected vertices.
	 */
	public List getConnectedSet();

	/**
	 * Returns the connected set to which the specified vertex belongs.
	 * 
	 * @param v
	 *            Object to which you want the connected set returned.
	 * @return List of connected vertices where the specified vertex belongs.
	 */
	public List getConnectedSet(Object v);

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
	public void mergeconnectedSet(Object v1, Object v2);

	// ------------------------ Traversal methods
	/**
	 * Traverses the Graph starting at startat Object by performing a
	 * depth-first traversal. The vertices traversed from startat are stored in
	 * Visited. Only the connected components to which startat belongs to will
	 * be traversed.
	 * 
	 * @param startat
	 *            The Object to which you want to start the traversal.
	 */
	public List traverse(Object startat);

	/**
	 * Gets the traversal algorithm used by the Graph.
	 * 
	 * @return GraphTraversal object performing traversal for the Graph.
	 */
	public GraphTraversal getTraversal();

	/**
	 * Sets the graph traversal algorithm to be used
	 * 
	 * @param traversal
	 *            A concrete implementation of the GraphTraversal object.
	 */
	public void setTraversal(GraphTraversal traversal);

	// ------------------------ connectivity methods
	/**
	 * Determines if two vertices are connected
	 * 
	 * @param v1
	 *            starting Object for the path
	 * @param v2
	 *            ending Object for the path
	 * @return true if v1 and v2 are connected.
	 */
	public boolean isConnected(Object v1, Object v2);

	// ------------------------ Listener methods
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
	public void addGraphAddVertexListener(GraphAddVertexListener listener);

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
	public void addGraphAddEdgeListener(GraphAddEdgeListener listener);

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
	public void addGraphRemoveEdgeListener(GraphRemoveEdgeListener listener);

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
	public void addGraphRemoveVertexListener(GraphRemoveVertexListener listener);

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
	public void removeGraphAddVertexListener(GraphAddVertexListener listener);

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
	public void removeGraphAddEdgeListener(GraphAddEdgeListener listener);

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
	public void removeGraphRemoveEdgeListener(GraphRemoveEdgeListener listener);

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
			GraphRemoveVertexListener listener);

	/**
	 * A generic construction method that returns an new Graph instance
	 * according to concrete implementation. The basic contract is that the type
	 * of this graph and returned graph are equal, and the GraphFactory are the
	 * same.
	 * 
	 * @return a new Graph instance
	 */
	public Graph same();

	/**
	 * Find a vertex whose data is equal to o
	 * 
	 * @param block
	 * @return
	 */
	public Object findVertex(Object o);
}
