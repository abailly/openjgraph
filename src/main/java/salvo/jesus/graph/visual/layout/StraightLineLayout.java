package salvo.jesus.graph.visual.layout;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import salvo.jesus.graph.visual.GraphEditor;
import salvo.jesus.graph.visual.GraphScrollPane;
import salvo.jesus.graph.visual.VisualEdge;
import salvo.jesus.graph.visual.VisualGraph;
import salvo.jesus.graph.visual.VisualVertex;
import salvo.jesus.util.VisualGraphComponentPath;

/**
 * A simple layout class where edges are drawn as straight lines
 * 
 * @author Arnaud Bailly
 * @version $Id: StraightLineLayout.java 912 2005-04-05 15:54:08Z bailly $
 */
public class StraightLineLayout implements GraphLayoutManager {

    private boolean initialized;

    private VisualGraph vgraph;

    ////////////////////////////////////////////////////
    // COSNTRUCTORS
    ////////////////////////////////////////////////////

    /**
     * Creates a StraightLineLayout object used to layout the VisualGraph object
     * specified by vgraph.
     * 
     * @param vgraph
     *            The VisualGraph object to be laid out.
     */
    public StraightLineLayout(VisualGraph vgraph) {
        this.vgraph = vgraph;
    }

    /**
     * Creates a StraightLineLayout object used to layout the VisualGraph object
     * specified by vgraph.
     * 
     * @param gpane
     *            A GraphScrollPane object encapsulating the VisualGraph object
     *            to be laid out.
     */
    public StraightLineLayout(GraphScrollPane gpane) {
        this.vgraph = gpane.getVisualGraph();
    }

    /**
     * Creates a StraightLineLayout object used to layout the VisualGraph object
     * specified by vgraph.
     * 
     * @param gedit
     *            A GraphEditor object encapsulating a GraphScrollPane object
     *            which in turn encapsulates the VisualGraph object to be laid
     *            out.
     */
    public StraightLineLayout(GraphEditor gedit) {
        this.vgraph = gedit.getVisualGraph();
    }

    ////////////////////////////////////////////////////
    // GRAPHLAYOUTMANAGER OVERRIDES
    ////////////////////////////////////////////////////

    /**
     * This method is called to layout the vertices in the graph, running a
     * thread to perform the layout if the thread is not running, or stopping
     * the thread if the thread is running.
     */
    public void layout() {
        this.initialized = true;
    }

    /**
     * This method is called to actually paint or draw the layout of the graph.
     * This method should only be called after at least one call to layout().
     */
    public void drawLayout() {
        this.vgraph.repaint();
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

    public boolean isInitialized() {
        return initialized;
    }

    /**
     * @deprecated redirect call to paintEdge
     */
    public void paintEdge(Graphics2D g2d, VisualEdge vEdge) {
        this.routeEdge(g2d, vEdge);
    }

    public void routeEdge(Graphics2D g2d, VisualEdge vedge) {
        g2d.setColor(vedge.getOutlinecolor());

        Rectangle frombounds = vedge.getVisualVertexA().getBounds();
        Rectangle tobounds = vedge.getVisualVertexB().getBounds();
        Point2D.Float fromcenter = new Point2D.Float(new Double(frombounds
                .getCenterX()).floatValue(),
                new Double(frombounds.getCenterY()).floatValue());
        Point2D.Float tocenter = new Point2D.Float(new Double(tobounds
                .getCenterX()).floatValue(), new Double(tobounds.getCenterY())
                .floatValue());
        GeneralPath gPath = new GeneralPath();
        // standard path
        if (!vedge.getVisualVertexA().equals(vedge.getVisualVertexB())) {
            gPath.reset();
            gPath.moveTo(fromcenter.x, fromcenter.y);
            gPath.lineTo(tocenter.x, tocenter.y);

        } else { // to and from are same vertices
            float x1, y1, x2, y2; // control points
            x1 = frombounds.x;
            y1 = frombounds.y - 2 * frombounds.height;
            x2 = frombounds.x + frombounds.width;
            y2 = y1;
            // we draw a loop
            gPath.reset();
            gPath.moveTo(fromcenter.x, fromcenter.y - frombounds.height / 2);
            gPath.curveTo(x1, y1, x2, y2, tocenter.x, tocenter.y
                    - tobounds.height / 2);
        }
        vedge.setShape(new VisualGraphComponentPath(gPath));
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#setRepaint(boolean)
     */
    public void setRepaint(boolean b) {
        // NOP
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#setVisualGraph(salvo.jesus.graph.visual.VisualGraph)
     */
    public void setVisualGraph(VisualGraph vg) {
        this.vgraph = vg;
    }

}