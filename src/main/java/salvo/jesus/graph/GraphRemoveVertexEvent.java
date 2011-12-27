package salvo.jesus.graph;

import java.util.EventObject;

/**
 * This event is used to notify interested parties that a Object object
 * is about to be removed from a Graph object.
 *
 * @author		Jesus M. Salvo Jr.
 */
public class GraphRemoveVertexEvent extends EventObject {
  /**
    * The Object object that is about to be removed from a Graph object
    */
  Object	vertex;

  /**
    * Creates a GraphRemoveVertexEvent object indicating the source of the event
    * and the Object is about to be removed from a Graph object
    *
    * @param	source		source of the event. This is usually the Graph object
    * where the Object object is to be removed
    * @param	newvertex	Object object that is about to be removed from a Graph object
    */
  public GraphRemoveVertexEvent( Object source, Object vertextoremove ) {
    super( source );
    this.vertex = vertextoremove;
  }

  /**
    * Returns the Object object is about to be removed from a Graph object
    *
    * @return		The Object object to be removed
    */
  public Object getVertex( ) {
    return vertex;
  }
}
