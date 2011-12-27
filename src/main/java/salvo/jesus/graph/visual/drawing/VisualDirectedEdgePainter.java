package salvo.jesus.graph.visual.drawing;

import java.awt.Graphics2D;

import salvo.jesus.graph.visual.VisualEdge;

/**
 * An interface for drawing a <tt>VisualEdge</tt> that encapsulates
 * a <tt>DirectedEdge</tt>. The contract is for this painter to draw
 * an arrowhead on the sink endpoint of the <tt>DirectedEdge</tt>.
 *
 * @author Jesus M. Salvo Jr.
 */

public interface VisualDirectedEdgePainter extends VisualEdgePainter {

  /**
   * Draws the arrowhead for the edge.
   */
  public void paintArrowHead( VisualEdge ve, Graphics2D g2d );

}