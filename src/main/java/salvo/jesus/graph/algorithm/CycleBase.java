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
 * Created on 15 avr. 2005
 *
 */
package salvo.jesus.graph.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import salvo.jesus.graph.DirectedEdge;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.DirectedGraphImpl;
import salvo.jesus.graph.GraphException;


/**
 * Compute the cycle/cocycle bases of a given directed graph. 
 * <p />
 * The cycle base of a  * directed graph is a set of elementary cycles of a the graph such that every
 * other elementary cycle of the graph is a linear combination of cycles from
 * the base. The "vectors" are made from each edge of the graph.
 * <p />
 * The cocycle or cuts of a directed graph is a set of elementary paths of the graph such
 * that any two strongly connected component is separated by a linear combination
 * of cocycles.
 * <p /> 
 * The algorithm works as follows :
 * <ul>
 * <li>Compute a tree cover for all vertices in the graph, which is 
 * a subset of graph' edges ;</li>
 * <li>Any edge that is not in the tree cover makes a possible 
 * cycle in the graph: 
 * <ol>
 * <li>A cycle base is made from the minimal cycle this edge creates 
 * by adding it to edges in the tree,
 * </li>
 * <li>A cocycle base is made from 
 * </li>
 * </ol></li>
 * </ul>
 * 
 * @author nono
 * @version $Id$
 * @see <a href="">Graph Theory</a>, V. Diestel, Springer-Verlag
 */
public class CycleBase {

    private DirectedGraph digraph;

    /* cycles base */
    private Set cycles = new HashSet();

    private Comparator edgecomp = new Comparator() {
        public boolean equals(Object obj) {
            return false;
        }

        public int compare(Object o1, Object o2) {
            DirectedEdge de1 = (DirectedEdge) o1;
            DirectedEdge de2 = (DirectedEdge) o2;
            boolean d1d2 = de1.getSink().equals(de2.getSource());
            boolean d2d1 = de1.getSource().equals(de2.getSink());
            if (d1d2 && !d2d1)
                return -1;
            if (!d1d2 && d2d1)
                return 1;
            return 0;
        }
    };

    public CycleBase(DirectedGraph digraph) {
        this.digraph = digraph;
        /* compute bases */
        base();
    }

    /**
     * Computes cycle and cocycle bases of the graph given. Note that several bases may
     * exists so this algorithm may not always produce the same base for
     * isomorph graphs.
     * 
     */
    public Set base() {
        List edges = new ArrayList(digraph.getAllEdges());
        List /* < Edge > */base = new ArrayList();
        Set todo;
        int ne = digraph.getEdgesCount();
        /* traverse the graph */
        if (digraph.getConnectedSet().size() == 0
                || digraph.getConnectedSet().size() > 1)
            return null;
        List con = (List) digraph.getConnectedSet().get(0);
        if (con.isEmpty())
            return null;
        /* start at any vertex */
        Object v =  con.get(0);
        Object next;
        Stack stack = new Stack();
        Set visited = new HashSet();
        /* get a vertex coverage */
        // Push the starting vertex onto the stack
        stack.push(v);
        do {
            // Get the next vertex in the queue and add it to the visited
            next =  stack.pop();
            visited.add(next);

            // Get all of its adjacent vertices and push them onto the stack
            // only if it has not been visited and it has not been stacked
            List adj = digraph.getEdges(next);
            Iterator iterator = adj.iterator();
            while (iterator.hasNext()) {
                DirectedEdge e = (DirectedEdge) iterator.next();
                Object opp = e.getOppositeVertex(next);
                if (!visited.contains(opp) && !stack.contains(opp)) {
                    stack.push(opp);
                    /* add to base */
                    base.add(e);
                    /* remove from remaining edges */
                    edges.remove(e);
                }
            }

        } while (!stack.isEmpty());
        /* sort base */
        Collections.sort(base, edgecomp);
        List redges = new ArrayList(edges);
        /* base contains a list of edges that covers all vertices in digraph */
        /* compute cycles base by adding remaining edges one at a time and computing 
         * resulting cycle
         */
        for (Iterator i = redges.iterator(); i.hasNext();) {
            DirectedGraph dg = new DirectedGraphImpl();
            DirectedEdge de = (DirectedEdge) i.next();
            try {
                dg.addEdge(de);
            } catch (GraphException e) {
                e.printStackTrace();
            }
            /*
             * find edges in base that form a cycle and such that start and end
             * vertex of de are in the cycle
             */
            Object src = de.getSource();
            Object cur = de.getSink();
            for (Iterator i2 = base.iterator(); i2.hasNext();) {
                DirectedEdge bs = (DirectedEdge) i2.next();
                if (bs.getSource().equals(cur)) {
                    try {
                        dg.addEdge(bs);
                    } catch (GraphException e1) {
                        e1.printStackTrace();
                    }
                    cur = bs.getSink();
                    if (bs.getSink().equals(src))
                        break;
                }
            }
            i.remove();
            cycles.add(dg);
        }
        /* 
         * compute cocyles bases by removing one edge from base and
         * computing edge in edges that cross the two components induced 
         * by removing the base
         */
        for (Iterator i = base.iterator(); i.hasNext();) {
            
        }
        /* there may be remaining edges in base */
//        while (!todo.isEmpty()) {
//            DirectedGraph dg = new DirectedGraphImpl();
//            Object cur = null;
//            for (Iterator i = todo.iterator(); i.hasNext();) {
//                DirectedEdge de = (DirectedEdge)i.next();
//                if(cur == null || de.getSource().equals(cur)) {
//                    cur = de.getSink();
//                    try {
//                        dg.addEdge(de);
//                    } catch (GraphException e) {
//                        e.printStackTrace();
//                    }
//                    i.remove();
//                } 
//            }
//            cycles.add(dg);
//        }
        return null;
    }

}
