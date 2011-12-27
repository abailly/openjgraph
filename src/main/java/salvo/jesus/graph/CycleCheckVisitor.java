package salvo.jesus.graph;

import java.io.Serializable;

/**
 * This is a non-public class the extends salvo.jesus.graph.Vistor
 * to check if there is a cycle from a Object back to itself.
 *
 * @author		Jesus M. Salvo Jr.
 * @see			salvo.jesus.graph.Visitor
 */
class CycleCheckVisitor extends NullVisitor implements Serializable {
  /**
    * Object to check for a cycle path
    */
  Object	objectToCheck;

  /**
    * Creates a new instance of CheckCycleVisitor and specifies
    * which Object to be checked for a cycle path
    *
    * @param		objectToCheck		Object to be checked for a cycle path
    */
  CycleCheckVisitor( Object objectToCheck ){
    super();
    this.objectToCheck = objectToCheck;
  }

  /**
    * Override of superclass' visit() method. Compares the Vertex
    * being visited to the Object we are checking for a cycle path.
    * If they are the same, a cycle has been reached and false is returned.
    * Otherwise, true is returned
    *
    * @param		objectToVisit		Object being visited. This is compared
    * to the Object we are trying to check for a cycle path.
    * @return	false if the Object being visited is the same as the
    * Object we are trying to check for a cycle path. True otherwise.
    */
  public boolean visit( Object objectToVisit ){
    if( objectToVisit == objectToCheck )
      return false;
    else
      return true;
  }
}
