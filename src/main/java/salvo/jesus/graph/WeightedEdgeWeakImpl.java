package salvo.jesus.graph;

/**
 * A weak implementation a weighted edge in a graph. This is only used internally
 * to implement the weight of an edge without having an actual edge.
 *
 * @author		Jesus M. Salvo Jr.
 */
class WeightedEdgeWeakImpl implements WeightedEdge {

  /**
   * The weight of the edge
   */
  double  weight;

  /**
    * Creates an WeightedEdgeWeakImpl object. Note that constructor
    * does not have vertices as parameters, since the sole purpose
    * of this class is to implement weight without having an actual edge.
    *
    * @see		Vertex
    */
  public WeightedEdgeWeakImpl( double weight ) {
    this.weight = weight;
  }

  /**
   * Sets the weight
   */
  public void setWeight( double weight ) {
    this.weight = weight;
  }

  /**
   * Returns the weight
   */
  public double getWeight() {
    return this.weight;
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
	public Object getData() {return null;}

}