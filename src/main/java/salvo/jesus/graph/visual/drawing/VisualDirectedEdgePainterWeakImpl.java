package salvo.jesus.graph.visual.drawing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;

import salvo.jesus.geom.Intersection;
import salvo.jesus.graph.visual.Arrowhead;
import salvo.jesus.graph.visual.VisualEdge;
import salvo.jesus.graph.visual.VisualGraphComponent;
import salvo.jesus.graph.visual.VisualVertex;
import salvo.jesus.util.VisualGraphComponentShape;

/**
 * A weak implementation of the VisualDirectedEdgePainter interface.
 *
 * @author Jesus M. Salvo Jr.
 */

class VisualDirectedEdgePainterWeakImpl implements VisualDirectedEdgePainter {

	public VisualDirectedEdgePainterWeakImpl() {
	}

	/**
	 * Empty method implemetation that does nothing. This method should never
	 * be called or delegated to for whatever reason.
	 */
	public void paint(VisualGraphComponent component, Graphics2D g2d) {
	}

	/**
	 * Empty method implemetation that does nothing. This method should never
	 * be called or delegated to for whatever reason.
	 */
	public void paintText(
		Graphics2D g2d,
		Font font,
		Color fontColor,
		String text,
		float x,
		float y) {
	}

	/**
	* Draws the arrow head
	*/
	public void paintArrowHead(VisualEdge ve, Graphics2D g2d) {
		//System.out.println("[VisualDirectedEdgePainterWeakImpl] paintArrowHead");
		PathIterator edgepathiterator;
		Point intersection;
		Point arrowbase1, arrowbase2;
		Arrowhead arrowhead;
		int arrowx[], arrowy[];
		double edgesegment[] = new double[6];
		double previouspoint[] = new double[2];
		double currentpoint[] = new double[2];
		Line2D.Double edgelastsegment;
		int segmenttype;
		int icount = 0;
		VisualVertex visualVertexA = ve.getVisualVertexA();
		VisualVertex visualVertexB = ve.getVisualVertexB();
		VisualGraphComponentShape shape = ve.getShape();
		boolean self = visualVertexA.equals(visualVertexB);

		// Get the intersection between the edge and the vertex
		edgepathiterator = shape.getPathIterator(null, 3.0);

		while (!edgepathiterator.isDone()) {
			previouspoint[0] = currentpoint[0];
			previouspoint[1] = currentpoint[1];

			segmenttype = edgepathiterator.currentSegment(edgesegment);

			currentpoint[0] = edgesegment[0];
			currentpoint[1] = edgesegment[1];

			if (segmenttype == PathIterator.SEG_LINETO) {
				edgelastsegment =
					new Line2D.Double(
						previouspoint[0],
						previouspoint[1],
						currentpoint[0],
						currentpoint[1]);

				// Get the intersection point of the edge and the from vertex
				intersection =
					Intersection.getIntersection(
						edgelastsegment,
						visualVertexB.getShape());

				if (intersection != null) {
					// if we are in a self edge, we must draw an arrow to the second intersection, not the first one
					if (self && (icount++ == 0))
						continue;
					// Determine the coordinates of the arrowhead of the edge
					arrowhead =
						ve
							.getVisualGraph()
							.getVisualGraphComponentFactory()
							.createArrowhead();
					Shape ashape =
						arrowhead.getShape(edgelastsegment, intersection);
					// Draw the arrowhead
					g2d.setColor(ve.getFillcolor());
					g2d.fill(ashape);
					g2d.setColor(ve.getOutlinecolor());
					g2d.draw(ashape);

					break;
				}
			}

			edgepathiterator.next();
		}
	}

}
