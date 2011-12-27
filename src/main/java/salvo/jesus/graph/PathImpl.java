package salvo.jesus.graph;

import java.util.Iterator;
import java.util.Stack;

/**
 * An implementation of the <tt>Path</tt> interface that is a non-simple path.
 * A non-simple path is a <tt>Path</tt> whereby vertices maybe repeated.
 *
 * @author  Jesus M. Salvo Jr.
 */

public class PathImpl extends GraphImpl implements Path {

    /**
     * Stack of all the vertices in this Path.
     */
    Stack   vertexStack;

    /**
     * Createas an instance of <tt>PathImpl</tt>.
     * All this really does is call the ancestor's constructor
     * <tt>GraphImpl</tt>, then initializes other internal
     * variables.
     */
    public PathImpl() {
        super();
        this.vertexStack = new Stack();
    }

    /**
     * Returns the first <tt>Vertex</tt> in the <tt>Path</tt>.
     */
    public Object getFirstVertex() {
        return  this.vertexStack.firstElement();
    }

    /**
     * Returns the last <tt>Vertex</tt> in the <tt>Path</tt>.
     */
    public Object getLastVertex() {
        return  this.vertexStack.lastElement();
    }

    /**
     * Adds a Object into the Path.
     * <p>
     * This will call <tt>Graph.add( Object )</tt> only if
     * the <tt>Vertex</tt> is not yet part of the <tt>Graph</tt>.
     * Note that this will also automatically add an <tt>Edge</tt> from the
     * last <tt>Vertex</tt> that was added to the this <tt>Vertex</tt>
     * being added.
     * <p>
     * If adding this new <tt>Edge</tt> instance is not desired and you want
     * to add an existing <tt>Edge</tt> instance instead ( i.e.: from
     * an <tt>Edge</tt> in a <tt>Graph</tt> ), then you should call
     * <tt>addEdge( Edge )</tt> instead.
     *
     * @param		newvertex		Object to be added to the Path
     * @throws      IllegalPathException
     */
    public void add( Object newObject ) throws GraphException {
        boolean isStackEmpty = this.vertexStack.empty();
        Object  lastObject = null;

        // Get the top item in the stack before we push
        // another item onto the stack
        if( !isStackEmpty ) {
            lastObject = ( Object ) this.vertexStack.peek();
        }

        // Add the vertex to the Graph only if it is not existing yet.
        if( !this.vertices.contains( newObject )) {
            super.add( newObject );
        }

        // Always add the Object to the stack. Therefore, a Vertex
        // may appear here more than once even though there is only
        // one such Object in the List this.vertices.
        this.vertexStack.push( newObject );

        // Create a new Edge to append to the Path
        if( !isStackEmpty ) {
            super.addEdge( lastObject, newObject );
        }
    }

    /**
     * Removes the last Object that was added in the <tt>Path</tt>.
     */
    public void remove() throws Exception {
        Object  lastObject;
        lastObject =  this.vertexStack.peek();
        super.remove( lastObject );
        this.vertexStack.pop();
    }

    /**
     * Adds an <tt>Edge</tt> into <tt>Path</tt> if and only if the
     * <tt>vertexA</tt> property of the <tt>Edge</tt> being added
     * is the last <tt>Vertex</tt> added to the path.
     *
     * @params      edge       The Edge to be added to the Path
     * @throws      IllegalPathException    Thrown when the <tt>vertexA</tt>
     * property of the <tt>Edge</tt> being added is not the last <tt>Vertex</tt>
     * in the path.
     */
    public void addEdge( Edge edge ) throws GraphException {
        Object  lastObject = null;
        Object  v1 = edge.getVertexA();
        boolean isStackEmpty = this.vertexStack.empty();

        // Add the Edge only if the Edge's vertexA is the last Object in the stack.
        if( !isStackEmpty ) {
            lastObject =  this.vertexStack.peek();
        }

        if( lastObject != null && lastObject == v1 ) {
            super.addEdge( edge );
            this.vertexStack.push( edge.getVertexB() );
        }
        else {
            throw new IllegalPathException();
        }
    }

    /**
     * Throws a NoSuchMethodException as this method is not directly supported by the
     * <tt>Path</tt>. You should use the method <tt>remove()</tt> instead.
     * @throws  NoSuchMethodException
     */
    public void remove( Object v ) throws GraphException {
        throw new GraphModificationException( "Method remove( Object ) is not supported." );
    }

    /**
     * Throws a NoSuchMethodException as this method is not directly supported by the
     * <tt>Path</tt>. You are not allowed to directly remove an <tt>Edge</tt> of
     * classes implementing the <tt>Path</tt> interface.
     * @throws  NoSuchMethodException
     */
    public void removeEdge( Edge e ) throws GraphException {
        throw new GraphModificationException( "Method removeEdge( Edge ) is not supported." );
    }

    /**
     * Throws a NoSuchMethodException as this method is not directly supported by the
     * <tt>Path</tt>. You are not allowed to remove multiple <tt>Vertex</tt>s at once.
     * @throws  NoSuchMethodException
     */
    public void removeEdges( Object v ) throws GraphException {
        throw new GraphModificationException( "Method removeEdges( Object ) is not supported." );
    }

    /**
     * Returns a String representation of the Path.
     */
    public String toString() {
        Iterator    iterator = this.vertexStack.iterator();
        StringBuffer    out = new StringBuffer();
        String          arrow = "->";

        while( iterator.hasNext()) {
            out.append( iterator.next().toString() );
            if( iterator.hasNext()) {
                out.append( arrow );
            }
        }

        return out.toString();
    }

}
