package salvo.jesus.graph;

import java.io.Serializable;
import java.util.EventListener;

/**
 * The listener interface for receiving notification when a Object is
 * is about to be removed from a Graph.
 *
 * Implementations of this interface should be registered with a Graph,
 * via the Graph's addGraphRemoveVertexListener(), to be notified
 * when a Object object is about to be removed from the Graph.
 *
 * @author		Jesus M. Salvo Jr.
 */

public interface GraphRemoveVertexListener extends EventListener, Serializable {

  /**
    * Once implementations of this interface are registered with a Graph,
    * this method is automatically called whenever a Object is about to be
    * removed from the Graph object encapsulated by the VisualGraph object.
    *
    * @param	e		GraphRemoveVertexEvent object that also specifies the Vertex
    * that is about to be removed from the graph
    */
  public void vertexRemoved( GraphRemoveVertexEvent e );
}
