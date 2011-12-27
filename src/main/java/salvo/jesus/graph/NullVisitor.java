package salvo.jesus.graph;

/**
 * A visitor that always return true when visiting.
 *
 * @author  Jesus M. Salvo Jr.
 */

public class NullVisitor implements Visitor,EdgeVisitor {

    public boolean visit( Object vertexToVisit ){
        return true;
    }

    /* (non-Javadoc)
     * @see salvo.jesus.graph.EdgeVisitor#visit(salvo.jesus.graph.Edge)
     */
    public boolean visit(Edge e) {
        return true;
    }
}

