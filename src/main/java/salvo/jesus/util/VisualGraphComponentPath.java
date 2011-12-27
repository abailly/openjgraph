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
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

/**
 * Default class for drawing edges shape. This class encapsulates a
 * {@see GeneralPath}object that stores the shape of the edge.
 * 
 * @author nono
 * @version $Id: VisualGraphComponentPath.java 912 2005-04-05 15:54:08Z bailly $
 */
public class VisualGraphComponentPath implements VisualGraphComponentShape {

    private GeneralPath path;

    /**
     *  
     */
    public VisualGraphComponentPath() {
        this.path = new GeneralPath();
    }

    /**
     * @param path2
     */
    public VisualGraphComponentPath(GeneralPath path2) {
        this.path = path2;
    }

    /**
     * @return Returns the path.
     */
    public GeneralPath getPath() {
        return path;
    }

    /**
     * @param path
     *            The path to set.
     */
    public void setPath(GeneralPath path) {
        this.path = path;
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.util.EdgeShape#draw(java.awt.Graphics)
     */
    public void draw(Graphics g) {
        ((Graphics2D) g).draw(path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.util.VisualGraphComponentShape#getBounds()
     */
    public Rectangle getBounds() {
        return path.getBounds();
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.util.VisualGraphComponentShape#contains(double, double)
     */
    public boolean contains(double x, double y) {
        return path.contains(x, y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.util.VisualGraphComponentShape#transform(java.awt.geom.AffineTransform)
     */
    public void transform(AffineTransform transform) {
        path.transform(transform);
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.util.VisualGraphComponentShape#getPathIterator(java.awt.geom.AffineTransform,
     *      double)
     */
    public PathIterator getPathIterator(AffineTransform at, double d) {

        return path.getPathIterator(at, d);
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.util.VisualGraphComponentShape#getPathIterator(java.awt.geom.AffineTransform)
     */
    public PathIterator getPathIterator(AffineTransform at) {

        return path.getPathIterator(at);
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.util.VisualGraphComponentShape#fill(java.awt.Graphics)
     */
    public void fill(Graphics g2d) {
        ((Graphics2D) g2d).fill(path);
    }

}

/*
 * $Log: VisualGraphComponentPath.java,v $ Revision 1.1 2004/11/18 08:20:10
 * bailly resynchro added cubic curve shape handling for edgess
 *  
 */