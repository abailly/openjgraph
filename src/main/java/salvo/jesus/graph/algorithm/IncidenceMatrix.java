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
 * Created on 13 mai 2005
 *
 */
package salvo.jesus.graph.algorithm;

import java.util.Iterator;
import java.util.List;


import salvo.jesus.graph.DirectedEdge;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.Graph;


/**
 * Creates the incidence matrix of DirectedGraph.
 * <p>
 * The incidence matrix <code>I</code> of a directed graph <code>G=(S,A)</code>
 * is a <code>n x m </code> matrix M with <code>m = |A|</code>, <code>n = |S|</code>
 * with: 
 * <ul>
 * <li><code>M_ij = -1 </code> if i is a start vertex of j</li>  
 * <li><code>M_ij = 1 </code> if i is an end vertex of j</li>  
 * <li><code>M_ij = 0 </code> otherwise,</li> 
 * </ul> 
 * @author nono
 * @version $Id: IncidenceMatrix.java 1268 2006-08-14 13:25:12Z nono $
 */
public class IncidenceMatrix extends GraphMatrix {

    /**
     * @param graph
     */
    public IncidenceMatrix(DirectedGraph graph) {
        super(graph);
        setMatrix(makeMatrix(graph));
    }

    /**
     * @param graph
     * @return
     */
    private double[][] makeMatrix(DirectedGraph graph) {
        int n = graph.getVerticesCount();
        int m = graph.getEdgesCount();
        double[][] mat = new double[n][m];
        int i=0;
        List vs = graph.getAllVertices();
        for(Iterator it = graph.getAllEdges().iterator();it.hasNext();i++) {
            DirectedEdge de = (DirectedEdge)it.next();
            Object from = de.getSource();
            Object to = de.getSink();
            int j = vs.indexOf(from);
            int k = vs.indexOf(to);
            mat[j][i] = -1;
            mat[k][i] = 1;
        }
        return mat;
    }

    
}
