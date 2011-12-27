package salvo.jesus.graph.visual.layout;

import java.awt.Point;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import salvo.jesus.graph.Graph;
import salvo.jesus.graph.visual.VisualGraph;
import salvo.jesus.graph.visual.VisualVertex;

/**
 * An abstract implementation of the GraphLayoutManager interface.
 * Neither layout() nor drawLayout() are implemented. However, this
 * abstract implementation provides internal variables used by
 * concrete implementations of the interface. This class has an internal
 * instance of a VisualGraph to gain access to the graph itself and a
 * Grid object used to place the vertices on a hypothetical grid.
 *
 * It also has general purpose methods for grid point assignments of vertices.
 * These methods places the vertices in general position and median placement.
 *
 * @author		Jesus M. Salvo Jr.
 */

public abstract class AbstractGridLayout implements GraphGridLayoutManager, Runnable {
  private boolean repaint;

	/**
   * The Grid object where the vertices are laid out and aligned.
   */
  Grid    grid;

  /**
   * Used as a translation point when finally drawing the layout.
   *
   * Since the vertices are laid out on a Grid object, the Grid object
   * itself have no assigned x,y coordinates on a Container.
   *
   * When finally drawing the layout, position 0,0 of the Grid object is translated
   * to the Point specified by the startAt variable.
   */
  Point   startAt = new Point( 50, 50 );

  /**
   * The distance between vertical grid lines when finally drawing the layout.
   * Since this is a fixed value, this class does not take into account a VisualVertex's
   * actual width, so much so that a VisualVertex positioned on a specified
   * vertical grid line may overlap another vertical grid line if the width of the VisualVertex
   * is greater than the distance between vertical grid lines.
   */
  int     xGridDistance = 100;

  /**
   * The distance between horizontal grid lines when finally drawing the layout.
   * Since this is a fixed value, this class does not take into account a VisualVertex's
   * actual width, so much so that a VisualVertex positioned on a specified
   * horizontal grid line may overlap another horizontal grid line if the height of the VisualVertex
   * is greater than the distance between horizontal grid lines.
   */
  int     yGridDistance = 100;

  /**
   * Indicator whether or not the grid should be drawn.
   */
  boolean drawGrid = false;

  /**
   * The VisualGraph object that will be laid out.
   */
  VisualGraph vgraph;

  /**
   * An GraphLayoutListener object that receives notification whenever a vertex
   * in the graph has been laid out by the layout manager.
   */
  GraphLayoutListener   listener;

  boolean   initialized = false;

  ThreadQueue           threadqueue = new ThreadQueue();
  ThreadGroup           threadgroup;
  List                  grids = new ArrayList( 10 );


  /**
   * Creates a GraphLayoutManager object, specifying vgraph as the
   * VisualGraph object to be laid out. This will also initialize
   * the internal Grid object based on the number of vertices
   * that the graph has.
   */
  public AbstractGridLayout( VisualGraph vgraph ) {
    this.vgraph = vgraph;
  }

  /**
   * This method sets the point at which the grid starts.
   *
   * @param startAt   Point object indicating the upper left corner of the grid.
   */
  public void setStartAt( Point startAt ){
    this.startAt = startAt;
  }

  /**
   * This method sets the distance between vertical grids
   *
   * @param xGridDistance   Distance between vertical grids
   */
  public void setXGridDistance( int xGridDistance ){
    this.xGridDistance = xGridDistance;
  }

  /**
   * This method sets the distance between horizontal grids
   *
   * @param xGridDistance   Distance between horizontal  grids
   */
  public void setYGridDistance( int yGridDistance ){
    this.yGridDistance = yGridDistance;
  }

  /**
   * This method sets or unsets the drawing of the grid
   *
   * @param isdrawgrid  Boolean: If true, the grid will be drawn on the next
   * paint operation. Otherwise, the grid will not be drawn.
   */
  public void setDrawGrid( boolean isdrawgrid ){
    this.drawGrid = isdrawgrid;
  }

  /**
   * Returns the starting position where the grid will be drawn.
   *
   * @return  Point object indicating the starting position where the grid
   * will be drawn. By default, this is (0,0).
   */
  public Point getStartAt() {
    return this.startAt;
  }

  /**
   * Returns the distance between horizontal grid lines.
   *
   * @return  An int indicating the uniform distance between horizontal grid lines.
   * By default, this is 100.
   */
  public int getXGridDistance() {
    return this.xGridDistance;
  }

  /**
   * Returns the distance between vertical grid lines.
   *
   * @return  An int indicating the uniform distance between vertical grid lines.
   * By default, this is 100.
   */
  public int getYGridDistance() {
    return this.yGridDistance;
  }

  /**
   * Determines if the grid is to be drawn.
   *
   * @return  A boolean indicating if the grid will be drawn on the next
   * paint operation of the containers of the visual graph.
   */
  public boolean getDrawgrid() {
    return this.drawGrid;
  }

  /**
   * Returns the grid object where the visual vertices are laid out.
   *
   * @return  A Grid object where the visual vertices are laid out.
   */
  public Grid getGrid() {
    return this.grid;
  }

  /**
   * Sets the listener object that receives notification whenever a
   * vertex in the graph is laid out, either intermediately or for its
   * final position, by the layout manager.
   *
   * @param   listener    The listener object that will received such notifications.
   * Any previous listener will no longer receive notification.
   */
  public void setGraphLayoutListener( GraphLayoutListener listener ) {
    this.listener = listener;
  }

  /**
   * Partial implementation of the layout() method
   * of GraphLayoutManager. All this do is initialize the grid
   * and does not really layout the vertices at all.
   *
   * Subclasses of this class must override this method and
   * and call this method first before proceeding with any grid
   * actvity.
   */
  public void layout(){
    this.grid = new Grid( this.vgraph.getVisualVertices() );
    this.grid.initGrid();
  }

  public void drawLayout() {
        Iterator      iterator;
        VisualVertex  vvertex;
        Point         gridpoint;
        int           startingxgridpoint = this.grid.getStartingXGridPoint();
        int           startingygridpoint = this.grid.getStartingYGridPoint();


        iterator = this.vgraph.getGraph().getVerticesIterator();
        while( iterator.hasNext()){
            vvertex = this.vgraph.getVisualVertex(  iterator.next());
            gridpoint = this.grid.findVisualVertex( vvertex );
            vvertex.setLocation(
                (int) (this.startAt.x + this.xGridDistance * gridpoint.x -
                    (startingxgridpoint - 1) * this.xGridDistance -
                    vvertex.getBounds2D().getWidth() / 2),
                (int) (this.startAt.y + this.yGridDistance * gridpoint.y -
                    (startingygridpoint - 1) * this.yGridDistance -
                    vvertex.getBounds2D().getHeight() / 2));
        }
        this.vgraph.repaint();
  }

  /**
   * Determines if the graph has been initially laid out.
   * This method should be called prior to any painting to be done by the
   * graph layout manager, as most internal variables are only
   * initialized during layout.
   *
   * @return  True if the graph has at least been laid out once.
   */
  public boolean isInitialized() {
    return this.initialized;
  }

  /**
   * Lays out the visual vertices of the specified connected set general position,
   * meaning that only one visual vertex reside on each grid line. The end result
   * of this operation is that all visual vertices are in a straight diagonal line.
   * The VisualVertex object with the highest degree is placed in the center, and
   * those with lesser degrees are farther away from the center.
   *
   * To accomplish this, the grid matrix must be of size m x m,
   * which is what is done.
   *
   * @param   connectedset  List of VisualVertex that are connected to each other
   * @param   grid          Grid object where the visual vertices in the connected set
   *                        will be laid out for general position.
   * @return  Returns a VisualVertex object which is placed at the center. Put in another
   * way, this is the VisualVertex object with the highest degree. If there are tweo or more
   * vertices with the same highest degree, there is no guarantee as to which among them
   * will be returned or placed in the center first.
   */
  public VisualVertex generalPosition( java.util.List connectedset, Grid grid ){
    int       i, size, center, increment, point;
    int       randomx, randomy;
    Random    random = new Random();
    Iterator  iterator;
    VisualVertex    centervertex;

    size = connectedset.size();

    Collections.sort( connectedset, new NumberofEdgesComparator( this.vgraph.getGraph() ));
    center = size / 2 + ( size % 2 == 0 ? 0 : 1 );
    for( i = 0, increment = 0, point = center + increment - 1, centervertex = (VisualVertex) connectedset.get(0);
      i < size; i++ ) {
      increment = ( i + 1 )/2 * (i % 2 == 0 ? 1 : -1 );
      point = center + increment - 1;
      if( point < 0 || point >= size )
        point = center + (-increment) - 1;

      grid.setGridPoint(
        point, point,
        (VisualVertex) connectedset.get( i ));
    }

    return centervertex;
  }

  /**
   * Positions a VisualVertex to be in median placement, meaning
   * that the vertex should be in the center of all of its adjacent vertices,
   * without adjusting the positions of other vertices.
   *
   * The resulting position is not always on the center, since vertices are
   * restricted to grid points and another vertex may reside on the median,
   * in which case the grid points surrounding median is searched for an
   * alternate median position based on the minimal total edge length of the
   * incident edges of the vertex.
   *
   * @param   vvertex   The VisualVertex object which will be placed in the median
   * of its adjacent vertices.
   */
  private void medianPlacement( VisualVertex vvertex, List finalplacedvertices ){
    this.medianPlacement( vvertex,
      new HashSet( this.vgraph.translateToVisualVertices( this.vgraph.getGraph().getAdjacentVertices( vvertex.getVertex()))),
      this.grid, finalplacedvertices );
  }

  /**
   * Positions a VisualVertex to be in median placement, meaning
   * that the vertex should be in the center of all of its adjacent vertices,
   * specified in the argument adjacentVertices,
   * without adjusting the positions of other vertices.
   *
   * The resulting position is not always on the center, since vertices are
   * restricted to grid points and another vertex may reside on the median,
   * in which case the grid points surrounding median is searched for an
   * alternate median position based on the minimal total edge length of the
   * incident edges of the vertex.
   *
   * @param   vvertex   The VisualVertex object which will be placed in the median
   *                    of its adjacent vertices.
   * @param   adjacentVertices  Position the visual vertex in the center of its
   *                            adjacent vertices specified in this set only.
   */
  private void medianPlacement( VisualVertex vvertex, Set adjacentVertices,
  Grid grid, List finalplacedvertices ) {
    Iterator  iterator;
    Point     point, median, alternatemedian;
    VisualVertex  preplacedvertex;
    int     adjacentcount, xtotal = 0, ytotal = 0;
    int     xmedian, ymedian;

    adjacentcount = adjacentVertices.size();
    // Do not do anything if the number of adjacent vertices is less than 2.
    if( adjacentcount < 1 ) return;
    if( adjacentcount == 1 ) {
      iterator = adjacentVertices.iterator();
      VisualVertex  only1adjacentvertex = (VisualVertex) iterator.next();

      this.minimalEdgePlacement( vvertex, only1adjacentvertex, finalplacedvertices, grid );
      return;
    }

    // Now find the median placement of the vertex
    iterator = adjacentVertices.iterator();
    while( iterator.hasNext()){
      point = grid.findVisualVertex( (VisualVertex) iterator.next());
      xtotal += point.x;
      ytotal += point.y;
    }
    xmedian = xtotal / adjacentcount;
    ymedian = ytotal / adjacentcount;
    //System.out.println( "Calculated median at " + xmedian + ", " + ymedian );

    // Check if there is a vertex on the specified median grid point.
    // If there is none, or if it is the visual vertex itself, then set
    // median placement.
    preplacedvertex = grid.getGridPoint( xmedian, ymedian );
    if( preplacedvertex == null || preplacedvertex.equals( vvertex )){
      //System.out.println( "Set median at " + xmedian + ", " + ymedian );
      grid.setGridPoint( xmedian, ymedian, vvertex );
      }
    // If there is another visual vertex in the median grid point,
    // find an alternate median placement.
    else {
      alternatemedian = this.alternateMedianPlacement( vvertex, adjacentVertices, xmedian, ymedian, grid );

      // Use the alternate median grid point.
      if( alternatemedian != null ){
        //System.out.println( "Set alternate median at " + alternatemedian.x + ", " + alternatemedian.y );
        grid.setGridPoint( alternatemedian.x, alternatemedian.y, vvertex );
      }
      // If there is no alternate median placement, then force a swap
      //else {
        //System.out.println( "Swapped median at " + xmedian + ", " + ymedian );
        // Get the current grid point position of the vertex
      //  point = this.grid.findVisualVertex( vvertex );
        // Swap positions
      //  this.grid.setGridPoint( point.x, point.y, this.grid.getGridPoint( xmedian, ymedian ));
      //  this.grid.setGridPoint( xmedian, ymedian, vvertex );
      //}
    }

    // Notify the registered listener that a vertex has been laid out.
    if( listener != null )
      listener.layoutVisualVertex( new GraphLayoutEvent( this.vgraph, vvertex ));
  }

  /**
   * Finds an alternate median position based on the minimal total edge length
   * of the incident edges of the vertex.
   *
   * @param   vvertex   The VisualVertex which will be placed in the median
   * of its adjacent vertices.
   * @param   adjacentVertices    The adjacent vertices of the vertex.
   * @param   preferredXMedian    The original, preferred x median.
   * @param   preferredXMedian    The original, preferred y median.
   */
  private Point alternateMedianPlacement( VisualVertex vvertex, Set adjacentVertices,
    int preferredXMedian, int preferredYMedian, Grid grid ){

    List    alternateGridPoints = new ArrayList( 10 );
    VisualVertex  preplacedvertex;
    int     alternateXMedian, alternateYMedian;
    double  minimalLength = 0.00, alternateLength;

    int     level, n = 0;

    // Iterate through the preferred median point's surrounding to find
    // an alternate median placement by determining the total length of the
    // all incident edges for each alternation placement. The placement
    // with the lowest edge length will be the alternate median placement.

    for( level = 1; level <= 2; ++level ) {
      for( alternateYMedian = preferredYMedian - 1; alternateYMedian <= preferredYMedian + 1; alternateYMedian++ ) {
        if( level == 1 ) n = 0;
        if( level == 2 ) n = Math.abs( alternateYMedian - preferredYMedian ) == 2 ? 1 : 2;
        for( alternateXMedian = preferredXMedian - 1 * (level==1?1:n);
          alternateXMedian <= preferredXMedian + (level==1?1:n);
          alternateXMedian =  alternateXMedian + (level==1?1:2*(level==1?1:n))) {

          // If the grid point is valid (>=0) and there are no vertex in that grid point....
          if( alternateXMedian >= 0 && alternateYMedian >= 0 ) {
            preplacedvertex = grid.getGridPoint( alternateXMedian, alternateYMedian );
            if ( preplacedvertex == null || preplacedvertex.equals( vvertex ) ) {
              // get the length of the edges if the visualvertex was positioned in this grid point,
              alternateLength = this.getIncidentEdgesLength( adjacentVertices, alternateXMedian, alternateYMedian, grid );

              // ... and if this is less than all of previous edge lengths, empty the
              // set of alternate median placement and add this grid point to the set.
              if( alternateLength < minimalLength || alternateGridPoints.isEmpty() ){
                minimalLength = alternateLength;
                alternateGridPoints.clear();
                alternateGridPoints.add( new Point( alternateXMedian, alternateYMedian ));
              }
              // ... or if it is the same as the previous minimal edge length, simply
              // add it to the set of alternate median placement.
              else if( alternateLength == minimalLength || alternateGridPoints.isEmpty() )
                alternateGridPoints.add( new Point( alternateXMedian, alternateYMedian ));
            }
          }
        }
      }
      // Get the first alternate grid point in the set, if any.
      if( !alternateGridPoints.isEmpty() )
        return (Point) alternateGridPoints.get( 0 );
    }

    // Get the first alternate grid point in the set, if any.
    if( !alternateGridPoints.isEmpty() )
      return (Point) alternateGridPoints.get( 0 );
    else
      return null;
  }

  /**
   * Determines the total length of the incident edges of a vertex
   * if it were to be placed on an alternate median position.
   *
   * @param   adjacentVertices    The adjacent vertices of the vertex.
   * @param   alternateXMedian    The alternate x median.
   * @param   alternateYMedian    The alternate y median.
   * @return  The length of all the incident edges of the vertex if it
   * were placed in the specified alternate median position.
   */
  private double getIncidentEdgesLength( Set adjacentVertices, int alternateXMedian, int alternateYMedian, Grid grid ) {
    Iterator  iterator;
    Point     point;
    double    totaledgelength = 0.00;

    iterator = adjacentVertices.iterator();
    while( iterator.hasNext() ){
      point = grid.findVisualVertex( (VisualVertex) iterator.next() );
      totaledgelength += Math.sqrt(
        Math.pow(Math.abs( alternateXMedian - point.x ),2) +
        Math.pow(Math.abs( alternateYMedian - point.y ),2) );
    }
    return totaledgelength;
  }

  /**
   * Positions all vertices in median placement, starting with the
   * first vertex in the graph.
   */
  public void medianPlacement(){
    VisualVertex  vvertex;
    List        finalplacedvertices = new ArrayList();
    int   i, count = this.vgraph.getGraph().getVerticesCount();

    for( i = 0; i < count; i++ ) {
      vvertex = (VisualVertex) this.vgraph.getVisualVertices().get( i );
      this.medianPlacement( vvertex, finalplacedvertices );
      finalplacedvertices.add( vvertex );
    }
  }

  /**
   * Positions the adjacent vertex of a given vertex for minimal edge placement.
   *
   * @param   vvertex     The VisualVertex object whose adjacent vertex will be placed
   *                      as near as possible to it.
   * @param   adjacentObject  The VisualVertex adjacent to vvertex that will be positioned
   *                          for minimal edge placement.
   * @param   grid        Grid object where the visual vertices are laid out.
   */
  private void minimalEdgePlacement( VisualVertex vvertex, VisualVertex adjacentVertex,
    List finalplaced, Grid grid ) {

    VisualVertex  preplacedVertex;
    Point     point, adjacentVertexpoint, alternatePlacement;
    int       preferredx=0, preferredy=0;

    point = grid.findVisualVertex( vvertex );
    adjacentVertexpoint = grid.findVisualVertex( adjacentVertex );

    // If the adjacent vertex is already in minimal edge placement, then just return.
    if( (Math.abs( point.x - adjacentVertexpoint.x ) == 1 && point.y == adjacentVertexpoint.y) ||
        (Math.abs( point.y - adjacentVertexpoint.y ) == 1 && point.x == adjacentVertexpoint.x)){
        //System.out.println( "   " + adjacentVertex.getVertex() + " was already at minimal position." );
        return;
        }

    // If the adjacent vertex is in its final placement, then reverse the roles of the vertices.
    if( finalplaced.contains( adjacentVertex )) {
      if( Math.abs( point.x - adjacentVertexpoint.x ) == 1 && Math.abs( point.y - adjacentVertexpoint.y ) == 1) {
        //System.out.println( "   " + adjacentVertex.getVertex() + " was already at NEAR minimal position." );
        return;
      }

      if( finalplaced.contains( vvertex )) return;

      //System.out.println( "   Reversing roles with " + adjacentVertex.getVertex()  );
      //System.out.println( "   " + adjacentVertex + " is already in final:" + finalplaced );
      //this.minimalEdgePlacement( adjacentVertex, vvertex, finalplaced, interimfinalplaced, vertexqueue, grid );
      this.minimalEdgePlacement( adjacentVertex, vvertex, finalplaced, grid );
      return;
      }

    // ... find the preferred placement of the adjacent vertex ...
    if( Math.abs( point.x - adjacentVertexpoint.x ) <= Math.abs( point.y - adjacentVertexpoint.y )){
      preferredx = point.x;
      preferredy = point.y > adjacentVertexpoint.y ? point.y - 1 : point.y + 1;
    }

    if( Math.abs( point.x - adjacentVertexpoint.x ) >= Math.abs( point.y - adjacentVertexpoint.y )){
      preferredy = point.y;
      preferredx = point.x > adjacentVertexpoint.x ? point.x - 1 : point.x + 1;
    }

    preplacedVertex = grid.getGridPoint( preferredx, preferredy );
    if( preplacedVertex == null  ){
      //System.out.println( "   Set minimal placement of " + adjacentVertex.getVertex() + ": " + preferredx + "," + preferredy );
      grid.setGridPoint( preferredx, preferredy, adjacentVertex );
      //vertexqueue.add( adjacentVertex );
    }
    //else if ( preplacedVertex != null && !finalplaced.contains( preplacedVertex ) && !interimfinalplaced.contains( preplacedVertex )) {
    else if ( preplacedVertex != null && !finalplaced.contains( preplacedVertex )) {
      Point   origpoint = grid.findVisualVertex( adjacentVertex );

      //System.out.println( "   Swapped " + preplacedVertex.getVertex() + ": " + origpoint.x + "," + origpoint.y );
      grid.setGridPoint( origpoint.x, origpoint.y, preplacedVertex );
      //vertexqueue.add( preplacedVertex );

      //System.out.println( "   Set minimal placement of " + adjacentVertex.getVertex() + ": " + preferredx + "," + preferredy );
      grid.setGridPoint( preferredx, preferredy, adjacentVertex );
      //vertexqueue.add( adjacentVertex );
    }
    else {
      //System.out.println( "   Finding alternate minimal placement because of " + preplacedVertex.getVertex() + " at " + grid.findVisualVertex( preplacedVertex ));
      alternatePlacement = this.alternateMinimalEdgePlacement(
        vvertex, adjacentVertex, point, adjacentVertexpoint, finalplaced, grid, preferredx, preferredy );
        //vvertex, adjacentVertex, point, adjacentVertexpoint, finalplaced, interimfinalplaced, grid, preferredx, preferredy );

      alternatePlacement = this.alternateMinimalEdgePlacement(
        vvertex, adjacentVertex, point, adjacentVertexpoint, finalplaced, grid, preferredx, preferredy,
        new int[] {-1,1,-1,1,0,-1,1}, new int[] {-1,-1,0,0,-2,-2} );
        //vvertex, adjacentVertex, point, adjacentVertexpoint, finalplaced, interimfinalplaced, grid, preferredx, preferredy );

      if( alternatePlacement == null )
        alternatePlacement = this.alternateMinimalEdgePlacement(
          vvertex, adjacentVertex, point, adjacentVertexpoint, finalplaced, grid, preferredx, preferredy,
          new int[] {-1,1,-2,2,-2,2,-1,1}, new int[] {1,1,0,0,-2,-2,-3,-3} );

      if( alternatePlacement == null )
        alternatePlacement = this.alternateMinimalEdgePlacement(
          vvertex, adjacentVertex, point, adjacentVertexpoint, finalplaced, grid, preferredx, preferredy,
          new int[] {-1,1,-2,2,-1,1,-2,2}, new int[] {2,2,2,2,-4,-4,-4,-4} );

      if( alternatePlacement == null )
        alternatePlacement = this.alternateMinimalEdgePlacement(
          vvertex, adjacentVertex, point, adjacentVertexpoint, finalplaced, grid, preferredx, preferredy,
          new int[] {-1,1,-2,2,-3,3,-1,1,-2,2,-3,3}, new int[] {3,3,3,3,3,3,-5,-5,-5,-5,-5,-5} );

      if( alternatePlacement != null ) {
        preplacedVertex = grid.getGridPoint( alternatePlacement.x, alternatePlacement.y );
        if( preplacedVertex != null ) {
          Point   origpoint = grid.findVisualVertex( adjacentVertex );

          //System.out.println( "         Swapping with " + preplacedVertex + " to: " + origpoint );
          grid.setGridPoint( origpoint.x, origpoint.y, preplacedVertex );
          //vertexqueue.add( preplacedVertex );
          }
        grid.setGridPoint( alternatePlacement.x, alternatePlacement.y, adjacentVertex );
        //vertexqueue.add( adjacentVertex );
        }
    }

  }

  /**
   * Finds an alternate minimal edge placement for a specified VisualVertex,
   * testing for each alternate grid point until an alternate position is found
   * or the alternate grid points are exhausted.
   *
   * The process for determining the alternate minimal edge placement is shown
   * by the following diagram below:
   *
   *    3   *P   4      4   P*  3
   *    1    A   2      2   A   1
   *    6    5   7      7   5   6
   *
   * where:
   *    - A is the anchor vertex
   *    - P is the preferred grid point of the adjacent vertex
   *    - 1, 2, 3, 4 ... 7 are the sequence of alternate grid points surrounding
   *      the anchor vertex A to be tested.
   *    - * marks which side of P is the original position of the adjacent vertex
   *      If the adjacent vertex was originally at the left side of the preferred
   *      placement, then use the sequence on the left diagram. Otherwise, use
   *      the sequence on the right diagram.
   *
   * For each alternate grid point, a test is made if there is already a vertex
   * in the alternate grid point. If there is none, that is used as the final
   * placement of the adjacent vertex.
   *
   * However, if there is already a vertex (called preplaced vertex) in the alternate grid point,
   * and it is not the final position of the preplaced vertex, then than grid point
   * is the alternate grid point for the specified vertex. Otherwise,
   * the next alternate grid point ( in the above sequence )
   * is tried until either an alternate grid point is found or all the alternates
   * are exhausted.
   *
   * The above diagram are used if the preferred placement is above the anchor vertex.
   * If the preferred placement is to the right of the anchor vertex, the diagam is rotated as follows:
   *
   *    6   1   3       7   2   4
   *            *
   *    5   A   P       5   A   P
   *                        *
   *    7   2   4       6   1   3
   *
   * Rotate the diagram further if the preferred placement is below or to the left
   * of the anchor vertex.
   *
   * @param   achorObject     Anchor vertex. This is represented as A in the diagram above.
   * @param   adjacentVertex  The VisualVertex adjacent to anchorVertex. This is the
   *                          VisualVertex object we want to position for minimal edge placement.
   * @param   anchorPoint     Point object representing the position of anchorObject within
   *                          the Grid object.
   * @param   adjacentPoint   Point object representing the original position of adjacentVertex.
   * @param   finalplaced     List of visual vertices that must no longer be moved from its current
   *                          grid point assingment.
   * @param   interimfinalplaced  List of visual vertices that must not be moved from its current
   *                          grid point assignment only for the duration of the current vertex's iteration.
   * @param   grid            Grid object where the visual vertices are laid out.
   * @param   preferredx      The x-coordinate of the preferred position of the adjacentVertex.
   * @param   preferredy      The y-coordinate of the preferred position of the adjacentVertex.
   */
  private Point alternateMinimalEdgePlacement(
    VisualVertex anchorVertex, VisualVertex adjacentVertex,
    Point anchorPoint, Point adjacentPoint, List finalplaced,
    Grid grid, int preferredx, int preferredy )
  {
    int       alternatex=0, alternatey=0, newx=0, newy=0;
    int       xstartAt, xinterval;
    int       ystartAt, yinterval;
    boolean   alternatefound = false;
    VisualVertex  preplaced;

    if( preferredx == anchorPoint.x ){
      xstartAt = adjacentPoint.x < anchorPoint.x ? anchorPoint.x - 1 : anchorPoint.x + 1;
      xinterval = adjacentPoint.x < anchorPoint.x ? 2 : -2;
      yinterval = adjacentPoint.y < anchorPoint.y ? -1 : 1;
      //System.out.println( "       Testing alternate points 1 to 4..." );
      for( alternatey = anchorPoint.y; !alternatefound && Math.abs( alternatey - anchorPoint.y )<=1; alternatey+=yinterval ) {
        for( alternatex = xstartAt; !alternatefound && Math.abs( alternatex - anchorPoint.x )==1; alternatex+=xinterval ) {
          if( alternatey>=0 && alternatey < grid.getHeight() &&
              alternatex>=0 && alternatex < grid.getWidth() ) {

            preplaced = grid.getGridPoint( alternatex, alternatey );
            //if( preplaced == null || ( preplaced != null && !finalplaced.contains( preplaced ) && !interimfinalplaced.contains( preplaced ))) {
            if( preplaced == null || ( preplaced != null && !finalplaced.contains( preplaced ) )) {
              alternatefound = true;
              newx = alternatex;
              newy = alternatey;
            }
          }

        }
      }

      if( !alternatefound ) {
        //System.out.println( "       Testing alternate points 5 to 7..." );
        alternatey = anchorPoint.y + (adjacentPoint.y < anchorPoint.y ? 1 : -1 );
        xinterval = adjacentPoint.x < anchorPoint.x ? -1 : 1;
        for( alternatex = anchorPoint.x; !alternatefound && Math.abs( alternatex - anchorPoint.x )<=1; alternatex+=xinterval ) {
          //System.out.println( "alternatex: " + alternatex );
          //System.out.println( "xinterval: " + xinterval );
          if( alternatex>=0 && alternatex < grid.getWidth() &&
              alternatey>=0 && alternatey < grid.getHeight()) {

            preplaced = grid.getGridPoint( alternatex, alternatey );
            //if( preplaced == null || ( preplaced != null && !finalplaced.contains( preplaced ) && !interimfinalplaced.contains( preplaced ))) {
            if( preplaced == null || ( preplaced != null && !finalplaced.contains( preplaced ) )) {
              alternatefound = true;
              newx = alternatex;
              newy = alternatey;
            }
          }

          xinterval =
            (adjacentPoint.x <= anchorPoint.x && alternatex == anchorPoint.x - 1 ? 2 :
            (adjacentPoint.x > anchorPoint.x && alternatex == anchorPoint.x + 1 ? -2 : xinterval ));
        }
      }
    }

    if( preferredy == anchorPoint.y ){
      ystartAt = adjacentPoint.y < anchorPoint.y ? anchorPoint.y - 1 : anchorPoint.y + 1;
      yinterval = adjacentPoint.y < anchorPoint.y ? 2 : -2;
      xinterval = adjacentPoint.x < anchorPoint.x ? -1 : 1;
      //System.out.println( "       Testing alternate points 1 to 4..." );
      for( alternatex = anchorPoint.x; !alternatefound && Math.abs( alternatex - anchorPoint.x )<=1; alternatex+=xinterval ) {
        for( alternatey = ystartAt; !alternatefound && Math.abs( alternatey - anchorPoint.y )==1; alternatey+=yinterval ) {
          if( alternatex>=0 && alternatex < grid.getWidth() &&
              alternatey>=0 && alternatey < grid.getHeight() ) {
            preplaced = grid.getGridPoint( alternatex, alternatey );
            //if( preplaced == null || ( preplaced != null && !finalplaced.contains( preplaced ) && !interimfinalplaced.contains( preplaced ))) {
            if( preplaced == null || ( preplaced != null && !finalplaced.contains( preplaced ) )) {
              alternatefound = true;
              newx = alternatex;
              newy = alternatey;
            }
          }
        }
      }

      if( !alternatefound ) {
        //System.out.println( "       Testing alternate points 5 to 7..." );
        alternatex = anchorPoint.x + (adjacentPoint.x < anchorPoint.x ? 1 : -1 );
        yinterval = adjacentPoint.y < anchorPoint.y ? -1 : 1;
        for( alternatey = anchorPoint.y; !alternatefound && Math.abs( alternatey - anchorPoint.y )<=1; alternatey+=yinterval ) {
          //System.out.println( "alternatey: " + alternatey );
          //System.out.println( "yinterval: " + yinterval );
          if( alternatex>=0 && alternatex < grid.getWidth() &&
              alternatey>=0 && alternatey < grid.getHeight() ) {
            preplaced = grid.getGridPoint( alternatex, alternatey );
            //if( preplaced == null || ( preplaced != null && !finalplaced.contains( preplaced ) && !interimfinalplaced.contains( preplaced ))) {
            if( preplaced == null || ( preplaced != null && !finalplaced.contains( preplaced ) )) {
              alternatefound = true;
              newx = alternatex;
              newy = alternatey;
            }
          }

          yinterval =
            (adjacentPoint.y <= anchorPoint.y && alternatey == anchorPoint.y - 1 ? 2 :
            (adjacentPoint.y > anchorPoint.y && alternatey == anchorPoint.y + 1 ? -2 : yinterval ));
        }
      }

    }

    if( alternatefound )
      return new Point( newx, newy );
    else
      return null;
  }

  private Point alternateMinimalEdgePlacement
  (
    VisualVertex anchorVertex, VisualVertex adjacentVertex,
    Point anchorPoint, Point adjacentPoint, List finalplaced,
    Grid grid, int preferredx, int preferredy, int xtestpoints[], int ytestpoints[] )
  {
    int xarraylength = Array.getLength( xtestpoints );
    int yarraylength = Array.getLength( ytestpoints );
    int arraylength = xarraylength > yarraylength ? yarraylength : xarraylength;
    int swapvalue;
    int multiple = 1, x, y, i;
    int       alternatex=0, alternatey=0, newx=0, newy=0;
    boolean   swap = false;
    boolean   alternatefound = false;
    VisualVertex  preplaced;

    if( preferredx == anchorPoint.x && preferredy > anchorPoint.y )
      multiple = 1;
    if( preferredx == anchorPoint.x && preferredy < anchorPoint.y )
      multiple = -1;
    if( preferredy == anchorPoint.y && preferredx > anchorPoint.x ) {
      multiple = 1;
      swap = true;
    }
    if( preferredy == anchorPoint.y && preferredx < anchorPoint.x ) {
      multiple = -1;
      swap = true;
    }

    for( x = 0; x < arraylength; x++ ){ xtestpoints[x]*=multiple; }
    for( y = 0; y < arraylength; y++ ){ ytestpoints[y]*=multiple; }

    if( swap ) {
      for( i = 0; i < arraylength; i++ ){
        swapvalue = xtestpoints[i];
        xtestpoints[i] = ytestpoints[i];
        ytestpoints[i] = swapvalue;
        }
    }

    for( i = 0; i < arraylength; i++ ){
      alternatex = preferredx + xtestpoints[i];
      alternatey = preferredy + ytestpoints[i];

      if( alternatex>=0 && alternatex < grid.getWidth() &&
        alternatey>=0 && alternatey < grid.getHeight() ) {

        preplaced = grid.getGridPoint( alternatex, alternatey );
        //if( preplaced == null || ( preplaced != null && !finalplaced.contains( preplaced ) && !interimfinalplaced.contains( preplaced ))) {
        if( preplaced == null || ( preplaced != null && !finalplaced.contains( preplaced ) )) {
          alternatefound = true;
          newx = alternatex;
          newy = alternatey;
        }
      }
    }

    if( alternatefound )
      return new Point( newx, newy );
    else
      return null;
  }


  public void run() {
    ThreadQueueObject   threadqueueobject = (ThreadQueueObject) this.threadqueue.remove();
    //System.out.println( "threadqueueobject is " + threadqueueobject );

    if( threadqueueobject != null ) {
      Grid  grid = threadqueueobject.getGrid();

      //System.out.println( " Now strarting work with vertex " + (VisualVertex) threadqueueobject.getConnectedset().get(0) );
      //this.minimalEdgePlacement( threadqueueobject.getConnectedset(), grid );
      //System.out.printlnpr( " Finsihed working with vertex " + (VisualVertex) threadqueueobject.getConnectedset().get(0) );
      //this.appendGrid( grid );
    }
  }

  private synchronized void appendGrid( Grid grid ) {
    this.grids.add( grid );
  }

	/* (non-Javadoc)
	 * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#setRepaint(boolean)
	 */
	public void setRepaint(boolean b) {
		this.repaint = b;
	}

    public void setVisualGraph(VisualGraph vg) {
        this.vgraph = vg;
    }

}

/**
 * NumberofEdgesComparator implements the Comparator interface to compare
 * the number of edges between two VisualVertices.
 *
 * @author		Jesus M. Salvo Jr.
 */
class NumberofEdgesComparator implements Comparator, Serializable {
  /**
   * The Graph object containing the VisualVertices to be compared.
   */
  Graph   graph;

  /**
   * Creates a NumberofEdgesComparator object, specifying the graph containing
   * the VisualVertices to be compared.
   *
   * @param   graph   The Graph object containing the VisualVertices to be compared.
   */
  NumberofEdgesComparator( Graph graph ){
    this.graph = graph;
  }

  /**
   * Compares the number of incident edges between two VisualVertices.
   *
   * @param   o1  VisualVertex or Object object whose number of edges will be compared
   * to o2.
   * @param   o2  VisualVertex or Object object whose number of edges will be compared
   * to o1.
   *
   * @return  Returns -1 if the number of incident edges of o1 is less than o2.
   * Returns 1 if the number of incident edges of 02 is is less than 01.
   * If the number of incident edges are the same, returns 0.
   */
  public int compare( Object o1, Object o2 ) {
    Object    v1 = null, v2 = null;
    int       adjacentv1, adjacentv2;

    if( o1 instanceof Object ) v1 =  o1;
    if( o1 instanceof VisualVertex ) v1 = ((VisualVertex) o1).getVertex();
    if( o2 instanceof Object ) v2 =  o2;
    if( o2 instanceof VisualVertex ) v2 = ((VisualVertex) o2).getVertex();

    adjacentv1 = this.graph.getAdjacentVertices( v1 ).size();
    adjacentv2 = this.graph.getAdjacentVertices( v2 ).size();

    if( adjacentv1 > adjacentv2 ) return -1;
    if( adjacentv1 < adjacentv2 ) return 1;
    return 0;
  }

  /**
   * Indicates wether some other object is "equal to" this comapator.
   * This method simply calls the equals() method of the argument obj.
   *
   * @param   obj   The object reference with which to compare.
   * @return  True if the specified object is also a comparator.
   */
  public boolean equals( Object obj ) {
    return obj.equals( this );
  }
}

class FinalPlacedComparator implements Comparator, Serializable {
  List finalplaced;

  public FinalPlacedComparator( List finalplaced ) {
    this.finalplaced = finalplaced;
  }

  public int compare( Object obj1, Object obj2 ) {
    boolean obj1final = finalplaced.contains( obj1 );
    boolean obj2final = finalplaced.contains( obj2 );

    if( !obj1final && obj2final ) return 1;
    if( obj1final && !obj2final ) return -1;
    return 0;
  }

  public boolean equals( Object comparatorobject ) {
    return comparatorobject.equals( this );
  }
}

class ThreadQueueObject implements Serializable {
  List    connectedset;
  Grid    grid;

  public ThreadQueueObject ( List connectedset, Grid grid ) {
    this.connectedset = connectedset;
    this.grid = grid;
  }

  public List getConnectedset() {
    return this.connectedset;
  }

  public Grid getGrid() {
    return this.grid;
  }
}

class ThreadQueue implements Serializable {
  List  threadqueueobjects;

  public ThreadQueue(){
    threadqueueobjects = new ArrayList( 10 );
  }

  public synchronized void add( Object object ) {
    threadqueueobjects.add( object );
  }

  public synchronized Object remove() {
    if( !threadqueueobjects.isEmpty())
      return threadqueueobjects.remove( 0 );
    else
      return null;
  }

}


class VisualVertexPositionComparator implements Comparator, Serializable {
  Grid    grid;
  int     compareXY = 1;

  public VisualVertexPositionComparator( Grid grid ) {
    this.grid = grid;
  }

  public void setCompareXY( int setter ) {
    this.compareXY = setter;
  }

  public int compare( Object o1, Object o2 ) {
    VisualVertex v1 = (VisualVertex) o1;
    VisualVertex v2 = (VisualVertex) o2;

    if( compareXY >= 0 )
      return grid.findVisualVertex( v1 ).y - grid.findVisualVertex( v2 ).y;
    else
      return grid.findVisualVertex( v1 ).x - grid.findVisualVertex( v2 ).x;
  }

  public boolean equals( Object obj ) {
    return obj.equals( this );
  }
}

