// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Distance.java

package salvo.jesus.graph.algorithm;

import java.util.*;
import salvo.jesus.graph.Graph;


/**
 * A class for computing various metrics on graphs.
 * 
 * @author nono
 * @version $Id$
 */
public class Distance {
    public class Eccentricity {

        double e;

        Object v;

        public Eccentricity(Object v2, double e2) {
            v = v2;
            e = e2;
        }
    }

    public Distance(GraphMatrix im) {
        adjacency = null;
        if (im == null) {
            throw new IllegalArgumentException(
                    "Adjacency matrix algorithm must have been set");
        } else {
            vertices = im.getMatrix().length;
            g = im.getGraph();
            adjacency = im;
            computeDistances();
        }
    }

    private void computeDistances() {
        double from[][] = adjacency.getMatrix();
        /* substitute 0 on non diagonal vertices with +infnty */
        for (int i = 0; i < vertices; i++)
            for (int j = 0; j < vertices; j++)
                if (from[i][j] == 0)
                    from[i][j] = Double.POSITIVE_INFINITY;
        double to[][] = new double[vertices][vertices];
        double tmp[][] = (double[][]) null;
        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    double dij = from[i][j];
                    double cm = from[i][k] + from[k][j];
                    to[i][j] = (dij < cm) ? dij : cm;
                }
            }
            tmp = from;
            from = to;
            to = from;
        }
        distances = from;
    }

    public double eccentricity(Object v) {
        int i = g.getAllVertices().indexOf(v);
        return eccentricity(i);
    }

    private List eccentricities() {
        List l = new ArrayList();
        Iterator it = g.getAllVertices().iterator();
        int i = 0;
        Object v;
        double e;
        for (; it.hasNext(); l.add(new Eccentricity(v, e))) {
            v =  it.next();
            e = eccentricity(i++);
        }

        Collections.sort(l, new Comparator() {

            public boolean equals(Object obj) {
                return false;
            }

            public int compare(Object o1, Object o2) {
                Eccentricity e1 = (Eccentricity) o1;
                Eccentricity e2 = (Eccentricity) o2;
                return e1.e <= e2.e ? e1.e >= e2.e ? 0 : -1 : 1;
            }

        });
        return l;
    }

    public double[][] distances() {
        return distances;
    }

    private double eccentricity(int i) {
        if (distances == null)
            return 1.7976931348623157E+308D;
        double ret = 4.9406564584124654E-324D;
        for (int j = 0; j < vertices; j++)
            if (distances[i][j] > ret)
                ret = distances[i][j];

        return ret;
    }

    public List border() {
        ArrayList ret = new ArrayList();
        List l = eccentricities();
        int sz = l.size();
        double d = ((Eccentricity) l.get(sz - 1)).e;
        ListIterator it = l.listIterator(sz);
        do {
            if (!it.hasPrevious())
                break;
            Eccentricity e = (Eccentricity) it.previous();
            if (e.e < d) {
                it.remove();
                break;
            }
            ret.add(0, e.v);
        } while (true);
        return ret;
    }

    public List center() {
        ArrayList ret = new ArrayList();
        List l = eccentricities();
        int sz = l.size();
        double d = ((Eccentricity) l.get(0)).e;
        ListIterator it = l.listIterator(0);
        do {
            if (!it.hasNext())
                break;
            Eccentricity e = (Eccentricity) it.next();
            if (e.e < d) {
                it.remove();
                break;
            }
            ret.add(e.v);
        } while (true);
        return ret;
    }

    public double diameter() {
        double ret = 4.9406564584124654E-324D;
        for (int i = 0; i < vertices; i++) {
            double e = eccentricity(i);
            if (e > ret)
                ret = e;
        }

        return ret;
    }

    public double radius() {
        double ret = 1.7976931348623157E+308D;
        for (int i = 0; i < vertices; i++) {
            double e = eccentricity(i);
            if (e < ret)
                ret = e;
        }

        return ret;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        String nl = System.getProperty("line.separator");
        for (int i = 0; i < vertices; i++) {
            sb.append("[ ");
            for (int j = 0; j < vertices; j++) {
                if (distances[i][j] == Double.POSITIVE_INFINITY)
                     sb.append('#');
                else
                    sb.append(distances[i][j]);
                sb.append(' ');
            }

            sb.append("]").append(nl);
        }

        return sb.toString();
    }

    private int vertices;

    private double graph[][];

    private double distances[][];

    private GraphMatrix adjacency;

    private Graph g;
}
