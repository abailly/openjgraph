package salvo.jesus.graph.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import salvo.jesus.graph.Graph;
import salvo.jesus.graph.NullVisitor;

import salvo.jesus.graph.Visitor;

/**
 * A concrete subclass of GraphTraversal that uses depth-first search
 * in traversing a graph. Note that the traverse() method will only
 * traverse the connected set to which the Object the traversal will start at belongs.
 *
 * @author  Jesus M. Salvo Jr.
 */

public class DepthFirstGraphTraversal extends GraphTraversal {
  Stack   stack;

  /**
   * Creates a DepthFirstGraphTraversal object.
   */
  public DepthFirstGraphTraversal( Graph graph ) {
    super( graph );
    this.stack = new Stack();
  }

  public int traverse( Object startat, List visited, Visitor visitor ) {
    Object  next;
    Object  adjacent;
    List    adjacentVertices;
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

      // Get all of its adjacent vertices and push them onto the stack
      // only if it has not been visited and it has not been stacked
      adjacentVertices = graph.getAdjacentVertices( next );
      iterator = adjacentVertices.iterator();
      while( iterator.hasNext()) {
        adjacent =  iterator.next();
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
    List  visited = new ArrayList( 10 );

    this.traverse( startat, visited, visitor );
    return visited;
  }
}