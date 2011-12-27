package salvo.jesus.graph.visual.layout;

import java.util.EventObject;

import salvo.jesus.graph.visual.VisualGraph;
import salvo.jesus.graph.visual.VisualVertex;

/**
 * This is a general purpose graph layout event that is used to notify
 * interested parties that a vertex has just been laid out in the graph.
 *
 * This event is useful for drwaing intermediate layouts of the graph,
 * allowing the user to see the graph from its original unordered layout
 * all the way to its final ordered layout.
 */
public class GraphLayoutEvent extends EventObject {

  /**
   * The VisualVertex object that has just been laid out.
   */
  VisualVertex  vvertex;

  /**
   * Creates a GraphLayoutEvent object indicating the VisualVertex object
   * that has been laid out and the VisualGraph where the VisualVertex object
   * belongs.
   *
   * @param   vgraph    The source of the event. The method getSource() will
   * return an object of type VisualGraph. This also indicates the
   * VisualGraph object where the VisualVertex that has been laid out belongs.
   * @param   vvertex   The VisualVertex object that has been laid out.
   */
  public GraphLayoutEvent( VisualGraph vgraph, VisualVertex vvertex ) {
    super( vgraph );
    this.vvertex = vvertex;
  }

  /**
   * Convenience method that is similar to getSource() but returns an explicit
   * VisualGraph object.
   *
   * @return  The VisualGraph object to which the VisualVertex that has been laid out
   * belongs. This is also the object returned by the getSource() method.
   */
  public VisualGraph getVisualGraph() {
    return ((VisualGraph) source);
  }

  /**
   * Returns the VisualVertex object that has been laid out.
   *
   * @return  The VisualVertex object that has been laid out.
   */
  public VisualVertex getVisualVertex() {
    return this.vvertex;
  }
}