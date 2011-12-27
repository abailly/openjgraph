/*______________________________________________________________________________
 * 
 * Copyright 2005 Arnaud Bailly - NORSYS/LIFL
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
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on 3 avr. 2005
 *
 */
package salvo.jesus.graph.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import salvo.jesus.graph.DirectedEdge;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.EdgeVisitor;
import salvo.jesus.graph.Graph;
import salvo.jesus.graph.NullVisitor;

import salvo.jesus.graph.Visitor;

/**
 * This traversal algorith visit each edge in the graph in a depth-first manner
 * from a starting vertex.
 * 
 * @author nono
 * @version $Id$
 */
public class DFDirectedGraphEdgeTraversal extends GraphEdgeTraversal {

    private Stack stack;

    private DirectedGraph dgraph;

    /**
     * @param graph
     */
    public DFDirectedGraphEdgeTraversal(Graph graph) {
        super(graph);
        this.dgraph = (DirectedGraph) graph;
        this.stack = new Stack();
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.algorithm.GraphTraversal#traverse(salvo.jesus.graph.Vertex,
     *      java.util.List, salvo.jesus.graph.Visitor)
     */
    public int traverse(Object startat, List visited, EdgeVisitor visitor) {
        DirectedEdge next;
        Object adjacent;
        List outEdges;
        DirectedEdge edge;
        Iterator iterator;

        // clear stack
        this.stack.clear();

        // Push the starting vertex onto the stack
        for (Iterator it = dgraph.getOutgoingEdges(startat).iterator(); it
                .hasNext();) {
            this.stack.push(it.next());
        }

        do {
            // Get the next vertex in the queue and add it to the visited
            next = (DirectedEdge) this.stack.pop();
            visited.add(next);

            // Exit if the visitor tells us so
            if (!visitor.visit(next))
                return TERMINATEDBYVISITOR;

            // Get all of its adjacent vertices, respecting the edge direction,
            // and push them onto the stack only if it has not been visited and
            // it has not been stacked
            if (next.getDirection() == DirectedEdge.DIRECTION_A_TO_B)
                adjacent = next.getVertexB();
            else
                adjacent = next.getVertexA();
            for (Iterator it = this.dgraph.getOutgoingEdges(adjacent)
                    .iterator(); it.hasNext();) {
                edge = (DirectedEdge) it.next();
                if (!visited.contains(edge) && !this.stack.contains(edge)) {
                    this.stack.push(edge);
                }
            }

        } while (!this.stack.isEmpty());
        return OK;
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.algorithm.GraphTraversal#traverse(salvo.jesus.graph.Vertex)
     */
    public List traverse(Object startat) {
        return this.traverse(startat, new NullVisitor());
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.algorithm.GraphEdgeTraversal#traverse(salvo.jesus.graph.Vertex,
     *      salvo.jesus.graph.EdgeVisitor)
     */
    public List traverse(Object startat, EdgeVisitor visitor) {
        List visited = new ArrayList(10);
        this.traverse(startat, visited, visitor);
        return visited;
    }

}