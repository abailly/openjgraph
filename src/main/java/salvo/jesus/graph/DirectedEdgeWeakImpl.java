package salvo.jesus.graph;

/**
 * A weak implementation a directed edge in a graph. This is only used internally
 * to implement the direction of an edge without having an actual edge.
 *
 * @author		Jesus M. Salvo Jr.
 */
class DirectedEdgeWeakImpl implements DirectedEdge {

  /**
   * The source Object of the Edge
   */
  private Object  sourceVertex;

  /**
   * The sink Object of the Edge
   */
  private Object  sinkVertex;

  /**
   * The direction of the edge.
   */
  int direction;

  /**
    * Creates an DirectedEdgeWeakImpl object whose origin and destination vertices
    * are specified by the method parameters.
    *
    * @see		Vertex
    */
  public DirectedEdgeWeakImpl( Object sourceVertex, Object sinkVertex){
    this.sourceVertex = sourceVertex;
    this.sinkVertex = sinkVertex;
    this.direction = DirectedEdge.DIRECTION_A_TO_B;
  }

  /**
   * Returns the source Object of the edge.
   *
   * @return  The source Vertex.
   */
  public Object getSource() {
    return this.sourceVertex;
  }

  /**
   * Returns the sink Object of the edge.
   *
   * @return  The sink Vertex.
   */
  public Object getSink() {
    return this.sinkVertex;
  }

  /**
   * Returns the direction of the Edge
   *
   */
  public int getDirection() {
    return this.direction;
  }

  /**
   * Empty method implemetation that returns null. This method should never
   * be called or delegated to for whatever reason.
   */
  public Object getVertexA() { return null; }

  /**
   * Empty method implemetation that returns null. This method should never
   * be called or delegated to for whatever reason.
   */
  public Object getVertexB() { return null; }

  /**
   * Empty method implemetation does noething. This method should never
   * be called or delegated to for whatever reason.
   */
  public void setObject( Object obj ) {}

  /**
   * Empty method implemetation that returns null. This method should never
   * be called or delegated to for whatever reason.
   */
  public Object getObject() { return null; }

  /**
   * Empty method implemetation that returns null. This method should never
   * be called or delegated to for whatever reason.
   */
  public Object getOppositeVertex( Object vertex ) { return null; }

  /**
   * Empty method implemetation that does nothing.
   */
  public void setString( String text ) {}
	/* (non-Javadoc)
	 * @see salvo.jesus.graph.Edge#getData()
	 */
	public Object getData() {		return null;
	}

}








