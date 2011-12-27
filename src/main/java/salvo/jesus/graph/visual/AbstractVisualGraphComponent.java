package salvo.jesus.graph.visual;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import salvo.jesus.graph.GraphComponent;
import salvo.jesus.graph.java.awt.geom.SerializablePathIterator;
import salvo.jesus.graph.visual.drawing.Painter;
import salvo.jesus.util.VisualGraphComponentPath;
import salvo.jesus.util.VisualGraphComponentShape;

/**
 * This abstract class is meant to encapsulate a <tt>GraphComponent</tt>, either
 * a <tt>Verte</tt> or an <tt>Edge</tt>, and define visual attributes for that component
 * for use in a <tt>GraphPanel</tt>.
 * <p>
 * These attributes include:
 * <ul>
 * <li>GeneralPath used to draw</li>
 * <li>Font used to draw the string label</li>
 * <li>Outline color and the background color</li>
 * <li>Outline color and the background color</li>
 * <li><tt>Painter</tt> responsible for painting the <tt>GraphComponent</tt></li>
 * <li><tt>VisualGraph</tt>s where the <tt>VisualGraphComponent is drawn</li>
 * <li><tt>MouseListener</tt>s and <tt>MouseMotionListner</tt>s for mouse events
 * on the <tt>VisualGraphComponent</tt>.</li>
 * </ul>
 *
 * @author  Jesus M. Salvo Jr.
 */

public abstract class AbstractVisualGraphComponent implements VisualGraphComponent {

  /**
   * The GraphComponent that is encapsulated.
   */
  Object    component;

  /**
   * Determines the color used to draw the outline of the VisualGraphComponent.
   */
  Color outlinecolor  = Color.black;

  /**
    * Determines the color used to fill the VisualGraphComponent.
    */
  Color fillcolor     = new Color( 0, 255, 255 );

  /**
   * The Font used to draw describing the VisualGraphComponent.
   *
   * If the component is an instance of VisualVertex, then this is
   * the Font used to draw the string inside the VisualVertex's shape.
   * The string drawn is the return value of vertex.toString().
   * The length and height of the string will determine the width and height
   * of the shape used to render the VisualVertex.
   */
  Font  font          = new Font( "Lucida Sans", Font.PLAIN, 10 );

  /**
   * Determines the color used to draw string representation of the VisualGraphComponent.
   */
  Color fontcolor     = Color.black;

  /**
    * The GeneralPath describing the visual representation of the VisualGraphComponent.
    */
  transient VisualGraphComponentShape componentShape;

  /**
   * List of listeners interested in mouse motion events on the VisualGraphComponent.
   */
  java.util.List      mouseMotionListeners = new ArrayList( 10 );

  /**
   * List of listeners interested in mouse events on the VisualGraphComponent.
   */
  java.util.List      mouseListeners = new ArrayList(  10 );

  /**
   * Delegate responsible for the actual drawing / painting
   */
  Painter   painter;

  /**
   * The VisualGraph where the VisualGraphComponent is contained.
   */
  VisualGraph   visualGraph;

  /**
   * The label for the VisualGraphComponent.
   */
  String        label;

  /**
   * FontMetrics so that we do not always create an instance
   * of Panel() just to get a FontMetrics. The property
   * fontMetrics ( e.g.: getFontMetrics()) is null unless setFont() is first called.
   */
  transient FontMetrics fontMetrics = new Panel().getFontMetrics( this.getFont() );

  /**
   * Returns the text displayed for the VisualGraphComponent
   */
  public String getText() {
    return this.label;
  }

  /**
   * Returns the color used to fill the VisualGraphComponent
   *
   * @return	The fill or background Color
   */
  public Color getFillcolor() {
    return this.fillcolor;
  }

  /**
   * Returns the color used to draw the outline of the VisualGraphComponent
   *
   * @return	The outline Color
   */
  public Color getOutlinecolor() {
    return this.outlinecolor;
  }

  /**
   * Returns the font used to draw the String describing the VisualGraphComponent.
   *
   * @return	The Font used to draw the string
   */
  public Font  getFont() {
    return this.font;
  }

  /**
   * Return the <tt>FontMetrics</tt> for this <tt>VisualGraphComponent</tt>.,
   * which is used to define the width and height of the text and therefore also
   * of the component.
   */
  public FontMetrics getFontMetrics() {
    return this.fontMetrics;
  }

  /**
   * Returns the color used to draw the string representation of the VisualGraphComponent
   *
   * @return	The font Color
   */
  public Color getFontcolor() {
    return this.fontcolor;
  }

  /**
   * Returns the GeneralPath used for rendering the outline of the VisualGraphComponent.
   *
   * @return	The GeneralPath used to draw the outline of the VisualGraphComponent.
   */
  public VisualGraphComponentShape getShape(){
    return this.componentShape;
  }

  /**
   * Returns the <tt>Painter</tt> that is used to paint this <tt>VisualGraphComponent</tt>.
   *
   * @return    Painter delegate responsible for painting.
   */
  public Painter getPainter() {
    return this.painter;
  }

  /**
   * Returns the VisualGraph where the VisualGraphComponent is contained.
   */
  public VisualGraph getVisualGraph() {
    return this.visualGraph;
  }

  /**
    * Sets the fill color used to draw the VisualGraphComponent
    *
    * @param	c		The new Color object that will be used as the fill color
    * on the next painting of the VisualGraphComponent
    */
  public void setFillcolor( Color fillcolor ) {
    this.fillcolor = fillcolor;
  }

  /**
   * Sets the text displayed for the VisualGraphComponent
   */
  public void setText( String text ) {
    this.label = text;
  }

  /**
    * Sets the outline color used to draw the VisualGraphComponent
    *
    * @param	c		The new Color object that will be used as the outline color
    * on the next painting of the VisualGraphComponent
    */
  public void setOutlinecolor( Color outlinecolor ) {
    this.outlinecolor = outlinecolor;
  }

  /**
   * Sets the font used to draw the String describing the VisualGraphComponent
   *
   * @param	f		The new Font that will be used
   */
  public void setFont( Font font ) {
    this.font = font;
    this.fontMetrics = new Panel().getFontMetrics( this.font );
  }

  /**
   * Sets the color of the font used to draw the String describing the VisualGraphComponent
   *
   * @param	fontcolor   Font color to be used
   */
  public void setFontcolor( Color fontcolor ) {
    this.fontcolor = fontcolor;
  }

  /**
   * Sets geometry used to draw the outline of the VisualGraphComponent.
   *
   * @param   path   A GeneralPath object used to draw outline of the VisualGraphComponent.
   */
  public void setShape( VisualGraphComponentShape path ) {
    this.componentShape = path;
    // Force rescale to scale the geometry appropriately.
    // this.rescale();
  }

  public void setGeneralPath( PathIterator pathIterator ) {
    GeneralPath newPath = new GeneralPath();
    newPath.append( pathIterator, true );
    this.componentShape = new VisualGraphComponentPath( newPath );
  }

  /**
   * Assigns a new <tt>Painter</tt> delegate which will be responsible for further
   * painting of this <tt>VisualGraphComponent</tt>.
   *
   * @param newPainter  The new <tt>Painter</tt> delegate.
   */
  public void setPainter( Painter newPainter ) {
    this.painter = newPainter;
  }

  /**
   * Returns the bounding Rectangle of the VisualGraphComponent.
   *
   * @return	The Rectangle bounding the VisualGraphComponent.
   */
  public Rectangle    getBounds() {
    return this.componentShape.getBounds();
  }

  /**
   * Returns the bounding Rectangle of the VisualGraphComponent.
   *
   * @return	The Rectangle2D bounding the VisualGraphComponent.
   */
  public Rectangle2D  getBounds2D() {
    return this.componentShape.getBounds();
  }

  /**
   * Adds a listener to receive mouse events on this VisualGraphComponent.
   *
   * @param l   The listener to receive mouse events.
   */
  public void addMouseListener( MouseListener l ) {
    this.mouseListeners.add( l );
  }

  /**
   * Adds a listener to receive mouse motion events on this VisualGraphComponent.
   *
   * @param l   The listener to receive mouse motion events.
   */
  public void addMouseMotionListener( MouseMotionListener l ) {
    this.mouseMotionListeners.add( l );
  }

  /**
   * Removes the specified mouse listener so that it no longer receives
   * mouse events from this VisualGraphComponent.
   *
   * @param l   The listener to be removed.
   */
  public void removeMouseListener( MouseListener l ) {
    this.mouseListeners.remove( l );
  }

  /**
   * Removes the specified mouse motion listener so that it no longer receives
   * mouse motion events from this VisualGraphComponent.
   *
   * @param l   The listener to receive mouse events.
   */
  public void removeMouseMotionListener( MouseMotionListener l ) {
    this.mouseMotionListeners.remove( l );
  }

  /**
   * Processes mouse events occuring on this VisualGraphComponent.
   * Notifies the registered mouse event listeners with the specified
   * mouse event.
   *
   * This method is not directly called by Java, but must be called
   * explicitly by the container (GraphPanel) where VisualGraphComponent
   * is contained.
   *
   * @param   e   The mouse event
   */
  public void processMouseEvent( MouseEvent e ) {
    MouseListener   listener;
    int i, size = this.mouseListeners.size();
    int id;
      /* reset the source to this so that listeners can easily access the component */
      e.setSource(this);

    for( i = 0; i < size; i++ ) {
      listener = (MouseListener) mouseListeners.get( i );
      id = e.getID();
      if( id == MouseEvent.MOUSE_CLICKED )
        listener.mouseClicked( e );
      else if( id == MouseEvent.MOUSE_PRESSED )
        listener.mousePressed( e );
      else if( id == MouseEvent.MOUSE_RELEASED )
        listener.mouseReleased( e );
      else if( id == MouseEvent.MOUSE_ENTERED )
        listener.mouseEntered( e );
      else if( id == MouseEvent.MOUSE_EXITED )
        listener.mouseExited( e );
    }
  }

  /**
   * Processes mouse motion events occuring on this VisualGraphComponent.
   * Notifies the registered mouse motion event listeners with the specified
   * mouse motion event.
   *
   * This method is not directly called by Java, but must be called
   * explicitly by the container (GraphPanel) where VisualGraphComponent
   * is contained.
   *
   * @param   e   The mouse event
   */
  public void processMouseMotionEvent( MouseEvent e ) {
    MouseMotionListener   listener;
    int i, size = this.mouseMotionListeners.size();
    int id;
      /* reset the source to this so that listeners can easily access the component */
      e.setSource(this);

    for( i = 0; i < size; i++ ) {
      listener = (MouseMotionListener) mouseMotionListeners.get( i );
      id = e.getID();
      if( id == MouseEvent.MOUSE_DRAGGED )
        listener.mouseDragged( e );
      else if( id == MouseEvent.MOUSE_MOVED )
        listener.mouseMoved( e );
    }
  }

    /**
    * Processes key events occuring on this VisualGraphComponent.
    * This method is not directly called by Java, but must be called
    * explicitly by the container (GraphPanel) where VisualGraphComponent
    * is contained.
    * <p>
    * Currently, this method does not do anything.
    */
    public void processKeyEvent( KeyEvent e ) {
        return;
    }

    /**
     * Serializes the VisualGraphComponent including its GeneralPath.
     * This method is called during serialization.
     * 
     * @deprecated
     */
    private void writeObject( ObjectOutputStream out ) throws IOException {
        SerializablePathIterator    sIterator;

        // Call default first
        out.defaultWriteObject();

        // Now write out the path of the GeneralPath
 //       sIterator = new SerializablePathIterator(
//            this.componentShape.getPathIterator( new AffineTransform() ));
//        out.writeObject( sIterator );
    }

    /**
     * De-serializes the VisualGraphComponent including its GeneralPath.
     * This method is called during serialization.
     *
     * @deprecated
     */
    private void readObject( ObjectInputStream in )
            throws IOException, ClassNotFoundException
    {
        SerializablePathIterator    sIterator;

        // Call default first
        in.defaultReadObject();

        // Now read in the PathIterator for the GeneralPath
        sIterator = (SerializablePathIterator) in.readObject();
//        this.componentShape = new GeneralPath();
//        this.componentShape.append( sIterator, false );
    }

}

