package salvo.jesus.graph.visual.drawing;

import java.awt.Graphics2D;
import java.io.Serializable;

import salvo.jesus.graph.visual.VisualGraphComponent;

/**
 * An interface representing painters that are responsible for
 * painting <tt>VisualGraphComponent</tt>s.
 *
 * @author  Jesus M. Salvo Jr.
 */

public interface Painter extends Serializable {

    /**
     * Paints the specified component using a Graphics2D context.
     *
     * @param   component   The <tt>VisualGraphComponent</tt> to be painted.
     * @param   g2d         The context to which the component will be painted.
     */
    public void paint( VisualGraphComponent component, Graphics2D g2d );

}