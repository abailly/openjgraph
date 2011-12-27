package salvo.jesus.graph.visual;

import salvo.jesus.graph.Edge;


/**
 * A factory interface for creating VisualGraphComponent objects : VisualEdge
 * , VisualVertices and Arrowheades
 *
 * Objects implementing this interface are created by VisualGraphComponentManager
 * and used by VisualGraph instances to create as needed VisualEdges and
 * VisualVertices.<p>
 * Arrowhead instances are created as needed by various painters object, this
 * class providing an entry point for constructing various style of arrows.
 *
 * @author Arnaud Bailly
 * @version 31082002
 */
public interface VisualGraphComponentFactory {

    /**
     * Creates a VisualVertex given a Object and
     * a VisualGraph.
     *
     * This method should be overriden as needed to provide
     * customized VisualVertex (i.e. with varying properties) 
     * given a Object and a graph
     *
     * @param vertex the Object object to create a VisualVertex from
     * @param graph the VisualGraph object the newly created VisualVertex
     * belongs to
     * @return an initialized VisualVertex
     */
    public VisualVertex createVisualVertex(Object vertex,VisualGraph graph);

    /**
     * Creates a VisualEdge given a Edge and
     * a VisualGraph.
     *
     * This method should be overriden as needed to provide
     * customized VisualEdge (i.e. with varying properties) 
     * given a Edge and a graph
     *
     * @param edge the Edge object to create a VisualEdge from
     * @param graph the VisualGraph object the newly created VisualEdge
     * belongs to
     * @return an initialized VisualEdge
     */
    public VisualEdge createVisualEdge(Edge edge,VisualGraph graph);


    /**
     * Creates an instance of Arrowhead.
     *
     * This method should be overriden as needed to provide
     * customized Arrowhead shapes
     *
     * @return an instance of Arrowhead
     */
    public Arrowhead createArrowhead();

}

