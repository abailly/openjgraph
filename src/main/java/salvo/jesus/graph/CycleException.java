package salvo.jesus.graph;

/**
 * Thrown when a cycle has occured when it is not desired.
 * This is typically thrown by <tt>DirectedAcyclicGraph</tt>s
 * and <tt>Tree</tt>s.
 *
 * @author  Jesus M. Salvo Jr.
 */

public class CycleException extends GraphModificationException {

    public CycleException() {
        super();
    }

    public CycleException( String msg ) {
        super( msg );
    }

}