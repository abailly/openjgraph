package salvo.jesus.graph.visual.drawing;

import salvo.jesus.graph.DirectedEdge;
import salvo.jesus.graph.DirectedWeightedEdge;
import salvo.jesus.graph.WeightedEdge;
import salvo.jesus.graph.visual.VisualEdge;
import salvo.jesus.graph.visual.VisualGraphComponent;

/**
 * Factory for returning <tt>VisualEdgePainter</tt>s, depending on
 * the type of <tt>VisualEdge</tt> to be painted.
 * <p>
 * Although different types of <tt>VisualEdgePainter</tt>s are returned
 * depending on the <tt>VisualEdge</tt> specified, a single instance
 * of that type is returned.
 *
 * @author  Jesus M. Salvo Jr.
 */

public final class VisualEdgePainterFactoryImpl extends VisualEdgePainterFactory {

    /**
     * Reference to the singleton instance of this class
     */
    private static VisualEdgePainterFactoryImpl     thisInstance;

    /**
     * Reference to the single instance of VisualEdgePainterImpl
     */
    private VisualEdgePainterImpl           basicPainter;

    /**
     * Reference to the single instance of VisualDirectedEdgePainterImpl
     */
    private VisualDirectedEdgePainterImpl   directedEdgePainter;

    /**
     * Reference to the single instance of VisualWeightedEdgePainterImpl
     */
    private VisualWeightedEdgePainterImpl   weightedEdgePainter;

    /**
     * Reference to the single instance of VisualDirectedWeightedEdgePainterImpl
     */
    private VisualDirectedWeightedEdgePainterImpl   directedWeightedEdgePainter;


    /**
     * Private constructor to enforce a singleton
     */
    private VisualEdgePainterFactoryImpl() {}

    /**
     * Returns the singleton instance of this class
     */
    public static VisualEdgePainterFactoryImpl getInstance() {
        if( thisInstance == null )
            thisInstance = new VisualEdgePainterFactoryImpl();
        return thisInstance;
    }

    /**
    * Return a <tt>VisualEdgePainter</tt> suitable for the specified
    * <tt>VisualEdge</tt>:
    * <ul>
    *  <li><tt>VisualEdgePainterImpl</tt> for <tt>VisualEdge</tt>s encapsulating
    *   a non-directed <tt>Edge</tt>.</li>
    *  <li><tt>VisualDirectedEdgePainterImpl</tt> for a <tt>VisualEdge</tt>
    *   encapsulating a <tt>DirectedEdge</tt>.</li>
    *  <li><tt>VisualWeightedEdgePainterImpl</tt> for a <tt>VisualEdge</tt>
    *   encapsulating a <tt>WeightedEdge</tt>.</li>
    *  <li><tt>VisualDirectedWeightedEdgePainterImpl</tt> for a <tt>VisualEdge</tt>
    *   encapsulating a <tt>DirectedWeightedEdge</tt>.</li>
    * </ul>
    *
    * @param ve  The <tt>VisualEdge</tt> that we want to paint.
    */
    public Painter getPainter( VisualGraphComponent component ) {
        VisualEdge ve = (VisualEdge) component;

        if( ve.getEdge() instanceof DirectedWeightedEdge ) {
            if( this.directedWeightedEdgePainter == null )
                this.directedWeightedEdgePainter = new VisualDirectedWeightedEdgePainterImpl();
             
            return this.directedWeightedEdgePainter;
        }
        if( ve.getEdge() instanceof WeightedEdge ) {
            if( this.weightedEdgePainter == null )
                this.weightedEdgePainter = new VisualWeightedEdgePainterImpl();
			
            return this.weightedEdgePainter;
        }
        if( ve.getEdge() instanceof DirectedEdge ) {
            if( this.directedEdgePainter == null )
                this.directedEdgePainter = new VisualDirectedEdgePainterImpl();
		
            return this.directedEdgePainter;
        }
        else {
            if( this.basicPainter == null )
                this.basicPainter = new VisualEdgePainterImpl();
			
            return this.basicPainter;
        }
    }
}