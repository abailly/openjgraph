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
 * Created on 31 mai 2005
 *
 */
package salvo.jesus.graph.visual.layout;

import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.DirectedGraphImpl;
import salvo.jesus.graph.Vertex;
import salvo.jesus.graph.VertexImpl;
import salvo.jesus.graph.visual.VisualGraph;
import junit.framework.TestCase;

/**
 * @author nono
 * @version $Id: DigraphLayeredLayoutTest.java 1123 2005-11-07 20:54:22Z nono $
 */
public class DigraphLayeredLayoutTest extends TestCase {

    private DirectedGraphImpl dg;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        dg = new DirectedGraphImpl();
        Vertex v1 = new VertexImpl("0");
        Vertex v2 = new VertexImpl("1");
        dg.add(v1);
        dg.add(v2);
        dg.addEdge(v1,v1);
        dg.addEdge(v1,v2);
    }

    public void testDGlayout() {
        VisualGraph vg = new VisualGraph(dg);
        DigraphLayeredLayout layout = new DigraphLayeredLayout(vg);
        layout.layout();
    }
}
