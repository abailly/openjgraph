package salvo.jesus.graph.visual;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

/**
 * State object that represents the edge mode in a GraphPanel.
 * Edge mode being the ability to add an edge interactively by dragging
 * the mouse from a vertex and releasing the mouse on another vertex.
 *
 * @author  Jesus M. Salvo Jr.
 */
public class GraphPanelEdgeState extends GraphPanelState {
  /**
   * Reference to the VisualVertex object selected during the mousePressed() method.
   * This identifies the source vertex of an edge being created.
   */
  VisualVertex sourcevertex;

  /**
   * Line2D object that is drawn when an edge is being interactively created.
   */
  Line2D.Double probableedgeline;

  /**
   * A cross-hair Cursor object
   */
  Cursor edgecursor;

  /**
   * Existing cursor prior to changing the cursor to a cross-hair
   */
  Cursor previouscursor;

  /**
   * Creates a GraphPanelEdgeState object for the specified GraphPanel object.
   */
  public GraphPanelEdgeState(DefaultGraphPanel gpanel) {
    super(gpanel);
    this.edgecursor = new Cursor(Cursor.CROSSHAIR_CURSOR);

  }

  /**
   * Identifies the source vertex of a new Edge being created.
   */
  public GraphPanelState mousePressed(MouseEvent e) {
    this.sourcevertex = gpanel.getVisualGraph().getNode(e.getX(), e.getY());
		gpanel.repaint();
		    // Notify the VisualGraphComponent of the event
    informTargetVisualGraphComponentOfMouseEvent(e);
    return this;

  }

  /**
   * Signifies the end of a drag. If there was a vertex clicked during
   * the start of the drag (during the mousePressed() event) and there is
   * a vertex at the end of the drag, then an edge is added to the graph,
   * and the mouse cursor is returned to its original.
   */
  public GraphPanelState mouseReleased(MouseEvent e) {
        this.probableedgeline = null;
      gpanel.repaint();
 // Notify the VisualGraphComponent of the event
    informTargetVisualGraphComponentOfMouseEvent(e);
    return this;
  }

  /**
   * If there was a vertex clicked during the start of the drag
   * (during the mousePressed() event), draw a line from the
   * source vertex to the current coordinate.
   */
  public GraphPanelState mouseDragged(MouseEvent e) {
    //     Edge mode. Draw a line between the vertex that was clicked
    //     on the mousePressed() event and the current coordinate of the mouse.
    if (this.sourcevertex != null) {
      if (probableedgeline == null)
        probableedgeline = new Line2D.Double();
      probableedgeline.setLine(
        this.sourcevertex.getBounds().getCenterX(),
        this.sourcevertex.getBounds().getCenterY(),
        (double) e.getX(),
        (double) e.getY());

      gpanel.repaint();
    }
    // Notify the VisualGraphComponent of the event
    informTargetVisualGraphComponentOfMouseEvent(e);
    return this;
  }

  /**
   * This method sets the cursor to a crosshair whenever the cursor
   * enters a VisualVertex object. The cursor is reset to its original
   * when the mouse cursor leaves a VisualVertex object.
   */
  public GraphPanelState mouseMoved(MouseEvent e) {
    VisualGraphComponent vObject =
      this.gpanel.getVisualGraph().getNode(e.getX(), e.getY());
    VisualGraphComponent vEdge =
      this.gpanel.getVisualGraph().getVisualEdge(e.getX(), e.getY());
    VisualGraphComponent component;

    if (vObject != null) {
      if (this.previouscursor == null)
        this.previouscursor = this.gpanel.getCursor();
      this.gpanel.setCursor(this.edgecursor);
    } else if (this.previouscursor != null) {
      this.gpanel.setCursor(this.previouscursor);
      this.previouscursor = null;
    }
    // Notify the VisualGraphComponent of the event
    informTargetVisualGraphComponentOfMouseEvent(e);
    return this;
  }

  /**
   * Call VisualGraph.paint() method, passing the Graphics2D context
   * and the probable edge line to be drawn
   */
  public void paint(Graphics2D g2d) {
    this.gpanel.getVisualGraph().paint(g2d, this.probableedgeline);
  }  
}
