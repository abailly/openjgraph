package salvo.jesus.graph.visual.layout;

import java.awt.Graphics2D;
import java.io.Serializable;

import salvo.jesus.graph.visual.VisualEdge;
import salvo.jesus.graph.visual.VisualGraph;
import salvo.jesus.graph.visual.VisualVertex;

/**
 * The layout manager interface responsible for visually laying out the vertices
 * in a graph.
 * 
 * Implementations of this interface should be registered with an instance of
 * VisualGraph, via the setGraphLayoutManager() method.
 * 
 * @author Jesus M. Salvo Jr.
 */

public interface GraphLayoutManager extends Serializable {

    /**
     * Determines if the graph has been initially laid out. This method should
     * be called prior to any painting to be done by the graph layout manager,
     * as most internal variables are only initialized during layout.
     * 
     * @return True if the graph has at least been laid out once.
     */
    public boolean isInitialized();

    /**
     * This method is called to layout the vertices in the graph, without
     * actually drawing the finished or intermediate layout.
     */
    public void layout();

    /**
     * Automatically called when a VisualVertex object has been added to the
     * VisualGraph object being laid out.
     * 
     * @param vvertex
     *            The newly added VisualVertex object.
     */
    public void addVertex(VisualVertex vvertex);

    /**
     * Automatically called when a VisualVertex object is about to be removed
     * from the VisualGraph object being laid out.
     * 
     * @param vvertex
     *            The VisualVertex object to be removed.
     */
    public void removeVertex(VisualVertex vvertex);

    /**
     * Automatically called when a VisualEdge object has been added to the
     * VisualGraph object being laid out.
     * 
     * @param vedge
     *            The newly added VisualEdge object.
     */
    public void addEdge(VisualEdge vedge);

    /**
     * Automatically called when a VisualEdge object is about to be removed from
     * the VisualGraph object being laid out.
     * 
     * @param vedge
     *            The VisualEdge object to be removed
     */
    public void removeEdge(VisualEdge vedge);

    /**
     * Automatically called when a VisualEdge object needs to be drawn.
     * 
     * @param g2d
     *            The Graphics2D object used for painting
     * @param vedge
     *            The VisualEdge object to be drawn
     * 
     * @deprecated This method is now replaced by <tt>routeEdge</tt>. This
     *             method will be removed in future releases.
     */
    public void paintEdge(Graphics2D g2d, VisualEdge vedge);

    /**
     * Automatically called when a VisualEdge object needs to be drawn. This
     * method is typically called from <tt>VisualEdgePainterImpl</tt> when the
     * <tt>VisualGraph</tt> encapsulating the <tt>VisualEdge</tt> has a
     * <tt>GraphLayoutManager</tt> set. Prior to calling this method, the
     * <tt>GeneralPath</tt> of the <tt>VisualEdge</tt> is reset.
     * <p>
     * Implementations of this method should set the segments of the
     * <tt>VisualEdge</tt>'s<tt>GeneralPath</tt> by calling the methods
     * <tt>moveTo()</tt>,<tt>lineTo()</tt>,<tt>curveTo()</tt>, and
     * <tt>quadTo()</tt>.
     * 
     * @param g2d
     *            The Graphics2D object used for painting
     * @param vedge
     *            The VisualEdge object to be drawn
     */
    public void routeEdge(Graphics2D g2d, VisualEdge vEdge);

    /**
     * This method is called to actually paint or draw the layout of the graph.
     * This method should only be called after at least one call to layout().
     */
    public void drawLayout();

    /**
     * Sets an hint for continuous layout managers to refresh display.
     * 
     * @param b
     */
    public void setRepaint(boolean b);

    /**
     * Set the visual graph to layout.
     * 
     * @param vg
     */
    public void setVisualGraph(VisualGraph vg);
}