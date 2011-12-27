package salvo.jesus.graph.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import salvo.jesus.graph.DirectedEdge;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.Graph;
import salvo.jesus.graph.NullVisitor;

import salvo.jesus.graph.Visitor;

/**
 * A concrete subclass of GraphTraversal that uses depth-first search
 * in traversing a directed graph. Note that the traverse() method will only
 * traverse the connected set to which the Object the traversal will start at belongs.
 *
 * Further note that due to the directions of edges, not all vertices may actually
 * be visited by the traversal.
 *
 * @author  Jesus M. Salvo Jr.
 */

public class DepthFirstDirectedGraphTraversal extends GraphTraversal {
  Stack   stack;
  DirectedGraph   dgraph;

  /**
   * Creates a DepthFirstDirectedGraphTraversal object that will perform
   * a depth first traversal on the specified DirectedGraph
   *
   * @param   graph   DirectedGraph on which the traversal will be performed.
   */
  public DepthFirstDirectedGraphTraversal( DirectedGraph graph ) {
    super( (Graph) graph );
    this.dgraph = (DirectedGraph) graph;
    this.stack = new Stack();
  }

  public int traverse(Object startat, List visited, Visitor visitor ) {
    Object  next;
    Object  adjacent;
    List    outEdges;
    DirectedEdge  edge;
    Iterator  iterator;

    // Push the starting vertex onto the stack
    this.stack.push( startat );

    do {
      // Get the next vertex in the queue and add it to the visited
      next =  this.stack.pop();
      visited.add( next );

      // Exit if the visitor tells us so
      if( !visitor.visit( next ))
        return TERMINATEDBYVISITOR;

      // Get all of its adjacent vertices, respecting the edge direction,
      // and push them onto the stack only if it has not been visited and
      // it has not been stacked
      outEdges = this.dgraph.getOutgoingEdges( next );
      iterator = outEdges.iterator();
      while( iterator.hasNext()) {
        edge = (DirectedEdge) iterator.next();
        adjacent =  edge.getOppositeVertex( next );
        if( !visited.contains( adjacent ) && !this.stack.contains( adjacent )) {
          this.stack.push( adjacent );
        }
      }

    } while( !this.stack.isEmpty() );
    return OK;
  }

  public List traverse( Object startat ) {
    return this.traverse( startat, new NullVisitor());
  }

  public List traverse( Object startat, Visitor visitor ) {
    List    visited = new ArrayList( 10 );

    this.traverse( startat, visited, visitor );
    return visited;
  }
}

