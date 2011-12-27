package salvo.jesus.graph.algorithm;

import java.io.Serializable;
import java.util.List;

import salvo.jesus.graph.EdgeVisitor;
import salvo.jesus.graph.Graph;

import salvo.jesus.graph.Visitor;

/**
 * Abstract class for an algorithm implementing graph traversal for edges.
 * Classes implementing the Graph interface uses the Strategy
 * pattern to allow different implementations of the graph traversal
 * algorithm to be used.
 *
 * Concrete implementations of this class must never modify the Graph itself.
 */

public abstract class GraphEdgeTraversal implements Serializable {
  static final public int   TERMINATEDBYVISITOR = -1;
  static final public int   OK = 1;

  /**
   * The Graph on which graph traversal will be performed.
   */
  Graph   graph;

  public GraphEdgeTraversal( Graph graph ) {
    this.graph = graph;
  }

  /**
   * Abstract traversal method to be implemented by subclasses.
   *
   * @param startat The vertex from which traversal will start.
   * @param visitor Visitor object controlling if and when traversal will stop,
   *                apart from having visited all the edges.
   * @param visited A List of edges that has been visited in sequence by the traversal
   */
  public abstract int traverse( Object startat, List visited, EdgeVisitor visitor );

  /**
   * Abstract traversal method to be implemented by subclasses.
   *
   * @param startat The vertex from which traversal will start.
   * @return  A List of edges that has been visited in sequence by the traversal
   */
  public abstract List traverse( Object startat );

  /**
   * Abstract traversal method to be implemented by subclasses.
   *
   * @param startat The vertex from which traversal will start.
   * @param visitor Visitor object controlling if and when traversal will stop,
   *                apart from having visited all the edges.
   * @return  A List of edges that has been visited in sequence by the traversal
   */
  public abstract List traverse( Object startat, EdgeVisitor visitor );
}