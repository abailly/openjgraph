package salvo.jesus.graph;

/**
 * Represents a directed edge in a graph.
 *
 * @author		Jesus M. Salvo Jr.
 */
public class DirectedEdgeImpl extends EdgeImpl implements DirectedEdge {

  /**
   * The direction of the edge. The implementation of the DirectedEdge
   * interface is delegated to this object.
   */
  DirectedEdgeWeakImpl    directionOfEdge;

  /**
    * Creates an DirectedEdgeImpl object whose origin and destination vertices
    * are specified by the method parameters.
    *
    * @see		Vertex
    */
  public DirectedEdgeImpl( Object sourceVertex, Object sinkObject ){
    super( sourceVertex, sinkObject ,sourceVertex.toString() + "->" + sinkObject.toString());
    directionOfEdge = new DirectedEdgeWeakImpl( sourceVertex, sinkObject );
  }

/**
 * s
 * @param sourceVertex
 * @param sinkVertex
 * @param data
 */
	public DirectedEdgeImpl(Object sourceVertex, Object sinkObject ,Object data){
	super( sourceVertex, sinkObject,data );
	directionOfEdge = new DirectedEdgeWeakImpl( sourceVertex, sinkObject );
  }
  
  /**
   * Returns the source Object of the edge.
   *
   * @return  The source Vertex.
   */
  public Object getSource() {
    return this.directionOfEdge.getSource();
  }

  /**
   * Returns the sink Object of the edge.
   *
   * @return  The sink Vertex.
   */
  public Object getSink() {
    return this.directionOfEdge.getSink();
  }

  /**
   * Returns the direction of the Edge
   *
   */
  public int getDirection() {
    return this.directionOfEdge.getDirection();
  }
  

  /**
    * Creates a clone of this Edge. This calls the Edge constructor,
    * thereby creating a new instance of Edge. However, the vertices
    * in both endpoints of the Edge are not cloned.
    *
    * @return  A clone of an instance of Edge. 	
    */
  protected Object clone(){
    return new DirectedEdgeImpl( vertexA, vertexB ,data);
  }

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		DirectedEdge de = (DirectedEdge)obj;
		return super.equals(obj);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return super.hashCode() ^ 0x87654321;
	}

}

