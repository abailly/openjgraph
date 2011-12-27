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

import java.util.Set;

import salvo.jesus.graph.DirectedGraph;


/**
 * This class computes the cyclomatic complexity for a directed graph 
 * and the local cyclomatic complexity for any of its vertices.
 * 
 * @author nono
 * @version $Id$
 */
public class CyclomaticComplexity {

    private DirectedGraph digraph;
    private Set sccs;

    public CyclomaticComplexity(DirectedGraph dg) {
        this.digraph = dg;
        this.sccs = new TarjanSCC(dg).SCC();
    }
    
    /**
     * Returns the local cyclomatic number of the underlying graph 
     * for given vertex.
     * The local cyclomatic number is defined as the cyclomatic number of 
     * the subgraph reachable from <code>v</code>.
     * @param v a Object from <code>digraph</code>.
     * @return local cyclomatic number for v.
     */
    public int cyclomaticNumber(Object v) {
        /* compute reachable graph */
        DirectedGraph dg = new Subgraph(digraph).reachable(v);
        return new CyclomaticComplexity(dg).cyclomaticNumber();
    }
    
    /**
     * Returns the cyclomatic number of the underlying graph.
     * This number is equal to the number of edges, minus the number of vertices, 
     * plus the number of strongly connected components of the graph.
     * 
     * @return cyclomatic number of <code>digraph</code>
     */
    public int cyclomaticNumber() {
        return digraph.getEdgesCount() - digraph.getVerticesCount() + sccs.size();
    }
}
