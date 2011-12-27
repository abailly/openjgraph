package salvo.jesus.graph.visual.layout;

import java.awt.Point;

/**
 * An extension of the GraphLayoutManager interface specifically
 * for placing vertices in grids.
 *
 * @author  Jesus M. Salvo Jr.
 */

public interface GraphGridLayoutManager extends GraphLayoutManager {

    /**
    * This method sets the point at which the grid starts.
    *
    * @param startat   Point object indicating the upper left corner of the grid.
    */
    public void setStartAt( Point startat );

    /**
    * This method sets the distance between vertical grids
    *
    * @param xgriddistance   Distance between vertical grids
    */
    public void setXGridDistance( int xgriddistance );

    /**
    * This method sets the distance between horizontal grids
    *
    * @param xgriddistance   Distance between horizontal  grids
    */
    public void setYGridDistance( int ygriddistance );

    /**
    * This method sets or unsets the drawing of the grid
    *
    * @param isdrawgrid  Boolean: If true, the grid will be drawn on the next
    * paint operation. Otherwise, the grid will not be drawn.
    */
    public void setDrawGrid( boolean isdrawgrid );

    /**
    * Returns the grid object where the visual vertices are laid out.
    *
    * @return  A Grid object where the visual vertices are laid out.
    */
    public Grid getGrid();

    /**
    * Returns the starting position where the grid will be drawn.
    *
    * @return  Point object indicating the starting position where the grid
    * will be drawn. By default, this is (0,0).
    */
    public Point getStartAt();

    /**
    * Returns the distance between horizontal grid lines.
    *
    * @return  An int indicating the uniform distance between horizontal grid lines.
    * By default, this is 100.
    */
    public int getXGridDistance();

    /**
    * Returns the distance between vertical grid lines.
    *
    * @return  An int indicating the uniform distance between vertical grid lines.
    * By default, this is 100.
    */
    public int getYGridDistance();

    /**
    * Determines if the grid is to be drawn.
    *
    * @return  A boolean indicating if the grid will be drawn on the next
    * paint operation of the containers of the visual graph.
    */
    public boolean getDrawgrid();
}

