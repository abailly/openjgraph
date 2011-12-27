/**
 * 
 */
package salvo.jesus.graph.algorithm;

import salvo.jesus.graph.Edge;

/**
 * An interface defining vertex folding operations on a graph. A vertex-fold of
 * a (directed)graph contains two operations:
 * <ul>
 * <li>A predicate {@link #fold(Object)} over all vertices for filtering folded
 * and non folded vertices,</li>
 * <li>A two-place function {@link #merge(Object,Object)} for merging the edges of
 * a folded vertex.</li>
 * </ul>
 * 
 * @author nono
 * 
 */
public interface GraphVertexFold {

	/**
	 * 
	 * @param o
	 *            a vertex in the graph.
	 * @return true of vertex should be folded (ie. suppressed)
	 */
	boolean fold(Object o);

	/**
	 * Ask for the result of merging two edges in one.
	 * 
	 * @param i
	 *            the incoming edge's label.
	 * @param o
	 *            the outgoing edge's lable.
	 * @return a new label. may return null.
	 */
	Object merge(Object i, Object o);

}
