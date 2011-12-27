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
 * Created on 27 avr. 2005
 *
 */
package salvo.jesus.graph.algorithm;

import java.util.BitSet;
import java.util.List;

import salvo.jesus.graph.Graph;

/**
 * Base class for manipulating matrices based on graphs.
 * 
 * @author nono
 * @version $Id$
 */
public class GraphMatrix {

    private final Graph graph;

    protected double[][] matrix;

    private int dimension;
    
    public GraphMatrix(Graph graph) {
        this.graph = graph;
    }

    /**
     * @return
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Returns a copy of the actual matrix
     * 
     * @return Returns the matrix.
     */
    public double[][] getMatrix() {
        if (matrix == null)
            throw new IllegalStateException("Matrix has not been computed");
        int n = getDimension();
        double[][] m = new double[n][n];
        for (int i = 0; i < n; i++)
            System.arraycopy(matrix[i], 0, m[i], 0, n);
        return matrix;
    }

    protected void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * Output as a string the characteristic polynomial of this matrix.
     * 
     * @return
     */
    public String polynomial() {
        int n = getDimension();
        BitSet is = new BitSet(n);
        BitSet js = new BitSet(n);
        is.set(0, n);
        js.set(0, n);
        return polynomial(is, js).toString();
    }

    /*
     * compute determinant of matrix without given indices
     */
    private Polynomial polynomial(BitSet is, BitSet js) {
        Polynomial ret = new Polynomial(0);
        /* simple case */
        if (is.cardinality() == 2) {
            int a = is.nextSetBit(0);
            int b = is.nextSetBit(a + 1);
            int c = js.nextSetBit(0);
            int d = js.nextSetBit(c + 1);
            Polynomial ad = basePoly(a, c).fois(basePoly(b, d));
            Polynomial bc = basePoly(b, c).fois(basePoly(a, d));
            return ad.moins(bc);
        } else {
            for (int i = is.nextSetBit(0); i != -1; i = is.nextSetBit(i + 1)) {
                for (int j = js.nextSetBit(0); j != -1; j = js
                        .nextSetBit(j + 1)) {
                    is.clear(i);
                    js.clear(j);
                    String sgn = null;
                    if ((i + j) % 2 == 1)
                        ret = ret.moins(polynomial(is, js));
                    else
                        ret = ret.plus(polynomial(is, js));
                    is.set(i);
                    js.set(j);
                }

            }
        }
        return ret;
    }

    private Polynomial basePoly(int i, int j) {
        double c = matrix[i][j];
        if (c == 0 && i != j)
            return new Polynomial(0);
        else if (i == j) {
            return new Polynomial(new double[] { c, -1 });
        } else
            return new Polynomial(new double[] { c });
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        String nl = System.getProperty("line.separator");
        for (int i = 0; i < matrix.length; i++) {
            sb.append("[ ");
            for (int j = 0; j < matrix[i].length; j++) {
                if (Double.isInfinite(matrix[i][j]))
                    sb.append('#').append(' ');
                else if (Polynomial.estEntier(matrix[i][j])) {
                    sb.append((int) matrix[i][j]).append(' ');
                } else
                    sb.append(matrix[i][j]).append(' ');
            }
            sb.append(']').append(nl);
        }
        return sb.toString();
    }
    
    public static String toString(double[][] matrix) {
        StringBuffer sb = new StringBuffer();
        String nl = System.getProperty("line.separator");
        for (int i = 0; i < matrix.length; i++) {
            sb.append("[ ");
            for (int j = 0; j < matrix[i].length; j++) {
                if (Double.isInfinite(matrix[i][j]))
                    sb.append('#').append(' ');
                else if (Polynomial.estEntier(matrix[i][j])) {
                    sb.append((int) matrix[i][j]).append(' ');
                } else
                    sb.append(matrix[i][j]).append(' ');
            }
            sb.append(']').append(nl);
        }
        return sb.toString();        
    }
    /**
     * @return Returns the dimension.
     */
    public int getDimension() {
        return dimension;
    }
    /**
     * @param dimension The dimension to set.
     */
    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
