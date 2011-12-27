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
import java.awt.Polygon;

class Cubic {

  float a,b,c,d;         /* a + b*u + c*u^2 +d*u^3 */

  public Cubic(float a, float b, float c, float d){
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }

  
  /** evaluate cubic */
  public float eval(float u) {
    return (((d*u) + c)*u + b)*u + a;
  }
}

/**
 * @author Tim Lambert (http://www.cse.unsw.edu.au/~lambert/)
 * @version $Id: NaturalCubicSpline.java 1157 2005-12-01 10:45:46Z nono $
 */
public class NaturalCubicSpline extends ControlCurve{
  
  /* calculates the natural cubic spline that interpolates
  y[0], y[1], ... y[n]
  The first segment is returned as
  C[0].a + C[0].b*u + C[0].c*u^2 + C[0].d*u^3 0<=u <1
  the other segments are in C[1], C[2], ...  C[n-1] */
    Cubic[] calcNaturalCubic(int n, int[] x) {
      float[] gamma = new float[n+1];
      float[] delta = new float[n+1];
      float[] D = new float[n+1];
      int i;
      
      /* We solve the equation
       *  [2 1       ] [D[0]]   [3(x[1] - x[0])  ]
       *  |1 4 1     | |D[1]|   |3(x[2] - x[0])  |
       *  |  1 4 1   | | .  | = |      .         |
       *  |    ..... | | .  |   |      .         |
       *  |     1 4 1| | .  |   |3(x[n] - x[n-2])|
       *  [       1 2] [D[n]]   [3(x[n] - x[n-1])]
       *  
       *  by using row operations to convert the matrix to upper triangular
       *  and then back sustitution.  The D[i] are the derivatives at the knots.
       */
      
      gamma[0] = 1.0f/2.0f;
      for ( i = 1; i < n; i++) {
        gamma[i] = 1/(4-gamma[i-1]);
      }
      gamma[n] = 1/(2-gamma[n-1]);
      
      delta[0] = 3*(x[1]-x[0])*gamma[0];
      for ( i = 1; i < n; i++) {
        delta[i] = (3*(x[i+1]-x[i-1])-delta[i-1])*gamma[i];
      }
      delta[n] = (3*(x[n]-x[n-1])-delta[n-1])*gamma[n];
      
      D[n] = delta[n];
      for ( i = n-1; i >= 0; i--) {
        D[i] = delta[i] - gamma[i]*D[i+1];
      }

      /* now compute the coefficients of the cubics */
      Cubic[] C = new Cubic[n];
      for ( i = 0; i < n; i++) {
        C[i] = new Cubic((float)x[i], D[i], 3*(x[i+1] - x[i]) - 2*D[i] - D[i+1],
  		       2*(x[i] - x[i+1]) + D[i] + D[i+1]);
      }
      return C;
    }


    final int STEPS = 12;

    /* draw a cubic spline */
    public void draw(Graphics g){
      if (pts.npoints >= 2) {
        Cubic[] X = calcNaturalCubic(pts.npoints-1, pts.xpoints);
        Cubic[] Y = calcNaturalCubic(pts.npoints-1, pts.ypoints);
        
        /* very crude technique - just break each segment up into steps lines */
        Polygon p = new Polygon();
        p.addPoint((int) Math.round(X[0].eval(0)),
            (int) Math.round(Y[0].eval(0)));
        for (int i = 0; i < X.length; i++) {
          for (int j = 1; j <= STEPS; j++) {
            float u = j / (float) STEPS;
            p.addPoint(Math.round(X[i].eval(u)),
                Math.round(Y[i].eval(u)));
          }
        }
        g.drawPolyline(p.xpoints, p.ypoints, p.npoints);
      }
    }

    public void fill(Graphics g){
      if (pts.npoints >= 2) {
        Cubic[] X = calcNaturalCubic(pts.npoints-1, pts.xpoints);
        Cubic[] Y = calcNaturalCubic(pts.npoints-1, pts.ypoints);
        
        /* very crude technique - just break each segment up into steps lines */
        Polygon p = new Polygon();
        p.addPoint((int) Math.round(X[0].eval(0)),
            (int) Math.round(Y[0].eval(0)));
        for (int i = 0; i < X.length; i++) {
          for (int j = 1; j <= STEPS; j++) {
            float u = j / (float) STEPS;
            p.addPoint(Math.round(X[i].eval(u)),
                Math.round(Y[i].eval(u)));
          }
        }
        g.fillPolygon(p.xpoints, p.ypoints, p.npoints);
      }
    }
}

/* 
 * $Log: NaturalCubicSpline.java,v $
 * Revision 1.1  2004/11/18 08:20:10  bailly
 * resynchro
 * added cubic curve shape handling for edgess
 *
*/