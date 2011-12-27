// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3)
// Source File Name: DirectedGraphAdjacencyMatrix.java

package salvo.jesus.graph.algorithm;

import java.util.Iterator;
import java.util.List;
import salvo.jesus.graph.*;

// Referenced classes of package salvo.jesus.graph.algorithm:
//            AdjacencyMatrix, BreadthFirstTraversal, GraphTraversal

public class DirectedGraphAdjacencyMatrix extends AdjacencyMatrix {

    public DirectedGraphAdjacencyMatrix(DirectedGraph dg) {
        super(dg);
    }

    protected void populateMatrix(final double matrix[][], Graph g) {
        final DirectedGraph dg = (DirectedGraph) g;
        final List l = g.getAllVertices();
        GraphTraversal gt = new BreadthFirstTraversal(dg);
        Visitor v = new Visitor() {

            public boolean visit(Object v) {
                int i = l.indexOf(v);
                for (Iterator it = dg.getOutgoingAdjacentVertices(v).iterator(); it
                        .hasNext();) {
                    int j = l.indexOf(it.next());
                    matrix[i][j] = 1.0D;
                }

                return true;
            }
        };
        List cs;
        for (Iterator it = dg.getConnectedSet().iterator(); it.hasNext(); gt
                .traverse( cs.get(0), v))
            cs = (List) it.next();
    }

}
