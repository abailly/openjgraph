package salvo.jesus.graph;


/**
 * An interface for edges in a Graph.
 *
 * @author		Jesus M. Salvo Jr.
 */
public interface Edge extends GraphComponent {
  /**
   * Returns the endpoint A of the edge.
   *
   * @return  Object  Endpoint A of the edge.
   */
  public Object getVertexA();

  /**
   * Returns the endpoint B of the edge.
   *
   * @return  Object  Endpoint B of the edge.
   */
  public Object getVertexB();

  /**
   * Returns the Object opposite to the specified Object in the edge.
   *
   * @return  Object  The Object object that is the opposite to the specifid
   *                  Vertex. If the specified Object is not an endpoint of the
   *                  edge, returns null.
   */
  public Object getOppositeVertex( Object v );

	/**
	 * Return data associated with this edge
	 * 
	 * @return
	 */
	public Object getData();
}

