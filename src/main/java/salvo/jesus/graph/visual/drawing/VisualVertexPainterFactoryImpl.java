package salvo.jesus.graph.visual.drawing;

import salvo.jesus.graph.visual.VisualGraphComponent;

/**
 * An implmentation of <tt>PainterFactory</tt> interface
 * that returns a singleton instance of <tt>VisualVertexPainterImpl</tt>.
 *
 * @author Jesus M. Salvo Jr.
 */

public final class VisualVertexPainterFactoryImpl extends VisualVertexPainterFactory {

    /**
     * Reference to the singleton instance of this class
     */
    private static VisualVertexPainterFactoryImpl  thisInstance;

    /**
     * Reference to a singleton instance of <tt>VisualVertexPainterImpl</tt>
     */
    private VisualVertexPainter painter;

    /**
     * Private constructor to enforce a singleton.
     */
    private VisualVertexPainterFactoryImpl() {}

    /**
     * Returns the singleton instance of this class
     */
    public static VisualVertexPainterFactoryImpl getInstance() {
        if ( thisInstance == null )
            thisInstance = new VisualVertexPainterFactoryImpl();
        return thisInstance;
    }

    /**
     * Implementation of the <tt>getPainter()</tt> method of the
     * <tt>PainterFactory</tt> interface that returns a singleton
     * instance of a <tt>VisualVertexPainterImpl</tt>.
     */
    public Painter getPainter( VisualGraphComponent component ) {
        if( this.painter == null )
            this.painter = new VisualVertexPainterImpl();
        return this.painter;
    }
}