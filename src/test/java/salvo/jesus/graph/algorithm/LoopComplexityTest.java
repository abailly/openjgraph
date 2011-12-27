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
 * Created on 18 avr. 2005
 *
 */
package salvo.jesus.graph.algorithm;

import salvo.jesus.graph.DirectedEdgeImpl;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.DirectedGraphImpl;
import salvo.jesus.graph.GraphException;
import salvo.jesus.graph.Vertex;
import salvo.jesus.graph.VertexImpl;
import junit.framework.TestCase;

/**
 * Test for loop complexity.
 * All samples are taken from {@link  http://www.vuibert.com/livre1516.html}, p.172. 
 * 
 * @author nono
 * @version $Id$
 */
public class LoopComplexityTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Constructor for LoopComplexityTest.
     * @param arg0
     */
    public LoopComplexityTest(String arg0) {
        super(arg0);
    }

    public void test1() throws GraphException {
        DirectedGraph dg = new DirectedGraphImpl();
        Vertex v = new VertexImpl(); 
        dg.add(v);
        dg.addEdge(new DirectedEdgeImpl(v,v));
        LoopComplexity lc = new LoopComplexity(dg);
        assertEquals(1,lc.loopComplexity());
    }
    
    public void test2() throws GraphException {
        DirectedGraph dg = new DirectedGraphImpl();
        Vertex v = new VertexImpl(); 
        Vertex v1 = new VertexImpl(); 
        dg.add(v);
        dg.add(v1);
        dg.addEdge(new DirectedEdgeImpl(v,v));
        dg.addEdge(new DirectedEdgeImpl(v,v1));
        dg.addEdge(new DirectedEdgeImpl(v1,v));
        LoopComplexity lc = new LoopComplexity(dg);
        assertEquals(1,lc.loopComplexity());
        
    }

    public void test3() throws GraphException {
        DirectedGraph dg = new DirectedGraphImpl();
        Vertex v = new VertexImpl(); 
        Vertex v1 = new VertexImpl(); 
        Vertex v2 = new VertexImpl(); 
        dg.add(v);
        dg.add(v1);
        dg.add(v2);
        dg.addEdge(new DirectedEdgeImpl(v,v));
        dg.addEdge(new DirectedEdgeImpl(v2,v2));
        dg.addEdge(new DirectedEdgeImpl(v,v1));
        dg.addEdge(new DirectedEdgeImpl(v1,v2));
        LoopComplexity lc = new LoopComplexity(dg);
        assertEquals(1,lc.loopComplexity());
        
    }

    public void test4() throws GraphException {
        DirectedGraph dg = new DirectedGraphImpl();
        Vertex v = new VertexImpl(); 
        Vertex v1 = new VertexImpl(); 
        Vertex v2 = new VertexImpl(); 
        Vertex v3 = new VertexImpl(); 
        dg.add(v);
        dg.add(v1);
        dg.add(v2);
        dg.add(v3);
        dg.addEdge(new DirectedEdgeImpl(v,v1));
        dg.addEdge(new DirectedEdgeImpl(v,v2));
        dg.addEdge(new DirectedEdgeImpl(v2,v3));
        dg.addEdge(new DirectedEdgeImpl(v1,v3));
        dg.addEdge(new DirectedEdgeImpl(v3,v));
        LoopComplexity lc = new LoopComplexity(dg);
        assertEquals(1,lc.loopComplexity());
        
    }
    
    public void test5() throws GraphException {
        DirectedGraph dg = new DirectedGraphImpl();
        Vertex v = new VertexImpl(); 
        Vertex v1 = new VertexImpl(); 
        Vertex v2 = new VertexImpl(); 
        Vertex v3 = new VertexImpl(); 
        dg.add(v);
        dg.add(v1);
        dg.add(v2);
        dg.add(v3);
        dg.addEdge(new DirectedEdgeImpl(v,v1));
        dg.addEdge(new DirectedEdgeImpl(v1,v2));
        dg.addEdge(new DirectedEdgeImpl(v2,v3));
        dg.addEdge(new DirectedEdgeImpl(v3,v));
        dg.addEdge(new DirectedEdgeImpl(v,v3));
        dg.addEdge(new DirectedEdgeImpl(v3,v2));
        dg.addEdge(new DirectedEdgeImpl(v2,v1));
        dg.addEdge(new DirectedEdgeImpl(v1,v));
        LoopComplexity lc = new LoopComplexity(dg);
        assertEquals(2,lc.loopComplexity());
    }

    public void test6() throws GraphException {
        DirectedGraph dg = new DirectedGraphImpl();
        Vertex v = new VertexImpl(); 
        Vertex v1 = new VertexImpl(); 
        Vertex v2 = new VertexImpl(); 
        Vertex v3 = new VertexImpl(); 
        Vertex v4 = new VertexImpl(); 
        Vertex v5 = new VertexImpl(); 
        dg.add(v);
        dg.add(v1);
        dg.add(v2);
        dg.add(v3);
        dg.add(v4);
        dg.add(v5);
        dg.addEdge(new DirectedEdgeImpl(v,v1));
        dg.addEdge(new DirectedEdgeImpl(v1,v2));
        dg.addEdge(new DirectedEdgeImpl(v2,v3));
        dg.addEdge(new DirectedEdgeImpl(v3,v4));
        dg.addEdge(new DirectedEdgeImpl(v4,v5));
        dg.addEdge(new DirectedEdgeImpl(v5,v));
        dg.addEdge(new DirectedEdgeImpl(v,v5));
        dg.addEdge(new DirectedEdgeImpl(v5,v4));
        dg.addEdge(new DirectedEdgeImpl(v4,v3));
        dg.addEdge(new DirectedEdgeImpl(v3,v2));
        dg.addEdge(new DirectedEdgeImpl(v2,v1));
        dg.addEdge(new DirectedEdgeImpl(v1,v));
        LoopComplexity lc = new LoopComplexity(dg);
        assertEquals(3,lc.loopComplexity());
    }

}
