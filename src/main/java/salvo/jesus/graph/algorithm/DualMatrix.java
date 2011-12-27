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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import salvo.jesus.graph.Edge;
import salvo.jesus.graph.Graph;

import salvo.jesus.graph.Visitor;

/**
 * compute the dual matrix of a graph.
 * <p>
 * The dual matrix of a graph is a <code>e x e</code> square matrix <code>M</code> where 
 * e is the  number  of edges of the graph. <code>Mij</code> equals 1 if 
 * edge i and edge j have respectively end and start vertex in common. 
 *  
 * @author nono
 * @version $Id$
 */
public class DualMatrix extends GraphMatrix {

    public DualMatrix(Graph g) {
        super(g);
        setMatrix(getMatrix(g));
    }

    /**
     * Populate a matrix from a graph.
     * 
     * @param matrix
     *            the matrix where result is stored in.
     * @param g
     *            the graph to extract matrix from.
     */
    protected void populateMatrix(final double matrix[][], final Graph g) {
        final List<Edge> l = new ArrayList<Edge>(g.getAllEdges());
        int i = 0;
        for(Iterator<Edge> it = l.iterator();it.hasNext();) {
            Edge e = it.next();
            /* find connected edges */
            Object v = e.getVertexB();
            List oe = g.getEdges(v);
            for(Iterator j = oe.iterator();j.hasNext();) {
                Edge ne = (Edge)j.next();
                int k = l.indexOf(ne);
                matrix[i][k] = matrix[k][i] = 1;
            }
            i++;
        }
    }

    /**
     * Construct a new adjacency matrix from a graph.
     * 
     * @param g
     *            the graph.
     * @return an array of double objects
     */
    private double[][] getMatrix(Graph g) {
        List l = new ArrayList(g.getAllEdges());
        int n = l.size();
        double matrix[][] = new double[n][n];
        for (int j = 0; j < n; j++) {
            matrix[j][j] = 0.0D;
            if (j > 0)
                Arrays.fill(matrix[j], 0, j, 0);
            if (j < n - 1)
                Arrays.fill(matrix[j], j + 1, n,
                        0);
        }

        populateMatrix(matrix, g);
        return matrix;
    }
}
