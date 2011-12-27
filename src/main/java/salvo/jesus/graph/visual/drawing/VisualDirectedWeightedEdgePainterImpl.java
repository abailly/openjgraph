package salvo.jesus.graph.visual.drawing;

import java.awt.Graphics2D;

import salvo.jesus.graph.visual.VisualEdge;
import salvo.jesus.graph.visual.VisualGraphComponent;

/**
 * An implementation of <tt>VisualDirectedWeightedEdgePainter</tt> interface.
 * This painter paints a <tt>VisualEdge</tt> that encapsulates a
 * <tt>DirectedWeightedEdge<tt>, that is, an <tt>Edge</tt> that is both
 * unidirectional and weighted.
 *
 * @author  Jesus M. Salvo Jr.
 */

public class VisualDirectedWeightedEdgePainterImpl extends VisualEdgePainterImpl
        implements VisualDirectedWeightedEdgePainter
{
    /**
     * Delegate painter for painting the arrowhead
     */
    private VisualDirectedEdgePainterWeakImpl   arrowHeadPainterDelegate;

    /**
     * Delegate painter for painting the weight
     */
    private VisualWeightedEdgePainterWeakImpl   weightPainterDelegate;

    /**
     * Creates an instance of <tt>VisualDirectedWeightedEdgePainterImpl</tt>
     */
    public VisualDirectedWeightedEdgePainterImpl() {
        this.arrowHeadPainterDelegate = new VisualDirectedEdgePainterWeakImpl();
        this.weightPainterDelegate = new VisualWeightedEdgePainterWeakImpl();
    }

    /**
     * Paints the <tt>VisualEdge</tt>.
     */
    public void paint( VisualGraphComponent component, Graphics2D g2d ) {
        super.paint( component, g2d );
        this.paintArrowHead( (VisualEdge) component, g2d );
    }

    /**
     * Paints the arrowhead of the <tt>VisualEdge</tt>
     */
    public void paintArrowHead( VisualEdge vEdge, Graphics2D g2d ) {
    	System.out.println("[VisualDirectedWeightedEdgePainterImpl] paintArrowHead");
        this.arrowHeadPainterDelegate.paintArrowHead( vEdge, g2d );
    }

}