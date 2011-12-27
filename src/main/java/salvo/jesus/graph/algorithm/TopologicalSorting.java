package salvo.jesus.graph.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import salvo.jesus.graph.DirectedAcyclicGraph;
import salvo.jesus.graph.NullVisitor;

import salvo.jesus.graph.Visitor;

/**
 * A concrete subclass of GraphTraversal that performs a topological sort
 * against a directed acyclic graph.
 *
 * @author  Jesus M. Salvo Jr.
 */

public class TopologicalSorting extends GraphTraversal {

  DirectedAcyclicGraph  dag;

  /**
   * Creates an instance of TopologicalSorting that will perform a
   * topological sort against a directed acyclic graph.
   *
   * @param dag   The DirectedAcyclicGraph on which topological sorting will be performed.
   */
  public TopologicalSorting( DirectedAcyclicGraph dag ) {
    super( dag );
    this.dag = dag;
  }

  /**
   * Perform a topological sort of the connected set of a directed acyclic graph
   * to which Object startat belongs, starting at Object startat.
   *
   * @param	startat	  The Object to which you want to start the traversal.
   * @param	visited	  List of vertices that has been visited,
   *                  in the sequence they were visited.
   * @param visitor   Visitor object to visit each vertex as they are visited.
   *                  Return value of the visitor is ignored.
   */
  public int traverse( Object startat, List visited, Visitor visitor) {
    int			indexOfstartat;
    List        currentadjacentVertices;
    Iterator	iterator;
    Object		adjacentObject;

    // Remove any instance of the vertex being visited.
    visited.remove( startat );
    visited.add( startat );
    // Let the visitor object visit this vertex. Ignore return value.
    visitor.visit( startat );

    // Get the adjacency edge list for the current vertex,
    // from which we can get the adjacent vertices
    currentadjacentVertices = this.dag.getOutgoingAdjacentVertices( startat );

    // Get an iterator for the adjacency list of the current vertex
    iterator = currentadjacentVertices.iterator( );

    // For each vertex adjacent to the current vertex, visit the vertex,
    // calling this method recursively
    while( iterator.hasNext() ){
      adjacentObject = iterator.next();
      // Ignore return value of traversal
      this.traverse( adjacentObject, visited, visitor );
    }

    return OK;
  }

  /**
   * Perform a topological sort of the connected set of a directed acyclic graph
   * to which Object startat belongs, starting at Object startat.
   *
   * @param	startat	  The Object to which you want to start the traversal.
   * @param visitor   Visitor object to visit each vertex as they are visited.
   *                  Return value of the visitor is ignored.
   *
   * @return  A List of vertices in the order that they were visited.
   */
  public List traverse( Object startat, Visitor visitor ) {
    List    visited = new ArrayList( 10 );

    this.traverse( startat, visited, visitor );
    return visited;
  }

  /**
   * Perform a topological sort of the connected set of a directed acyclic graph
   * to which Object startat belongs, starting at Object startat.
   *
   * @param	startat	  The Object to which you want to start the traversal.
   *
   * @return  A List of vertices in the order that they were visited.
   */
  public List traverse( Object startat ) {
    return this.traverse( startat, new NullVisitor());
  }

  /**
   * Perform a reverse topological sort of the connected set of a directed acyclic graph
   * to which Object startat belongs, starting at Object startat.
   *
   * This method is not part of the GraphTraversal abstract class, but is added
   * here for convenience.
   *
   * @param	startat	  The Object to which you want to start the traversal.
   *
   * @return  A List of vertices in the order that they were visited.
   */
  public List reverseTraverse( Object startat ) {
    List sortSequence = this.traverse( startat, new NullVisitor());
    Collections.reverse( sortSequence );
    return sortSequence;
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
  public List traverse() {
    List        sortSequence = new ArrayList( 10 );
    List        rootVertices = new ArrayList( 10 );
    Iterator    iterator;

    rootVertices = this.dag.getRoot();
    iterator = rootVertices.iterator();
    while( iterator.hasNext()){
      this.traverse(  iterator.next(), sortSequence, new NullVisitor());
    }

    return sortSequence;
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
  public List reverseTraverse( ){
    List    sortSequence = this.traverse();
    Collections.reverse( sortSequence );
    return sortSequence;
  }
}
