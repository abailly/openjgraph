package salvo.jesus.graph;

import java.io.Serializable;



/**
 * This interface defines a factory for creting Vertices and Edges
 * in a <tt>Graph</tt>. By default, each of the following
 * classes that implement the <tt>Graph</tt> interface uses a different
 * <tt>GraphFactory</tt> as shown below:
 * <p>
 * <table border="1">
 * <tr>
 *      <td>Class implementing the <tt>Graph</tt> interface</td>
 *      <td>Corresponding class implementing the <tt>GraphFactory</tt> interface</td>
 * </tr>
 * <tr>
 *      <td><tt>GraphImpl</tt></td><td><tt>GraphImplFactory</tt></td>
 * </tr>
 * <tr>
 *      <td><tt>DirectedGraphImpl</tt></td><td><tt>DirectedGraphImplFactory</tt></td>
 * </tr>
 * <tr>
 *      <td><tt>DirectedAcylicGraphImpl</tt></td><td><tt>DirectedAcylicGraphImplFactory</tt></td>
 * </tr>
 * <tr>
 *      <td><tt>WeightedGraphImpl</tt></td><td><tt>WeightedGraphImplFactory</tt></td>
 * </tr>
 * <tr>
 *      <td><tt>TreeImpl</tt></td><td><tt>TreeImplFactory</tt></td>
 * </tr>
 * <tr>
 *      <td><tt>BinaryTreeImpl</tt></td><td><tt>TreeImplFactory</tt></td>
 * </tr>
 * </table>
 * <p>
 * If you only want to replace the <tt>Vertex</tt> class being created from the factory,
 * the easiest way is to extend the <tt>GraphFactory</tt> class for the corresponding
 * <tt>Graph</tt> class that you are providing a factory for and just override the
 * <tt>createVertex()</tt> method without overriding the <tt>createEdge()</tt> method.
 * <p>
 * Otherwise, you must ensure that the <tt>Edge</tt> being created via the
 * <tt>createEdge()</tt> method is of the proper type as expected by the corresponding
 * <tt>Graph</tt> class.
 * <p>
 * Implementation of this interface must also have a noarg constructor, in order for it
 * to be properly serialized and deserialized from XML.
 *
 * @author  Jesus M. Salvo Jr.
 */

public interface GraphFactory extends Serializable {
    

    /**
     * Factory method to create an <tt>Edge</tt>.
     * All previous calls to <tt>Graph.createEdge()</tt> will be expected to be
     * replace by calling this method instead.
     * <p>
     * Internally within OpenJGraph, as of 0.9.0, this method is called in
     * <tt>GraphImpl.addEdge( Vertex, Object )</tt>.
     */
    public Edge createEdge( Object v1, Object v2 );

  public Edge createEdgeWith(Object v1,Object v2,Object data);
  /*
    public Object createVertex(String name);
  */
}
