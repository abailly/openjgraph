/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2004, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* --------------------
 * PerformanceDemo.java
 * --------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: PerformanceDemo.java 1123 2005-11-07 20:54:22Z nono $
 *
 * Changes
 * -------
 * 10-Aug-2003 : Initial revision (BN);
 *
 */
package salvo.jesus.graph;

import java.io.IOException;

import salvo.jesus.graph.algorithm.BreadthFirstTraversal;

/**
 * A simple demo to test memory and CPU consumption on a graph with 3 million
 * elements.
 * 
 * <p>
 * NOTE: To run this demo you may need to increase the JVM max mem size. In
 * Sun's JVM it is done using the "-Xmx" switch. Specify "-Xmx300M" to set it
 * to 300MB.
 * </p>
 * 
 * <p>
 * WARNING: Don't run this demo as-is on machines with less than 512MB memory.
 * Your machine will start paging severely. You need to first modify it to
 * have fewer graph elements. This is easily done by changing the loop
 * counters below.
 * </p>
 *
 * @author Barak Naveh
 *
 * @since Aug 10, 2003
 */
public final class PerformanceDemo {
    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     */
    public static void main( String[] args ) throws Exception {
        long time = System.currentTimeMillis(  );

        reportPerformanceFor( "starting at", time );

        Graph  g    = new DirectedGraphImpl(  );
        Vertex prev;
        Vertex curr;

        curr = prev = new VertexImpl(  );
        g.add( prev );

        int numVertices       = 10000;
        int numEdgesPerVertex = 200;
        int numElements       = numVertices * ( 1 + numEdgesPerVertex );

        System.out.println( "\n" + "allocating graph with " + numElements
            + " elements (may take a few tens of seconds)..." );

        for( int i = 0; i < numVertices; i++ ) {
            curr = new VertexImpl(  );
            g.add( curr );

            for( int j = 0; j < numEdgesPerVertex; j++ ) {
                g.addEdge( prev, curr );
            }

            prev = curr;
        }

        reportPerformanceFor( "graph allocation", time );

        time = System.currentTimeMillis(  );
        BreadthFirstTraversal tr = new BreadthFirstTraversal(g);
        tr.traverse((Vertex)g.getConnectedSet().get(0));

        reportPerformanceFor( "breadth traversal", time );

        time = System.currentTimeMillis(  );

        System.out.println( "\n"
            + "Paused: graph is still in memory (to check mem consumption)." );
        System.out.print( "press any key to free memory and finish..." );

        try {
            System.in.read(  );
        }
         catch( IOException e ) {
            e.printStackTrace(  );
        }

        System.out.println( "done." );
    }


    private static void reportPerformanceFor( String msg, long refTime ) {
        double time = ( System.currentTimeMillis(  ) - refTime ) / 1000.0;
        double mem = usedMemory(  ) / ( 1024.0 * 1024.0 );
        mem = Math.round( mem * 100 ) / 100.0;
        System.out.println( msg + " (" + time + " sec, " + mem + "MB)" );
    }


    private static long usedMemory(  ) {
        Runtime rt = Runtime.getRuntime(  );

        return rt.totalMemory(  ) - rt.freeMemory(  );
    }
}
