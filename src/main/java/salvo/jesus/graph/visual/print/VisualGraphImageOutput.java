/*
 * Created on Jul 9, 2004
 * 
 */
package salvo.jesus.graph.visual.print;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.jibble.epsgraphics.EpsGraphics2D;

import salvo.jesus.graph.visual.VisualGraph;

/**
 * A method that outputs a VisualGraph as an image. Several format can be used
 * according to the capabilities of the underlying platform. This method does
 * not layout the graph.
 * 
 * @author nono
 * @version $Id: VisualGraphImageOutput.java 1404 2007-05-29 18:52:51Z /CN=nono $
 */
public class VisualGraphImageOutput implements VisualGraphOutput {

  /* the format name used for output */
  private String format = "png";

  /*
   * (non-Javadoc)
   * 
   * @see salvo.jesus.graph.visual.print.VisualGraphOutput#output(salvo.jesus.graph.visual.VisualGraph,
   *      java.io.OutputStream)
   */
  public void output(VisualGraph vg, OutputStream os) throws IOException {
    if ("eps".equals(format))
      outputEps(vg, os);
    else
      outputImage(vg, os);
  }

  /**
   * @param vg
   * @param os
   * @throws IOException
   */
  private void outputEps(VisualGraph vg, OutputStream os) throws IOException {
    Dimension dim = vg.getMaxSize();
    EpsGraphics2D g = new EpsGraphics2D("OpenJGraph EPS", os, 0, 0, (int) dim
        .getWidth(), (int) dim.getHeight());
    g.setAccurateTextMode(false);
    g.setBackground(Color.WHITE);
    // g.clearRect(0, 0, dim.width, dim.height);
    /* paint graph to image */
    vg.paint(g);
    g.flush();
    g.close();
  }

  private void outputImage(VisualGraph vg, OutputStream os) throws IOException {
    Dimension dim = vg.getMaxSize();
    BufferedImage im = new BufferedImage(dim.width, dim.height,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D g = im.createGraphics();
    g.setBackground(Color.WHITE);
    g.clearRect(0, 0, dim.width, dim.height);
    /* paint graph to image */
    vg.paint(g);
    /* output graphics */
    Iterator it = ImageIO.getImageWritersBySuffix(format);
    if (!it.hasNext())
      throw new IOException("Cannot find a suitable encoder for " + format);
    ImageIO.write(im, format, os);
  }

  /**
   * @return Returns the format.
   */
  public String getFormat() {
    return format;
  }

  /**
   * @param format
   *          The format to set.
   */
  public void setFormat(String format) {
    this.format = format;
  }
}

/*
 * $Log: VisualGraphImageOutput.java,v $ Revision 1.3 2004/09/07 10:02:23 bailly
 * *** empty log message ***
 * 
 * Revision 1.2 2004/07/23 07:08:15 bailly added verification for the existence
 * of an ImageWriter
 * 
 * Revision 1.1 2004/07/13 15:40:14 bailly added an interface for
 * visualgraphoutput to a file system and implementation class for image output
 * 
 */