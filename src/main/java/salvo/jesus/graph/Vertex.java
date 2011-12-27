package salvo.jesus.graph;


/**
 * A vertex in a graph.
 *
 * @author		Jesus M. Salvo Jr.
 */
public interface Vertex extends GraphComponent {

		public Object getObject( );

	/**
	 * @param object
	 */
	public void setObject(Object object);

 
}
