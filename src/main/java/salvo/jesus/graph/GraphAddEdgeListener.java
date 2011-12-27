package salvo.jesus.graph;

import java.io.Serializable;
import java.util.EventListener;

/**
 * The listener interface for receiving notification when an Edge is
 * is added to a Graph.
 *
 * Implementations of this interface should be registered with a Graph,
 * via the Graph's addGraphAddEdgeListener(), to be notified
 * when an Edge is added to the Graph.
 *
 * @author		Jesus M. Salvo Jr.
 */

public interface GraphAddEdgeListener extends EventListener, Serializable {

  /**
    * Once implementations of this interface are registered with a Graph,
    * this method is automatically called whenever an Edge is added to the
    * Graph object.
    *
    * @param	e		GraphAddEdgeEvent object that also specifies the Edge that was added
    * to the graph
    */
  public void edgeAdded( GraphAddEdgeEvent e );
}

