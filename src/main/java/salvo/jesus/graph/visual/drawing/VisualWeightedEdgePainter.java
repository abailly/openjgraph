package salvo.jesus.graph.visual.drawing;


/**
 * An interface for drawing a <tt>VisualEdge</tt> that encapsulates a
 * <tt>WeightedEdge</tt>. Implementations of this interface are responsible
 * for drawing the "weight" label of the <tt>WeightedEdge</tt>.
 * <p>
 * However, this interface does not define any extra method to actually
 * paint the weight, since the weight itself is part of the
 * <tt>WeightedEdge.toString()</tt>.
 *
 * @author  Jesus M. Salvo Jr.
 */

public interface VisualWeightedEdgePainter extends VisualEdgePainter {

}