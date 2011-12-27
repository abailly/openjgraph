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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

/**
 * An interface used by edge painters to define the shape of the edge.
 * 
 * @author nono
 * @version $Id: VisualGraphComponentShape.java 1157 2005-12-01 10:45:46Z nono $
 */
public interface VisualGraphComponentShape {

  /**
   * Draws the shape using given Graphics state.
   * 
   * @param g
   */
  public void draw(Graphics g);

  /**
   * Returns the smallest rectangle enclosing this shape
   * @return
   */
  public Rectangle getBounds();

  /**
   * Checks if this shape contains the given points.
   * 
   * @param x
   * @param y
   * @return
   */
  public boolean contains(double x, double y);

  /**
   * @param transform
   */
  public void transform(AffineTransform transform);

  /**
   * @param object
   * @return
   */
  public PathIterator getPathIterator(AffineTransform at);

  /**
   * @param object
   * @param d
   * @return
   */
  public PathIterator getPathIterator(AffineTransform at, double d);

  /**
   * @param g2d
   */
  public void fill(Graphics g2d);
}

/* 
 * $Log: VisualGraphComponentShape.java,v $
 * Revision 1.1  2004/11/18 08:20:10  bailly
 * resynchro
 * added cubic curve shape handling for edgess
 *
*/