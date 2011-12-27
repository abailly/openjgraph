/*
 * Created on May 5, 2004
 * 
 * $Log: GraphFilter.java,v $
 * Revision 1.1  2004/05/06 14:01:59  bailly
 * added algorithms for morphism and graph filters
 * added methods for storing and retrieving data associated withe edges
 * added equals and hashcode method for edges
 *
 */
package salvo.jesus.graph.algorithm;

import salvo.jesus.graph.Edge;


/**
 * An interface for filtering out graph elements in 
 * subgraph construction
 * <p>
 * This interface should be specialized according to desired 
 * filters. Instances are passed as parameters to the subgraph
 * construction method in @see{salvo.jesus.graph.Graph} instances.
 * 
 * @author nono
 * @version $Id: GraphFilter.java 1268 2006-08-14 13:25:12Z nono $
 */
public interface GraphFilter {

	/**
	 * Callabck method for filtering vertices.
	 * 
	 * @param v the vertex to filter
	 * @return true if v is part of this filter
	 */
	public boolean filter(Object v);
	
	/**
	 * Callabck method for filtering edges.
	 * 
	 * @param e the edge to filter
	 * @return true if e is part of this filter
	 */
	public boolean filter(Edge e);
}
