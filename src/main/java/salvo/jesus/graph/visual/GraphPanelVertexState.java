package salvo.jesus.graph.visual;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * State object that represents the vertex mode in a GraphPanel.
 * Object mode being the ability to interactively add a vertex into a graph.
 *
 * @author  Jesus M. Salvo Jr.
 */
public class GraphPanelVertexState extends GraphPanelState {
  
    /**
    * VisualVertex object selected during the mousePressed() method
    */
  VisualVertex targetvertex;
 
  /**
   * Creates a GraphPanelVertexState object for the specified GraphPanel object.
   */
  public GraphPanelVertexState(DefaultGraphPanel gpanel) {
    super(gpanel);
  }

  /**
   * Creates a new vertex on the specified coordinate.
   */
  public GraphPanelState mousePressed(MouseEvent e) {
    informTargetVisualGraphComponentOfMouseEvent(e);    
    return this;

  }
  
//  public GraphPanel getGPanel() {
//    return gpanel;
//  }

  /**
   * Just call VisualGraph().paint()
   */
  public void paint(Graphics2D g2d) {
    this.gpanel.getVisualGraph().paint(g2d);
  }
}