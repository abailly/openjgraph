package salvo.jesus.graph;

import java.io.Serializable;

/**
 * A interface for a visitor in the Visitor Pattern.
 */

public interface Visitor extends Serializable {

  public boolean visit( Object vertexToVisit );
}

