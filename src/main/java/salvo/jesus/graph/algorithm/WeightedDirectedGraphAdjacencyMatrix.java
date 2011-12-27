// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WeightedDirectedGraphAdjacencyMatrix.java

package salvo.jesus.graph.algorithm;

import java.util.Iterator;
import java.util.List;
import salvo.jesus.graph.*;

// Referenced classes of package salvo.jesus.graph.algorithm:
//            AdjacencyMatrix, DepthFirstDirectedGraphTraversal, GraphTraversal

public class WeightedDirectedGraphAdjacencyMatrix extends AdjacencyMatrix
{

    public WeightedDirectedGraphAdjacencyMatrix(WeightedGraph wg)
    {
        super(wg);
    }

    protected void populateMatrix(final double matrix[][], Graph g)
    {
        final DirectedGraph dg = (DirectedGraph)g;
        final List l = g.getAllVertices();
        GraphTraversal gt = new DepthFirstDirectedGraphTraversal(dg);
        Visitor v = new Visitor() {

            public boolean visit(Object v)
            {
                int i = l.indexOf(v);
                for(Iterator it = dg.getOutgoingEdges(v).iterator(); it.hasNext();)
                {
                    DirectedWeightedEdge dwe = (DirectedWeightedEdge)it.next();
                    int j = l.indexOf(dwe.getVertexB());
                    matrix[i][j] = dwe.getWeight();
                }

                return true;
            }

        };
        List cs;
        for(Iterator it = dg.getConnectedSet().iterator(); it.hasNext(); gt.traverse(cs.get(0), v))
            cs = (List)it.next();

    }
}
