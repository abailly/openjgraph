/*
 * Created on May 6, 2004
 * 
 * $Log: AndGraphFilter.java,v $
 * Revision 1.1  2004/05/06 14:01:59  bailly
 * added algorithms for morphism and graph filters
 * added methods for storing and retrieving data associated withe edges
 * added equals and hashcode method for edges
 *
 */
package salvo.jesus.graph.algorithm;

import salvo.jesus.graph.Edge;


/**
 * Implements a binary and between two graph filters
 * 
 * @author nono
 * @version $Id: AndGraphFilter.java 1268 2006-08-14 13:25:12Z nono $
 */
public class AndGraphFilter implements GraphFilter {

	private GraphFilter b;

	private GraphFilter a;

	public AndGraphFilter(GraphFilter a,GraphFilter b) {
		this.a = a;
		this.b = b;
	}
	
	/* (non-Javadoc)
	 * @see salvo.jesus.graph.algorithm.GraphFilter#filter(salvo.jesus.graph.Vertex)
	 */
	public boolean filter(Object v) {
		return a.filter(v) & b.filter(v);
	}

	/* (non-Javadoc)
	 * @see salvo.jesus.graph.algorithm.GraphFilter#filter(salvo.jesus.graph.Edge)
	 */
	public boolean filter(Edge e) {
		return a.filter(e) & b.filter(e);
	}

}
