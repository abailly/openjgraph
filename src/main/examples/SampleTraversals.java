package examples;

import salvo.jesus.graph.Graph;
import salvo.jesus.graph.GraphImpl;
import salvo.jesus.graph.NullVisitor;
import salvo.jesus.graph.Vertex;
import salvo.jesus.graph.VertexImpl;
import salvo.jesus.graph.algorithm.BreadthFirstTraversal;
import salvo.jesus.graph.algorithm.GraphTraversal;

/**
 * A sample application demonstrating traversal algorithms,
 * and switching between depth-first-search and breadth-first-search.
 *
 * @author  Jesus M. Salvo Jr.
 */

public class SampleTraversals {

  public static void main( String args[] ) throws Exception {

    Graph	graph;
    Vertex	v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13;

    graph = new GraphImpl();

    v1 = new VertexImpl( "A" );
    v2 = new VertexImpl( "B" );
    v3 = new VertexImpl( "C" );
    v4 = new VertexImpl( "D" );
    v5 = new VertexImpl( "E" );
    v6 = new VertexImpl( "F" );
    v7 = new VertexImpl( "G" );
    v8 = new VertexImpl( "H" );
    v9 = new VertexImpl( "I" );
    v10 = new VertexImpl( "J" );
    v11 = new VertexImpl( "K" );
    v12 = new VertexImpl( "L" );
    v13 = new VertexImpl( "M" );

    graph.add( v1 );
    graph.add( v2 );
    graph.add( v3 );
    graph.add( v4 );
    graph.add( v5 );
    graph.add( v6 );
    graph.add( v7 );
    graph.add( v8 );
    graph.add( v9 );
    graph.add( v10 );
    graph.add( v11 );
    graph.add( v12 );
    graph.add( v13 );

    graph.addEdge( v1, v2 );
    graph.addEdge( v1, v6 );
    graph.addEdge( v1, v7 );
    graph.addEdge( v2, v3 );
    graph.addEdge( v2, v4 );
    graph.addEdge( v2, v5 );
    graph.addEdge( v3, v5 );
    graph.addEdge( v4, v5 );
    graph.addEdge( v4, v6 );
    graph.addEdge( v6, v5 );
    graph.addEdge( v7, v5 );
    graph.addEdge( v7, v8 );
    graph.addEdge( v8, v9 );
    graph.addEdge( v9, v11 );
    graph.addEdge( v11, v10 );
    graph.addEdge( v10, v12 );
    graph.addEdge( v10, v13 );
    graph.addEdge( v7, v10 );
    graph.addEdge( v12, v13 );
    graph.addEdge( v12, v7 );
    graph.addEdge( v12, v5 );
    graph.addEdge( v12, v6 );

    System.out.println( "Graph:" );
    System.out.println( graph );
    System.out.println();

    // Perform a dfs
    System.out.println( "Depth First Traversal: " + graph.traverse( v1 ));

    // Perform a bfs
    GraphTraversal traversal = new BreadthFirstTraversal( graph );
    graph.setTraversal( traversal );
    System.out.println( "Breadth First Traversal: " + graph.traverse( v1 ));

    // Perform a bfs, stopping at a particular vertex
    System.out.println( "Breadth First Traversal, stopping at " + v10 + ": " + traversal.traverse( v1, new StopVisitor( v10 ) ));
  }

}

class StopVisitor extends NullVisitor {
  Vertex stopat;

  public StopVisitor( Vertex stopat ) {
    this.stopat = stopat;
  }

  public boolean visit( Vertex visit ) {
    if( this.stopat == visit )
      return false;
    else
      return true;
  }
}
