// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GraphOpsTest.java

package salvo.jesus.graph;

import java.util.*;
import junit.framework.TestCase;

// Referenced classes of package salvo.jesus.graph:
//            DirectedGraph, Vertex, DirectedWeightedEdge, GraphImpl, 
//            GraphOps, Graph

public class GraphOpsTest extends TestCase
{

    public GraphOpsTest(String name)
    {
        super(name);
    }

    public void testMakeDirectedGraph()
    {
        double m[][] = {
            {
                0.0D, 1.0D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D
            }, {
                1.7976931348623157E+308D, 0.0D, 1.0D, 1.7976931348623157E+308D, 1.0D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D
            }, {
                1.7976931348623157E+308D, 1.7976931348623157E+308D, 0.0D, 1.0D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D
            }, {
                1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 0.0D, 1.7976931348623157E+308D, 1.0D, 1.0D, 1.7976931348623157E+308D, 1.7976931348623157E+308D
            }, {
                1.0D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 0.0D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.0D, 1.7976931348623157E+308D
            }, {
                1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.0D, 1.7976931348623157E+308D, 1.0D, 0.0D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D
            }, {
                1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.0D, 0.0D, 1.7976931348623157E+308D, 1.0D
            }, {
                1.0D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 0.0D, 1.0D
            }, {
                1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.0D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 0.0D
            }
        };
        try
        {
            DirectedGraph dg = (DirectedGraph)GraphOps.createFrom(m);
            List l = dg.getAllVertices();
            Collections.sort(l, comp);
            Vertex f = (Vertex)l.get(5);
            Vertex d = (Vertex)l.get(3);
            Vertex c = (Vertex)l.get(2);
            assertTrue(dg.isConnected(f, c));
            assertTrue(dg.getEdge(f, d) == null);
            assertTrue(dg.isPath(f, d));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail();
        }
    }

    public void testWeightedDirectedGraph()
    {
        double m[][] = {
            {
                0.0D, 3D, 8D, 1.7976931348623157E+308D, -4D
            }, {
                1.7976931348623157E+308D, 0.0D, 1.7976931348623157E+308D, 1.0D, 7D
            }, {
                1.7976931348623157E+308D, 4D, 0.0D, 1.7976931348623157E+308D, 1.7976931348623157E+308D
            }, {
                2D, 1.7976931348623157E+308D, -5D, 0.0D, 1.7976931348623157E+308D
            }, {
                1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 6D, 0.0D
            }
        };
        try
        {
            DirectedGraph dg = (DirectedGraph)GraphOps.createFrom(m);
            List l = dg.getAllVertices();
            Collections.sort(l, comp);
            Vertex e = (Vertex)l.get(4);
            Vertex d = (Vertex)l.get(3);
            Vertex c = (Vertex)l.get(2);
            assertTrue(dg.isConnected(e, d));
            assertTrue(dg.isPath(e, c));
            assertTrue(dg.getEdge(e, c) == null);
            DirectedWeightedEdge dwe = (DirectedWeightedEdge)dg.getEdge(e, d);
            assertEquals(6D, dwe.getWeight(), 0.0D);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail();
        }
    }

    public void testSimpleGraph()
    {
        double m[][] = {
            {
                0.0D, 1.0D, 1.0D, 1.7976931348623157E+308D, 1.0D
            }, {
                1.0D, 0.0D, 1.7976931348623157E+308D, 1.0D, 1.0D
            }, {
                1.0D, 1.7976931348623157E+308D, 0.0D, 1.7976931348623157E+308D, 1.7976931348623157E+308D
            }, {
                1.7976931348623157E+308D, 1.0D, 1.7976931348623157E+308D, 1.0D, 1.7976931348623157E+308D
            }, {
                1.0D, 1.0D, 1.7976931348623157E+308D, 1.7976931348623157E+308D, 0.0D
            }
        };
        try
        {
            Graph dg = GraphOps.createFrom(m);
            List l = dg.getAllVertices();
            Collections.sort(l, comp);
            Vertex e = (Vertex)l.get(4);
            Vertex a = (Vertex)l.get(0);
            Vertex d = (Vertex)l.get(3);
            assertTrue(dg instanceof GraphImpl);
            assertTrue(dg.isConnected(e, a));
            assertTrue(dg.getAdjacentVertices(d).contains(d));
            assertTrue(!dg.getAdjacentVertices(e).contains(d));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail();
        }
    }

    private static final double inf = 1.7976931348623157E+308D;
    private static final Comparator comp = new Comparator() {

        public boolean equals(Object obj)
        {
            return false;
        }

        public int compare(Object o1, Object o2)
        {
            Vertex v1 = (Vertex)o1;
            Vertex v2 = (Vertex)o2;
            int i1 = Integer.parseInt((String)v1.getObject());
            int i2 = Integer.parseInt((String)v2.getObject());
            return i1 <= i2 ? i1 >= i2 ? 0 : -1 : 1;
        }

    };

}
