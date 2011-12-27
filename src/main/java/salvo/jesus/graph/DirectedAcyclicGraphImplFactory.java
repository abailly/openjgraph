package salvo.jesus.graph;

/**
 * The factory for creating Vertices and Edges in a <tt>GraphImpl</tt> class.
 *
 * @author  Jesus M. Salvo jr.
 */

public class DirectedAcyclicGraphImplFactory implements GraphFactory {

    public DirectedAcyclicGraphImplFactory() {}

    public Object createVertex() {
      System.out.println("[DirectedAcyclicGraphImplFactory]");
      return new String("New Object");
    }

			 
//			 
//	public Object createVertex(Object[] settings){
//			 Object vertex = new VertexImpl("New Vertex");
//			 vertex.setObject(settings);
//			 return vertex;
//		 }

    public Edge createEdge( Object v1, Object v2 ) {
        return new DirectedEdgeImpl( v1, v2 );
    }
	/* (non-Javadoc)
	 * @see salvo.jesus.graph.GraphFactory#createEdgeWith(salvo.jesus.graph.Vertex, salvo.jesus.graph.Vertex, java.lang.Object)
	 */
	public Edge createEdgeWith(Object v1, Object v2, Object data) {
		return new DirectedEdgeImpl(v1,v2,data);
	}

}
