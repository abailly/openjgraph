package salvo.jesus.graph.visual.drawing;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.StringTokenizer;

import salvo.jesus.graph.visual.VisualGraphComponent;
import salvo.jesus.graph.visual.VisualVertex;
import salvo.jesus.util.VisualGraphComponentShape;

/**
 * An implementation of <tt>VisualVertexPainter</tt> that draws the
 * <tt>VisualVertex</tt> based on its attributes.
 * 
 * @author Jesus M. Salvo Jr.
 */

public class VisualVertexPainterImpl implements VisualVertexPainter {

	/**
	 * Distance from the top and bottom edge of the bounds VisualVertex to the
	 * ascent of the first line or the descent of the last line, respectively.
	 */
	private double topBottomMargin = 5;

	/**
	 * Distance from the top and bottom edge of the bounds of the VisualVertex
	 * to the of the first character and the last character of each line,
	 * respectively.
	 */
	private double leftRightMargin = 5;

	/**
	 * Reference to a newline so that we do not create this String all the time.
	 */
	private static final String newLine = "\n";

	public VisualVertexPainterImpl() {
	}

	/**
	 * Draw the <tt>VisualVertex</tt> with the specified 2D graphics context.
	 * Each call to this method will draw the fill color, the outline color, and
	 * the string inside the shape, in that order.
	 * <p>
	 * The contract is that calling this <tt>paint</tt> method will call the
	 * other methods in this interface.
	 * 
	 * @param vv
	 *            The <tt>VisualVertex</tt> to be painted.
	 * @param g2d
	 *            The Graphics2D graphics context object used to draw the
	 *            VisualVertex.
	 */
	public void paint(VisualGraphComponent vg, Graphics2D g2d) {
		if (g2d.getClipBounds() != null
				&& !vg.getBounds().intersects(g2d.getClipBounds()))
			return;

		// Draw the node's fill color
		this.paintFill(vg, g2d);

		// Draw the node's outline
		this.paintOutline(vg, g2d);

		// Finally, paint the node's display text to describe the node
		this.paintText(vg, g2d);
	}

	/**
	 * Paints the outline of the <tt>VisualVertex</tt>
	 */
	public void paintOutline(VisualGraphComponent component, Graphics2D g2d) {
		VisualVertex vv = (VisualVertex) component;
		g2d.setColor(vv.getOutlinecolor());
		vv.getShape().draw(g2d);
	}

	/**
	 * Paints the <tt>VisualVertex</tt>'s fill color.
	 */
	public void paintFill(VisualGraphComponent component, Graphics2D g2d) {
		VisualVertex vv = (VisualVertex) component;
		g2d.setColor(vv.getFillcolor());
		vv.getShape().fill(g2d);
	}

	/**
	 * Paints the text of the <tt>VisualVertex</tt>.
	 */
	public void paintText(VisualGraphComponent component, Graphics2D g2d) {
		FontMetrics fontMetrics = component.getFontMetrics();
		VisualVertex vv = (VisualVertex) component;
		StringTokenizer strTokenizer;
		int line = 1;
		int lineHeight;
		Rectangle bounds;

		lineHeight = fontMetrics.getHeight();

		bounds = vv.getShape().getBounds();

		g2d.setFont(vv.getFont());
		g2d.setColor(vv.getFontcolor());
		g2d.clip(bounds);
		strTokenizer = new StringTokenizer(vv.getText(),
				VisualVertexPainterImpl.newLine);
		while (strTokenizer.hasMoreTokens()) {
			g2d.drawString(strTokenizer.nextToken(), (float) (bounds.x
					+ this.leftRightMargin + 1), (float) (bounds.y
					+ this.topBottomMargin + lineHeight * line - 2));
			line++;
		}
		g2d.setClip(null);
	}

	/**
	 * Rescales the VisualVertex. It determines the height of the text to be
	 * painted and adjusts the size of the GeneralPath so that the entire text
	 * fits in the VisualVertex.
	 */
	public void rescale(VisualVertex vv) {
		FontMetrics fontMetrics = vv.getFontMetrics();
		VisualGraphComponentShape drawPath = vv.getShape();
		StringTokenizer strTokenizer;
		AffineTransform transform = new AffineTransform();
		Rectangle originalLocation;
		double scalex, scaley;
		int lineHeight;
		int height = 0, width, maxWidth = 0;

		lineHeight = fontMetrics.getHeight();

		// Since there is no setSize() method (or something similar)
		// for the class GeneralPath, we will transform the shape by
		// scaling it. Because scaling will update the origin of the
		// GeneralPath, we need to save the original location before proceeding.
		originalLocation = drawPath.getBounds();
		strTokenizer = new StringTokenizer(vv.getText(),
				VisualVertexPainterImpl.newLine);
		while (strTokenizer.hasMoreTokens()) {
			height += lineHeight;
			width = fontMetrics.stringWidth(strTokenizer.nextToken());
			maxWidth = width > maxWidth ? width : maxWidth;
		}

		// Now scale the GeneralPath, effectively "resizing" it.
		scalex = (maxWidth + this.leftRightMargin * 2)
				/ drawPath.getBounds().getWidth();
		scaley = (height + this.topBottomMargin * 2)
				/ drawPath.getBounds().getHeight();
		transform.scale(scalex, scaley);

		// We have to draw the GeneralPath before the scaling takes effect.
		drawPath.transform(transform);

		// Set the shape back to its original location.
		// setToTranslation() is used to remove the transformation or scaling.
		// Otherwise, the shape will be scaled twice.
		transform.setToTranslation(originalLocation.getMinX()
				- drawPath.getBounds().getMinX(), originalLocation.getMinY()
				- drawPath.getBounds().getMinY());

		// Draw again.
		drawPath.transform(transform);
	}
}
