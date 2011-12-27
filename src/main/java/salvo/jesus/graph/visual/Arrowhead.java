package salvo.jesus.graph.visual;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;

/**
 * An interface specifying Arrowhead objects behavior;
 * 
 * This interface is implemented by various instances of Arrowhead objects
 * allowing the user to specify its own style of arrows. By convention, the arrow
 * should point toward the (X2,Y2) point of the first argument to <code>getShape()</code>. 
 * <p>
 * The shape of the arrohead can then be retrieved through the get Shape method, 
 * along with other parameters.
 *
 * @author Arnaud Bailly
 * @version 31082002
 */
public interface Arrowhead {

    /**
     * Initializes this Arrowhead with the given stem 
     * and head point. The stem line defines the direction
     * the arrohead points to, the head point is self-explicit.
     *
     * @param stem the general direction of this arrowhead
     * @param head the head point of this arrowhead
     * @return a Shape object allowing caller to draw this Arrowhead
     */
    public Shape getShape(Line2D stem, Point head);

}

