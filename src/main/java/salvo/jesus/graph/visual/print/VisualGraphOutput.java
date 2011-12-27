/*
 * Created on Jul 9, 2004
 * 
 */
package salvo.jesus.graph.visual.print;

import java.io.IOException;
import java.io.OutputStream;

import salvo.jesus.graph.visual.VisualGraph;

/**
 * Interface implemented by all objects able to output
 * {@link VisualGraph} to an external stream
 *   
 * @author nono
 * @version $Id: VisualGraphOutput.java 1157 2005-12-01 10:45:46Z nono $
 */
public interface VisualGraphOutput {
  
  /**
  	 * Output the given VisualGraph to the given OutputStream. The format
  	 * is implementation dependant. 
  	 * 
   * @param vg the VisualGraph to output
   * @param os the output stream used.
   * @throws IOException
   */
  public void output(VisualGraph vg, OutputStream os) throws IOException;
  
}

/* 
 * $Log: VisualGraphOutput.java,v $
 * Revision 1.1  2004/07/13 15:40:14  bailly
 * added an interface for visualgraphoutput to a file system and implementation class for image output
 *
*/