package salvo.jesus.graph;

/**
 * Exception superclass thrown from methods of graphs.
 *
 * @author  Jesus M. Salvo Jr.
 */

public class GraphException extends Exception {

    public GraphException() {
        super();
    }

    public GraphException( String msg ) {
        super( msg );
    }

    /**
     * @param cause
     */
    public GraphException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public GraphException(String message, Throwable cause) {
        super(message,cause);
    }
}