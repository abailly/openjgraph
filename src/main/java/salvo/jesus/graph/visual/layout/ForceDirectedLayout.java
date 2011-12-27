package salvo.jesus.graph.visual.layout;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import salvo.jesus.graph.visual.VisualEdge;
import salvo.jesus.graph.visual.VisualGraph;
import salvo.jesus.graph.visual.VisualVertex;
import salvo.jesus.util.VisualGraphComponentPath;
import salvo.jesus.util.VisualGraphComponentShape;

/**
 * An implementation of a directed-force layout using logarithmic springs
 * and electrical forces, as discussed in Chapter 10 of the book
 * "Graph Drawing".
 * <p>
 * However, note that the implementation is a bit different to the equation
 * 10.2 in the book such that:
 * <ul>
 * <li>Electrical repulsion is subtracted from the force of the spring, since
 * they tend to be opposite forces. The book adds them instead of subtracting.</li>
 * <li>The distance between the vertices in the calculation of the electrical
 * repulsion is not squared or multiplied by itself.</li>
 * <li>This used logarithimic springs</li>
 * </ul>
 * I am not a mathematician, but the above adjustments to the equation greatly
 * improved the force-directed layout.
 *
 * @author      Jesus M. Salvo Jr.
 */

public class ForceDirectedLayout implements GraphLayoutManager, Runnable {

	private boolean repaint;
	private VisualGraph vGraph;
	private double springLength = 150;
	private double stiffness = 30;
	private double electricalRepulsion = 200;
	private double increment = 0.50;
	private double baseIncrement = 0.50;

	private boolean initialized = false;

	private Thread runner;
	private ArrayList fixedVertexList;

	public ForceDirectedLayout(VisualGraph vGraph) {
		this.vGraph = vGraph;
		this.fixedVertexList = new ArrayList(10);
	}

	/**
	 * Returns the desired spring length for all edges.
	 * The default value is 50.
	 */
	public double getSpringLength() {
		return this.springLength;
	}

	/**
	 * Returns the stiffness for all edges.
	 * The default value is 50.
	 */
	public double getStiffness() {
		return this.stiffness;
	}

	/**
	 * Returns the eletrical repulsion between all vertices
	 * The default value is 400.
	 */
	public double getEletricalRepulsion() {
		return this.electricalRepulsion;
	}

	/**
	 * Returns the increment by which the vertices gets closer
	 * to the equilibrium or closer to the force. The default
	 * value is 0.50.
	 */
	public double getIncrement() {
		return this.increment;
	}

	/**
	 * Sets the desired length of the spring among all edges
	 */
	public void setSpringLength(double length) {
		this.springLength = length;
	}

	/**
	 * Sets the value of stiffness among all edges
	 */
	public void setStiffness(double stiffness) {
		this.stiffness = stiffness;
	}

	/**
	 * Sets the value of the electrical repulsion between all vertices
	 */
	public void setEletricalRepulsion(double repulsion) {
		this.electricalRepulsion = repulsion;
	}

	/**
	 * Sets the increment by which the vertices gets close to the equilibrium
	 * or gets closer to the direction of the force. This must be a number
	 * > 0 and <= 1. The higher the value, the faster the layout reaches equilibrium.
	 */
	public void setIncrement(double increment) {
		this.increment = increment;
	}

	/**
	 * Adds a <tt>VisuaLVertex</tt> that will not be moved from its position
	 * during the layout operation of <tt>ForceDirectedLayout</tt>.
	 */
	public void addFixedVertex(VisualVertex vVertex) {
		this.fixedVertexList.add(vVertex);
	}

	/**
	 * Returns <tt>true</tt> if the specified <tt>VisualVertex</tt> has
	 * a fixed position.
	 */
	public boolean isVertexFixed(VisualVertex vVertex) {
		return this.fixedVertexList.contains(vVertex);
	}

	/**
	 * Removes a <tt>VisualVertex</tt> from the list of <tt>VisualVertices</tt>
	 * that has a fixed position.
	 */
	public void removeFixedVertex(VisualVertex vVertex) {
		this.fixedVertexList.remove(vVertex);
	}

	/**
	* Determines if the graph has been initially laid out.
	* This method should be called prior to any painting to be done by the
	* graph layout manager, as most internal variables are only
	* initialized during layout.
	*
	* @return  True if the graph has at least been laid out once.
	*/
	public boolean isInitialized() {
		return initialized;
	}

	/**
 * @deprecated redirect call to paintEdge
 */
public void paintEdge(Graphics2D g2d, VisualEdge vEdge) {
		this.routeEdge(g2d, vEdge);
	}

	public void routeEdge(Graphics2D g2d, VisualEdge vEdge) {
		Rectangle frombounds = vEdge.getVisualVertexA().getBounds();
		Rectangle tobounds = vEdge.getVisualVertexB().getBounds();
		GeneralPath gPath = new GeneralPath();

		g2d.setColor(vEdge.getOutlinecolor());

		Point2D.Float fromcenter =
			new Point2D.Float(
				new Double(vEdge.getVisualVertexA().getBounds2D().getCenterX())
					.floatValue(),
				new Double(vEdge.getVisualVertexA().getBounds2D().getCenterY())
					.floatValue());
		Point2D.Float tocenter =
			new Point2D.Float(
				new Double(vEdge.getVisualVertexB().getBounds2D().getCenterX())
					.floatValue(),
				new Double(vEdge.getVisualVertexB().getBounds2D().getCenterY())
					.floatValue());

		// standard path
		if (!vEdge.getVisualVertexA().equals(vEdge.getVisualVertexB())) {
			gPath.reset();
			gPath.moveTo(fromcenter.x, fromcenter.y);
			gPath.lineTo(tocenter.x, tocenter.y);
			// to and from are same vertices
		} else {
			float x1, y1, x2, y2; // control points
			x1 = frombounds.x;
			y1 = frombounds.y - 2 * frombounds.height;
			x2 = frombounds.x + frombounds.width;
			y2 = y1;
			// we draw a loop 
			gPath.reset();
			gPath.moveTo(fromcenter.x, fromcenter.y - frombounds.height / 2);
			gPath.curveTo(
				x1,
				y1,
				x2,
				y2,
				tocenter.x,
				tocenter.y - tobounds.height / 2);
		}
		vEdge.setShape(new VisualGraphComponentPath(gPath));
	}

	/**
	* This method is called to layout the vertices in the graph, running
	* a thread to perform the layout if the thread is not running, or stopping
	* the thread if the thread is running.
	*/
	public void layout() {
		this.initialized = true;
		if (this.runner != null) {
			this.runner = null;
		} else {
			this.runner = new Thread(this);
			this.runner.start();
		}
	}

	/**
	 * "Relax" the force on the VisualVertex. "Relax" here means to
	 * get closer to the equilibrium position.
	 * <p>
	 * This method will:
	 * <ul>
	 * <li>Find the spring force between all of its adjacent VisualVertices</li>
	 * <li>Get the electrical repulsion between all VisualVertices, including
	 * those which are not adjacent.</li>
	 * </ul>
	 */
	private void relax(VisualVertex vVertex) {

		// If the VisualVertex is fixed, dont do anything.
		if (this.fixedVertexList.contains(vVertex)) {
			return;
		}

		double xForce = 0;
		double yForce = 0;

		double distance;
		double spring;
		double repulsion;
		double xSpring = 0;
		double ySpring = 0;
		double xRepulsion = 0;
		double yRepulsion = 0;

		double adjacentDistance = 0;

		double adjX;
		double adjY;
		double thisX = vVertex.getBounds2D().getCenterX();
		double thisY = vVertex.getBounds2D().getCenterY();

		List adjacentVertices;
		VisualVertex adjacentVisualVertex;
		int i, size;

		// Get the spring force between all of its adjacent vertices.
		adjacentVertices =
			this.vGraph.getGraph().getAdjacentVertices(vVertex.getVertex());
		size = adjacentVertices.size();
		for (i = 0; i < size; i++) {
			adjacentVisualVertex =
				this.vGraph.getVisualVertex( adjacentVertices.get(i));
			if (adjacentVisualVertex == vVertex)
				continue;

			adjX = adjacentVisualVertex.getBounds2D().getCenterX();
			adjY = adjacentVisualVertex.getBounds2D().getCenterY();

			distance = Point2D.distance(adjX, adjY, thisX, thisY);
			if (distance == 0)
				distance = .0001;

			//spring = this.stiffness * ( distance - this.springLength ) *
			//    (( thisX - adjX ) / ( distance ));
			spring =
				this.stiffness
					* Math.log(distance / this.springLength)
					* ((thisX - adjX) / (distance));

			xSpring += spring;

			//spring = this.stiffness * ( distance - this.springLength ) *
			//    (( thisY - adjY ) / ( distance ));
			spring =
				this.stiffness
					* Math.log(distance / this.springLength)
					* ((thisY - adjY) / (distance));

			ySpring += spring;

		}

		// Get the electrical repulsion between all vertices,
		// including those that are not adjacent.
		List allVertices = this.vGraph.getVisualVertices();
		VisualVertex aVisualVertex;
		size = allVertices.size();
		for (i = 0; i < size; i++) {
			aVisualVertex = (VisualVertex) allVertices.get(i);
			if (aVisualVertex == vVertex)
				continue;

			adjX = aVisualVertex.getBounds2D().getCenterX();
			adjY = aVisualVertex.getBounds2D().getCenterY();

			distance = Point2D.distance(adjX, adjY, thisX, thisY);
			if (distance == 0)
				distance = .0001;

			repulsion =
				(this.electricalRepulsion / distance)
					* ((thisX - adjX) / (distance));

			xRepulsion += repulsion;

			repulsion =
				(this.electricalRepulsion / distance)
					* ((thisY - adjY) / (distance));

			yRepulsion += repulsion;
		}

		// Combine the two to produce the total force exerted on the vertex.
		xForce = xSpring - xRepulsion;
		yForce = ySpring - yRepulsion;

		// Move the vertex in the direction of "the force" --- thinking of star wars :-)
		// by a small proportion
		double xadj = 0 - (xForce * this.increment);
		double yadj = 0 - (yForce * this.increment);

		double newX = vVertex.getBounds2D().getMinX() + xadj;
		double newY = vVertex.getBounds2D().getMinY() + yadj;

		// Ensure the vertex's position is never negative.
		if (newX >= 0 && newY >= 0)
			vVertex.setLocationDelta(xadj, yadj);
		else if (newX < 0 && newY >= 0) {
			if (vVertex.getBounds2D().getMinX() > 0)
				xadj = 0 - vVertex.getBounds2D().getMinX();
			else
				xadj = 0;
			vVertex.setLocationDelta(xadj, yadj);
		} else if (newY < 0 && newX >= 0) {
			if (vVertex.getBounds2D().getMinY() > 0)
				yadj = 0 - vVertex.getBounds2D().getMinY();
			else
				yadj = 0;
			vVertex.setLocationDelta(xadj, yadj);
		}
	}

	/**
	* This method is called to actually paint or draw the layout of the graph.
	* This method should only be called after at least one call to layout().
	*/
	public void drawLayout() {
		this.vGraph.repaint();
	}

	public void addVertex(VisualVertex vVertex) {
		// Do nothing here
	}

	public void removeEdge(VisualEdge vEdge) {
		// Do nothing here
	}

	public void removeVertex(VisualVertex vVertex) {
		// Do nothing here
	}

	public void addEdge(VisualEdge vEdge) {
		// Do nothing here
	}

	public void run() {
		List visualVertices;
		VisualVertex vVertex;
		int i, j, size;
		double incr = this.baseIncrement;
		//this.forceDirectLayout();
		//this.drawLayout();
		this.setIncrement(incr);
		
		Thread me = Thread.currentThread();
		while (me == this.runner && (incr > 0.01) ) {
			visualVertices = this.vGraph.getVisualVertices();
			size = visualVertices.size();
			for (i = 0; i < size; i++) {
				vVertex = (VisualVertex) visualVertices.get(i);
				this.relax(vVertex);
			}
			if(repaint) {
				this.vGraph.repaint();
				this.repaint = false;
			} 
			this.setIncrement(incr = incr *.99);
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				break;
			}
		}
	}
    
    public void doLayout() {
	
		List visualVertices;
		VisualVertex vVertex;
		int i, j, size;
		double incr = this.baseIncrement;
		//this.forceDirectLayout();
		//this.drawLayout();
		this.setIncrement(incr);
		while ( (incr > 0.01) ) {
			visualVertices = this.vGraph.getVisualVertices();
			size = visualVertices.size();
			for (i = 0; i < size; i++) {
				vVertex = (VisualVertex) visualVertices.get(i);
				this.relax(vVertex);
			}
			this.setIncrement(incr = incr *.99);
		}
		this.vGraph.repaint();
	}
    

	/* (non-Javadoc)
	 * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#setRepaint(boolean)
	 */
	public void setRepaint(boolean b) {
		this.repaint = b;
	}

    /* (non-Javadoc)
     * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#setVisualGraph(salvo.jesus.graph.visual.VisualGraph)
     */
    public void setVisualGraph(VisualGraph vg) {
        this.vGraph = vg;
    }

}
