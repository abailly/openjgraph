package salvo.jesus.graph;

/**
 * The factory for creating Vertices and Edges in a <tt>DirectedGraphImpl</tt> class.
 *
 * @author  Jesus M. Salvo jr.
 */

public class DirectedGraphImplFactory implements GraphFactory {

  private int vcount = 0;

  public DirectedGraphImplFactory() {
  }

  public Edge createEdge(Object v1, Object v2) {
    return new DirectedEdgeImpl(v1, v2);
  }

  /* (non-Javadoc)
   * @see salvo.jesus.graph.GraphFactory#createEdgeWith(salvo.jesus.graph.Vertex, salvo.jesus.graph.Vertex, java.lang.Object)
   */
  public Edge createEdgeWith(Object v1, Object v2, Object data) {
    return new DirectedEdgeImpl(v1, v2, data);
  }

}
