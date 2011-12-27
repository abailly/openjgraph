package salvo.jesus.graph.visual.layout;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import salvo.jesus.graph.java.awt.geom.Point2DDouble;
import salvo.jesus.graph.visual.GraphEditor;
import salvo.jesus.graph.visual.GraphScrollPane;
import salvo.jesus.graph.visual.VisualEdge;
import salvo.jesus.graph.visual.VisualGraph;
import salvo.jesus.graph.visual.VisualVertex;
import salvo.jesus.util.VisualGraphComponentPath;

/**
 * A concrete implementation of GraphLayoutManager, extending AbstractGraphLayout,
 * used to layout the vertices of a graph with horizontal and vertices line edges.
 *
 * @author		Jesus M. Salvo Jr.
 */
public class OrthogonalLineLayout extends AbstractGridLayout {
  /**
   * A List object where each element is in turn a List of VisualEdges.
   * The index of a VisualVertex in vgraph.visualvertices matches the index of
   * the VisualVertex's List of top VisualEdges.
   */
  List      topedges;

  /**
   * A List object where each element is in turn a List of VisualEdges.
   * The index of a VisualVertex in vgraph.visualvertices matches the index of
   * the VisualVertex's List of right VisualEdges.
   */
  List      rightedges;

  /**
   * A List object where each element is in turn a List of VisualEdges.
   * The index of a VisualVertex in vgraph.visualvertices matches the index of
   * the VisualVertex's List of left VisualEdges.
   */
  List      leftedges;

  /**
   * A List object where each element is in turn a List of VisualEdges.
   * The index of a VisualVertex in vgraph.visualvertices matches the index of
   * the VisualVertex's List of bottom VisualEdges.
   */
  List      bottomedges;

  /**
   * Creates a OrthogonalLineLayout object used to layout the VisualGraph object
   * specified by vgraph.
   *
   * Because no translation parameters are specified, position 0,0
   * of the internal Grid object will be drawn on 0,0 of the Container;
   * and the distance between grid lines is 100.
   *
   * @param   vgraph    The VisualGraph object to be laid out.
   */
  public OrthogonalLineLayout( VisualGraph vgraph ) {
    super( vgraph );
  }

  /**
   * Creates a OrthogonalLineLayout object used to layout the VisualGraph object
   * specified by vgraph.
   *
   * Because no translation parameters are specified, position 0,0
   * of the internal Grid object will be drawn on 0,0 of the Container;
   * and the distance between grid lines is 100.
   *
   * @param   gpane   A GraphScrollPane object encapsulating the VisualGraph
   * object to be laid out.
   */
  public OrthogonalLineLayout( GraphScrollPane gpane ){
    super( gpane.getVisualGraph() );
  }

  /**
   * Creates a OrthogonalLineLayout object used to layout the VisualGraph object
   * specified by vgraph.
   *
   * Because no translation parameters are specified, position 0,0
   * of the internal Grid object will be drawn on 0,0 of the Container;
   * and the distance between grid lines is 100.
   *
   * @param   gedit    A GraphEditor object encapsulating a GraphScrollPane object
   * which in turn encapsulates the VisualGraph object to be laid out.
   */
  public OrthogonalLineLayout( GraphEditor gedit ){
    super( gedit.getVisualGraph() );
  }

  /**
   * Initializes the internal List objects
   */
  protected void initGraphLayout() {
    int i, size = this.vgraph.getVisualVertices().size();

    this.topedges = new ArrayList( size );
    this.bottomedges = new ArrayList( size );
    this.leftedges = new ArrayList( size );
    this.rightedges = new ArrayList( size );

    for( i = 0; i < size; i++ ) {
      this.topedges.add( new ArrayList());
      this.bottomedges.add( new ArrayList());
      this.leftedges.add( new ArrayList());
      this.rightedges.add( new ArrayList());
    }
  }

  /**
   * Automatically called by the VisualGraph's vertexAdded() method
   * whenever a vertex has been added to the graph and consequently
   * when the VisualVertex has been added to the VisualGraph object.
   *
   * @param vvertex   The newly added VisualVertex object.
   */
  public void addVertex( VisualVertex vvertex ){
    // No need to do anything if layout has not been initialized,
    // as the Lists will be reinitialized anyway during layout.
    if( !this.initialized ) return;

    int index = this.vgraph.getVisualVertices().indexOf( vvertex );

    this.topedges.add( new ArrayList());
    this.bottomedges.add( new ArrayList());
    this.leftedges.add( new ArrayList());
    this.rightedges.add( new ArrayList());
  }

  /**
   * Automatically called by the VisualGraph's vertexRemoved() method
   * whenever a vertex is about to be removed from the graph and
   * consequently when a VisualVertex is about to be removed
   * from the VisualGraph object.
   *
   * @param vvertex   The VisualVertex object about to be removed.
   */
  public void removeVertex( VisualVertex vvertex ){
    // No need to do anything if layout has not been initialized,
    // as the Lists will be reinitialized anyway during layout.
    if( !this.initialized ) return;

    int index = this.vgraph.getVisualVertices().indexOf( vvertex );

    ((List) this.topedges.get( index )).clear();
    this.topedges.remove( index );
    ((List) this.bottomedges.get( index )).clear();
    this.bottomedges.remove( index );
    ((List) this.leftedges.get( index )).clear();
    this.leftedges.remove( index );
    ((List) this.rightedges.get( index )).clear();
    this.rightedges.remove( index );
  }

  /**
   * Automatically called by the VisualGraph's edgeAdded() method
   * whenever an edge has been added to the graph and consequently
   * when a VisualEdge has been added to the VisualGraph object.
   *
   * This method currently does not do anything as VisualEdges are
   * not assigned their port assignments until during layout.
   *
   * @param   vedge   The newly added VisualEdge object
   */
  public void addEdge( VisualEdge vedge ) {
    // Nothing to do for this layuot manager, as visual edges are not
    // assigned their port assignments until during layout.
  }

  /**
   * Automatically called by the VisualGraph's edgeRemoved() method
   * whenever an edge is about to be removed from the graph and consequently
   * when a VisualEdge is about to be removed from the VisualGraph object.
   *
   * This method will remove all top, left, right, and bottom side assignments of
   * a VisualEdge from all vertices.
   *
   * @param   vedge   The VisualEdge object about to be removed.
   */
  public void removeEdge( VisualEdge vedge ) {
    // No need to do anything if layout has not been initialized,
    // as the Lists will be reinitialized anyway during layout.
    if( !this.initialized ) return;

    int fromindex = this.vgraph.getVisualVertices().indexOf( vedge.getVisualVertexA() );
    int toindex = this.vgraph.getVisualVertices().indexOf( vedge.getVisualVertexB() );
    int i, size;
    List  side;

    side = ((List) this.topedges.get( fromindex ));
    while( side.remove( vedge ));
    side = ((List) this.bottomedges.get( fromindex ));
    while( side.remove( vedge ));
    side = ((List) this.leftedges.get( fromindex ));
    while( side.remove( vedge ));
    side = ((List) this.rightedges.get( fromindex ));
    while( side.remove( vedge ));

    side = ((List) this.topedges.get( toindex ));
    while( side.remove( vedge ));
    side = ((List) this.bottomedges.get( toindex ));
    while( side.remove( vedge ));
    side = ((List) this.leftedges.get( toindex ));
    while( side.remove( vedge ));
    side = ((List) this.rightedges.get( toindex ));
    while( side.remove( vedge ));
  }

  /**
   * @deprecated redirect call to paintEdge
   */
 public void paintEdge( Graphics2D g2d, VisualEdge vedge ){
    this.routeEdge( g2d, vedge );
  }

  public void routeEdge( Graphics2D g2d, VisualEdge vedge ) {
    // No need to do anything if layout has not been initialized,
    // as the Lists will be reinitialized anyway during layout.
    if( !this.initialized ) return;

    int fromindex = this.vgraph.getVisualVertices().indexOf( vedge.getVisualVertexA() );
    int toindex = this.vgraph.getVisualVertices().indexOf( vedge.getVisualVertexB() );

    g2d.setColor( vedge.getOutlinecolor() );

    Point2D.Double  fromport = vedge.getFromPortAssignment();
    Point2D.Double  toport = vedge.getToPortAssignment();
    Point2D.Double   fromcenter = new Point2D.Double(
        vedge.getVisualVertexA().getBounds2D().getCenterX(),
        vedge.getVisualVertexA().getBounds2D().getCenterY() );
    Point2D.Double   tocenter = new Point2D.Double(
        vedge.getVisualVertexB().getBounds2D().getCenterX(),
        vedge.getVisualVertexB().getBounds2D().getCenterY() );
    GeneralPath gPath = new GeneralPath();

    gPath.reset();
    gPath.moveTo(
      (float) (fromcenter.x + fromport.x),
      (float) (fromcenter.y + fromport.y) );


    // Find at which side of the vertex is the edge eminating from to determine
    // where the bend should be
    if( ((List) topedges.get( fromindex )).contains( vedge ) ||
        ((List) bottomedges.get( fromindex )).contains( vedge )) {
      gPath.lineTo(
        (float) (fromcenter.x + fromport.x),
        (float) (tocenter.y + toport.y) );
    }
    else if( ((List) leftedges.get( fromindex )).contains( vedge ) ||
        ((List) rightedges.get( fromindex )).contains( vedge )) {
      gPath.lineTo(
        (float) (tocenter.x + toport.x),
        (float) (fromcenter.y + fromport.y) );
    }

    gPath.lineTo(
      (float) (tocenter.x + toport.x),
      (float) (tocenter.y + toport.y) );
  }

  /**
   * Routes all the edges
   */
  protected void routeEdges() {
    int i, size = this.vgraph.getVisualVertices().size();

    // Clear the side assignments of edges.
    for( i = 0; i < size; i++ ) {
      ((List) this.topedges.get( i )).clear();
      ((List) this.bottomedges.get( i )).clear();
      ((List) this.leftedges.get( i )).clear();
      ((List) this.rightedges.get( i )).clear();
    }

    size = this.vgraph.getVisualEdges().size();
    for( i = 0; i < size; i++ )
      this.routeEdge( (VisualEdge) this.vgraph.getVisualEdges().get( i ));
  }

  protected void routeEdge( VisualEdge vedge ) {
    Rectangle   frombounds = vedge.getVisualVertexA().getBounds();
    Rectangle   tobounds = vedge.getVisualVertexB().getBounds();
    Point2D.Float   fromcenter = new Point2D.Float(
        new Double( frombounds.getCenterX()).floatValue(),
        new Double( frombounds.getCenterY()).floatValue() );
    Point2D.Float   tocenter = new Point2D.Float(
        new Double( tobounds.getCenterX()).floatValue(),
        new Double( tobounds.getCenterY()).floatValue() );
    Point   fromgridpoint = this.grid.findVisualVertex( vedge.getVisualVertexA());
    Point   togridpoint = this.grid.findVisualVertex( vedge.getVisualVertexB());
    double  fromwidth = frombounds.getWidth();
    double  fromheight = frombounds.getHeight();
    double  towidth = tobounds.getWidth();
    double  toheight = tobounds.getHeight();
    int     fromindex = this.vgraph.getVisualVertices().indexOf( vedge.getVisualVertexA() );
    int     toindex = this.vgraph.getVisualVertices().indexOf( vedge.getVisualVertexB() );
    GeneralPath gPath = new GeneralPath();

    // Route straight edges first....
    if( fromgridpoint.x == togridpoint.x ) {
      // route upwards
      if( togridpoint.y < fromgridpoint.y ) {
        vedge.setFromPortAssignment( new Point2DDouble( (fromwidth / 2), 0 ));
        vedge.setToPortAssignment( new Point2DDouble( (towidth / 2), toheight ));
        ((List) this.topedges.get( fromindex )).add( vedge );
        ((List) this.bottomedges.get( toindex )).add( vedge );
      }
      // route downwards
      else if( togridpoint.y > fromgridpoint.y ) {
        vedge.setFromPortAssignment( new Point2DDouble( (fromwidth / 2), fromheight) );
        vedge.setToPortAssignment( new Point2DDouble( (towidth / 2), 0) );
        ((List) this.bottomedges.get( fromindex )).add( vedge );
        ((List) this.topedges.get( toindex )).add( vedge );
      }
      gPath.reset();
      gPath.moveTo( fromcenter.x , fromcenter.y  );
      gPath.lineTo( fromcenter.x, tocenter.y );
    }
    else if( togridpoint.y == fromgridpoint.y ) {
      // route to the left
      if( togridpoint.x < fromgridpoint.x ) {
        vedge.setFromPortAssignment( new Point2DDouble( 0, (fromheight / 2) ));
        vedge.setToPortAssignment( new Point2DDouble( towidth, (toheight / 2) ));
        ((List) this.leftedges.get( fromindex )).add( vedge );
        ((List) this.rightedges.get( toindex )).add( vedge );
      }
      // route to the right
      else if( togridpoint.x > fromgridpoint.x ) {
        vedge.setFromPortAssignment( new Point2DDouble( fromwidth, (fromheight / 2) ));
        vedge.setToPortAssignment( new Point2DDouble( 0, (toheight / 2) ));
        ((List) this.rightedges.get( fromindex )).add( vedge );
        ((List) this.leftedges.get( toindex )).add( vedge );
      }
      gPath.reset();
      gPath.moveTo( fromcenter.x , fromcenter.y  );
      gPath.lineTo( tocenter.x, fromcenter.y );
    }

    // Now route free edges
    // tocenter is at the upper-right of fromcenter
    if( togridpoint.x > fromgridpoint.x && togridpoint.y < fromgridpoint.y ) {
      gPath.reset();
      gPath.moveTo( fromcenter.x, fromcenter.y );
      if(
        (fromwidth - ((List) this.topedges.get( fromindex )).size() +
         toheight - ((List) this.leftedges.get( toindex )).size()) >=
        (fromheight - ((List) this.rightedges.get( fromindex )).size() +
         towidth - ((List) this.bottomedges.get( toindex )).size()) ){

        // route vertically (up) then horizontally (right)
        vedge.setFromPortAssignment( new Point2DDouble( (fromwidth / 2), 0 ));
        vedge.setToPortAssignment( new Point2DDouble( 0, (toheight / 2) ));
        ((List) this.topedges.get( fromindex )).add( vedge );
        ((List) this.leftedges.get( toindex )).add( vedge );
        gPath.lineTo( fromcenter.x, tocenter.y );
      }
      else {
        // route horizontally (right) then vertically (up)
        vedge.setFromPortAssignment( new Point2DDouble( fromwidth, (fromheight / 2) ));
        vedge.setToPortAssignment( new Point2DDouble( (towidth / 2), toheight ));
        ((List) this.rightedges.get( fromindex )).add( vedge );
        ((List) this.bottomedges.get( toindex )).add( vedge );
        gPath.lineTo( tocenter.x, fromcenter.y );
      }
      gPath.lineTo( tocenter.x, tocenter.y );
    }
    // tocenter is at the lower-right of fromcenter
    else if( togridpoint.x > fromgridpoint.x && togridpoint.y > fromgridpoint.y ) {
      gPath.reset();
      gPath.moveTo( fromcenter.x, fromcenter.y );
      if(
        (fromwidth - ((List) this.bottomedges.get( fromindex )).size() +
         toheight - ((List) this.leftedges.get( toindex )).size()) >=
        (fromheight - ((List) this.rightedges.get( fromindex )).size() +
         towidth - ((List) this.topedges.get( toindex )).size()) ){

        // route vertically (down) then horizontally (right)
        vedge.setFromPortAssignment( new Point2DDouble( (fromwidth / 2), fromheight ));
        vedge.setToPortAssignment( new Point2DDouble( 0, (toheight / 2) ));
        ((List) this.bottomedges.get( fromindex )).add( vedge );
        ((List) this.leftedges.get( toindex )).add( vedge );
        gPath.lineTo( fromcenter.x, tocenter.y );
      }
      else {
        // route horizontally (right) then vertically (down)
        vedge.setFromPortAssignment( new Point2DDouble( fromwidth, (fromheight / 2) ));
        vedge.setToPortAssignment( new Point2DDouble( (towidth / 2), 0 ));
        ((List) this.rightedges.get( fromindex )).add( vedge );
        ((List) this.topedges.get( toindex )).add( vedge );
        gPath.lineTo( tocenter.x, fromcenter.y );
      }
      gPath.lineTo( tocenter.x, tocenter.y );
    }
    // tocenter is at the lower-left of fromcenter
    else if( togridpoint.x < fromgridpoint.x && togridpoint.y > fromgridpoint.y ) {
      gPath.reset();
      gPath.moveTo( fromcenter.x, fromcenter.y );
      if(
        (fromwidth - ((List) this.bottomedges.get( fromindex )).size() +
         toheight - ((List) this.rightedges.get( toindex )).size()) >=
        (fromheight - ((List) this.leftedges.get( fromindex )).size() +
         towidth - ((List) this.topedges.get( toindex )).size()) ){

        // route vertically (down) then horizontally (left)
        vedge.setFromPortAssignment( new Point2DDouble( (fromwidth / 2), fromheight ));
        vedge.setToPortAssignment( new Point2DDouble( towidth, (toheight / 2) ));
        ((List) this.bottomedges.get( fromindex )).add( vedge );
        ((List) this.rightedges.get( toindex )).add( vedge );
        gPath.lineTo( fromcenter.x, tocenter.y );
      }
      else {
        // route horizontally (left) then vertically (down)
        vedge.setFromPortAssignment( new Point2DDouble( 0, (fromheight / 2) ));
        vedge.setToPortAssignment( new Point2DDouble( (towidth / 2), 0 ) );
        ((List) this.leftedges.get( fromindex )).add( vedge );
        ((List) this.topedges.get( toindex )).add( vedge );
        gPath.lineTo( tocenter.x, fromcenter.y );
      }
      gPath.lineTo( tocenter.x, tocenter.y );
    }
    // tocenter is at the upper-left of fromcenter
    else if( togridpoint.x < fromgridpoint.x && togridpoint.y < fromgridpoint.y ) {
      gPath.reset();
      gPath.moveTo( fromcenter.x, fromcenter.y );
      if(
        (fromwidth - ((List) this.topedges.get( fromindex )).size() +
         toheight - ((List) this.rightedges.get( toindex )).size()) >=
        (fromheight - ((List) this.leftedges.get( fromindex )).size() +
         towidth - ((List) this.bottomedges.get( toindex )).size()) ){

        // route vertically (up) then horizontally (left)
        vedge.setFromPortAssignment( new Point2DDouble( ( fromwidth / 2), 0 ));
        vedge.setToPortAssignment( new Point2DDouble( towidth, (toheight / 2) ));
        ((List) this.topedges.get( fromindex )).add( vedge );
        ((List) this.rightedges.get( toindex )).add( vedge );
        gPath.lineTo( fromcenter.x, tocenter.y );
      }
      else {
        // route horizontally (left) then vertically (up)
        vedge.setFromPortAssignment( new Point2DDouble( 0, (fromheight / 2) ));
        vedge.setToPortAssignment( new Point2DDouble( (towidth / 2), toheight ) );
        ((List) this.leftedges.get( fromindex )).add( vedge );
        ((List) this.bottomedges.get( toindex )).add( vedge );
        gPath.lineTo( tocenter.x, fromcenter.y );
      }
      gPath.lineTo( tocenter.x, tocenter.y );
    }
    vedge.setShape(new VisualGraphComponentPath(gPath));
  }

  /**
   * Assigns ports to all edges. A port being a point along the outside perimeter
   * of a vertex.
   */
  private void portassignment() {
    int i, size = this.vgraph.getVisualVertices().size();

    for( i = 0; i < size; i++ )
      this.portassignment( (VisualVertex) this.vgraph.getVisualVertices().get( i )) ;
  }

  /**
   * Assigns ports to all edge of a vertex.
   *
   * @param vvertx    VisualVertex object whose edges will be assigned ports
   */
  private void portassignment( VisualVertex vvertex ) {
    Point2D   center = new Point2D.Double (vvertex.getBounds2D().getCenterX(), vvertex.getBounds2D().getCenterY());
    int       index = this.vgraph.getVisualVertices().indexOf( vvertex );

    VisualEdge      vedge;
    AffineTransform transform = new AffineTransform();
    Point2DDouble   portpoint = new Point2DDouble();
    Point2DDouble   translatedportpoint = new Point2DDouble();
    double          segment[] = new double[6];
    double          width, height;
    int             i, size;

    // Now give the port assignments
    transform.setToTranslation(
      vvertex.getBounds2D().getMinX() - vvertex.getBounds2D().getCenterX(),
      vvertex.getBounds2D().getMinY() - vvertex.getBounds2D().getCenterY() );
    width = vvertex.getBounds2D().getWidth();
    height = vvertex.getBounds2D().getHeight();

    // Give port assignment for top edges, sorted in clockwise order
    size = ((List) this.topedges.get( index )).size();
    for( i = 0 ; i < size; i++ ) {
      vedge = (VisualEdge) ((List) topedges.get( index )).get( i );
      portpoint.x = ((i+1) * width / (size + 1));
      portpoint.y = 0;
      transform.transform( portpoint, translatedportpoint );
      if( vedge.getVisualVertexA() == vvertex )
        vedge.setFromPortAssignment( translatedportpoint );
      else
        vedge.setToPortAssignment( translatedportpoint );
    }

    // Give port assignment for left edges, sorted on clockwise order
    size = ((List) this.leftedges.get( index )).size();
    for( i = 0 ; i < size; i++ ) {
      vedge = (VisualEdge) ((List) leftedges.get( index )).get( i );
      portpoint.x = 0;
      portpoint.y = ((size - i) * height / (size + 1));
      transform.transform( portpoint, translatedportpoint );
      if( vedge.getVisualVertexA() == vvertex )
        vedge.setFromPortAssignment( translatedportpoint );
      else
        vedge.setToPortAssignment( translatedportpoint );
    }

    // Give port assignment for right edges, sorted on clockwise order
    size = ((List) this.rightedges.get( index )).size();
    for( i = 0 ; i < size; i++ ) {
      vedge = (VisualEdge) ((List) rightedges.get( index )).get( i );
      portpoint.x = width;
      portpoint.y = ((i+1) * height / (size + 1));
      transform.transform( portpoint, translatedportpoint );
      if( vedge.getVisualVertexA() == vvertex )
        vedge.setFromPortAssignment( translatedportpoint );
      else
        vedge.setToPortAssignment( translatedportpoint );
    }

    // Give port assignment for bottom edges, sorted on clockwise order
    size = ((List) this.bottomedges.get( index )).size();
    for( i = 0 ; i < size; i++ ) {
      vedge = (VisualEdge) ((List) bottomedges.get( index )).get( i );
      portpoint.x = ((size - i) * width / (size + 1));
      portpoint.y = height;
      transform.transform( portpoint, translatedportpoint );
      if( vedge.getVisualVertexA() == vvertex )
        vedge.setFromPortAssignment( translatedportpoint );
      else
        vedge.setToPortAssignment( translatedportpoint );
    }
  }

  /**
   * Lays out the graph. The layout is performed in the following steps:
   * generalPosition() and the medianPlacement().
   *
   * @see #drawLayout()
   */
  public void layout(){
    super.layout();
    this.initialized = true;
    this.initGraphLayout();
    this.generalPosition( this.vgraph.getVisualVertices(), this.grid );
    this.medianPlacement();
    this.routeEdges();
    this.portassignment();
    this.drawLayout();
  }

}


