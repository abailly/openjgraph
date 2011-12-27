package salvo.jesus.graph;

import java.util.List;

/**
 * The superinterface of all <tt>Tree</tt>s. This interface
 * abstracts a free tree. A free tree is a tree
 * whereby any of the nodes can be a root node.
 *
 * @author  Jesus M. Salvo Jr.
 */

public interface Tree extends Graph {

    /**
     * Sets the root of the <tt>Tree</tt>. The
     * <tt>Vertex</tt> specified must already be in the <tt>Tree</tt>.
     * Otherwise, a <tt>NoSuchVertexException</tt> is returned.
     */
    public void setRoot( Object rootObject ) throws GraphException;

    /**
     * Returns the current root of the <tt>Tree</tt>. It is
     * possible that this method will return null if the <tt>Tree</tt>
     * is empty.
     */
    public Object getRoot();

    /**
     * Returns the parent node of the node specified by the argument.
     * @throws  EmptyTreeException is the <tt>Tree</tt> is empty.
     */
    public Object getParent( Object vertex ) throws GraphException;

    /**
     * Returns the child nodes of the node specified by the argument.
     * @throws  EmptyTreeException is the <tt>Tree</tt> is empty.
     */
    public List getChildren( Object vertex ) throws GraphException;

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
    public Tree getSubTree( Object subTreeRootObject ) throws Exception;

    /**
     * Returns the depth of the node in the <tt>Tree</tt>.
     */
    public int getDepth( Object node ) throws GraphException;

    /**
     * Returns a <tt>List</tt> of the leaves of ths <tt>Tree</tt>.
     */
    public List getLeaves();

    /**
     * Returns the height of the <tt>Tree</tt>.
     */
    public int  getHeight();

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
    public Edge addNode( Object parent, Object childNode ) throws Exception;

    /**
     * Factory method so that implementations of this interface can create
     * the property type of <tt>Tree</tt>.
     */
    public Tree createTree();

    /**
     * Returns true if the specified node is a leaf.
     * @throws  EmptyTreeException is the <tt>Tree</tt> is empty.
     */
    public boolean isLeaf( Object vertex ) throws GraphException;

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
    public boolean isPath( Object v1, Object v2 );


}