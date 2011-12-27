package salvo.jesus.graph;

/**
 * The factory for creating Vertices and Edges in a <tt>WeightedGraphImpl</tt> class.
 *
 * @author  Jesus M. Salvo jr.
 */

public class WeightedGraphImplFactory implements GraphFactory {

    public WeightedGraphImplFactory() {}

    public Object createVertex() {
    	System.out.println("[WeightedGraphImplFactory]");
        return "New Object ";
    }

			 
    public Edge createEdge( Object v1, Object v2 ) {
        return new WeightedEdgeImpl( v1, v2, 0 );
    }
    
	/* (non-Javadoc)
	 * @see salvo.jesus.graph.GraphFactory#createEdgeWith(salvo.jesus.graph.Vertex, salvo.jesus.graph.Vertex, java.lang.Object)
	 */
	public Edge createEdgeWith(Object v1, Object v2, Object data) {
		return new WeightedEdgeImpl(v1,v2,0,data);
	}

}
