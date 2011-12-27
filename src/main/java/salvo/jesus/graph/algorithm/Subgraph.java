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
 * Created on 18 avr. 2005
 *
 */
package salvo.jesus.graph.algorithm;

import java.util.Iterator;
import java.util.List;

import salvo.jesus.graph.DirectedEdge;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.DirectedGraphImpl;
import salvo.jesus.graph.Graph;
import salvo.jesus.graph.GraphException;


/**
 * An algorithm class for computing subgraphs of a given graph.
 * 
 * @author nono
 * @version $Id$
 */
public class Subgraph {

    /* the directed graph this algorithm works on */
    private DirectedGraph digraph;

    public Subgraph(DirectedGraph g) {
        this.digraph = g;
    }

    /**
     * Compute the directed graph obtained by removing given vertex from
     * <code>digraph</code>.
     * 
     * @param V
     *            the vertex to remove.
     * @return a new DirectedGraph instance containing all vertices of original
     *         graph minus <code>v</code> and all edges minus edges going to
     *         or coming from <code>v</code>.
     */
    public DirectedGraph complement(Object v) {
        DirectedGraph dg = new DirectedGraphImpl();
        /* iterate over vertices */
        for (Iterator i = digraph.getAllVertices().iterator(); i.hasNext();) {
            Object vv =  i.next();
            if (vv.equals(v))
                continue;
            /* iterate over edges on vv */
            for (Iterator i1 = digraph.getEdges(vv).iterator(); i1.hasNext();) {
                DirectedEdge de = (DirectedEdge) i1.next();
                if (de.getOppositeVertex(vv).equals(v))
                    continue;
                try {
                    dg.addEdge(de);
                } catch (GraphException e) {
                    e.printStackTrace();
                }
            }
        }
        return dg;
    }

    /**
     * Computes the subgraph reachable from vertex <code>v</code>.
     * 
     * @param v
     *            a Object from underlying digraph.
     * @return a new DirectedGraph instance that is the reachable subgraph from
     *         v.
     */
    public DirectedGraph reachable(Object v) {
        DirectedGraph dg = new DirectedGraphImpl();
        List l = new BreadthFirstDirectedGraphTraversal(digraph).traverse(v);
        for (Iterator i = l.iterator(); i.hasNext();) {
            Object vv =  i.next();
            try {
                /* add vertex */
                dg.add(vv);
            } catch (GraphException e) {
                e.printStackTrace();
            }
            for (Iterator i2 = digraph.getOutgoingEdges(vv).iterator(); i2
                    .hasNext();) {
                DirectedEdge de = (DirectedEdge) i2.next();
                try {
                    dg.addEdge(de);
                } catch (GraphException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return dg;
    }

    /**
     * Compute cyclomatic complexity of reachable subgraph from v 
     * excluding edges going to v.
     * 
     * @param v
     * @return
     */
    public DirectedGraph reachableLoopFree(Object v) {
        DirectedGraph dg = new DirectedGraphImpl();
        List l = new BreadthFirstDirectedGraphTraversal(digraph).traverse(v);
        for (Iterator i = l.iterator(); i.hasNext();) {
            Object vv =  i.next();
            try {
                /* add vertex */
                dg.add(vv);
            } catch (GraphException e) {
                e.printStackTrace();
            }
            for (Iterator i2 = digraph.getOutgoingEdges(vv).iterator(); i2
                    .hasNext();) {
                DirectedEdge de = (DirectedEdge) i2.next();
                /* do not close loops to v*/
                if(de.getOppositeVertex(vv).equals(v))
                    continue;
                try {
                    dg.addEdge(de);
                } catch (GraphException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return dg;
    }
}
