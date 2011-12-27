/**
 * 
 */
package salvo.jesus.graph;

import junit.framework.TestCase;

/**
 * Basic test cases for properties of graph.
 * 
 * @author nono
 * 
 */
public abstract class AbstractGraphTest extends TestCase {

	abstract Graph getGraph();

	abstract Edge makeEdge(Object from, Object data, Object to);

	/**
	 * Adding 1 edge then 1 equal edge to a graph should not add the second one.
	 * Edges are equal if source, target and data are equals.
	 * 
	 * @throws GraphException
	 */
	public void testUniqueEdges() throws GraphException {
		Graph g = getGraph();
		g.add("toto");
		g.add("tutu");
		Edge e = makeEdge("toto", "data", "tutu");
		g.addEdge(e);
		e = makeEdge("toto", "data", "tutu");
		g.addEdge(e);
		assertEquals("wrong number of edges", 1, g.getAllEdges().size());
	}

	public void testUniqueVertices() throws GraphException {
		Graph g = getGraph();
		g.add("toto");
		g.add("toto");
		assertEquals("wrong number of nodes", 1, g.getAllVertices().size());
	}
}
