/*______________________________________________________________________________
 *
 * Copyright 2004 Arnaud Bailly - NORSYS/LIFL
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * (1) Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 * (2) Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 * (3) The name of the author may not be used to endorse or promote
 *     products derived from this software without specific prior
 *     written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *______________________________________________________________________________
 *
 * Created on 17 nov. 2004
 * 
 */
package salvo.jesus.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

/**
 * @author Tim Lambert (http://www.cse.unsw.edu.au/~lambert/)
 * @version $Id: ControlCurve.java 1157 2005-12-01 10:45:46Z nono $
 */
public abstract class ControlCurve implements VisualGraphComponentShape {

  protected Polygon pts;

  protected int selection = -1;

  public ControlCurve() {
    pts = new Polygon();
  }

  static Font f = new Font("Courier", Font.PLAIN, 12);

  /** paint this curve into g. */
  /*
   * public void paint(Graphics g){ FontMetrics fm = g.getFontMetrics(f);
   * g.setFont(f); int h = fm.getAscent()/2;
   *  // draw control points name for(int i = 0; i < pts.npoints; i++) { String
   * s = Integer.toString(i); int w = fm.stringWidth(s)/2;
   * g.drawString(Integer.toString(i),pts.xpoints[i]-w,pts.ypoints[i]+h); } }
   */

  static final int EPSILON = 36; /* square of distance for picking */

  /** return index of control point near to (x,y) or -1 if nothing near */
  public int selectPoint(int x, int y) {
    int mind = Integer.MAX_VALUE;
    selection = -1;
    for (int i = 0; i < pts.npoints; i++) {
      int d = sqr(pts.xpoints[i] - x) + sqr(pts.ypoints[i] - y);
      if (d < mind && d < EPSILON) {
        mind = d;
        selection = i;
      }
    }
    return selection;
  }

  // square of an int
  static int sqr(int x) {
    return x * x;
  }

  /** add a control point, return index of new control point */
  public int addPoint(int x, int y) {
    pts.addPoint(x, y);
    return selection = pts.npoints - 1;
  }

  /** set selected control point */
  public void setPoint(int x, int y) {
    if (selection >= 0) {
      pts.xpoints[selection] = x;
      pts.ypoints[selection] = y;
    }
  }

  /** remove selected control point */
  public void removePoint() {
    if (selection >= 0) {
      pts.npoints--;
      for (int i = selection; i < pts.npoints; i++) {
        pts.xpoints[i] = pts.xpoints[i + 1];
        pts.ypoints[i] = pts.ypoints[i + 1];
      }
    }
  }

  public String toString() {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < pts.npoints; i++) {
      result.append(" " + pts.xpoints[i] + " " + pts.ypoints[i]);
    }
    return result.toString();
  }

  /**
   * 
   */
  public void reset() {
    this.pts = new Polygon();
  }
  /* (non-Javadoc)
   * @see salvo.jesus.util.VisualGraphComponentShape#getBounds()
   */
  public Rectangle getBounds() {
    return pts.getBounds();
  }
  
  /* (non-Javadoc)
   * @see salvo.jesus.util.VisualGraphComponentShape#contains(double, double)
   */
  public boolean contains(double x, double y) {
    return pts.contains(x,y);
  }
  
  /* (non-Javadoc)
   * @see salvo.jesus.util.VisualGraphComponentShape#transform(java.awt.geom.AffineTransform)
   */
  public void transform(AffineTransform transform) {
    double coords[] = new double[6];
    GeneralPath gp = new GeneralPath(pts);
    pts = new Polygon();
    PathIterator it = gp.getPathIterator(transform);
    while(!it.isDone()) {
      it.currentSegment(coords);
      /* update pts */
      pts.addPoint((int)coords[0],(int)coords[1]);
      it.next();
    }
  }
  /* (non-Javadoc)
   * @see salvo.jesus.util.VisualGraphComponentShape#getPathIterator(java.lang.Object)
   */
  public PathIterator getPathIterator(AffineTransform at) {
    return pts.getPathIterator(at);
  }
  /*
   *  (non-Javadoc)
   * @see salvo.jesus.util.VisualGraphComponentShape#getPathIterator(java.awt.geom.AffineTransform)
   */
  public PathIterator getPathIterator(AffineTransform at,double d) {
    return pts.getPathIterator(at,d);
  }
}

/*
 * $Log: ControlCurve.java,v $
 * Revision 1.1  2004/11/18 08:20:10  bailly
 * resynchro
 * added cubic curve shape handling for edgess
 *
 */