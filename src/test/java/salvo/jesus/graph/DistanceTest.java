// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3)
// Source File Name: DistanceTest.java

package salvo.jesus.graph;

import java.io.PrintStream;
import java.util.List;
import junit.framework.TestCase;
import salvo.jesus.graph.*;
import salvo.jesus.graph.algorithm.AdjacencyMatrix;
import salvo.jesus.graph.algorithm.DirectedGraphAdjacencyMatrix;
import salvo.jesus.graph.algorithm.Distance;
import salvo.jesus.graph.algorithm.WeightedDirectedGraphAdjacencyMatrix;

// Referenced classes of package salvo.jesus.graph.algorithm:
//            WeightedDirectedGraphAdjacencyMatrix, Distance, DirectedGraphAdjacencyMatrix,
// AdjacencyMatrix

public class DistanceTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        dg = new DirectedGraphImpl();
        a = new VertexImpl("a");
        b = new VertexImpl("b");
        c = new VertexImpl("c");
        d = new VertexImpl("d");
        e = new VertexImpl("e");
        f = new VertexImpl("f");
        g = new VertexImpl("g");
        h = new VertexImpl("h");
        j = new VertexImpl("j");
        dg.add(a);
        dg.add(b);
        dg.add(c);
        dg.add(d);
        dg.add(e);
        dg.add(f);
        dg.add(g);
        dg.add(h);
        dg.add(j);
        dg.addEdge(a, b);
        dg.addEdge(b, c);
        dg.addEdge(b, e);
        dg.addEdge(c, d);
        dg.addEdge(d, g);
        dg.addEdge(d, f);
        dg.addEdge(e, h);
        dg.addEdge(e, a);
        dg.addEdge(f, e);
        dg.addEdge(f, c);
        dg.addEdge(g, f);
        dg.addEdge(g, j);
        dg.addEdge(h, a);
        dg.addEdge(h, j);
        dg.addEdge(j, f);
    }

    public DistanceTest(String name) {
        super(name);
    }

/*
 *     public void testD1() throws Exception {
        DirectedGraph dg = new DirectedGraphImpl();
        Vertex v1 = new VertexImpl("1");
        Vertex v2 = new VertexImpl("2");
        Vertex v3 = new VertexImpl("3");
        Vertex v4 = new VertexImpl("4");
        Vertex v5 = new VertexImpl("5");
        dg.add(v1);
        dg.add(v2);
        dg.add(v3);
        dg.add(v4);
        dg.add(v5);
        dg.addEdge(new DirectedWeightedEdgeImpl(v1, v3, 8D));
        dg.addEdge(new DirectedWeightedEdgeImpl(v1, v5, -4D));
        dg.addEdge(new DirectedWeightedEdgeImpl(v1, v2, 3D));
        dg.addEdge(new DirectedWeightedEdgeImpl(v2, v5, 7D));
        dg.addEdge(new DirectedWeightedEdgeImpl(v2, v4, 1.0D));
        dg.addEdge(new DirectedWeightedEdgeImpl(v3, v2, 4D));
        dg.addEdge(new DirectedWeightedEdgeImpl(v4, v3, -5D));
        dg.addEdge(new DirectedWeightedEdgeImpl(v4, v1, 2D));
        dg.addEdge(new DirectedWeightedEdgeImpl(v5, v4, 6D));
        AdjacencyMatrix im = new WeightedDirectedGraphAdjacencyMatrix(dg);
        System.out.println(im);
        Distance d = new Distance(im);
        double dist[][] = d.distances();
        System.err.println(d.toString());
        assertEquals(5D, dist[2][3], 0.0D);
    }
*/
    public void testD2() throws Exception {
        AdjacencyMatrix im = new DirectedGraphAdjacencyMatrix(dg);
        System.out.println(im);
        Distance ds = new Distance(im);
        double dist[][] = ds.distances();
        System.err.println(ds.toString());
        assertEquals(3D, dist[4][5], 0.0D);
    }

    public void testEccentricity() throws Exception {
        AdjacencyMatrix im = new DirectedGraphAdjacencyMatrix(dg);
        Distance ds = new Distance(im);
        assertEquals(5D, ds.eccentricity(c), 0.0D);
    }

    public void testDiameter() throws Exception {
        AdjacencyMatrix im = new DirectedGraphAdjacencyMatrix(dg);
        Distance ds = new Distance(im);
        assertEquals(5D, ds.diameter(), 0.0D);
    }

    public void testRadius() throws Exception {
        AdjacencyMatrix im = new DirectedGraphAdjacencyMatrix(dg);
        Distance ds = new Distance(im);
        assertEquals(3D, ds.radius(), 0.0D);
    }

    public void testBorder() throws Exception {
        AdjacencyMatrix im = new DirectedGraphAdjacencyMatrix(dg);
        Distance ds = new Distance(im);
        List l = ds.border();
        System.err.println(l);
        assertTrue(l.contains(h) && l.contains(c) && l.contains(e));
    }

    public void testCenter() throws Exception {
        AdjacencyMatrix im = new DirectedGraphAdjacencyMatrix(dg);
        Distance ds = new Distance(im);
        List l = ds.center();
        System.err.println(l);
        assertTrue(l.contains(b) && l.contains(d) && l.contains(f)
                && l.contains(g));
    }

    public void testDet() {
        
    }
    
    public String toString(double distances[][]) {
        StringBuffer sb = new StringBuffer("{");
        int vertices = distances.length;
        String nl = System.getProperty("line.separator");
        for (int i = 0; i < vertices; i++) {
            sb.append("{ ");
            for (int j = 0; j < vertices; j++) {
                sb.append(distances[i][j]);
                if (j < vertices - 1)
                    sb.append(',');
            }

            sb.append("}");
            if (i < vertices - 1)
                sb.append(',').append(nl);
        }

        sb.append("}");
        return sb.toString();
    }

    DirectedGraph dg;

    private Vertex a;

    private Vertex b;

    private Vertex c;

    private Vertex d;

    private Vertex e;

    private Vertex f;

    private Vertex g;

    private Vertex h;

    private Vertex j;
}