package salvo.jesus.graph.visual;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * A default implementation of Arrowhead interface.
 * 
 * This is a simple Arrowhead with a flat base, a default size
 * of 10.0 points 
 *
 * @author Salvo Jesus
 */
public class DefaultArrowhead implements Serializable, Arrowhead {
    Line2D		stem;
    Point             head;
    Point             base1, base2, arrowmedian;
    final double		arrowsize = 10.0;
    private Polygon shape = null;
    
    public Shape getShape( Line2D line, Point intersection ){
	if(shape != null)
	    return shape;
	// create polygon and add head point
	stem = line;
	head = intersection;
	shape = new Polygon();
	shape.addPoint(head.x,head.y);
	
	Point2D.Double    edgeto;
	double            dy, dx, distance;
	
	dy = stem.getY2() - stem.getY1();
	dx = stem.getX2() - stem.getX1();
	
	edgeto = new Point2D.Double( stem.getX2(), stem.getY2() );
	distance = edgeto.distance( stem.getX1(), stem.getY1() );
	distance = distance == 0 ? 1 : distance;
	
	arrowmedian = new Point((int) (head.getX() - dx * arrowsize / distance ),
				(int) (head.getY() - dy * arrowsize / distance ));
	
	base1 = new Point((int) (arrowmedian.getX() - dy * ( arrowsize / 2 ) / distance ),
			  (int) (arrowmedian.getY() + dx * ( arrowsize / 2 ) / distance ));
	shape.addPoint(base1.x,base1.y);
	
	base2 = new Point((int) (arrowmedian.getX() + dy * ( arrowsize / 2 ) / distance ),
			  (int) (arrowmedian.getY() - dx * ( arrowsize / 2 ) / distance ));
	shape.addPoint(base2.x,base2.y);
	return shape;	
    }
}

