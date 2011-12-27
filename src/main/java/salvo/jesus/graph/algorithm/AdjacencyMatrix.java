package salvo.jesus.graph.algorithm;

import java.util.*;
import salvo.jesus.graph.*;

/**
 * Representation of a Graph as an adjacency matrix.
 * <p>
 * An adjacency matrix for a graph G is a
 * <code>n x n<code> matrix <code>M</code> where 
 * <code>n</code> is the number of vertices in the graph, such that 
 * <ul>
 * <li><code>M_ij = 1</code> if there exists an arc betwee vertices i and j </li>
 * <li><code>M_ij = 0</code> otherwise.</li>
 * </ul>
 * This class initially creates the array of doubles to store the 
 * matrix, fill it with zeros values then calls protected {@see #populateMatrix(double[][],salvo.jesus.graph.Graph)}
 * to allow customization by subclasses of the values stored in it.
 * Default implementation of populate matrix stores symetric values.
 * <p> 
 * This implementation can computes the transitive closure of the 
 * Matrix using Ffloyd-Warshall algorithm.
 * <p>
 * Subclasses of adjacency matrix gives particular values
 * @author nono
 * @version $Id$
 */
public class AdjacencyMatrix extends GraphMatrix {

    public AdjacencyMatrix(Graph graph) {
        super(graph);
        setMatrix(getMatrix(graph));
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
        final List l = g.getAllVertices();
        GraphTraversal gt = new BreadthFirstTraversal(g);
        Visitor v = new Visitor() {

            public boolean visit(Object v) {
                int i = l.indexOf(v);
                for (Iterator it = g.getAdjacentVertices(v).iterator(); it
                        .hasNext();) {
                    int j = l.indexOf(it.next());
                    matrix[i][j] = 1.0;
                    matrix[j][i] = 1.0;
                }

                return true;
            }

        };
        List cs;
        for (Iterator it = g.getConnectedSet().iterator(); it.hasNext(); gt
                .traverse( cs.get(0), v))
            cs = (List) it.next();

    }

    /**
     * Construct a new adjacency matrix from a graph.
     * 
     * @param g
     *            the graph.
     * @return an array of double objects
     */
    private double[][] getMatrix(Graph g) {
        int vertices = g.getVerticesCount();
        double matrix[][] = new double[vertices][vertices];
        for (int j = 0; j < vertices; j++) {
            Arrays.fill(matrix[j], 0, vertices, 0);

        }

        populateMatrix(matrix, g);
        return matrix;
    }

    /**
     * Returns the transitive closure of this matrix's graph .
     * 
     * @return a double matrix where a 1 on a cell ij indicate a path from i to
     *         j
     */
    public double[][] transitiveClosure() {
        double[][] from = getMatrix();
        int n = from.length;
        double[][] to = new double[n][n];
        for (int i = 0; i < n; i++)
            from[i][i] = 1;
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    to[i][j] = (from[i][j] == 1 || (from[i][k] == 1 && from[k][j] == 1)) ? 1
                            : 0;
            double[][] tmp = from;
            from = to;
            to = tmp;
        }
        return from;
    }

}