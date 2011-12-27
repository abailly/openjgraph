package salvo.jesus.graph.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import salvo.jesus.graph.Graph;
import salvo.jesus.graph.NullVisitor;
import salvo.jesus.graph.Visitor;
import salvo.jesus.util.EmptyQueueException;
import salvo.jesus.util.Queue;

/**
 * A concrete subclass of GraphTraversal that uses breadth-first search
 * in traversing a graph. Note that the traverse() method will only
 * traverse the connected set to which the Object the traversal will start at belongs.
 *
 * @author  Jesus M. Salvo Jr.
 */

public class BreadthFirstTraversal extends GraphTraversal {
  Queue queue;

  /**
   * Creates a BreadthFirstTraversal object
   */
  public BreadthFirstTraversal( Graph graph ) {
    super( graph );
    this.queue = new Queue();
  }

  public int traverse(Object startat, List visited, Visitor visitor) {
    Object  next;
    Object  adjacent;
    List    adjacentVertices;
    Iterator  iterator;

    // Put the starting vertex in the queue
    this.queue.put( startat );

    try {
      do {
        // Get the next vertex in the queue and add it to the visited
        next = this.queue.get();
        visited.add( next );

        // Exit if the visitor tells us so
        if( !visitor.visit( next ))
          return TERMINATEDBYVISITOR;

        // Get all of its adjacent vertices and put them in the queue
        // only if it has not been visited and it has not been queued
        adjacentVertices = this.graph.getAdjacentVertices( next );
        iterator = adjacentVertices.iterator();
        while( iterator.hasNext()) {
          adjacent = iterator.next();
          if( !visited.contains( adjacent ) && !queue.isQueued( adjacent )) {
            this.queue.put( adjacent );
          }
        }

      } while( !this.queue.isEmpty() );
    }
    // This should not happen, but catch it anyway as it is required,
    // but do nothing.
    catch( EmptyQueueException e ) {}
      return OK;
  }

  public List traverse(Object startat, Visitor visitor) {
    List    visited = new ArrayList( 10 );

    this.traverse( startat, visited, visitor );
    return visited;
  }

  public List traverse(Object startat) {
    List    visited = new ArrayList( 10 );

    this.traverse( startat, visited, new NullVisitor() );
    return visited;
  }
}
