package salvo.jesus.graph.visual.layout;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import salvo.jesus.geom.Intersection;
import salvo.jesus.graph.Edge;
import salvo.jesus.graph.Graph;

import salvo.jesus.graph.visual.VisualEdge;
import salvo.jesus.graph.visual.VisualGraph;
import salvo.jesus.graph.visual.VisualVertex;
import salvo.jesus.util.VisualGraphComponentPath;

/**
 * Graph layout using Simulated Annealing algorithm
 * 
 * @author Arnaud Bailly
 * @version $Id: SimulatedAnnealingLayout.java 1360 2007-02-22 15:17:41Z
 *          /CN=nono $
 */

public class SimulatedAnnealingLayout implements GraphLayoutManager, Runnable {

	private static final Logger log = Logger
			.getLogger(SimulatedAnnealingLayout.class.getName());

	private double miny;

	private double minx;

	/* constant bound for multiplicators */
	private static final double MAX_C = 1000;

	private boolean repaint;

	// the visual graph to layout
	private VisualGraph vGraph;

	/** random move factor */
	private double randomMove = 0.05;

	/** maximum distance */
	private double expectedDist = 100;

	/** attractive force */
	private double attractiveForce = 2;

	/** cooling factor */
	private double coolFactor = 0.003;

	/** cooling threshold */
	private double coolThreshold = 1;

	/** temperature */
	private double temperature = 250;

	/** current temperature */
	private double currentTemperature = temperature;

	private Map fixed;

	private boolean initialized = false;

	private Thread runner;

	private boolean background;

	private long sleep;

	Random rand = new Random();

	private VisualVertex[] vvs;

	private Timer timer;

	/**
	 * A Layout that implements a simulated annealing algorithm
	 * 
	 * Constructs a new layout manager that implements simulated annealing
	 * algorithm. This layout can run either in the background thus continuously
	 * updating the visual graph or in the foreground. In the latter case, the
	 * current thread may appear to hang as the calculation is quite long
	 * 
	 * @param vGraph
	 *            visualGraph to layout
	 * @param background
	 *            calculate in the background or not
	 */
	public SimulatedAnnealingLayout(VisualGraph vGraph, boolean background,
			int sleep) {
		setVisualGraph(vGraph);
		this.fixed = new HashMap();
		this.background = background;
		this.sleep = sleep;
	}

	public SimulatedAnnealingLayout(VisualGraph vGraph, boolean background) {
		this(vGraph, background, 100);
	}

	public SimulatedAnnealingLayout(boolean bg) {
		this(null, bg, 100);
	}

	public SimulatedAnnealingLayout() {
		this(null, false, 100);
	}

	public double getCoolFactor() {
		return coolFactor;
	}

	public void setCoolFactor(double cf) {
		coolFactor = cf;
	}

	public double getCoolThreshold() {
		return coolThreshold;
	}

	public void setCoolThreshold(double ct) {
		coolThreshold = ct;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temp) {
		temperature = temp;
	}

	public double getExpectedDist() {
		return expectedDist;
	}

	public void setExpectedDist(double temp) {
		expectedDist = temp;
	}

	public double getAttractiveForce() {
		return attractiveForce;
	}

	public void setAttractiveForce(double temp) {
		attractiveForce = temp;
	}

	/**
	 * Adds a <tt>VisuaLVertex</tt> that will not be moved from its position
	 * during the layout operation of <tt>ForceDirectedLayout</tt>.
	 */
	public void addFixedVertex(VisualVertex vVertex) {
		this.fixed.put(vVertex, new Point2D.Double(vVertex.getShape()
				.getBounds().getCenterX(), vVertex.getShape().getBounds()
				.getCenterY()));

	}

	/**
	 * Returns <tt>true</tt> if the specified <tt>VisualVertex</tt> has a
	 * fixed position.
	 */
	public boolean isVertexFixed(VisualVertex vVertex) {
		return this.fixed.containsKey(vVertex);
	}

	/**
	 * Removes a <tt>VisualVertex</tt> from the list of
	 * <tt>VisualVertices</tt> that has a fixed position.
	 */
	public void removeFixedVertex(VisualVertex vVertex) {
		this.fixed.remove(vVertex);
	}

	/**
	 * Fix the position of a vertex
	 * 
	 * @param v
	 * @param x
	 * @param y
	 */
	public void fixVertex(VisualVertex v, double x, double y) {
		this.fixed.put(v, new Point2D.Double(x, y));
	}

	/**
	 * Determines if the graph has been initially laid out. This method should
	 * be called prior to any painting to be done by the graph layout manager,
	 * as most internal variables are only initialized during layout.
	 * 
	 * @return True if the graph has at least been laid out once.
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
		g2d.setStroke(new BasicStroke(vEdge.getLineThickness()));
		Point2D.Float fromcenter = new Point2D.Float(new Double(vEdge
				.getVisualVertexA().getBounds2D().getCenterX()).floatValue(),
				new Double(vEdge.getVisualVertexA().getBounds2D().getCenterY())
						.floatValue());
		Point2D.Float tocenter = new Point2D.Float(new Double(vEdge
				.getVisualVertexB().getBounds2D().getCenterX()).floatValue(),
				new Double(vEdge.getVisualVertexB().getBounds2D().getCenterY())
						.floatValue());

		// standard path
		if (!vEdge.getVisualVertexA().equals(vEdge.getVisualVertexB())) {
			boolean opp = false;
			gPath.reset();
			gPath.moveTo(fromcenter.x, fromcenter.y);
			/* check reverse path */
			Object va = vEdge.getVisualVertexA().getVertex();
			Iterator it = vEdge.getVisualGraph().getGraph().getEdges(
					vEdge.getVisualVertexB().getVertex()).iterator();
			while (it.hasNext()) {
				Edge ed = (Edge) it.next();
				if (ed.getVertexB().equals(va)) {
					opp = true;
					break;
				}
			}
			if (opp) { /* there is an inverse path - draw a curve */
				/* compute center vector */
				Point2D.Float center = new Point2D.Float(
						(tocenter.x - fromcenter.x) / 2,
						(tocenter.y - fromcenter.y) / 2);
				/* rotate by 30? */
				AffineTransform rot = AffineTransform
						.getRotateInstance(Math.PI / 6);
				rot.deltaTransform(center, center);

				gPath.curveTo(fromcenter.x + center.x, fromcenter.y + center.y,
						fromcenter.x + center.x, fromcenter.y + center.y,
						tocenter.x, tocenter.y);
			} else { /* draw a line */
				gPath.lineTo(tocenter.x, tocenter.y);
			}
			// to and from are same vertices
		} else {
			float x1, y1, x2, y2; // control points
			x1 = frombounds.x;
			y1 = frombounds.y - frombounds.height;
			x2 = frombounds.x + frombounds.width;
			y2 = y1;
			// we draw a loop
			gPath.reset();
			gPath.moveTo(fromcenter.x, fromcenter.y - frombounds.height / 2);
			gPath.curveTo(x1, y1, x2, y2, tocenter.x, tocenter.y
					- tobounds.height / 2);
		}
		vEdge.setShape(new VisualGraphComponentPath(gPath));
	}

	/**
	 * This method is called to layout the vertices in the graph, running a
	 * thread to perform the layout if the thread is not running, or stopping
	 * the thread if the thread is running.
	 */
	public void layout() {
		this.initialized = true;
		if (this.runner != null) {
			log.info("Stopping layout thread " + runner.getName());
			this.runner.interrupt();
			this.runner = null;
		} else {
			if (background) {
				Thread th = new Thread(this);
				th.setPriority(Thread.MIN_PRIORITY);
				th.start();
				timer = new Timer(100, new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (repaint) {
							vGraph.repaint();
						}
					}
				});
				timer.start();
			} else {
				run();
			}
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
		this.initialized = true;
		setCurrentTemperature(temperature);
		while (currentTemperature > coolThreshold) {
			move();
			currentTemperature = currentTemperature * (1 - coolFactor);
		}
		if (timer != null)
			timer.stop();
		this.vGraph.repaint();
	}

	private boolean areAdjacent(Graph g, Object v1, Object v2) {
		return g.getAdjacentVertices(v1).contains(v2)
				|| g.getAdjacentVertices(v2).contains(v1);
	}

	/**
	 * a basic move of automaton
	 */
	private void move() {
		double s1mx = 0.0;
		double s1my = 0.0;
		double s1ox, s1oy;
		double s1x = 0, s1y = 0;
		double s2x = 0, s2y = 0;
		double avx, avy;
		int len = vvs.length;
		Line2D line = new Line2D.Double();
		double sqexpect = expectedDist * expectedDist;
		minx = miny = Double.MAX_VALUE;
		// calcul d'attraction
		for (int i = 0; i < len; i++) {
			VisualVertex s1 = vvs[i];
			if (isVertexFixed(s1))
				continue;
			s1mx = 0.0;
			s1my = 0.0;
			s1x = s1.getShape().getBounds().getCenterX();
			s1y = s1.getShape().getBounds().getCenterY();
			s1ox = s1.getShape().getBounds().getX();
			s1oy = s1.getShape().getBounds().getY();
			for (int j = 0; j < len; j++) {
				if (i == j)
					continue;
				VisualVertex s2 = vvs[j];
				s2x = s2.getShape().getBounds().getCenterX();
				s2y = s2.getShape().getBounds().getCenterY();
				/* compute nearest intersectionpoint for shapes */
				line.setLine(s1x, s1y, s2x, s2y);
				Point2D inter1 = Intersection.getIntersection(line, s1
						.getShape());
				Point2D inter2 = Intersection.getIntersection(line, s2
						.getShape());
				if (inter1 != null) {
					s1x = inter1.getX();
					s1y = inter1.getY();
				}
				if (inter2 != null) {
					s2x = inter2.getX();
					s2y = inter2.getY();
				}
				double sqdist = (s1x - s2x) * (s1x - s2x) + (s1y - s2y)
						* (s1y - s2y);
				double dist = Math.sqrt(sqdist);
				// calculate normalized vector from s1 to s2
				avx = (s2x - s1x) / sqdist;
				avy = (s2y - s1y) / sqdist;
				if (areAdjacent(vGraph.getGraph(), s1.getVertex(), s2
						.getVertex())) {
					double attract = attractiveForce
							* ((sqdist - sqexpect) / sqexpect);
					// adjust vector
					s1mx += avx * attract;
					s1my += avy * attract;
				} else {
					double repulse = (expectedDist / dist);
					// adjust
					if (repulse >= 1 && repulse < 50) {
						s1mx -= avx * repulse;
						s1my -= avy * repulse;
					}
				}
			}
			// random
			s1mx += (rand.nextDouble() - 0.5) * randomMove;
			s1my += (rand.nextDouble() - 0.5) * randomMove;
			/* define position of attraction point */
			/*
			 * long grid = (long) expectedDist / 2; long nx = (((long) s1x) /
			 * grid) * (grid + 1); long ny = (((long) s1y) / grid) * (grid + 1);
			 * s1mx += (nx - s1x) / sqexpect; s1my += (ny - s1y) / sqexpect;
			 */
			// repulse from wall
			// repulseFromWalls(s1Point2D, s1move);
			// adjust for temperature
			s1mx *= currentTemperature;
			s1my *= currentTemperature;
			// normalize to prevent too much movement
			// keepslow(s1move);
			// apply move
			s1.setLocationDelta(s1mx, s1my);
			if ((s1ox + s1mx) < minx)
				minx = (s1ox + s1mx);
			if ((s1oy + s1my) < miny)
				miny = s1oy + s1my;
		}
		// System.err.println("Temp = " + currentTemperature);
		/* adjust vertices location to stay in viewport */
		/*
		 * it = vGraph.getVisualVertices().iterator(); while (it.hasNext()) {
		 * VisualVertex s1 = (VisualVertex) it.next();
		 * s1.setLocationDelta(-minx, -miny); }
		 */
		for (int i = 0; i < len; i++)
			vvs[i].setLocationDelta(-minx, -miny);
	}

	/**
	 * Add a component for attracting nodes to grid
	 * 
	 * @param s1
	 * 
	 * @param s1move
	 */
	private void adjust2Grid(Point2D s1, Point2D s1move) {

	}

	/**
	 * @param s1
	 */
	private void adjustMin(VisualVertex s1) {
		double x = s1.getBounds2D().getX();
		double y = s1.getBounds2D().getY();
	}

	/**
	 * @param s1move
	 */
	private void keepslow(Point2D s1move) {
		double x = s1move.getX();
		double y = s1move.getY();
		double nx, ny;
		if (x > 5 * expectedDist)
			nx = 5 * expectedDist;
		else if (x < -5 * expectedDist)
			nx = -5 * expectedDist;
		else
			nx = x;
		if (y > 5 * expectedDist)
			ny = 5 * expectedDist;
		else if (y < -5 * expectedDist)
			ny = -5 * expectedDist;
		else
			ny = y;
		s1move.setLocation(nx, ny);
	}

	// adjust second vector to keep vertices in the viewport
	// i.e. vertices are repulsed from the walls at a rate of 1/5 the
	// expectedDist
	private void repulseFromWalls(Point2D pos, Point2D move) {
		Point2D hz = new Point2D.Double(pos.getX(), 0.0);
		Point2D vt = new Point2D.Double(0.0, pos.getY());
		// horizontal force
		double h = Math.abs(pos.getX());
		double v = Math.abs(pos.getY());
		// normalize vectors
		hz.setLocation(hz.getY() / h, 0.0);
		vt.setLocation(0.0, vt.getY() / v);
		// calculate and apply hz force
		double force = 0.0;
		if (pos.getX() > 0)
			force = (expectedDist / 5) / h;
		else
			force = -h / expectedDist;
		hz.setLocation(hz.getX() * force, hz.getY());
		move.setLocation(move.getX() + hz.getX(), move.getY());
		// calculate and apply vt force
		if (pos.getY() > 0)
			force = (expectedDist / 5) / (v * v);
		else
			force = -v / expectedDist;
		vt.setLocation(vt.getX(), vt.getY() * force);
		move.setLocation(move.getX(), move.getY() + vt.getY());
	}

	public void doLayout() {
		while (currentTemperature > coolThreshold) {
			move();
			currentTemperature = currentTemperature * (1 - coolFactor);
		}
		this.vGraph.repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#setRepaint(boolean)
	 */
	public void setRepaint(boolean b) {
		repaint = b;
	}

	/**
	 * @return
	 */
	public double getRandomMove() {
		return randomMove;
	}

	/**
	 * @param d
	 */
	public void setRandomMove(double d) {
		randomMove = d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#setVisualGraph(salvo.jesus.graph.visual.VisualGraph)
	 */
	public void setVisualGraph(VisualGraph vg) {
		if (vg == null)
			return;
		this.vGraph = vg;
		this.vvs = new VisualVertex[vg.getVisualVertices().size()];
		this.vvs = (VisualVertex[]) vg.getVisualVertices().toArray(vvs);
	}

	public double getCurrentTemperature() {
		return currentTemperature;
	}

	public synchronized void setCurrentTemperature(double currentTemperature) {
		this.currentTemperature = currentTemperature;
	}

}