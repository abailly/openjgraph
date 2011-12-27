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
import java.util.Set;

import salvo.jesus.graph.DirectedGraph;


/**
 * compute the loop complexity of a directed graph. This algorithm computes the
 * loop complexity of a directed graph. See
 * {@link http://www.vuibert.com/livre1516.html}.
 * 
 * @author nono
 * @version $Id$
 */
public class LoopComplexity {

    /* the directed graph */
    private DirectedGraph digraph;

    public LoopComplexity(DirectedGraph dg) {
        this.digraph = dg;
    }
    
    /**
     * Returns the loop complexity of a the strongly connected component
     * of <code>digraph</code>  containing the given vertex.
     * 
     * @param v vertex 
     * @return loop complexity of the SCC containing the given vertex 
     */
    public int loopComplexity(Object  v) {
        DirectedGraph dg = digraph;
        TarjanSCC subscc = new TarjanSCC(dg);
        Set sccs = subscc.SCC();
        Iterator it = sccs.iterator();
        while(it.hasNext()) {
            dg = (DirectedGraph)it.next();
            if(dg.getAllVertices().contains(v)) 
                break;
        }
        /* dg is the SCC containing v */
        if(dg.getAllVertices().size() == 1) {
            /* base case : scc contains only one vertex */
            if(dg.getOutgoingEdges(v).size() > 0) {
                /* this is a self loop hence lc = 1 */
                return 1;
            }else 
                return 0;
        }
        /* dg is a scc - analyses sub graphs */
        return loopComplexity(dg);
    }

    private int indent =0;
    
    /**
     * Returns the loop complexity of a subgraph of <code>digraph</code>. 
     * 
     * @param dg a subgraph of the associated graph of this algorithm.
     * @return an integer denoting loop complexity of the subgraph.
     */
    public int loopComplexity(DirectedGraph dg) {
        /* comput sccs of the subgraph */
        TarjanSCC subscc = new TarjanSCC(dg);
        Set subsccs = subscc.SCC();
        if(subsccs.isEmpty())
            /* empty graph */
            return 0;
        if(subsccs.size() == 1) {
            dg = (DirectedGraph)subsccs.iterator().next();
            /* special case for one vertex graphs */
            if(dg.getAllVertices().size() == 1) {
                 Object v = dg.getAllVertices().iterator().next();
                /* base case : scc contains only one vertex */
                if(dg.getOutgoingEdges(v).size() > 0) {
                    /* this is a self loop hence lc = 1 */
                    return 1;
                }else 
                    return 0;
            } 
            Subgraph sub = new Subgraph(dg);
            /* 
             * dg is strongly connected.
             * return min of loop complexity of all subgraphs 
             * of dg.
             */
            int min = Integer.MAX_VALUE;
            indent++;
            for(Iterator i = dg.getAllVertices().iterator();i.hasNext();) {
                /* compute subgraph */
                DirectedGraph sg = sub.complement(i.next());
                for(int j=0;j< indent;j++)
                    System.err.print(' ');
                System.err.println("computing lc of "+ sg.getAllVertices().size());
                /* compute lc */
                int lc = loopComplexity(sg);
                if(lc < min)
                    min = lc;
            }
            indent--;
            return 1+ min;
        } else {
            /* 
             * dg is not strongly connected. Find sccs
             * of dg and get maximum lc of these scc. 
             */
            int max = Integer.MIN_VALUE;
            for(Iterator i= subsccs.iterator();i.hasNext();) {
                int lc = loopComplexity((DirectedGraph)i.next());
                if(lc > max)
                    max = lc;
            }
            return max;
        }
    }

    /**
     * Returns the loop complexity of the associated digraph.
     * 
     * @return an integer denoting loop complexity of digraph.
     */
    public int loopComplexity() {
        indent = 0;
        return loopComplexity(digraph);
    }
}
