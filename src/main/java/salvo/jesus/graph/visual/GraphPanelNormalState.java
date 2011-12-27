package salvo.jesus.graph.visual;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * State object that represents the normal mode in a GraphPanel. Normal mode
 * being 1) drag vertex and right click on VisualGraphComponents to cause a
 * popup menu to be displayed.
 * 
 * @author Jesus M. Salvo Jr.
 */
public class GraphPanelNormalState extends GraphPanelState {
  /**
   * VisualVertex object selected during the mousePressed() method
   */
  VisualVertex targetvertex;

  /**
   * VisualEdge object selected during the mousePressed() method
   */
  VisualEdge targetedge;

  /**
   * Existing cursor prior to changing the cursor.
   */
  Cursor previouscursor;

  /**
   * A Cursor object of type Cursor.MOVE_CURSOR
   */
  Cursor movecursor;

  /**
   * Stores the previous x-coordinate of targetedvertex when dragging a
   * VisualVertex object.
   */
  int previous_x;

  /**
   * Stores the previous y-coordinate of targetedvertex when dragging a
   * VisualVertex object.
   */
  int previous_y;

  /**
   * Creates a GraphPanelNormalState object for the specified GraphPanel object.
   */
  public GraphPanelNormalState(DefaultGraphPanel gpanel) {
    super(gpanel);
    this.movecursor = new Cursor(Cursor.MOVE_CURSOR);
  }

  public GraphPanelNormalState() {
    this(null);
  }

  /**
   * If there is a VisualGraphComponent at the coordinate specified in the
   * MouseEvent e, then a JPopupMenu will be shown by calling the popup() method
   * whose context is the selected VisualGraphComponent.
   */
  public GraphPanelState mousePressed(MouseEvent e) {
    // adjust coordinates by zoom factor
    this.previous_x = e.getX();
    this.previous_y = e.getY();

    this.targetvertex = gpanel.getVisualGraph().getNode(
        (int) (this.previous_x / gpanel.getZoomFactor()),
        (int) (this.previous_y / gpanel.getZoomFactor()));
    this.targetedge = gpanel.getVisualGraph().getVisualEdge(
        (int) (this.previous_x / gpanel.getZoomFactor()),
        (int) (this.previous_y / gpanel.getZoomFactor()));

    // Notify the VisualGraphComponent of the event
    informTargetVisualGraphComponentOfMouseEvent(e);
    return this;
  }

  /**
   * Shows a popup menu if there was a VisualGraphComponent during the
   * mousePressed() event.
   */
  public GraphPanelState mouseReleased(MouseEvent e) {
    // Different platforms return true for isPopupTrigger() on different events.
    // Some return true on mousePressed(), while other on mouseReleased().
    // Therefore, adaptee.vertexlicked or adaptee.edgeclicked may have been set
    // on mousePressed(),
    // but the popup menu may not appear until mouseReleased().

    // Notify the VisualGraphComponent of the event
    informTargetVisualGraphComponentOfMouseEvent(e);

    // Do not forget to remove any reference to the vertex and edge that was
    // clicked
    // during the mousePressed.
    this.targetvertex = null;
    this.targetedge = null;
    this.previous_x = e.getX();
    this.previous_y = e.getY();
    return this;
  }

  /**
   * If there was a VisualVertex object selected during the mousePressed()
   * method, then drag the VisualVertex object to the new location specfied by
   * the MouseEvent e.
   */
  public GraphPanelState mouseDragged(MouseEvent e) {
    if (this.targetvertex != null)
      this.dragVertex(e.getX(), e.getY());

    // Notify the VisualGraphComponent of the event
    informTargetVisualGraphComponentOfMouseEvent(e);

    return this;
  }

  /**
   * This method is automatically called by the mouseDragged() method if there
   * was a VisualVertex selected during the mousePressed() method, to drag the
   * selected VisualVertex object to the specifid coordinate.
   * 
   * @param x
   *          New x coordinate
   * @param y
   *          New y coordinate
   * 
   */
  private void dragVertex(int x, int y) {
    Rectangle vertexrect;
    int deltax, deltay;

    vertexrect = this.targetvertex.getBounds();

    // Do not allow coordinates to be negative, as there is no way
    // to adjust the scrollbars of JScrollPane to set the viewport to negative
    // by default.
    deltax = (vertexrect.x + x - this.previous_x < 0 ? -vertexrect.x : x
        - this.previous_x);
    deltay = vertexrect.y + y - this.previous_y < 0 ? -vertexrect.y : y
        - this.previous_y;

    // Now drag the vertex to its new location.
    this.targetvertex.setLocationDelta(deltax / gpanel.getZoomFactor(), deltay
        / gpanel.getZoomFactor());

    // Make the current coordinate the "previous" coordinate for
    // the next mouseDragged event
    this.previous_x = this.previous_x + deltax;
    this.previous_y = this.previous_y + deltay;

    gpanel.vgraph.repaint();
  }

  /**
   * Creates and shows a JPopMenu object, whose context is the
   * VisualGraphComponent selected during the mousePressed() event.
   * 
   * @param x
   *          The x-coordinate where the popup menu will be shown.
   * @param y
   *          The y-coordinate where the popup menu will be shown.
   */
  private void popup(int x, int y) {
    JPopupMenu popup;
    JMenuItem propertiesmenuitem;
    JMenuItem deletemenuitem;

    // Show a popup menu for a vertex
    if (this.targetvertex != null) {
      popup = new JPopupMenu();
      propertiesmenuitem = new JMenuItem("Object Properties..");
      deletemenuitem = new JMenuItem("Delete Vertex");

      popup.add(propertiesmenuitem);
      popup.addSeparator();
      popup.add(deletemenuitem);

      // Specify what action to take when the Properties menu items is selected
      propertiesmenuitem.addActionListener(new ActionListener() {
        // We must keep a reference to object referenced by targetvertex,
        // because after the popup() method is called, targertvertex is set to
        // null
        // and there will no way for the dialog to know which context is it
        // displaying information for.
        VisualVertex selectedvertex = targetvertex;

        public void actionPerformed(ActionEvent actionevent) {
          new VisualGraphComponentPropertiesDialog(gpanel, this.selectedvertex);
          gpanel.repaint();
        }
      });

      // For the Delete menu item, delete the vertex from the graph
      deletemenuitem.addActionListener(new ActionListener() {
        // We must keep a reference to object referenced by targetvertex,
        // because after the popup() method is called, targertvertex is set to
        // null
        // and there will no way for us to know which vertex to remove.
        VisualVertex nodetoremove = targetvertex;

        public void actionPerformed(ActionEvent actionevent) {
          if (nodetoremove != null) {
            try {
              gpanel.getVisualGraph().remove(this.nodetoremove);
              gpanel.repaint();
            } catch (Exception ex) {
              ex.printStackTrace();
              return;
            }
          }
        }
      });

      popup.show(this.gpanel, x, y);
    }

    // Show a popup menu for an edge
    else if (this.targetedge != null) {
      popup = new JPopupMenu();
      propertiesmenuitem = new JMenuItem("Edge Properties...");
      deletemenuitem = new JMenuItem("Delete Edge");

      popup.add(propertiesmenuitem);
      popup.addSeparator();
      popup.add(deletemenuitem);

      // Specify what action to take when the Properties menu items is selected
      propertiesmenuitem.addActionListener(new ActionListener() {
        // We must keep a reference to object referenced by targetedge,
        // because after the popup() method is called, targertedge is set to
        // null
        // and there will no way for the dialog to know which context is it
        // displaying information for.
        VisualEdge selectededge = targetedge;

        public void actionPerformed(ActionEvent actionevent) {
          new VisualGraphComponentPropertiesDialog(gpanel, selectededge);
          gpanel.repaint();
        }
      });

      // For the Delete menu item, delete the vertex from the graph
      deletemenuitem.addActionListener(new ActionListener() {
        // We must keep a reference to object referenced by targetedge,
        // because after the popup() method is called, targertedge is set to
        // null
        // and there will no way for us to know which edge to delete.
        VisualEdge edgetoremove = targetedge;

        public void actionPerformed(ActionEvent actionevent) {
          if (edgetoremove != null) {
            try {
              gpanel.getVisualGraph().removeEdge(edgetoremove);
              gpanel.repaint();
            } catch (Exception ex) {
              ex.printStackTrace();
              return;
            }
          }
        }
      });
      popup.show(this.gpanel, x, y);
    }
  }

  /**
   * This method sets the cursor to a MOVE_CURSOR whenever the cursor enters a
   * VisualVertex object. The cursor is reset to its original when the mouse
   * cursor leaves a VisualVertex object.
   */
  public GraphPanelState mouseMoved(MouseEvent e) {
    VisualGraphComponent vObject = this.gpanel.getVisualGraph().getNode(
        (int) (e.getX() / gpanel.getZoomFactor()),
        (int) (e.getY() / gpanel.getZoomFactor()));

    if (vObject != null) {
      if (this.previouscursor == null)
        this.previouscursor = this.gpanel.getCursor();
      this.gpanel.setCursor(this.movecursor);
    } else {
      this.gpanel.setCursor(this.previouscursor);
      this.previouscursor = null;
    }

    // Notify the VisualGraphComponent of the event
    informTargetVisualGraphComponentOfMouseEvent(e);

    return this;

  }

  /**
   * Call VisualGraph.paint() method, passing the Graphics2D context and the
   * targetvertex
   */
  public void paint(Graphics2D g2d) {
    this.gpanel.getVisualGraph().paint(g2d, this.targetvertex);
  }

}
