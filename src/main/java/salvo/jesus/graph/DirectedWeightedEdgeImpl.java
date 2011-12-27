package salvo.jesus.graph;

/**
 * Represents a directed and weighted edge in a graph.
 *
 * @author		Jesus M. Salvo Jr.
 */
public class DirectedWeightedEdgeImpl extends EdgeImpl implements DirectedWeightedEdge {
  /**
   * The direction of the edge. The implementation of the DirectedEdge
   * interface is delegated to this object.
   */
  DirectedEdgeWeakImpl    directionOfEdge;
  
  /**
   * The weight of the edge. The implementation of the WeightedEdge interface
   * is delegated to this object.
   */
  WeightedEdgeWeakImpl    weightOfEdge;

  /**
   * Creates a DirectedWeightedEdgeImpl object whose source and sink vertices and
   * weight are specified by the parameters.
   */
  public DirectedWeightedEdgeImpl( Object sourceVertex, Object sinkVertex, double weight ) {
    // Create an EdgeImpl object.
    super( sourceVertex, sinkVertex );
    // Create delegation object for direction of Edge
    directionOfEdge = new DirectedEdgeWeakImpl( sourceVertex, sinkVertex );
    // Create delegation object for weight of Edge
    weightOfEdge = new WeightedEdgeWeakImpl( weight );
  }
  
  public DirectedWeightedEdgeImpl( Object sourceVertex, Object sinkVertex, double weight ,Object data) {
  // Create an EdgeImpl object.
  super( sourceVertex, sinkVertex ,data);
  // Create delegation object for direction of Edge
  directionOfEdge = new DirectedEdgeWeakImpl( sourceVertex, sinkVertex );
  // Create delegation object for weight of Edge
  weightOfEdge = new WeightedEdgeWeakImpl( weight );
}

  /**
    * Returns a String representation of the DirectedWeightedEdge.
    *
    * @return	The String representation of the Edge
    * @see		Vertex
    */
  public String toString(){
    return data.toString() + " (" + this.weightOfEdge.getWeight() +")";
  }

  // ==========================================
  // Implementation of the WeightEdge interface
  // ==========================================

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
  
  // ==========================================
  // Implementation of the DirectedEdge interface
  // ==========================================
  
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
}
