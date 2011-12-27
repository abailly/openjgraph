/*
 * Created on May 5, 2004
 * 
 * $Log: GraphMorphism.java,v $
 * Revision 1.1  2004/05/06 14:01:59  bailly
 * added algorithms for morphism and graph filters
 * added methods for storing and retrieving data associated withe edges
 * added equals and hashcode method for edges
 *
 */
package salvo.jesus.graph.algorithm;

import salvo.jesus.graph.Edge;
import salvo.jesus.graph.Graph;


/**
 * An interface for computing user-defined morphisms.
 * <p>
 * THis interface allow an implementation to define a
 * (homo)morphims on graphs. The methods are called by the 
 * @see{salvo.jesus.graph.GraphOps.morph(Graph,GraphMorphism)} 
 * method to query the implementation for images of vertices
 * and edges. 
 * <p>
 * Note that although the contract for this interface does
 * not mandate this morphism to be consistent between
 * edges and vertices images, the homomorphic property
 * is verified by method morph from class GraphOps. 
 * If a and b have a' and b' respectively
 * as images, and there exists an edge (a,b), then this edge 
 * should be mapped to (a',b') for this morphism to be an 
 * homomorphism. 
 * 
 * @see salvo.jesus.graph.GraphOps
 * @author nono
 * @version $Id: GraphMorphism.java 1268 2006-08-14 13:25:12Z nono $
 */
public interface GraphMorphism {

	/**
	 * Sets the target for following image calls 
	 * to be the given graph.
	 * <p>
	 * This method is used to allow morphism implementation 
	 * to know that a new morphism calculation has started and
	 * that given vertices and edges whose images are queried will
	 * be queried from given graph. This leaves opportunity for
	 * the implementation to use the right factory object for
	 * vertices and edges.
	 * 
	 * @param target the graph whose image is being calculated
	 */
	public void target(Graph target);
	
	/**
	 * Retrieves the image of given vertex by this morphism.
	 * <p>
	 * Please note the image must stay consistent over succesive
	 * calls of image after initial call to {@link #target(Graph)}. This means 
	 * that until a {@link #target(Graph)} method is called, this method
	 * must return the same image given same vertex. HEre same
	 * means same object.
	 * 
	 * @param v the vertex whose image must be calculated
	 * @return a new Object or null if this morphism is 
	 * not defined on v
	 */
	public Object image(Object v);
	
	/**
	 * Retrieves the image of a given edge this morphism
	 * <p>
	 * As it is the case for {@link #image}, returns from
	 * this method must stay constant between successive calls 
	 * to target graph.
	 * 
	 * @param e the edge whose  image is queried
	 * @return a new Edge object or null if this edge has no image
	 * by this morphism
	 */
	public Edge imageOf(Edge e);
	
}
