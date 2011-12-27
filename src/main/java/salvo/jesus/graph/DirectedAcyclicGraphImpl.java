package salvo.jesus.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import salvo.jesus.graph.algorithm.TopologicalSorting;

/**
 * The DirectedAcyclicGraph class represents a directed acyclic graph (DAG)
 * where there is no cyclic paths for any of its vertices.
 *
 * A cylic path is a path from a vertex back to itself by following the
 * direction of the edges.
 *
 * @author		Jesus M. Salvo Jr.
 */

public class DirectedAcyclicGraphImpl extends DirectedGraphImpl implements DirectedAcyclicGraph {
  /**
   * Delegate object to handle topological sorting
   */
  TopologicalSorting  topologicalsorting;

  /**
   * Creates a DirectedAcyclicGraph object.
   */
  public DirectedAcyclicGraphImpl() {
    super();
    topologicalsorting = new TopologicalSorting( this );
  }

  /**
    * Adds an Edge into the DirectedAcyclicGraph. This will only add the Edge
    * if there is currently no path from the Object toObject to Object fromVertex.
    * If there is, there will be a cycle thereby violating the property of a
    * directed acyclic graph.
    *
    * @param		fromVertex		Object that will be the origin of the Edge
    *           that will be added into the Graph.
    * @param		toVertex		Object that will be the destination of the Edge
    *           that will be added into the Graph.
    * @return   The Edge object added to the Graph. The Edge object will be
    *           an instance of DirectedEdge. If there is already a path
    *           from fromObject to toVertex, then this method returns null.
    */
  public Edge addEdge( Object fromObject, Object toObject ) throws GraphException {
    if( !isPath( toObject, fromObject ))
        return super.addEdge( fromObject, toObject );
    else
        throw new CycleException();
  }

  /**
    * Adds an Edge into the DirectedAcyclicGraph. This will only add the Edge
    * if there is currently no path from the Object toObject to Object fromVertex
    * of the edge being added. If there is, there will be a cycle thereby
    * violating the property of a directed acyclic graph.
    *
    * @param	e   The edge to be added to the Graph.
    */
  public void addEdge( Edge edge ) throws GraphException {
    DirectedEdge dEdge = ( DirectedEdge ) edge;
    Object  fromObject = dEdge.getSource();
    Object  toObject = dEdge.getSink();

    if( !isPath( toObject, fromObject ))
        super.addEdge( dEdge );
    else
        throw new CycleException();
  }

  /**
    * Returns a List of vertices that is not depended on by other vertices.
    * That is, a List of vertices where there are no Edges pointing to it.
    *
    * @return	List of vertices
    */
  public List getRoot( ){
    Iterator  iterator;
    Iterator  edgeiterator;
    List        currentedges;
    List        rootVertices;
    DirectedEdge  dedge;

    // Initially assume all vertices as root vertices
    rootVertices = (List) ((ArrayList) vertices).clone();

    iterator = this.getOutgoingEdges().iterator();
    while( iterator.hasNext() ){
      currentedges = (List) iterator.next();
      // Then remove all vertices that are sinks of an edge
      edgeiterator = currentedges.iterator();
      while( edgeiterator.hasNext() ){
        dedge = (DirectedEdge) edgeiterator.next();
        rootVertices.remove( dedge.getSink() );
      }
    }

    // The remaining verices in the vector will be the root vertices.
    return rootVertices;
  }

  /**
   * Perform a topological sort of the entire directed acyclic graph.
   * Note that the sequence of vertices in the return List will not distinguish
   * between connected components of the graph.
   *
   * This method is not part of the GraphTraversal abstract class, but is added
   * here for convenience.
   *
   * @return List containing the sequence of the vertices visited in the
   * entire directed acyclic graph, regardless of the connected components of the graph.
   */
  public List topologicalSort( ){
    return this.topologicalsorting.traverse();
  }

  /**
   * Perform a reverse topological sort of the entire directed acyclic graph.
   * Note that the sequence of vertices in the return List will not distinguish
   * between connected components of the graph.
   *
   * This method is not part of the GraphTraversal abstract class, but is added
   * here for convenience.
   *
   * @return List containing the sequence of the vertices visited in the
   * entire directed acyclic graph, regardless of the connected components of the graph.
   */
  public List reverseTopologicalSort( ){
    return this.topologicalsorting.reverseTraverse();
  }

  /**
   * Perform a topological sort of the connected set of a directed acyclic graph
   * to which Object startat belongs, starting at Object startat.
   *
   * @param	startat	  The Object to which you want to start the traversal.
   *
   * @return  A List of vertices in the order that they were visited.
   */
  public List topologicalSort( Object startat ){
    return this.topologicalsorting.traverse( startat );
  }

  /**
   * Perform a reverse topological sort of the connected set of a directed acyclic graph
   * to which Object startat belongs, starting at Object startat.
   *
   * @param	startat	  The Object to which you want to start the traversal.
   *
   * @return  A List of vertices in the order that they were visited.
   */
  public List reverseTopologicalSort( Object startat ){
    return this.topologicalsorting.reverseTraverse( startat );
  }

		/* (non-Javadoc)
		 * @see salvo.jesus.graph.Graph#same()
		 */
		public Graph same() {
			Graph g = new GraphImpl();
			g.setGraphFactory(this.getGraphFactory());
			return g;
		}

}
