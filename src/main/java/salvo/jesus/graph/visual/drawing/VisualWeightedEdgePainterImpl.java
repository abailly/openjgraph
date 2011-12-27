package salvo.jesus.graph.visual.drawing;

import java.awt.Graphics2D;

import salvo.jesus.graph.visual.VisualGraphComponent;

/**
 * An implementation of <tt>VisualWeightedEdgePainter</tt> that draws
 * the <tt>VisualEdge</tt> based on its attributes, and the paints the
 * weight of the edge.
 *
 * @author Jesus M. Salvo Jr.
 */

public class VisualWeightedEdgePainterImpl extends VisualEdgePainterImpl implements VisualWeightedEdgePainter {

    /**
     * Delegate responsible for painting the weight of the <tt>WeightedEdge</tt>.
     */
    private VisualWeightedEdgePainterWeakImpl   weightPainterDelegate = new VisualWeightedEdgePainterWeakImpl();

    public VisualWeightedEdgePainterImpl() {}

    public void paint( VisualGraphComponent component, Graphics2D g2d ) {
        super.paint( component, g2d );
    }

}