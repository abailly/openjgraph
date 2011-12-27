/*______________________________________________________________________________
*
* Copyright 2004 Arnaud Bailly - NORSYS/LIFL
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions
* are met:
*
* (1) Redistributions of source code must retain the above copyright
*     notice, this list of conditions and the following disclaimer.
*
* (2) Redistributions in binary form must reproduce the above copyright
*     notice, this list of conditions and the following disclaimer in
*     the documentation and/or other materials provided with the
*     distribution.
*
* (3) The name of the author may not be used to endorse or promote
*     products derived from this software without specific prior
*     written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
* IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
* INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
* HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
* STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
* OF THE POSSIBILITY OF SUCH DAMAGE.
*______________________________________________________________________________
*
* Created on 16 nov. 2004
* 
*/
package salvo.jesus.graph.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import salvo.jesus.graph.DirectedEdge;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.Edge;
import salvo.jesus.graph.Graph;
import salvo.jesus.graph.NullVisitor;
import salvo.jesus.graph.Visitor;
import salvo.jesus.util.EmptyQueueException;
import salvo.jesus.util.Queue;

/**
 * @author nono
 * @version $Id: BreadthFirstDirectedGraphTraversal.java 1268 2006-08-14 13:25:12Z nono $
 */
public class BreadthFirstDirectedGraphTraversal extends GraphTraversal{
  Queue queue;

  /**
   * Creates a BreadthFirstTraversal object
   */
  public BreadthFirstDirectedGraphTraversal( DirectedGraph graph ) {
    super( graph );
    this.queue = new Queue();
  }

  public int traverse(Object startat, List visited, Visitor visitor) {
    Object  next;
    Object  adjacent;
    List    outEdges;
    Edge edge;
    Iterator  iterator;

    // Put the starting vertex in the queue
    this.queue.put( startat );

    try {
      do {
        // Get the next vertex in the queue and add it to the visited
        next =  this.queue.get();
        visited.add( next );

        // Exit if the visitor tells us so
        if( !visitor.visit( next ))
          return TERMINATEDBYVISITOR;

        // Get all of its adjacent vertices and put them in the queue
        // only if it has not been visited and it has not been queued
        outEdges = ((DirectedGraph)this.graph).getOutgoingEdges( next );
        iterator = outEdges.iterator();
        while( iterator.hasNext()) {
          edge = (DirectedEdge) iterator.next();
          adjacent = edge.getOppositeVertex( next );
          if( !visited.contains( adjacent ) && !this.queue.contains( adjacent )) {
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

/* 
 * $Log: BreadthFirstDirectedGraphTraversal.java,v $
 * Revision 1.1  2004/11/18 08:18:07  bailly
 * added BFT for directed graphs
 *
*/
