package salvo.jesus.graph;


/**
 * Implementation of <tt>CyclePath</tt> that ensures that the first
 * and last vertices in the <tt>Path</tt> forms a cycle.
 *
 * @author  Jesus M. Salvo Jr.
 */

public class CyclePathImpl extends PathImpl implements CyclePath {

    /**
     * Reference to whether we have
     */
    boolean isPathClosed;

    /**
     * Reference if the last Object added to the Path made the path non-simple.
     */
    boolean lastVertexFormedNonSimplePath;

    /**
     * Reference if the last Object added to the Path formed a cycle.
     */
    boolean lastVertexFormedCycle;

    private static final String     CYCLE_PATH_ALREADY_CLOSED = "CyclePath is already closed.";

    /**
     * Creates an instance of <tt>CyclePathImpl</tt>.
     */
    public CyclePathImpl() {
        super();
        this.isPathClosed = false;
        this.lastVertexFormedCycle = false;
    }

    /**
     * Adds a Object into the Path. The check whether or not
     * the resulting <tt>Path</tt> is still simple and non-cycle
     * is done on subsequent calls to <tt>add( Object )</tt> or
     * when <tt>closeCycle()</tt> is called.
     * <p>
     * If adding this new <tt>Edge</tt> instance is not desired and you want
     * to add an existing <tt>Edge</tt> instance instead ( i.e.: from
     * an <tt>Edge</tt> in a <tt>Graph</tt> ), then you should call
     * <tt>addEdge( Edge )</tt> instead.
     *
     * @param		newvertex		Object to be added to the Path
     * @throws      IllegalPathException    See the error message that
     * comes with the Exception generated.
     */
    public void add( Object newObject ) throws GraphException {
        // If path is already closed, throw Exception
        if( this.isPathClosed ) {
            throw new IllegalPathException( CYCLE_PATH_ALREADY_CLOSED );
        }

        // Raise an exception if the last Object added to the path
        // caused to the path to become a cycle ( when the path is not yet closed )
        // or non-simple.
        this.validatePath();

        // Set the flags depending on the effect adding the new Vertex
        // has on the Path for subsequent add() or closeCycle() method calls.
        this.setFlags( newObject );

        // Let PathImpl handle the actual adding of the Object to the Path.
        super.add( newObject );
    }

    /**
     * Adds an <tt>Edge</tt> into <tt>Path</tt>.
     * The check whether or not the resulting <tt>Path</tt>
     * is still simple and non-cycle is done on subsequent calls to
     * <tt>add( Object )</tt> or when <tt>closeCycle()</tt> is called.
     *
     * @params      edge       The Edge to be added to the Path
     * @throws      IllegalPathException    See the error message that
     * comes with the Exception generated.
     */
    public void addEdge( Edge edge ) throws GraphException {
        // If path is already closed, throw Exception
        if( this.isPathClosed ) {
            throw new IllegalPathException( CYCLE_PATH_ALREADY_CLOSED );
        }

        // Raise an exception if the last Object added to the path
        // caused to the path to become a cycle ( when the path is not yet closed )
        // or non-simple.
        this.validatePath();

        // Set the flags depending on the effect adding the new Vertex
        // has on the Path for subsequent add() or closeCycle() method calls.
        this.setFlags( edge.getVertexB() );

        // Let the superclass PathImpl handle the actual adding
        // of the Vertex/Edge to the path.
        super.addEdge( edge );
    }

    /**
     * Removes the last Object that was added in the <tt>Path</tt>.
     */
    public void remove() throws Exception {
        // If path is already closed, throw Exception
        if( this.isPathClosed ) {
            throw new IllegalPathException( CYCLE_PATH_ALREADY_CLOSED );
        }

        // Let the superclass PathImpl handle the actual removal
        // of the Object from the Path.
        super.remove();
    }

    /**
     * Method to be called indicating that the <tt>Path</tt> has been
     * fully specified. Implementations of this method should therefore
     * check that the <tt>Path</tt> is indeed a <tt>Cycle</tt>.
     *
     * @throws  IllegalPathException    Thrown when the CyclePath is already
     * closed or is being closed but is not a cycle.
     */
    public void closeCycle() throws IllegalPathException {
        Object  lastObject = ( Object ) this.vertexStack.peek();

        // If path is already closed, throw Exception
        if( this.isPathClosed ) {
            throw new IllegalPathException( CYCLE_PATH_ALREADY_CLOSED );
        }

        // If last Object added to the Path did not form a cycle,
        // then we do not have a cycle path. Raise exception.
        if( !this.lastVertexFormedCycle ) {
            throw new IllegalPathException( "CyclePath is being closed but is not a cycle." );
        }

        this.isPathClosed = true;
    }

    /**
     * Checks the status of the flags and raise an error if required.
     * This method should only be called when the <tt>CyclePath</tt> is not
     * yet closed or not being closed.
     */
    private void validatePath() throws IllegalPathException {
        // If the last Object added formed a cycle, raise the exception
        if( this.lastVertexFormedCycle ) {
            throw new IllegalPathException( "Path is already a cycle but path is not closed." );
        }

        // If the last Object added change the path to become non-simple,
        // raise the exception
        if( this.lastVertexFormedNonSimplePath ) {
            throw new IllegalPathException( "Path is no longer simple." );
        }
    }

    /**
     * Sets the stastus of the flags depending on what will be the effect should
     * a specified <tt>Vertex</tt> is added to the <tt>CyclePath</tt>.
     *
     * @param   newObject   The <tt>Vertex</tt> that is intended to be added to
     * the <tt>CyclePath</tt>.
     */
    private void setFlags( Object newObject ) {
        // Set the flags depending on the effect of
        // adding the new vertex to the Path. The new status
        // of the flags will be read on the next add( Object )
        // or on closeCycle().
        if( !this.vertexStack.empty() &&
            (( this.vertexStack.get( 0 ) ) == newObject )) {
            this.lastVertexFormedCycle = true;
        }

        if( this.vertexStack.contains( newObject )) {
            this.lastVertexFormedNonSimplePath = true;
        }
    }

}