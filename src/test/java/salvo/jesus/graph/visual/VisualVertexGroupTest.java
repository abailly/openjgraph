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
 * Created on 12 avr. 2005
 *
 */
package salvo.jesus.graph.visual;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;

import salvo.jesus.graph.DirectedEdgeImpl;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.DirectedGraphImpl;
import salvo.jesus.graph.Edge;
import salvo.jesus.graph.GraphException;
import salvo.jesus.graph.Vertex;
import salvo.jesus.graph.VertexImpl;
import salvo.jesus.graph.visual.layout.SimulatedAnnealingLayout;
import salvo.jesus.graph.visual.print.VisualGraphImageOutput;
import junit.framework.TestCase;

/**
 * @author nono
 * @version $Id: VisualVertexGroupTest.java 1123 2005-11-07 20:54:22Z nono $
 */
public class VisualVertexGroupTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Constructor for VisualVertexGroupTest.
     * 
     * @param arg0
     */
    public VisualVertexGroupTest(String arg0) {
        super(arg0);
    }

    public void testSubgraph() throws GraphException, FileNotFoundException,
            IOException, InterruptedException {
        DirectedGraph g1 = new DirectedGraphImpl();
        VertexImpl v1;
        g1.add(v1 = new VertexImpl("a"));
        VertexImpl v2;
        g1.add(v2 = new VertexImpl("b"));
        final VertexImpl v3;
        g1.add(v3 = new VertexImpl("c"));
        g1.addEdge(new DirectedEdgeImpl(v1, v2));
        g1.addEdge(new DirectedEdgeImpl(v2, v3));
        g1.addEdge(new DirectedEdgeImpl(v3, v1));
        VertexImpl v4;
        g1.add(v4 = new VertexImpl("d"));
        VertexImpl v5;
        g1.add(v5 = new VertexImpl("e"));
        g1.addEdge(new DirectedEdgeImpl(v1, v4));
        g1.addEdge(new DirectedEdgeImpl(v4, v5));
        g1.addEdge(new DirectedEdgeImpl(v5, v4));

        final VisualGraph vg = new VisualGraph();
        vg.setGraph(g1);
        VertexGroup grp = new VertexGroup(new VertexImpl(""), vg);
        VisualVertex vv1 = vg.getVisualVertex(v4);
        VisualVertex vv2 = vg.getVisualVertex(v5);
        vv1.setLocation(10, 10);
        vv2.setLocation(10, 100);
        grp.add(vv1);
        grp.add(vv2);
        VertexGroup grp2 = new VertexGroup(new VertexImpl(""), vg);
        VisualVertex vv3 = vg.getVisualVertex(v1);
        VisualVertex vv4 = vg.getVisualVertex(v2);
        VisualVertex vv5 = vg.getVisualVertex(v3);
        vv3.setLocation(50, 10);
        vv4.setLocation(50, 100);
        vv5.setLocation(100, 50);
        grp2.add(vv3);
        grp2.add(vv4);
        grp2.add(vv5);
        final SimulatedAnnealingLayout lay = new SimulatedAnnealingLayout(vg,
                false);
        vg.setGraphLayoutManager(lay);
        lay.layout();
        VisualGraphImageOutput out = new VisualGraphImageOutput();
        out.output(vg, new FileOutputStream("test.eps"));
    }
}
