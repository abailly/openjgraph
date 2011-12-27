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

import salvo.jesus.graph.DirectedEdgeImpl;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.DirectedGraphImpl;
import salvo.jesus.graph.Vertex;
import salvo.jesus.graph.VertexImpl;
import junit.framework.TestCase;

/**
 * @author nono
 * @version $Id: MatrixTest.java 1123 2005-11-07 20:54:22Z nono $
 */
public class MatrixTest extends TestCase {

    private DirectedGraphImpl dg;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        dg = new DirectedGraphImpl();
        Vertex v1 = new VertexImpl("1");
        Vertex v2 = new VertexImpl("2");
        Vertex v3 = new VertexImpl("3");
        Vertex v4 = new VertexImpl("4");
        dg.add(v1);
        dg.add(v2);
        dg.add(v3);
        dg.add(v4);
        dg.addEdge(new DirectedEdgeImpl(v1, v2));
        dg.addEdge(new DirectedEdgeImpl(v2, v3));
        dg.addEdge(new DirectedEdgeImpl(v3, v2));
        dg.addEdge(new DirectedEdgeImpl(v3, v4));
        dg.addEdge(new DirectedEdgeImpl(v4, v1));
    }

    /**
     * Constructor for MatrixTest.
     * @param arg0
     */
    public MatrixTest(String arg0) {
        super(arg0);
    }

    public void testBase() {
        DirectedGraphAdjacencyMatrix m = new DirectedGraphAdjacencyMatrix(dg);
        System.err.println(m);
        System.err.println(GraphMatrix.toString(m.transitiveClosure()));
    }
    
    
}
