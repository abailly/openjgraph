package salvo.jesus.graph.visual;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import salvo.jesus.graph.Edge;
import salvo.jesus.graph.java.awt.geom.Point2DDouble;
import salvo.jesus.graph.visual.layout.GraphLayoutManager;
import salvo.jesus.util.VisualGraphComponentPath;
import salvo.jesus.util.VisualGraphComponentShape;

/**
 * The VisualEdge class encapsulates an <tt>Edge</tt> with attributes
 * used for visual rendering of the vertex. Visual attributes in
 * addition to those already defined in <tt>AbstractVisualGraphComponent</tt>
 * are the <tt>Point2D</tt>s where the <tt>VisualEdge</tt> should
 * start and finish. The actual direction and path taken from the two
 * <tt>Point2D</tt>s is defined by the <tt>GeneralPath</tt> in
 * <tt>AbstractVisualGraphComponent</tt>.
 *
 * @author		Jesus M. Salvo Jr.
 */
public class VisualEdge extends AbstractVisualGraphComponent {

	private float lineThickness;

	/**
	* The VisualVertex object of the edge's tail.
	*/
	private VisualVertex visualVertexA;

	/**
	  * The VisualVertex object of the edge's head.
	  */
	private VisualVertex visualVertexB;

	int linetype = STRAIGHT_LINE;
	static final int STRAIGHT_LINE = 1;
	static final int ORTHOGONAL_LINE = 2;

	private Point2DDouble fromPortAssignment = new Point2DDouble(-1, -1);
	private Point2DDouble toPortAssignment = new Point2DDouble(-1, -1);

	/**
	  * Creates a new VisualEdge object that encapsulates the given Edge object.
	  * Because an Edge object does not have any knowledge of the VisualVertex
	  * in its endpoints, the parameter visualgraph is required for this class
	  * to determine the VisualVertex objects at its endpoints.
	  *
	  * @param	edge	    The Edge object that the VisualEdge will encapsulate.
	  * @param  visualgraph   The VisualGraph object where the VisualEdge will be drawn.
	  */
	public VisualEdge(Edge edge, VisualGraph vGraph) {
		this.component = edge;
		this.painter = vGraph.getVisualEdgePainterFactory().getPainter(this);

		visualVertexA = vGraph.getVisualVertex(edge.getVertexA());
		visualVertexB = vGraph.getVisualVertex(edge.getVertexB());
		componentShape = new VisualGraphComponentPath(new GeneralPath());
		this.setOutlinecolor(Color.blue);
		this.setFillcolor(Color.blue);
		this.visualGraph = vGraph;
		this.label = edge.toString();
	}

	/**
	 * Returns the Edge object that VisualEdge encapsulates.
	 *
	 * @return	The Edge object that the VisualEdge encapsulates.
	 */
	public Edge getEdge() {
		return (Edge) this.component;
	}

	/**
	 * Returns the VisualVertex object at the tail of the edge.
	 * For directed graphs, this is the VisualVertex at the opposite end
	 * of where the arrowhead is drawn.
	 *
	 * @return	The VisualVertex object at the tail of the edge.
	 */
	public VisualVertex getVisualVertexA() {
		return visualVertexA;
	}

	/**
	 * Returns the VisualVertex object at the head of the edge.
	 * For directed graphs, this is the VisualVertex at the end
	 * where the arrowhead is drawn.
	 *
	 * @return	The VisualVertex object at the head of the edge.
	 */
	public VisualVertex getVisualVertexB() {
		return visualVertexB;
	}

	protected void setLinetype(int linetype) {
		this.linetype = linetype;
	}

	public void setOrthogonalLine() {
		this.setLinetype(ORTHOGONAL_LINE);
	}

	public void setStraightLine() {
		this.setLinetype(STRAIGHT_LINE);
	}

	public void setFromPortAssignment(Point2D newport) {
		this.fromPortAssignment.setLocation(newport);
	}

	public void setToPortAssignment(Point2D newport) {
		this.toPortAssignment.setLocation(newport);
	}

	public Point2D.Double getFromPortAssignment() {
		return this.fromPortAssignment;
	}

	public Point2D.Double getToPortAssignment() {
		return this.toPortAssignment;
	}

	public String toString() {
		return this.getText();
	}

	/**
	  * Returns the distance between a point and the edge's path.
	  * All segments of the edge's GeneralPath is iterated to determine
	  * which segment has the shortest distance with the point, and that
	  * distance is returned.
	  *
	  * @param	x   x coordinate of the point
	  * @param    y   y coordinate of the points
	  * @return     The distance between the point and the edge's path.
	  */
	public double ptSegDist(int x, int y) {
		PathIterator edgepathiterator;
		double edgesegment[] = new double[6];
		double previouspoint[] = new double[2];
		double currentpoint[] = new double[2];
		int segmenttype;
		Line2D.Double edgecurrsegment;
		double distance = 0.0;
		double currdistance;
		boolean firstSegment = true;

		// Iterater through all the segments of the edge until we find the edge
		// that is nearest to the point
		edgepathiterator = componentShape.getPathIterator(null);
		while (!edgepathiterator.isDone()) {
			previouspoint[0] = currentpoint[0];
			previouspoint[1] = currentpoint[1];

			segmenttype = edgepathiterator.currentSegment(edgesegment);
			switch (segmenttype) {
				case PathIterator.SEG_LINETO :
				case PathIterator.SEG_MOVETO :
					currentpoint[0] = edgesegment[0];
					currentpoint[1] = edgesegment[1];
					break;
				case PathIterator.SEG_QUADTO :
					currentpoint[0] = edgesegment[2];
					currentpoint[1] = edgesegment[3];
					break;
				case PathIterator.SEG_CUBICTO :
					currentpoint[0] = edgesegment[4];
					currentpoint[1] = edgesegment[5];
			}

			if (segmenttype == PathIterator.SEG_LINETO
				|| segmenttype == PathIterator.SEG_QUADTO
				|| segmenttype == PathIterator.SEG_CUBICTO) {
				currdistance =
					Line2D.ptSegDist(
						previouspoint[0],
						previouspoint[1],
						currentpoint[0],
						currentpoint[1],
						x,
						y);
				if (currdistance < distance || firstSegment) {
					firstSegment = false;
					distance = currdistance;
				}
			}
			edgepathiterator.next();
		}
		return distance;
	}

	/**
	 * Draw the VisualEdge with the specified 2D graphics context.
	 * The endpoints of the line is always determined at every call to this method
	 * by the center coordinates of the bounding rectangle of
	 * both visualVertexA and visualVertexB.
	 *
	 * @param	g2d		The Graphics2D graphics context object used to draw
	 * the VisualEdge object.
	 */
	public void paint(Graphics2D g2d, GraphLayoutManager layoutmanager) {
		//System.out.println("[VisualEdge] paint");
		this.painter.paint(this, g2d);
		//System.out.println("[VisualEdge] SORTIE paint");
	}

	/**
	 * Do nothing.
	 */
	public void rescale() {
	}

	/**
	 * Returns the length of the <tt>VisualEdge</tt> as dictated by its <tt>GeneralPath</tt>
	 */
	public float getEdgeLength(
		VisualEdge vEdge,
		Point fromPoint,
		Point toPoint) {
		VisualGraphComponentShape gPath = vEdge.getShape();
		PathIterator iterator = gPath.getPathIterator(null, 1.0);
		float edgeSegment[] = new float[6];
		float edgeLength = 0;
		float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
		int segmentType;
		boolean firstPointInitialized = false;

		while (!iterator.isDone()) {
			segmentType = iterator.currentSegment(edgeSegment);

			switch (segmentType) {
				case PathIterator.SEG_LINETO :
				case PathIterator.SEG_MOVETO :
					x2 = edgeSegment[0];
					y2 = edgeSegment[1];
					break;
				case PathIterator.SEG_QUADTO :
					x2 = edgeSegment[2];
					y2 = edgeSegment[3];
					break;
				case PathIterator.SEG_CUBICTO :
					x2 = edgeSegment[4];
					y2 = edgeSegment[5];
			}

			if (firstPointInitialized) {
				edgeLength += Point2D.distance(x1, y1, x2, y2);
			}

			iterator.next();

			x1 = x2;
			y1 = y2;

			if (!firstPointInitialized) {
				firstPointInitialized = true;
				fromPoint.setLocation(x1, y1);
			}
		}
		toPoint.setLocation(x2, y2);

		return edgeLength;
	}

	/**
	 * 
	 */
	public float getLineThickness() {
		return lineThickness;
	}
	/**
	 * @param f
	 */
	public void setLineThickness(float f) {
		lineThickness = f;
	}

}
