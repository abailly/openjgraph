package salvo.jesus.graph;

import java.util.EventObject;

/**
 * This event is used to notify interested parties that a Object object
 * has been added to a Graph object.
 *
 * @author		Jesus M. Salvo Jr.
 */
public class GraphAddVertexEvent extends EventObject {
  /**
    * The Object object that was added to a Graph object
    */
  Object	vertex;

  /**
    * Creates a GraphAddVertexEvent object indicating the source of the event
    * and the Object that was added to a Graph object
    *
    * @param	source		source of the event. This is usually the Graph object
    * where the Object object was added.
    * @param	newvertex	Object object that was added to a Graph object
    */
  public GraphAddVertexEvent( Object source, Object newvertex ) {
    super( source );
    this.vertex = newvertex;
  }

  /**
    * Returns the Object object that was added to a Graph object
    *
    * @return		The Object object added
    */
  public Object getVertex( ) {
    return this.vertex;
  }
}
