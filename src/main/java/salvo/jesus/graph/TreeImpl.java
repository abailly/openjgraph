package salvo.jesus.graph;

import java.util.ArrayList;
import java.util.List;

import salvo.jesus.graph.algorithm.DepthFirstGraphTraversal;
import salvo.jesus.graph.algorithm.GraphTraversal;

/**
 * An implementation of a <tt>Tree</tt>. This implementation
 * is a free tree and allows zero, one or more child nodes for each node.
 * A free tree is a tree whereby any of the nodes can be a root node.
 * By defauly, the first node added when the tree is empty is the root node.
 * <p>
 * Directions of <tt>Edge</tt>s in tree are generally ignored.
 * Directionality is basically implied when specifying the root
 * of a <tt>Tree</tt>, where such operation requires it.
 * <p>
 * A <tt>Tree</tt> does not allow cycle paths. So any attempts to
 * create a cycle will thrown a <tt>CycleException</tt>.
 *
 * @author  Jesus M. Salvo Jr.
 */

public class TreeImpl extends GraphImpl implements Tree {

    /**
     * Reference to the root of the <tt>Tree</tt>.
     */
    Object  rootVertex;

    /**
     * Creates an instance of a <tt>TreeImpl</tt>
     */
    public TreeImpl() {
        super();
    }

    /**
     * Sets the root of the <tt>Tree</tt>. The
     * <tt>Vertex</tt> specified must already be in the <tt>Tree</tt>.
     * Otherwise, a <tt>NoSuchVertexException</tt> is returned.
     */
    public void setRoot( Object rootVertex ) throws GraphException {
        if( !this.vertices.contains( rootVertex  )) {
            throw new NoSuchVertexException();
        }
        this.rootVertex = rootVertex;
    }

    /**
     * Returns the current root of the <tt>Tree</tt>. It is
     * possible that this method will return null if the <tt>Tree</tt>
     * is empty.
     */
    public Object getRoot() {
        return this.rootVertex;
    }

    /**
     * Returns true if the specified node is a leaf.
     * @throws  EmptyTreeException is the <tt>Tree</tt> is empty.
     */
    public boolean isLeaf( Object vertex ) throws GraphException {
        if( this.rootVertex == null ) {
            throw new EmptyTreeException();
        }

        // A leaf is one that has zero or one incident Edge.
        if( this.getDegree( vertex ) > 1 )
            return false;
        else
            return true;
    }

    /**
     * Returns the parent node of the node specified by the argument.
     * @throws  EmptyTreeException is the <tt>Tree</tt> is empty.
     */
    public Object getParent( Object vertex ) throws GraphException {
        // Check that root has been specified
        if( this.rootVertex == null ) {
            throw new EmptyTreeException();
        }

        List    visited = new ArrayList( 10 );
        GraphTraversal  traversal = new DepthFirstGraphTraversal( this );
        Object  parent = null;
        List    adjacentVertices = this.getAdjacentVertices( vertex );

        // Now do a depth-first traversal until we stop at our vertex
        traversal.traverse( this.rootVertex, visited, new StopAtVisitor( vertex ));

        // Now starting from the last element in the vertices that were visited,
        // find the first Object which is adjacent to the Object we are interested in.
        int size = visited.size();
        for( int i = size; i > 0; i-- ) {
            parent =  visited.get( i - 1 );
            if( adjacentVertices.contains( parent )) {
                return parent;
            }
        }
        return null;
    }

    /**
     * Returns the child nodes of the node specified by the argument.
     * @throws  EmptyTreeException is the <tt>Tree</tt> is empty.
     */
    public List getChildren( Object vertex ) throws GraphException {
        // Check that root has been specified
        if( this.rootVertex == null ) {
            throw new EmptyTreeException();
        }

        // The children of a node is all its adjacent vertices
        // minus its parent.
        List    children = this.getAdjacentVertices( vertex );

        children.remove( this.getParent( vertex ) );
        return children;
    }

    /**
     * Returns a new instance of a <tt>Tree</tt> that is rooted
     * from the specified node. This method does not alter the <tt>Tree</tt>
     * itself.
     * <p>
     * Although a new <tt>Tree</tt> is returned,
     * the nodes and edges within the new <tt>Tree</tt> are the same
     * instances as those of the <tt>Tree</tt> from where it was taken from.
     *
     * @param   subTreeRootObject   The root of the subtree that we want to return
     * @throws  EmptyTreeException is the <tt>Tree</tt> is empty.
     */
    public Tree getSubTree( Object subTreeRootObject ) throws Exception {
        // Check that root has been specified
        if( this.rootVertex == null ) {
            throw new EmptyTreeException();
        }

        Object  parent = this.getParent( subTreeRootObject );
        List    visited = new ArrayList( 10 );
        GraphTraversal  traversal = new DepthFirstGraphTraversal( this );

        // Add the parent as visited so that we wont be traversing the parent anymore
        // The result of the traversal will be all vertices / nodes
        // that includes the root of the subtree plus all its children
        visited.add( parent );
        traversal.traverse( subTreeRootObject, visited, new NullVisitor() );
        visited.remove( parent );

        // Find out the edge connecting the subtree's root node and the
        // subtree's root's parent node.
        Edge    edgeToParent = null;
        Edge    currentEdge;
        List    incidentEdges = this.getEdges( subTreeRootObject );
        for( int i = 0; i < incidentEdges.size(); i++ ) {
            currentEdge = (Edge) incidentEdges.get( i );
            if( currentEdge.getVertexA() == parent || currentEdge.getVertexB() == parent )
                edgeToParent = currentEdge;
        }

        // Call factory method to create new tree.
        Tree    subTree = this.createTree();

        // Now add all the edges to the subtree.
        // The nodes of the subtree is basically the result of the traversal's
        // visited List plus all their Edges except for the Edge leading to
        // the parent of the subtree's root node.
        Object      currentObject;
        for( int i = 0; i < visited.size(); i++ ) {
            // No need to add the Object manually, because
            // addEdge() will add it for us.
            currentObject =  visited.get( i );

            // Add the Vertex's Edges to the subtree, except for the
            // Edge leading to the parent of the subtree's root node.
            incidentEdges = (List) ((ArrayList) this.getEdges( currentObject )).clone();
            if( currentObject == subTreeRootObject && edgeToParent != null ) {
                incidentEdges.remove( edgeToParent );
            }
            for( int j = 0; j < incidentEdges.size(); j++ ) {
                currentEdge = (Edge) incidentEdges.get( j );
                if( !subTree.isPath( currentEdge.getVertexA(), currentEdge.getVertexB() )) {
                    subTree.addEdge( currentEdge );
                }
            }
        }
        return subTree;
    }

    /**
     * Returns the depth of the node in the <tt>Tree</tt>.
     * @throws  NoSuchVertexException if the specified vertex is not in the <tt>Tree</tt>.
     */
    public int getDepth( Object node ) throws GraphException {
        if( !this.vertices.contains( node )) {
            throw new NoSuchVertexException();
        }

        Object  parent = this.getParent( node );
        int     depth = 1;

        while( parent != null ) {
            parent = this.getParent( parent );
            depth++;
        }

        return depth;
    }

    /**
     * Returns a <tt>List</tt> of the leaves of ths <tt>Tree</tt>.
     */
    public List getLeaves() {
        int size = this.vertices.size();
        List    leaves = new ArrayList( 10 );
        Object  currentVertex;

        for( int i = 0; i < size; i++ ) {
            currentVertex =  this.vertices.get( i );
            if( this.getDegree( currentVertex ) <= 1 ) {
                leaves.add( currentVertex );
            }
        }

        return leaves;
    }

    /**
     * Returns the height of the <tt>Tree</tt>.
     */
    public int getHeight() {
        List    leaves = this.getLeaves();
        int    size = leaves.size();
        int    maxHeight = 0;
        int    currentVertexHeight = 0;

        for( int i = 0; i < size; i++ ) {
            try {
                currentVertexHeight = this.getDepth(  leaves.get( i ));
            }
            catch( Exception ex ) {
                ex.printStackTrace();
            }
            maxHeight = Math.max( currentVertexHeight, maxHeight );
        }
        leaves = null;

        return maxHeight;
    }

    /**
     * Factory method that returns a new instance of <tt>TreeImpl</tt>.
     */
    public Tree createTree() {
        return new TreeImpl();
    }

    /**
     * Determines if there is a path from Object fromObject to Object toVertex.
     * <p>
     * Directionality of Edges along the path are ignored. Therefore,
     * <tt>isPath( a, b )</tt> is the same as <tt>isPath( b, a )</tt>.
     * <p>
     * If either one of the vertices are not in the <tt>Tree</tt>,
     * the result will be false.
     *
     * @param		v1		Endpoint of a path
     * @param		v2	    Endpoint of a path
     * @return	true if there is a path from v1 to v2 or vice-versa. false otherwise.
     */
    public boolean isPath( Object v1, Object v2 ) {
        if( !this.vertices.contains( v1 ) || !this.vertices.contains( v2 )) {
            return false;
        }

        List    visited = new ArrayList( 10 );
        GraphTraversal  traversal = new DepthFirstGraphTraversal( this );

        traversal.traverse( v1, visited, new StopAtVisitor( v2 ));
        if( v2 ==  visited.get( visited.size() - 1 ) )
            return true;
        else
            return false;
    }

    /**
     * Adds a node to the <tt>Tree</tt>. The parent node
     * must already be existing in the <tt>Tree</tt> before
     * the child node can be added. To add the root node,
     * specify null as the parent.
     * <p>
     * Note that the parent and child relativity is dependent
     * for whatever is the current root node of the <tt>Tree</tt>.
     * Hence, if childNode later on becomes root via setRoot(), then the
     * parent actually becomes the child of childNode.
     * <p>
     * This method should be used instead of the superclass' add() methods.
     *
     * @returns     The <tt>Edge</tt> created as a result of
     * adding the child to its parent. This may be null if
     * the parent is null.
     * @throws      GraphException if there is already a root or the child node
     * is already existing.
     * @throws      NoSuchVertexException if the parent is not null and is not existing
     */
    public Edge addNode( Object parent, Object childNode ) throws Exception {
        if( this.rootVertex != null && parent == null ) {
            throw new GraphException( "There is already a root for this Tree" );
        }
        if( parent != null && !this.vertices.contains( parent )) {
            throw new NoSuchVertexException();
        }
        if( this.vertices.contains( childNode )) {
            throw new GraphException( "Child node already exists in Tree" );
        }

        super.add( childNode );
        if( parent == null ) {
            this.rootVertex = childNode;
            return null;
        }
        else {
            // It is important here to call this.addEdge() instead of super.addEdge(),
            // since this.addEdge() has additional checking to make sure the edge
            // will not result in a cycle.
            return this.addEdge( parent, childNode );
        }
    }

    /**
    * Adds an Edge into the Tree. This will only add the Edge
    * if there is currently no path between the two Vertices indicated in the Edge.
    * If there is, a <tt>CycleException</tt> is thrown.
    *
    * @param	e   The edge to be added to the Graph.
    * @throws   <tt>CycleException</tt> if adding the <tt>Edge</tt> will result in a cycle.
    */
    public void addEdge( Edge edge ) throws GraphException {
        if( !this.isPath( edge.getVertexA(), edge.getVertexB() ))
            super.addEdge( edge );
        else
            throw new CycleException();
    }

    /**
    * Adds an Edge into the Tree by creating a new instance of <tt>EdgeImpl</tt>.
    * This will only add the Edge if there is currently no path between the
    * two Vertices indicated in the Edge. If there is,
    * a <tt>CycleException</tt> is thrown.
    *
    * @param	v1      Endpoint of the Edge to be created / added
    * @param    v2      Endpoint of the Edge to be created / added
    * @throws   <tt>CycleException</tt> if creating the <tt>Edge</tt> will result in a cycle.
    */
    public Edge addEdge( Object v1, Object v2 ) throws GraphException {
        if( !isPath( v2, v1 ))
            return super.addEdge( v1, v2 );
        else
            throw new CycleException();
    }

    /**
     * Removes a leaf node from a <tt>Tree</tt>. This method will return
     * an <tt>IllegalTreeException</tt> if you attempt to delete a <tt>Vertex</tt>
     * that is not a leaf node.
     * <p>
     * In this method, a leaf node is defined as a <tt>Vertex</tt> that has zero or
     * one indicent <tt>Edge</tt>.
     *
     * @param   v   The <tt>Vertex</tt> which must be a leaf node that
     * is to be removed from the <tt>Tree</tt>.
     * @throws  IllegalTreeException if removing the <tt>Vertex</tt> will result
     * in a forest of <tt>Tree</tt>s.
     */
    public void remove( Object v ) throws GraphException {
        // Check if removing the vertex will result in a forest.
        if( this.getEdges( v ).size() > 1 ) {
            throw new IllegalTreeException();
        }
        // Safe to remove now
        super.remove( v );
    }

    /**
     * Throws a <tt>NoSuchMethodException</tt>. This method is not supported.
     */
    /*
    public void removeEdge( Edge edge ) throws Exception {
        throw new NoSuchMethodException( "removeEdge( Edge ) is not supported" );
    }
    */

    /**
     * Throws a <tt>NoSuchMethodException</tt>. This method is not supported.
     */
    /*
    public void removeEdges( Object v ) throws Exception {
        throw new NoSuchMethodException( "removeEdges( Object ) is not supported" );
    }
    */
	/* (non-Javadoc)
	 * @see salvo.jesus.graph.Graph#same()
	 */
	public Graph same() {
		Graph g = new GraphImpl();
		g.setGraphFactory(this.getGraphFactory());
		return g;
	}

}
