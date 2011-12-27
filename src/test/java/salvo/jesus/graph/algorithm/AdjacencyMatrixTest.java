package salvo.jesus.graph.algorithm;

import salvo.jesus.graph.DirectedEdgeImpl;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.DirectedGraphImpl;
import salvo.jesus.graph.GraphException;
import junit.framework.TestCase;

public class AdjacencyMatrixTest extends TestCase {

  public void testTransitiveDependencyOnNullMatrix() {
    DirectedGraph dg = new DirectedGraphImpl();
    AdjacencyMatrix aj = new AdjacencyMatrix(dg);
    double[][] cl = aj.transitiveClosure();
    assertTrue("wrong size", cl.length == 0);
  }

  public void testTransitiveDependency1() throws GraphException {
    DirectedGraph dg = new DirectedGraphImpl();
    dg.add("toto");
    dg.add("tutu");
    dg.add("tata");
    dg.addEdge(new DirectedEdgeImpl("toto", "tutu"));
    dg.addEdge(new DirectedEdgeImpl("tutu", "tata"));
    dg.addEdge(new DirectedEdgeImpl("toto", "toto"));
    DirectedGraphAdjacencyMatrix aj = new DirectedGraphAdjacencyMatrix(dg);
    double[][] cl = aj.transitiveClosure();
    int i = dg.getAllVertices().indexOf("toto");
    int j = dg.getAllVertices().indexOf("tata");
    assertEquals("missing edge", 1.0, cl[i][j],0);
    assertEquals("should not have edge", 0, cl[j][i],0);
  }
}
