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
 * Created on 6 mai 2005
 *
 */
package salvo.jesus.graph.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import salvo.jesus.graph.DirectedEdge;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.Edge;
import salvo.jesus.graph.Graph;


/**
 * @author nono
 * @version $Id$
 */
public class DirectedGraphDualMatrix extends DualMatrix {

    /**
     * @param g
     */
    public DirectedGraphDualMatrix(DirectedGraph g) {
        super(g);
    }
    
    
    /* (non-Javadoc)
     * @see salvo.jesus.graph.algorithm.DualMatrix#populateMatrix(double[][], salvo.jesus.graph.Graph)
     */
    protected void populateMatrix(double[][] matrix, Graph g) {
        DirectedGraph dg = (DirectedGraph)g;
        final List<Edge> l = new ArrayList<Edge>(g.getAllEdges());
        int i = 0;
        for(Iterator it = l.iterator();it.hasNext();) {
            DirectedEdge e = (DirectedEdge)it.next();
            /* find connected edges */
            Object v = e.getSink();
            List oe = dg.getOutgoingEdges(v);
            for(Iterator j = oe.iterator();j.hasNext();) {
                Edge ne = (Edge)j.next();
                int k = l.indexOf(ne);
                matrix[i][k] = 1;
            }
            i++;
        }
    }
}
