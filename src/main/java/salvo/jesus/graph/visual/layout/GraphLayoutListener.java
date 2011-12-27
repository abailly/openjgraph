package salvo.jesus.graph.visual.layout;

import java.util.EventListener;

/**
 * The listener interface for receiving notification of graph layout events.
 * These events are usually those of a vertex being laid out on a graph,
 * either intermediately or for its final position.
 *
 * @author		Jesus M. Salvo Jr.
 */
public interface GraphLayoutListener extends EventListener {

  /**
   * This method is automatically called whenever a vertex has been laid out
   * on a graph, either its intermediate or final position.
   *
   * @param   e   A GraphLayoutEvent object that indicates what VisualVertex was laid out.
   */
  public void layoutVisualVertex( GraphLayoutEvent e );
}
