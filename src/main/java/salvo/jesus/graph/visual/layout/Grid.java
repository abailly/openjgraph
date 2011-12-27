package salvo.jesus.graph.visual.layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import salvo.jesus.graph.visual.VisualVertex;

/**
 * The Grid class represents a matrix of hypothetical vertical and horizontal grid lines,
 * where each intersection of the grid lines is a grid point.
 * <p>
 * This matrix is used to layout the vertices of a graph, where each VisualVertex is assigned
 * to a grid point and one grid point can have only one VisualVertex.
 * <p>
 * In reality, the matrix will probably non-proportional. That is, certain columns will
 * have different height than the other columns. This usually happens when appending Grids.
 *
 * @author		Jesus M. Salvo Jr.
 */

public class Grid implements Serializable {

  /**
   * The visual vertices that will be laid out on the grid.
   */
  java.util.List          visualVertices;

  /**
   * This is the actual grid. This should have been implemented with
   * two-dimensional array. However, Java has a limit of 255 elements per array.
   *
   * In this case, grid is a List of vertical(x) grid lines.
   * Each vertical grid line in turn is represented by a List of grid points that
   * intersect with (y) horizontal grid lines.
   *
   * The grid point is represented by a VisualVertex object, which is null if there
   * is no VisualVertex assigned to that grid point.
   */
  java.util.List        grid;

  /**
   * Because the grid will be large depending on the number of vertices in the graph,
   * this List indexes the grid point assignments of each VisualVertex, such that
   * the index of the vertex in Graph.visualvertices is the same index of the
   * VisualVertex in this List.
   *
   * Therefore, instead of searching through the Grid to determine the grid point
   * assignment of a VisualVertex, we simply call gridpointassignment.indexOf(),
   * returning a Point object.
   */
  java.util.List        gridPointAssignment;


  /**
   * Creates a Grid object to be used by the vertices.
   * This will also initialize the grid to be of size m x m, where m is the
   * number of vertices specified in the argument.
   *
   * @param   visualvertices    List of VisualVertexs that will be laid out on the grid.
   */
  public Grid( java.util.List visualVertices ){
    this.visualVertices = visualVertices;
    this.initGrid();
  }

  public Grid( java.util.List visualVertices, int width, int height ) {
    this.visualVertices = visualVertices;
    this.initGrid( width, height );

  }

  protected void initGrid( int width, int height ) {
    List    xgrid, ygrid;
    int     i, j;

    // Initialize a two-dimensional grid of w x h dimension.
    // Because arrays in java are limited to a size of 255, we
    // have to implement a List of List for two-dimensional arrays.
    xgrid = new ArrayList( width );
    for( i = 0; i < width; i++ ){
      ygrid = new ArrayList( height );
      for( j = 0; j < height; j++ ) {
        ygrid.add( (VisualVertex) null );
      }
      xgrid.add( ygrid );
    }
    this.grid = xgrid;

    // Initialize the vector that will hold the
    // grid point assignments of the vertices. The index of the visualvertex
    // in visualvertices should match the index of its grid point assignment
    // in this vector
    int size = this.visualVertices.size();
    this.gridPointAssignment = new ArrayList( size );
    for( i = 0; i < size; i++ ){
      this.gridPointAssignment.add( (java.awt.Point) null );
    }
  }

  /**
   * Initializes the grid to be of size m x m, where m is the
   * number of vertices in the graph.
   *
   * This effectively creates List of List of null VisualVertex objects
   * for the internal grid variable.
   *
   * This also initializes the gridpointassignment List of null Points.
   *
   */
  protected void initGrid(){
    int dimension;

    dimension = this.visualVertices.size();
    this.initGrid( dimension, dimension );
  }

  /**
   * Return the size of the internal grid
   */
  public int getGridSize() {
    return this.grid.size();
  }

  public int getStartingXGridPoint() {
    int     i, j, height, width;
    List    ygrid;

    height = this.getHeight();
    width = this.getWidth();

    for( i = 0; i < width; i++ ) {
      ygrid = (List) this.grid.get( i );
      height = ygrid.size();
      for( j = 0; j < height; j++ )
        if( ygrid.get( j ) != null ) return i;
    }

    return -1;
  }

  public int getStartingYGridPoint() {
    int     i, j, height, width;
    List       ygrid;

    height = this.getHeight();
    width = this.getWidth();

    for( j = 0; j < height; j++ ) {
      for( i = 0; i < width; i++ ) {
        ygrid = (List) this.grid.get( i );
        if( ygrid.get( j ) != null ) return j;
      }
    }

    return -1;
  }

  /**
   * Returns the number of x grid lines.
   *
   * @return  An int specifying the number of x (vertical) grid lines.
   */
  public int getWidth() {
    return this.grid.size();
  }

  /**
   * Returns the number of y grid lines.
   *
   * @return  An int specifying the number of y (horizontal) grid lines.
   * If width is zero, this also returns zero. Otherwise, it checks the column
   * with the highest height and returns that height.
   */
  public int getHeight() {
    int maxHeight = 0, width;

    width = this.getWidth();
    if( width == 0 )
        return 0;
    else {
        for( int i = 0; i < width; i++ ) {
            List    sampleYGrid = (List) this.grid.get( i );
            maxHeight = Math.max( maxHeight, sampleYGrid.size());
        }
        return maxHeight;
    }
  }

  /**
   * Assigns a VisualVertex object of the graph to the specified position in the grid.
   * If the VisualVertex has an existing grid point assignment, it is first cleared
   * to guarantee that a vertex has only one grid point assignment.
   *
   * @param   x   The vertical grid line, starting at 0, of the grid point.
   * @param   y   The horizontal grid line, starting at 0, of the grid point.
   * @param   vvertex   The VisualVertex object to be assigned to the specified grid point.
   */
  public void setGridPoint( int x, int y, VisualVertex vvertex ){
    List    ygrid;
    int     size, xi, previousypoint;

    // Before assigning the visual vertex to the specified grid point,
    // remove any prior grid point assignments of the visual vertex.
    // Note: do not use ygrid.remove() or ygrid.removeElement(),
    // as the dimension/size of the ygrid has to be maintained.
    size = this.grid.size();
    for( xi = 0; xi < size; xi++ ) {
      ygrid = (List) this.grid.get( xi );
      previousypoint = ygrid.indexOf( vvertex );
      if( previousypoint >= 0 )
        ygrid.set( previousypoint, (VisualVertex) null );
    }

    // Assign the visual vertex to the specified grid point
    ygrid = (List) this.grid.get( x );
    ygrid.set( y, vvertex );

    // Index the visual vertex's gridpoint
    java.awt.Point   point = new java.awt.Point( x, y );

    this.gridPointAssignment.set( this.visualVertices.indexOf( vvertex ), point );

  }

  /**
   * Returns the VisualVertex object assigned to the specified grid point.
   *
   * @param   x   The vertical grid line, starting at 0, of the grid point.
   * @param   y   The horizontal grid line, starting at 0, of the grid point.
   * @return  The VisualVertex object assigned to the specified grid point. If
   * nothing is currently assigned to the grid point, null is returned.
   */
  public VisualVertex getGridPoint( int x, int y ){
    List    ygrid;

    ygrid = (List) this.grid.get( x );
    return (VisualVertex) ygrid.get( y );
  }

  /**
   * Returns the grid point assignment of a VisualVertex.
   *
   * @param   vvertex   The VisualVertex object of which we want to find
   * the grid point assignment.
   * @return  Point object indicating the grid point assignment
   * of the specified VisualVertex object.
   */
  public java.awt.Point findVisualVertex( VisualVertex v ){
    return (java.awt.Point) this.gridPointAssignment.get( this.visualVertices.indexOf( v ));
  }

  /**
   * Appends the specified grid to this grid. The grid to append will be appended
   * to the right of the grid.
   *
   * @param   gridtoappend    Grid object that is to be appended to this Grid object.
   */
  public void appendToRight( Grid gridtoappend ) {
    int   origwidthgrid = this.getWidth();
    int   addsize = gridtoappend.gridPointAssignment.size();
    int   i;
    java.awt.Point point;

    // Append the grid
    this.grid.addAll( gridtoappend.grid );
    // Append the visual vertices
    this.visualVertices.addAll( gridtoappend.visualVertices );
    // Append the index of grid point assignments, but adjust the point coordinatges
    // accordingly.

    for( i = 0; i < addsize; i++ ) {
      point = (java.awt.Point) gridtoappend.gridPointAssignment.get( i );
      point.translate( origwidthgrid, 0 );

      // Is this necessary??
      gridtoappend.gridPointAssignment.set( i, point );
    }
    this.gridPointAssignment.addAll( gridtoappend.gridPointAssignment );
  }

  /**
   * Appends the specified grid to this grid. The grid to append will be appended
   * to the bottom of the grid.
   *
   * @param   gridtoappend    Grid object that is to be appended to this Grid object.
   */
  public void appendToBottom( Grid gridtoappend ) {
    int   origHeightGrid = this.getHeight();
    int   addsize = gridtoappend.gridPointAssignment.size();
    int   i;
    java.awt.Point point;

    // Append the grid to the bottom, meaning we should increase
    // the List in each of the elements of this.grid
    for( i = 0; i < this.getWidth(); i++ ) {
        List    yGrid = (List) this.grid.get( i );
        List    yGridtoAppend = (List) gridtoappend.grid.get( i );
        yGrid.addAll( yGridtoAppend );

    }

    // Append the visual vertices
    this.visualVertices.addAll( gridtoappend.visualVertices );
    // Append the index of grid point assignments, but adjust the point coordinatges
    // accordingly.

    for( i = 0; i < addsize; i++ ) {
      point = (java.awt.Point) gridtoappend.gridPointAssignment.get( i );
      point.translate( 0, origHeightGrid );

      // Is this necessary??
      gridtoappend.gridPointAssignment.set( i, point );
    }
    this.gridPointAssignment.addAll( gridtoappend.gridPointAssignment );
  }

  /**
   * Inserts an empty grid before the specified index
   */
  public void insertEmptyGrid( int insertBeforeIndex ) {
    // Insert a blank column
    List    newRows = new ArrayList( 10 );

    for( int j = 0; j < this.getHeight(); j++ ) {
        newRows.add( (VisualVertex) null );
    }

    this.grid.add( insertBeforeIndex, newRows );

    // Now translate all points that are beyong this column
    int width = this.getWidth();
    for( int i = insertBeforeIndex + 1; i < width; i++ ) {
        List    yGrid = (List) this.grid.get( i );
        java.awt.Point   point;
        VisualVertex    vv;
        int     vvIndex;
        for( int yGridIndex = 0; yGridIndex < yGrid.size(); yGridIndex++ ) {
            vv = (VisualVertex) yGrid.get( yGridIndex );
            if( vv != null ) {
                vvIndex = this.visualVertices.indexOf( vv );
                point = (java.awt.Point) this.gridPointAssignment.get( vvIndex );
                point.translate( 1, 0 );
            }
        }

      // Is this necessary??
      //this.gridPointAssignment.set( i, point );
    }
  }

  /**
   * Returns a String representation of the Grid object.
   *
   * @return  The String representation of the Grid object.
   */
  public String toString() {
    StringBuffer  output = new StringBuffer("\n");
    List    ygrid;
    VisualVertex  v;
    int x, y, width, height;

    width = this.getWidth();
    height = this.getHeight();
    output.append( "\n--------------------\nColumn:" );
    for( x = 0; x < width; x++ ){
      output.append( "\t\t" +x );
    }

    output.append(  "\n--------------------\n" );
    for( y = 0; y < height; y++ ){
      output.append(  "Line " + y + ":\t\t" );
      for( x = 0; x < width; x++ ){
        ygrid = (List) grid.get( x );
        if( (y <= ygrid.size() - 1) && (ygrid.get( y ) != null) )
            v = (VisualVertex) ygrid.get( y );
        else
            v = null;
        output.append( (v != null ? v.toString():"") + "\t\t" );
      }
      output.append( "\n" );
    }
    output.append( "--------------------\n" );

    return output.toString();
  }

}


