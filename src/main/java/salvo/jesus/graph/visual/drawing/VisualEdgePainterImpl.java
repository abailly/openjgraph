package salvo.jesus.graph.visual.drawing;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import salvo.jesus.graph.visual.VisualEdge;
import salvo.jesus.graph.visual.VisualGraphComponent;
import salvo.jesus.graph.visual.VisualVertex;
import salvo.jesus.graph.visual.layout.GraphLayoutManager;
import salvo.jesus.util.VisualGraphComponentPath;
import salvo.jesus.util.VisualGraphComponentShape;

/**
 * An implementation of <tt>VisualEdgePainter</tt> that draws the
 * <tt>VisualEdge</tt> based on its attributes.
 * 
 * @author Jesus M. Salvo Jr.
 */

public class VisualEdgePainterImpl implements VisualEdgePainter {

	public VisualEdgePainterImpl() {
	}

	/**
	 * Paints the <tt>visualEdge</tt>. No arrowhead is drawn.
	 */
	public void paint(VisualGraphComponent component, Graphics2D g2d) {
		// System.out.println("[VisualEdgePainterImpl] paint");
		VisualEdge vEdge = (VisualEdge) component;
		VisualGraphComponentShape shape;
		VisualVertex visualVertexA = vEdge.getVisualVertexA();
		VisualVertex visualVertexB = vEdge.getVisualVertexB();
		Rectangle frombounds = visualVertexA.getBounds();
		Rectangle tobounds = visualVertexB.getBounds();
		Point2D.Float fromcenter = new Point2D.Float(new Double(frombounds
				.getCenterX()).floatValue(),
				new Double(frombounds.getCenterY()).floatValue());
		Point2D.Float tocenter = new Point2D.Float(new Double(tobounds
				.getCenterX()).floatValue(), new Double(tobounds.getCenterY())
				.floatValue());
		GraphLayoutManager layoutmanager = vEdge.getVisualGraph()
				.getGraphLayoutManager();

		// If there is no layoutmanager or there is one but the layout has not
		// been initialised, by default, let us route edges as straight lines.
		if (layoutmanager == null
				|| (layoutmanager != null && !layoutmanager.isInitialized())) {
			GeneralPath gPath = new GeneralPath();
			if (!visualVertexA.equals(visualVertexB)) {
				gPath.reset();
				gPath.moveTo(fromcenter.x, fromcenter.y);
				gPath.lineTo(tocenter.x, tocenter.y);
				// to and from are same vertices
			} else {
				float x1, y1, x2, y2; // control points
				x1 = frombounds.x - frombounds.width;
				y1 = frombounds.y - frombounds.height;
				x2 = frombounds.x + frombounds.width;
				y2 = y1;
				// we draw a loop
				gPath.reset();
				gPath.moveTo(fromcenter.x, fromcenter.y);
				gPath.curveTo(x1, y1, x2, y2, tocenter.x, tocenter.y);
			}
			vEdge.setShape(new VisualGraphComponentPath(gPath));
		} else {

			// Let the layout manager determine how the edge will be routed.
			layoutmanager.routeEdge(g2d, vEdge);
		}
		shape = vEdge.getShape();
		// Draw the line
		g2d.setColor(vEdge.getOutlinecolor());
		shape.draw(g2d);
		// Draw the edge label
		this.paintText(vEdge, g2d);

	}

	/**
	 * Wrapper method around the <tt>paintText()</tt> method of the
	 * <tt>VisualEdgePainter</tt> interface. This method performs the
	 * calculation to determine the position where the text will be drawn.
	 */
	private void paintText(VisualEdge vEdge, Graphics2D g2d) {

		Point fromPoint = new Point();
		Point toPoint = new Point();
		VisualGraphComponentShape shape = vEdge.getShape();
		PathIterator iterator = shape.getPathIterator(null, 5.0);
		FontMetrics fontMetrics;
		float edgeSegment[] = new float[6];
		double currentLength = 0;
		float cumulativeLength = 0;
		float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
		int segmentType;
		boolean firstPointInitialized = false;

		// Get the total length of the edge
		float edgeLength = vEdge.getEdgeLength(vEdge, fromPoint, toPoint);
		// int i =0;
		while (!iterator.isDone()) {
			// System.out.println("[VisualEdgePainterImpl] tour : "+i++);
			segmentType = iterator.currentSegment(edgeSegment);

			switch (segmentType) {
			case PathIterator.SEG_LINETO:
			case PathIterator.SEG_MOVETO:
				// System.out.println("[VisualEdgePainterImpl] paintText 1");
				x2 = edgeSegment[0];
				y2 = edgeSegment[1];
				break;
			case PathIterator.SEG_QUADTO:
				// System.out.println("[VisualEdgePainterImpl] paintText 2");
				x2 = edgeSegment[2];
				y2 = edgeSegment[3];
				break;
			case PathIterator.SEG_CUBICTO:
				// System.out.println("[VisualEdgePainterImpl] paintText 3");
				x2 = edgeSegment[4];
				y2 = edgeSegment[5];
			}

			if (firstPointInitialized) {
				// System.out.println("[VisualEdgePainterImpl] paintText 4");
				currentLength = Point2D.distance(x1, y1, x2, y2);
				cumulativeLength += currentLength;
			}

			iterator.next();

			// If we are halfway through the length of the edge,
			// then paint the text
			if (cumulativeLength >= (edgeLength / 2)
					|| cumulativeLength >= edgeLength) {
				// System.out.println("[VisualEdgePainterImpl] paintText 5");
				// Ratio of the remaining half-length over the length of the
				// current
				// edge
				double ratio = ((edgeLength / 2) - (cumulativeLength - currentLength))
						/ currentLength;
				fontMetrics = vEdge.getFontMetrics();

				// Take into account the text's length
				this
						.paintText(
								g2d,
								vEdge.getFont(),
								vEdge.getFontcolor(),
								vEdge.getText(),
								(float) (fromPoint.getX() < toPoint.getX() ? (x1 + (Math
										.abs(x2 - x1) * ratio))
										: (x1 - (Math.abs(x2 - x1) * ratio)))
										- fontMetrics.stringWidth(vEdge
												.getText()) / 2,
								(float) (fromPoint.getY() < toPoint.getY() ? (y1 + (Math
										.abs(y2 - y1) * ratio))
										: (y1 - (Math.abs(y2 - y1) * ratio))));
				break;
			}

			x1 = x2;
			y1 = y2;

			if (!firstPointInitialized) {
				// System.out.println("[VisualEdgePainterImpl] paintText 6");
				firstPointInitialized = true;
			}
		}
	}

	/**
	 * Paints the text of the <tt>VisualEdge</tt>.
	 */
	public void paintText(Graphics2D g2d, Font font, Color fontColor,
			String text, float x, float y) {
		// System.out.println("[VisualEdgePainterImpl] paintText 7");
		g2d.setFont(font);
		g2d.setColor(fontColor);
		g2d.drawString(text, x, y);
		// System.out.println("[VisualEdgePainterImpl] SORTIE paintText 7");
	}

}