package salvo.jesus.graph;

/**
 * Represents a weighted edge in a graph.
 *
 * @author		Jesus M. Salvo Jr.
 */
public class WeightedEdgeImpl extends EdgeImpl implements WeightedEdge{
  /**
   * The weight of the edge. The implementation of the WeightedEdge interface
   * is delegated to this object.
   */
  WeightedEdgeWeakImpl  weightOfEdge;

  /**
    * Creates an WeightedEdgeImpl object.
    *
    * @see		Vertex
    */
  public WeightedEdgeImpl( Object a, Object b, double weight ) {
    super( a, b );
    this.weightOfEdge = new WeightedEdgeWeakImpl( weight );
  }

	public WeightedEdgeImpl( Object a, Object b, double weight ,Object data) {
	super( a, b ,data);
	this.weightOfEdge = new WeightedEdgeWeakImpl( weight );
  }
  
  /**
   * Returns the weight of the edge.
   */
  public double getWeight() {
    return this.weightOfEdge.getWeight();
  }

  /**
   * Sets the weight of the edge.
   *
   * @param   weight    The new weight of the edge
   */
  public void setWeight( double weight ) {
    this.weightOfEdge.setWeight( weight );
  }

  /**
    * Returns a String representation of the WeightedEdge.
    *
    * @return	The String representation of the Edge
    * @see		Vertex
    */
  public String toString(){
    return data.toString()+ " ( " + this.weightOfEdge.getWeight() +" )";
  }
}