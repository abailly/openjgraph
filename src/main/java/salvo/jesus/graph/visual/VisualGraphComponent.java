package salvo.jesus.graph.visual;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import salvo.jesus.util.VisualGraphComponentShape;

/**
 * The interface for all 'components' in a VisualGraph. Note that
 * classes implementing this interface may not be real components
 * in the same sense as AWT or Swing components.
 *
 * @author  Jesus M. Salvo Jr.
 */
public interface VisualGraphComponent extends Serializable {
  /**
   * Returns the text displayed for the VisualGraphComponent
   */
  public String getText();

  /**
   * Returns the color used to fill the VisualGraphComponent
   *
   * @return	The fill or background Color
   */
  public Color getFillcolor();

  /**
   * Returns the color used to draw the outline of the VisualGraphComponent
   *
   * @return	The outline Color
   */
  public Color getOutlinecolor();

  /**
   * Returns the font used to draw the String describing the VisualGraphComponent.
   *
   * @return	The Font used to draw the string
   */
  public Font  getFont();

  /**
   * Return the <tt>FontMetrics</tt> for this <tt>VisualGraphComponent</tt>.,
   * which is used to define the width and height of the text and therefore also
   * of the component.
   */
  public FontMetrics getFontMetrics();

  /**
   * Returns the color used to draw the string representation of the VisualGraphComponent
   *
   * @return	The font Color
   */
  public Color getFontcolor();

  /**
   * Returns the GeneralPath used for rendering the outline of the VisualGraphComponent.
   *
   * @return	The GeneralPath used to draw the outline of the VisualGraphComponent.
   */
  public VisualGraphComponentShape getShape();

  /**
   * Returns the VisualGraph where the VisualGraphComponent is contained.
   */
  public VisualGraph getVisualGraph();

  /**
   * Sets the text displayed for the VisualGraphComponent
   */
  public void setText( String text );

  /**
    * Sets the fill color used to draw the VisualGraphComponent
    *
    * @param	c		The new Color object that will be used as the fill color
    * on the next painting of the VisualGraphComponent
    */
  public void setFillcolor( Color fillcolor );

  /**
    * Sets the outline color used to draw the VisualGraphComponent
    *
    * @param	c		The new Color object that will be used as the outline color
    * on the next painting of the VisualGraphComponent
    */
  public void setOutlinecolor( Color outlinecolor );

  /**
   * Sets the font used to draw the String describing the VisualGraphComponent
   *
   * @param	f		The new Font that will be used
   */
  public void setFont( Font font );

  /**
   * Sets the color of the font used to draw the String describing the VisualGraphComponent
   *
   * @param	fontcolor   Font color to be used
   */
  public void setFontcolor( Color fontcolor );

  /**
   * Sets geometry used to draw the outline of the VisualGraphComponent.
   *
   * @param   path   A GeneralPath object used to draw outline of the VisualGraphComponent.
   */
  public void setShape( VisualGraphComponentShape path );

  /**
   * Sets geometry used to draw the outline of the VisualGraphComponent.
   *
   * @param   pathIterator   A PathIterator object used to draw outline of the VisualGraphComponent.
   */
  public void setGeneralPath( PathIterator pathIterator );

  /**
   * Returns the bounding Rectangle of the VisualGraphComponent.
   *
   * @return	The Rectangle bounding the VisualGraphComponent.
   */
  public Rectangle    getBounds();

  /**
   * Rescales the VisualGraphComponent.
   */
  public void rescale();

  /**
   * Returns the bounding Rectangle of the VisualGraphComponent.
   *
   * @return	The Rectangle2D bounding the VisualGraphComponent.
   */
  public Rectangle2D  getBounds2D();

  /**
   * String representation of the VisualGraphComponent.
   */
  public String toString();

  /**
   * Adds a listener to receive mouse events on this VisualGraphComponent.
   *
   * @param l   The listener to receive mouse events.
   */
  public void addMouseListener( MouseListener l );

  /**
   * Adds a listener to receive mouse motion events on this VisualGraphComponent.
   *
   * @param l   The listener to receive mouse motion events.
   */
  public void addMouseMotionListener( MouseMotionListener l );

  /**
   * Removes the specified mouse listener so that it no longer receives
   * mouse events from this VisualGraphComponent.
   *
   * @param l   The listener to be removed.
   */
  public void removeMouseListener( MouseListener l );

  /**
   * Removes the specified mouse motion listener so that it no longer receives
   * mouse motion events from this VisualGraphComponent.
   *
   * @param l   The listener to receive mouse events.
   */
  public void removeMouseMotionListener( MouseMotionListener l );

  /**
   * Processes mouse events occuring on this VisualGraphComponent.
   * This method is not directly called by Java, but must be called
   * explicitly by the container (GraphPanel) where VisualGraphComponent
   * is contained.
   *
   * @param   e   The mouse event
   */
  public void processMouseEvent( MouseEvent e );

  /**
   * Processes mouse motion events occuring on this VisualGraphComponent.
   * This method is not directly called by Java, but must be called
   * explicitly by the container (GraphPanel) where VisualGraphComponent
   * is contained.
   *
   * @param   e   The mouse event
   */
  public void processMouseMotionEvent( MouseEvent e );

  /**
   * Processes key events occuring on this VisualGraphComponent.
   * This method is not directly called by Java, but must be called
   * explicitly by the container (GraphPanel) where VisualGraphComponent
   * is contained.
   */
  public void processKeyEvent( KeyEvent e );

}

