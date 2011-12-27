package salvo.jesus.graph.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import salvo.jesus.graph.Graph;
import salvo.jesus.graph.visual.layout.GraphLayoutManager;

/**
 * GraphPanel encapsulates the visual representation of a graph (VisualGraph)
 * into a panel. The panel is automatically resized depending on the location of
 * the vertices. Therefore, if the user drags the mouse outside of the existing
 * size of the panel, the panel is automaticall resized. However, the panel will
 * not be smaller than the viewport of the GraphScrollPane.
 * 
 * @author Jesus M. Salvo Jr.
 * @see salvo.jesus.graph.visual.GraphScrollPane
 */
public class DefaultGraphPanel extends JPanel implements MouseListener,
        MouseMotionListener, KeyListener, Scrollable, GraphPanel {
  
    private double zoomFactor = 1.0;

    /**
     * The GraphScrollPane that encapsulates this class.
     */
    GraphScrollPane gpcontainer;

    /**
     * The VisualGraph that this pane encapsulates
     */
    VisualGraph vgraph;

    /**
     * State object representing the current mode of the GraphPanel.
     */
    GraphPanelState state;

    /** antialias flag for painting the graph */
    private boolean antialias = false;

    /** hints object */
    private RenderingHints hints = null;

    /** stores old dimension to prevent resizing if not necessary */
    private Dimension currentDim;

    public DefaultGraphPanel() {
        this(new GraphPanelNormalState(), new VisualGraph(), false);
    }

    public DefaultGraphPanel(VisualGraph vgraph) {
        this(new GraphPanelNormalState(), vgraph, false);
    }

    public DefaultGraphPanel(GraphPanelState gps) {
        this(gps, new VisualGraph(), false);
    }

    public DefaultGraphPanel(GraphPanelState gps, VisualGraph vgraph, boolean antialias) {
        this.state = gps;
        setAntialias(antialias);
        gps.setGraphPanel(this);
        this.vgraph = vgraph;
        vgraph.addContainer(this);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method toggles the antialias rendering for this panel
     * 
     * @param antialias
     *          value to set this panel rendering status
     */
    public void setAntialias(boolean antialias) {
        this.antialias = antialias;
        if (hints == null) {
            hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            hints.put(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
        }
    }

    private void jbInit() throws Exception {
        // this.setBorder( new EtchedBorder());
        // By default, mode of operation is normal
        this.setNormalMode();
        this.setBackground(Color.white);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /**
     * Sets the VisualGraph that this class encapsulates. This will automatically
     * redraw the contents of the pane, thereby drawing the vertices and edges of
     * VisualGraph.
     * 
     * @param vg
     *          The visual graph to be encapsulated and drawn.
     * @param gp
     *          The container that will contain this class. This is later on used
     *          to determine if we should resize GraphPanelSizeable
     */
    public void setVisualGraph(VisualGraph vg, GraphScrollPane gp) {

        if (vgraph != null)
            vgraph.removeContainer(this);

        this.vgraph = vg;
        this.vgraph.addContainer(this);
        this.gpcontainer = gp;
        this.repaint();
    }

    public void setVisualGraph(VisualGraph vg) {

        if (vgraph != null)
            vgraph.removeContainer(this);

        this.vgraph = vg;
        this.vgraph.addContainer(this);
        this.invalidate();
        this.repaint();
    }

    /**
     * Sets the Graph that this class encapsulates. This will automatically redraw
     * the contents of the pane, thereby drawing the vertices and edges of Graph.
     * 
     * @param g
     *          The graph to be encapsulated and drawn.
     * @param gp
     *          The container that will contain this class. This is later on used
     *          to determine if we should resize GraphPanelSizeable
     */
    public void setGraph(Graph g, GraphScrollPane gp) {
        VisualGraph vg = new VisualGraph();

        vg.setGraph(g);
        this.setVisualGraph(vg, gp);
    }

    public void setGraph(Graph g) {
        VisualGraph vg = new VisualGraph();

        vg.setGraph(g);
        this.setVisualGraph(vg);
    }

    /**
     * Returns the VisualGraph that this class encapsulates.
     * 
     * @return The VisualGraph that is encapsulated within this class.
     */
    public VisualGraph getVisualGraph() {
        return this.vgraph;
    }

    /**
     * Sets the mode of operation to NORMAL_MODE
     *  
     */
    public void setNormalMode() {
        this.state = state.recommendState(new ChangeStateEvent(this,
                new GraphPanelNormalState(this)));
    }

    /**
     * Sets the mode of operation to VERTEX_MODE
     *  
     */
    public void setVertexMode() {
        this.state = state.recommendState(new ChangeStateEvent(this,
                new GraphPanelVertexState(this)));
    }

    /**
     * Sets the mode of operation to EDGE_MODE
     *  
     */
    public void setEdgeMode() {
        this.state = state.recommendState(new ChangeStateEvent(this,
                new GraphPanelEdgeState(this)));
    }

    /**
     * Sets the layout manager to use to layout the vertices of the graph.
     * 
     * @param layoutmanager
     *          An object implementing the GraphLayoutManager interface.
     */
    public void setGraphLayoutManager(GraphLayoutManager layoutmanager) {
        this.vgraph.setGraphLayoutManager(layoutmanager);
    }

	/**
     * Override of JPanel's paint() method. Before the superclass' paint method is
     * called, it compares the size (the maximum coordinates of the visual
     * vertices) of vgraph with that of gpcontainer. If any coordinates (x or y)
     * of vgraph is greater than the coordinates (size) of gpcontainer,
     * GraphPanelSizeable's size is adjusted to that of vgraph. Doing this will
     * have the effect of automatically adjusting the scrollbars of gpcontainer.
     * 
     * @param g
     *          Graphics context that will be used to draw.
     */
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        //System.out.println("[GraphPanel] paintComponent");
        Dimension vgraphsize;
        Graphics2D g2d = (Graphics2D) g.create();
        int w, h;

        // Set the size of the graphanelsizeable depending on the
        // coordinates of the vertices on visualgraph.
        // Settings the size will adjust the scrollbars on
        // GraphScrollPane.

        // Note that setting the width/height of graphpanelsizeable
        // to the width/height of graphpanel has the effect of
        // moving the viewport area when a vertex that is at the edge
        // of the width/height is deleted (thereby at least one of the
        // if conditions below is false).
        // If this is the desired, do not change the width/height.
        // That is, on the else statement:
        // 	width = (int) this.getSize().getWidth();
        // Do the same for the height.

        // Call the superclass paint() method, then draw the graph
        // using vgraph.paint(). If we call vgraph's paint() method first,
        // it will be overwritten by the superclass' paint() method.
        vgraphsize = vgraph.getMaxSize();
        // Dimension vportsize = gpcontainer.getViewport().getExtentSize();
        Insets insets = getInsets();
        int currentWidth = getWidth() - insets.left - insets.right;
        int currentHeight = getHeight() - insets.top - insets.bottom;
        Dimension vportsize = new Dimension(currentWidth, currentHeight);

        if (vgraphsize.getWidth() > vportsize.getWidth())
            w = (int) vgraphsize.getWidth();
        else
            w = (int) vportsize.getWidth();

        if (vgraphsize.getHeight() > vportsize.getHeight())
            h = (int) vgraphsize.getHeight();
        else
            h = (int) vportsize.getHeight();

        Dimension newdim = new Dimension(w, h);

        if (!newdim.equals(currentDim)) {
            this.setPreferredSize(newdim);
            currentDim = newdim;
//            this.validate();
            this.invalidate();
        }
        insets = getInsets();
        if (antialias) {
            g2d.setRenderingHints(hints);
        }
        if (isOpaque()) { //paint background
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, w, h);
            //        g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        /* clip to viewport */
        g2d.clipRect(insets.left, insets.top, w - insets.right, h
                - insets.bottom);
        /* rescale g2d to account for zooming */
        if (zoomFactor != 1)
            g2d.scale(zoomFactor, zoomFactor);
        // Do additional paint operation based on the current state or mode.
        this.state.paint(g2d);
        g2d.dispose();
    }

    /**
     * Automatically called when the mouse is dragged. The result of this method
     * call depends on the current mode or state of the GraphPanel, as determined
     * by the internal GraphPanelState variable by passing the method call to the
     * internal GraphPanelState object.
     */
    public void mouseDragged(MouseEvent e) {
        this.state = this.state.mouseDragged(e);
    }

    /**
     * Automatically called when the mouse is pressed. The result of this method
     * call depends on the current mode or state of the GraphPanel, as determined
     * by the internal GraphPanelState variable by passing the method call to the
     * internal GraphPanelState object.
     */
    public void mousePressed(MouseEvent e) {
        this.state = this.state.mousePressed(e);
    }

    /**
     * Automatically called when the mouse is released. The result of this method
     * call depends on the current mode or state of the GraphPanel, as determined
     * by the internal GraphPanelState variable by passing the method call to the
     * internal GraphPanelState object.
     */
    public void mouseReleased(MouseEvent e) {
        this.state = this.state.mouseReleased(e);
    }

    /**
     * Automatically called when the mouse enters the GraphPanel. The result of
     * this method call depends on the current mode or state of the GraphPanel, as
     * determined by the internal GraphPanelState variable by passing the method
     * call to the internal GraphPanelState object.
     */
    public void mouseEntered(MouseEvent e) {
        this.state = this.state.mouseEntered(e);
    }

    /**
     * Automatically called when the mouse leaves the GraphPanel. The result of
     * this method call depends on the current mode or state of the GraphPanel, as
     * determined by the internal GraphPanelState variable by passing the method
     * call to the internal GraphPanelState object.
     */
    public void mouseExited(MouseEvent e) {
        this.state = this.state.mouseExited(e);
    }

    /**
     * Automatically called when the mouse is clicked on the GraphPanel. The
     * result of this method call depends on the current mode or state of the
     * GraphPanel, as determined by the internal GraphPanelState variable by
     * passing the method call to the internal GraphPanelState object.
     */
    public void mouseClicked(MouseEvent e) {
        this.state = this.state.mouseClicked(e);
    }

    /**
     * Automatically called when the mouse is moved over the GraphPanel. The
     * result of this method call depends on the current mode or state of the
     * GraphPanel, as determined by the internal GraphPanelState variable by
     * passing the method call to the internal GraphPanelState object.
     */
    public void mouseMoved(MouseEvent e) {
        this.state = this.state.mouseMoved(e);
    }

    public void keyPressed(KeyEvent e) {
        this.state = this.state.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        this.state = this.state.keyReleased(e);
    }

    public void keyTyped(KeyEvent e) {
        this.state = this.state.keyTyped(e);
    }

    /**
     * Just make sure that we remove GraphPanelSizeable from vgraph's container
     * list.
     */
    protected void finalize() {
        // Make sure we remove this container from the virtual graph's container
        // list
        this.vgraph.removeContainer(this);
    }

    /**
     * Processes the <tt>ChangeStateEvent</tt> by delegating the event to the
     * <tt>recommendState()</tt> method of the current state, possibly returning
     * a new state.
     * 
     * @return the resulting state. Can be the previous state or a new state,
     *         depending on the current state's decision.
     */
    public GraphPanelState processChangeStateEvent(ChangeStateEvent cse) {
        this.state = this.state.recommendState(cse);
        return this.state;
    }

    /**
     * Returns the gpcontainer.
     * 
     * @return GraphScrollPane
     */
    public GraphScrollPane getGpcontainer() {
        return gpcontainer;
    }

    /**
     * @param i
     */
    public void setZoomFactor(double d) {
        this.zoomFactor = d;
    }

    /**
     * Retunrs the current zoom factor.
     * The zoom factor is a double coefficient greater than 0 which
     * is used to scale the graphics for repainting.
     * 
     * @return
     */
    public double getZoomFactor() {
        return zoomFactor;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
     */
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
     */
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
     */
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public Dimension getPreferredSize() {
    	Dimension sz = vgraph.getMaxSize();
      return sz;
	}

	/*
     * (non-Javadoc)
     * 
     * @see javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle,
     *      int, int)
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        if (orientation == SwingConstants.VERTICAL) {
            int h = visibleRect.height;
            if (direction > 0) { // down
                int rh = getHeight() - h - visibleRect.x; // unvisible panel
                return ((h) > rh) ? rh : (h);
            } else { //up
                int rh = visibleRect.y; // unvisible panel
                return ((h) > visibleRect.y) ? visibleRect.y : (h);
            }
        } else if (orientation == SwingConstants.HORIZONTAL) {
            int h = visibleRect.width;
            if (direction > 0) { // down
                int rh = getWidth() - h - visibleRect.x; // unvisible panel
                return ((h) > rh) ? rh : (h);
            } else { //up
                int rh = visibleRect.x; // unvisible panel
                return ((h) > visibleRect.x) ? visibleRect.x : (h);
            }
        }
        return 5;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.Scrollable#getScrollableUnitIncrement(java.awt.Rectangle,
     *      int, int)
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        if (orientation == SwingConstants.VERTICAL) {
            int h = visibleRect.height;
            if (direction > 0) { // down
                int rh = getHeight() - h - visibleRect.x; // unvisible panel
                return ((h / 10) > rh) ? rh : (h / 10);
            } else { //up
                int rh = visibleRect.y; // unvisible panel
                return ((h / 10) > visibleRect.y) ? visibleRect.y : (h / 10);
            }
        } else if (orientation == SwingConstants.HORIZONTAL) {
            int h = visibleRect.width;
            if (direction > 0) { // down
                int rh = getWidth() - h - visibleRect.x; // unvisible panel
                return ((h / 10) > rh) ? rh : (h / 10);
            } else { //up
                int rh = visibleRect.x; // unvisible panel
                return ((h / 10) > visibleRect.x) ? visibleRect.x : (h / 10);
            }
        }
        return 5;
    }

}