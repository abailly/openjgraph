package salvo.jesus.graph;

import java.io.Serializable;
import java.util.EventListener;

/**
 * The listener interface for receiving notification when a Object is
 * is added to a Graph.
 *
 * Implementations of this interface should be registered with a Graph,
 * via the Graph's addGraphAddVertexListener(), to be notified
 * when a Object is added to the Graph.
 *
 * @author		Jesus M. Salvo Jr.
 */

public interface GraphAddVertexListener extends EventListener, Serializable {

  /**
    * Once implementations of this interface are registered with a Graph,
    * this method is automatically called whenever a Object is added to the
    * Graph object.
    *
    * @param	e		GraphAddVertexEvent object that also specifies the Object that was added
    * to the graph
    */
  public void vertexAdded( GraphAddVertexEvent e );
}
