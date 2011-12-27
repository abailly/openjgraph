package salvo.jesus.graph;

/**
 * Tests directed graphs implementation.
 * @author nono
 *
 */
public class DirectedGraphImplTest extends AbstractGraphTest {

	private DirectedGraphImpl graph;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.graph = new DirectedGraphImpl();
	}

	@Override
	Graph getGraph() {
		return graph;
	}

	@Override
	Edge makeEdge(Object from, Object data, Object to) {
		return new DirectedEdgeImpl(from,to,data);
	}

}
