package examples;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import salvo.jesus.graph.visual.VisualGraphComponent;
import salvo.jesus.graph.visual.drawing.VisualEdgePainterImpl;

/**
 * An example of an alternative <tt>VisualEdgePainter</tt> that
 * changes the color to red and doubles the width.
 *
 * @author Jesus M. Salvo Jr.
 */

public class AlternateVisualEdgePainter extends VisualEdgePainterImpl {
    private transient BasicStroke origStroke = null;
    private transient BasicStroke fatStroke;


    public void paint( VisualGraphComponent component, Graphics2D g2d ) {
        // Change the color to red and do not set the color back to its original
        // so that when the user sees the visual properties of the edge, it is red.
        component.setOutlinecolor( Color.red );

        // Set the stroke for edges in thesubgraph to be double the stroke
        // of the default stroke.
        if( this.origStroke == null ) {

            this.origStroke = new BasicStroke();
            this.fatStroke = new BasicStroke( origStroke.getLineWidth() * 2 );
        }
        g2d.setStroke( this.fatStroke );
        super.paint( component, g2d );

        // Restore the stroke to the default
        g2d.setStroke( this.origStroke );
    }

}
