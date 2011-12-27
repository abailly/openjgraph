package salvo.jesus.graph;

/**
 * Represents an undirected edge in a graph.
 * 
 * @author Jesus M. Salvo Jr.
 */
public class EdgeImpl implements Edge {
    /**
     * The A vertex of the edge.
     */
    protected Object vertexA;

    /**
     * The B vertex of the edge.
     */
    protected Object vertexB;

    /**
     * The data associated with edge.
     */
    protected Object data;

    /**
     * Creates an UndirectedEdge object.
     * 
     * @see Vertex
     */
    public EdgeImpl(Object a, Object b) {
        this(a, b, a.toString() + "-" + b.toString());
    }

    /**
     * Construct an edge with a data object attached
     * 
     * @param a
     *            first vertex
     * @param b
     *            seond vertex
     * @param data
     *            associated data
     */
    public EdgeImpl(Object a, Object b, Object data) {
        if ((a == null) || (b == null))
            throw new IllegalArgumentException("One vertex of edge is null");
        this.vertexA = a;
        this.vertexB = b;
        this.data = data;

    }

    /**
     * Returns the endpoint A of the edge.
     * 
     * @return Object Endpoint A of the edge.
     */
    public Object getVertexA() {
        return this.vertexA;
    }

    /**
     * Returns the endpoint B of the edge.
     * 
     * @return Object Endpoint B of the edge.
     */
    public Object getVertexB() {
        return this.vertexB;
    }

    /**
     * Returns the Object opposite to the specified Object in the edge.
     * 
     * @return Object The Object object that is the opposite to the specifid
     *         Vertex. If the specified Object is not an endpoint of the edge,
     *         returns null.
     */
    public Object getOppositeVertex(Object v) {
        if (this.vertexA == v)
            return this.vertexB;
        else if (this.vertexB == v)
            return this.vertexA;
        else
            return null;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Returns a String representation of the Edge. By default, the format is:
     * fromVertex.toString() + "->" + toVertex.toString()
     * 
     * @return The String representation of the Edge
     * @see Vertex
     */
    public String toString() {
        return this.data.toString();
    }

    /**
     * Creates a clone of this Edge. This calls the EdgeImpl constructor,
     * thereby creating a new instance of EdgeImpl. However, the vertices in
     * both endpoints of the Edge are not cloned.
     * 
     * @return A clone of an instance of Edge.
     */
    protected Object clone() {
        return new EdgeImpl(vertexA, vertexB, data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.Edge#getData()
     */
    public Object getData() {
        return data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        Edge e = (Edge) obj;
        if (e == null)
            return false;
        Object a = e.getVertexA();
        Object b = e.getVertexB();
        if (!getVertexA().equals(a) && !getVertexB().equals(b))
            return false;
        if (getData() == null)
            return e.getData() == null;
        return getData().equals(e.getData());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getVertexA().hashCode() ^ getVertexB().hashCode()
                ^ (data == null ? 0 : getData().hashCode());
    }

}