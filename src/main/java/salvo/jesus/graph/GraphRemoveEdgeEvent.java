package salvo.jesus.graph;

import java.util.EventObject;

/**
 * This event is used to notify interested parties that an Edge object
 * is about to be removed from a Graph object.
 *
 * @author		Jesus M. Salvo Jr.
 */
public class GraphRemoveEdgeEvent extends EventObject {
  /**
    * The Edge object is about to be removed from a Graph object
    */
  Edge	edge;

  /**
    * Creates a GraphRemoveEdgeEvent object indicating the source of the event
    * and the Edge that is about to be removed from a Graph object
    *
    * @param	source		source of the event. This is usually the Graph object
    * where the Edge object is to be removed.
    * @param	newedge		Edge object that is about to be removed from a Graph object
    */
  public GraphRemoveEdgeEvent( Object source, Edge edgetoremove ) {
    super( source );
    edge = edgetoremove;
  }

  /**
    * Returns the Edge object is about to be removed from a Graph object
    *
    * @return		The Edge object to be removed
    */
  public Edge getEdge( ) {
    return this.edge;
  }
}
