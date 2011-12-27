package salvo.jesus.graph;


/**
 * The factory for creating Vertices and Edges in a <tt>GraphImpl</tt> class.
 *
 * @author  Jesus M. Salvo jr.
 */

public class GraphImplFactory implements GraphFactory {

	private int count = 0;
	
    public GraphImplFactory() {}

    public Object createVertex() {
    	System.out.println("[GraphimplFactory]");
        return "New Object "+count++ ;
    }
    
   /*
	public Object createVertex(String name) {
		   return new VertexImpl(name);
	   }
	*/	   
    public Edge createEdge( Object v1, Object v2 ) {
        return new EdgeImpl( v1, v2 );
    }
	/* (non-Javadoc)
	 * @see salvo.jesus.graph.GraphFactory#createEdgeWith(salvo.jesus.graph.Vertex, salvo.jesus.graph.Vertex, java.lang.Object)
	 */
	public Edge createEdgeWith(Object v1, Object v2, Object data) {
		return new EdgeImpl(v1,v2,data);
	}

}
