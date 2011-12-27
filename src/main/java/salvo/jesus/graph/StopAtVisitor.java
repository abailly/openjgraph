package salvo.jesus.graph;

/**
 * A <tt>Visitor</tt> that notifies a traversal to stop at a particular <tt>Vertex</tt>.
 *
 * @author Jesus M. Salvo Jr.
 *
 * $Id: StopAtVisitor.java 1268 2006-08-14 13:25:12Z nono $
 */

public class StopAtVisitor extends NullVisitor {
  /**
    * Object to check for when visiting a vertex
    */
  Object	objectToCheck;

  /**
    * Creates a new instance of StopAtVisitor and specifies
    * which Object stop
    *
    * @param		objectToCheck		stop at the specified vertex
    */
  public StopAtVisitor( Object objectToCheck ){
    super();
    this.objectToCheck = objectToCheck;
  }

  /**
    * Override of superclass' visit() method. Compares the Vertex
    * being visited to the Object specified in the constructor.
    * If they are the same, return false. Otherwise, return true.
    *
    * @param	objectToVisit		Object being visited.
    * @return	false if the Object being visited is the same as the
    * Object specified in the constructor. True otherwise.
    */
  public boolean visit( Object objectToVisit ){
    if( objectToVisit.equals(objectToCheck) )
      return false;
    else
      return true;
  }

}
