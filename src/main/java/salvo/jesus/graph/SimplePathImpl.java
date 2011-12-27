package salvo.jesus.graph;


/**
 * Implementation of <tt>SimplePath</tt> interface guaranteeing that the
 * path is simple, meaning no <tt>Vertex</tt> is repeated in the path.
 *
 * @author  Jesus M. Salvo Jr.
 */

public class SimplePathImpl extends PathImpl implements SimplePath {

    /**
     * Creates an instance of <tt>SimplePathImpl</tt>.
     */
    public SimplePathImpl() {
        super();
    }

    /**
     * Adds a Object into the Path. This method ensures that the
     * resulting <tt>Path</tt> is still simple. If not, an <tt>IllegalPathException</tt>
     * is thrown.
     * <p>
     * If adding this new <tt>Edge</tt> instance is not desired and you want
     * to add an existing <tt>Edge</tt> instance instead ( i.e.: from
     * an <tt>Edge</tt> in a <tt>Graph</tt> ), then you should call
     * <tt>addEdge( Edge )</tt> instead.
     *
     * @param		newvertex		Object to be added to the Path
     * @throws      IllegalPathException    Thrown if adding the <tt>Vertex</tt>
     * will result to a non-simple <tt>Path</tt>.
     */
    public void add( Object newObject ) throws GraphException {
        // Throw exception if it is already in the path..
        // synonymous to if it is already in the Graph.
        this.validatePath( newObject );
        super.add( newObject );
    }

    /**
     * Adds an <tt>Edge</tt> into <tt>Path</tt>.
     * This method ensures that the resulting <tt>Path</tt> is still simple.
     * If not, an <tt>IllegalPathException</tt> is thrown.
     *
     * @params      edge       The Edge to be added to the Path
     * @throws      IllegalPathException    Thrown when the <tt>vertexA</tt>
     * property of the <tt>Edge</tt> being added is not the last <tt>Vertex</tt>
     * in the path.
     */
    public void addEdge( Edge edge ) throws GraphException {

        // Make sure the vertexB() is not in the Path already
        this.validatePath( edge.getVertexB() );
        // The ancestor method will make sure that the last
        // Object in the stack is the same as vertexA()
        super.addEdge( edge );
    }

    /**
     * Throws an exception if the vertex being added will result in a non-simple path.
     * Otherwise, returns true.
     */
    private void validatePath( Object vertex ) throws IllegalPathException {
        if( this.vertices.contains( vertex )) {
            throw new IllegalPathException( "Adding the Object will result in a non-simple Path.");
        }
    }

}