package salvo.jesus.graph;

import java.util.List;

/**
 * A directed Graph where edges have a specified direction. Edges in this graph
 * are therefore instances of DirectedEdge.
 * 
 * @author Jesus M. Salvo Jr.
 */

public interface DirectedGraph extends Graph{
	/**
	 * Returns the outgoing edges of a particular Object in the Graph.
	 * 
	 * @param v
	 *            Object you want to determine its outgoing edges.
	 * @return List of outgoing edges of the specified Vertex.
	 */
	public List<DirectedEdge> getOutgoingEdges(Object v);

	/**
	 * Returns the incoming edges of a particular Object in the Graph.
	 * 
	 * @param v
	 *            Object you want to determine its incoming edges.
	 * @return List of incoming edges of the specified Vertex.
	 */
	public List<DirectedEdge> getIncomingEdges(Object v);

	/**
	 * Returns the vertices that are adjacent to a specified Vertex where the
	 * Edge is outgoing from the specified Object to the adjacent vertex.
	 * 
	 * @param v
	 *            Object you want to determine its outgoing adjacent vertices.
	 * @return List of outgoing vertices adjacent to the specified Vertex.
	 */
	public List getOutgoingAdjacentVertices(Object v);

	/**
	 * Returns the vertices that are adjacent to a specified Vertex where the
	 * Edge is incoming from the specified Object to the adjacent vertex.
	 * 
	 * @param v
	 *            Object you want to determine its incoming adjacent vertices.
	 * @return List of incoming vertices adjacent to the specified Vertex.
	 */
	public List getIncomingAdjacentVertices(Object v);

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
	public DirectedEdge getEdge(Object fromvertex, Object tovertex);

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
	public boolean isPath(Object fromVertex, Object toObject);

	/**
	 * Determines if there is a cycle from Object fromVertex. A cycle occurs
	 * when there is a path from the specified Object back to itself, taking
	 * into consideration that direction of the Edges along the path. This
	 * simply calls isPath(), where both parameters are the same Vertex.
	 * 
	 * @param fromVertex
	 *            Object to be tested for a cycle path.
	 * @return true if there is a cycle path from fromObject to itself.
	 */
	public boolean isCycle(Object fromObject);
}
