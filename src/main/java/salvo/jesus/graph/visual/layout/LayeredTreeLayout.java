package salvo.jesus.graph.visual.layout;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import salvo.jesus.graph.GraphException;
import salvo.jesus.graph.Tree;
import salvo.jesus.graph.Visitor;
import salvo.jesus.graph.algorithm.DepthFirstGraphTraversal;
import salvo.jesus.graph.algorithm.GraphTraversal;
import salvo.jesus.graph.visual.VisualEdge;
import salvo.jesus.graph.visual.VisualGraph;
import salvo.jesus.graph.visual.VisualVertex;
import salvo.jesus.util.VisualGraphComponentPath;

/**
 * @author  Jesus M. Salvo Jr.
 */

public class LayeredTreeLayout extends AbstractGridLayout implements Visitor {

    List    verticesReadyForPositioning;

    List    gridsOfVertices;

 

    public LayeredTreeLayout( VisualGraph vGraph ) {
        super( vGraph );
    }

    /**
     * Execute the layered-tree layout algorithm
     */
    public void layout(){
        GraphTraversal traversal;
        Tree    tree;
        List    visited = new ArrayList( 10 );

        super.layout();
        // Always reinitialise these vertices
        this.verticesReadyForPositioning = new ArrayList( 10 );
        this.initGridsOfVertices();

        // Traverse the tree
        tree = (Tree) this.vgraph.getGraph();
        traversal = new DepthFirstGraphTraversal( this.vgraph.getGraph());
        // During traversal, the Visitor ( this ) performs
        // the necessary work to do the layout
        traversal.traverse( tree.getRoot(), visited, this );

        // Finally, draw
        this.drawLayout();
    }

    private void initGridsOfVertices() {
        int size = this.vgraph.getGraph().getVerticesCount();

        this.gridsOfVertices = new ArrayList( size );
        for( int i = 0; i < size; i++ ){
            this.gridsOfVertices.add( (Grid) null );
        }
    }

    /**
     * Lays out the child nodes of the specified vertex.
     * It is assumed that the child nodes are "ready" for positioning.
     */
    private void layout( Object rootVertex, List children ) {
        Grid    newGrid;
        int     numberOfChildren = children.size();

        if( numberOfChildren == 0 ) {
            // If the node has no children, create a 1x1 grid
            List  singleElementList = new ArrayList( 10 );
            VisualVertex    rootVisualVertex = this.vgraph.getVisualVertex( rootVertex );

            singleElementList.add( rootVisualVertex );
            newGrid = new Grid( singleElementList, 1, 1 );
            // assign the visual vertex to the only point in the grid
            newGrid.setGridPoint( 0, 0, rootVisualVertex );
            // then set the grid in the List
            this.gridsOfVertices.set(
                this.vgraph.getVisualVertices().indexOf( rootVisualVertex ),
                newGrid );
            // Indicate that this vertex is ready for positioning for its parent
            this.verticesReadyForPositioning.add( rootVertex );
        }
        else {
            // If the node has children, it is presumed by the algorithm in visit()
            // that all of its children already has a grid assigned
            // Therefore get all the grids of the children and append them together.
            Grid    childGrid = null;
            int     size = children.size();
            for( int i = 0; i < size; i++ ) {
                VisualVertex    vObject;
                vObject = this.vgraph.getVisualVertex(  children.get( i ));
                if( i == 0 ) {
                    childGrid = (Grid) this.gridsOfVertices.get(
                        this.vgraph.getVisualVertices().indexOf( vObject ));
                }
                if( i > 0 ) {
                    childGrid.appendToRight( (Grid)
                        this.gridsOfVertices.get(
                            this.vgraph.getVisualVertices().indexOf( vObject )));
                }
            }

            // Find the width of the node's direct children, excluding grandchildrens.
            int childMinX = 0, childMaxX = 0, childWidth;
            for( int i = 0; i < size; i++ ) {
                VisualVertex    vObject;
                vObject = this.vgraph.getVisualVertex(  children.get( i ));
                Point vertexPoint = childGrid.findVisualVertex( vObject );
                if( i == 0 ) {
                    childMinX = vertexPoint.x;
                    childMaxX = vertexPoint.x;
                }
                else {
                    childMinX = Math.min( childMinX, vertexPoint.x );
                    childMaxX = Math.max( childMaxX, vertexPoint.x );
                }
            }
            childWidth = childMaxX - childMinX + 1;

            // If the width of the node's direct children is even, make it odd by adding a blank grid
            // in the middle position of its direct children, not the middle position
            // of the total grid's width.
            int insertedColumnX;
            if( childWidth % 2 == 0 ) {
                insertedColumnX = childMinX + Math.round( childWidth / 2 );
                childGrid.insertEmptyGrid( insertedColumnX );

               TreeGridAdjuster adjuster = new TreeGridAdjuster(
                    this.vgraph, rootVertex, childGrid, insertedColumnX );
                adjuster.adjust();
                adjuster = null;
            }

            // Create a new grid for the parent of the children
            List  singleElementList = new ArrayList( 10 );
            VisualVertex    rootVisualVertex = this.vgraph.getVisualVertex( rootVertex );

            singleElementList.add( rootVisualVertex );
            newGrid = new Grid( singleElementList, childGrid.getWidth(), 1 );

            //newGrid = new Grid( singleElementList,
            //    childGrid.getWidth(), 1 );
            newGrid.setGridPoint( childMinX + Math.round( childWidth / 2 ), 0, rootVisualVertex );
            // Now append the concatenated grid of all of its children to the bottom
            newGrid.appendToBottom( childGrid );

            // Finally, set the grid on the List
            this.gridsOfVertices.set(
                this.vgraph.getVisualVertices().indexOf( rootVisualVertex ),
                newGrid );
            // Indicate that this vertex is ready for positioning for its parent
            this.verticesReadyForPositioning.add( rootVertex );
        }
        // Dont forget to replace the grid
        this.grid = newGrid;

        // Now recursively ( upward ) test if we need to layout the parent.
        Tree    tree = (Tree) this.vgraph.getGraph();
        Object  parent;
        List    siblings;

        try {
            parent = tree.getParent( rootVertex );
            siblings = tree.getChildren( parent );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
            return;
        }

        if( parent != null ) {
            // If all of its siblings ( its parent's children ) are ready for positioning,
            // then layout the vertices of the parent
            siblings.remove( rootVertex );
            if( this.verticesReadyForPositioning.containsAll( siblings )) {
                // Add the current vertex being visited back to siblings
                // since the layout() method requires all children.
                siblings.add( rootVertex );
                this.layout( parent, siblings );
                this.verticesReadyForPositioning.add( rootVertex );
            }
        }
    }

    /**
     * Implementation of the visit() method of the Visitor interface.
     */
    public boolean visit( Object vertexToVisit ) {
        Tree    tree = (Tree) this.vgraph.getGraph();
        Object  parent;
        List  siblings;
        List  children = null;

        try {
            parent = tree.getParent( vertexToVisit );
            siblings = tree.getChildren( parent );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
            return false;
        }

        // Remove the vertex being visited since it is not a sibling
        // but it is itself.
        siblings.remove( vertexToVisit );
        try {
            children = tree.getChildren( vertexToVisit );
        }
        catch( GraphException ex ) {
            ex.printStackTrace();
        }

        // If it is a leaf, the layout the leaf by itself
        if( children.size() == 0 ) {
             this.layout( vertexToVisit, children );
        }

        return true;
    }

	/**
	 * @deprecated redirect call to paintEdge
	 */
 public void paintEdge( Graphics2D g2d, VisualEdge vEdge ){
        this.routeEdge( g2d, vEdge );
    }

    public void routeEdge( Graphics2D g2d, VisualEdge vEdge ) {

        Rectangle     fromvertexBounds;
        Rectangle     tovertexBounds;
        GeneralPath   drawPath;
        VisualVertex  visualVertexA = vEdge.getVisualVertexA();
        VisualVertex  visualVertexB = vEdge.getVisualVertexB();

        drawPath = new GeneralPath();;

        fromvertexBounds = visualVertexA.getBounds();
        tovertexBounds = visualVertexB.getBounds();

        // Make sure to clear the GeneralPath() first. Otherwise, the edge's previous
        // path will be redrawn as well.
        drawPath.reset();

        // Start the line from the center of the vEdgertex
        drawPath.moveTo( (float)fromvertexBounds.getCenterX(), (float)fromvertexBounds.getCenterY() );
        drawPath.lineTo( (float)tovertexBounds.getCenterX(), (float)tovertexBounds.getCenterY() );
    		vEdge.setShape(new VisualGraphComponentPath(drawPath));
    }

    public void addVertex(VisualVertex vVertex) {
        // Do nothing here
    }

    public void removeEdge(VisualEdge vEdge) {
        // Do nothing here
    }

    public void removeVertex(VisualVertex vVertex) {
        // Do nothing here
    }

    public void addEdge(VisualEdge vEdge) {
        // Do nothing here
    }

}


class TreeGridAdjuster implements Visitor {

    VisualGraph vGraph;
    Tree    tree;
    Object  parentNode;
    Grid    grid;
    int     insertedColumnX;
    boolean adjustToRight;


    public TreeGridAdjuster( VisualGraph vGraph, Object parentNode,
            Grid grid, int insertedColumnX )
    {
        this.vGraph = vGraph;
        this.tree = (Tree) this.vGraph.getGraph();
        this.parentNode = parentNode;
        this.grid = grid;
        this.insertedColumnX = insertedColumnX;
    }

    public void adjust() {
        List  children;

        try {
            children = this.tree.getChildren( parentNode );
        }
        catch( GraphException ex ) {
            ex.printStackTrace();
            return;
        }

        // Find the child that that is immediately to the right and to the left
        // of the inserted column
        VisualVertex    immediateRightChild = null, immediateLeftChild = null;
        int             immediateRightChildX = grid.getWidth();
        int             immediateLeftChildX = 0;

        for( int i = 0; i < children.size(); i++ ) {
            VisualVertex    vVertex;
            vVertex = this.vGraph.getVisualVertex(  children.get( i ));
            Point vertexPoint = grid.findVisualVertex( vVertex );
            if( vertexPoint.x > insertedColumnX &&
                ( immediateRightChild == null || vertexPoint.x < immediateRightChildX ))
            {
                immediateRightChild = vVertex;
                immediateRightChildX = vertexPoint.x;
            }
            if( vertexPoint.x < insertedColumnX &&
                ( immediateLeftChild == null || vertexPoint.x > immediateLeftChildX ))
            {
                immediateLeftChild = vVertex;
                immediateLeftChildX = vertexPoint.x;
            }
        }

        // Move the granchild nodes to the right
        // whose parent is immediately to the right of the inserted colunm grid
        // but itself is to the left of the inserted column grid
        if( immediateRightChild != null ) {
            this.adjustToRight = true;
            this.visit( immediateRightChild.getVertex() );
        }

        // Move the granchild nodes to the left
        // whose parent is immediately to the left of the inserted colunm grid
        // but itself is to the right of the inserted column grid
        if( immediateLeftChild != null ) {
            this.adjustToRight = false;
            this.visit( immediateLeftChild.getVertex() );
        }
    }

    public boolean visit( Object vertexToVisit ) {
        List  children;

        try {
            children    = this.tree.getChildren( vertexToVisit );
        }
        catch( GraphException ex ) {
            ex.printStackTrace();
            return false;
        }
        int     numberOfChildren = children.size();
        VisualVertex    vObject;
        Point   parentPoint = this.grid.findVisualVertex( this.vGraph.getVisualVertex( vertexToVisit ));
        Point   childPoint;

        for( int i = 0; i < numberOfChildren; i++ ) {
            vObject = this.vGraph.getVisualVertex(  children.get( i ));
            childPoint = grid.findVisualVertex( vObject );

            // If the child is to the left of its parent and is to the left
            // of the inserted column but the parent is to the right of the inserted column,
            // move the child node to the right
            if( adjustToRight && childPoint.x < parentPoint.x ) {
                if( childPoint.x < this.insertedColumnX ) {
                    this.grid.setGridPoint( childPoint.x + 1, childPoint.y, vObject );
                }
                this.visit(  children.get(i) );
            }

            // If the child is to the right of its parent and is to the right
            // of the inserted column but the parent is to the left of the inserted column,
            // move the child node to the left
            if( !adjustToRight && childPoint.x > parentPoint.x ) {
                if( childPoint.x > this.insertedColumnX ) {
                     this.grid.setGridPoint( childPoint.x - 1, childPoint.y, vObject );
                }
                this.visit(  children.get(i) );
            }

        }

        return true;
    }

}
