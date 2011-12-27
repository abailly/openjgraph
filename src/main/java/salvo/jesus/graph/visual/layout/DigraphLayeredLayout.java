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
 * Created on Aug 16, 2004
 * 
 */
package salvo.jesus.graph.visual.layout;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import salvo.jesus.graph.CycleException;
import salvo.jesus.graph.DirectedAcyclicGraphImpl;
import salvo.jesus.graph.DirectedEdge;
import salvo.jesus.graph.DirectedEdgeImpl;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.Edge;
import salvo.jesus.graph.Path;
import salvo.jesus.graph.PathImpl;
import salvo.jesus.graph.Visitor;
import salvo.jesus.graph.algorithm.BreadthFirstDirectedGraphTraversal;
import salvo.jesus.graph.algorithm.BreadthFirstTraversal;
import salvo.jesus.graph.algorithm.GraphTraversal;
import salvo.jesus.graph.visual.VisualEdge;
import salvo.jesus.graph.visual.VisualGraph;
import salvo.jesus.graph.visual.VisualVertex;
import salvo.jesus.util.ControlCurve;
import salvo.jesus.util.EmptyQueueException;
import salvo.jesus.util.NaturalCubicSpline;
import salvo.jesus.util.Queue;
import salvo.jesus.util.VisualGraphComponentPath;

/**
 * A layout algorithm for directed graphs where vertices are placed in layers
 * according to their dominance order.
 * <p>
 * This instance of LayoutManager is used to layout directed graphs using a
 * classical layer algorithms :
 * <ol>
 * <li>the graph is sorted topologically, with some edges being temporarily
 * inverted to cope for cycles ;</li>
 * <li>each vertex is put into a layer, starting from layer 0. A vertex v is in
 * layer n if there is an edge from a node in layer n-1 to vertex v ;</li>
 * <li>edges are routed to minimize crossing. Some fictitious vertices are
 * created for edges between non adjacent layers ;</li>
 * <li>inverted edges are fixed and the exact coordinates of each vertex is set
 * up. Invisible vertices are used as control points to curve edges.</li>
 * </ol>
 * </p>
 * <p>
 * This algorithm is classical and described in ???
 * </p>
 * 
 * @author nono
 * @version $Id: DigraphLayeredLayout.java 1268 2006-08-14 13:25:12Z nono $
 */
public class DigraphLayeredLayout implements GraphLayoutManager {

    /**
     * Lay out the graph from left to right
     */
    public static final int LR_AXIS = 0;

    /**
     * Lay out the graph from right to left
     */
    public static final int RL_AXIS = 1;

    /**
     * Lay out the graph from top to bottom
     */
    public static final int TD_AXIS = 2;

    /**
     * Layout the graph from bottom to up
     */
    public static final int BU_AXIS = 3;

    abstract class Position {

        abstract void position();
    }

    /* position from left to right */
    private final Position lrposition = new Position() {

        void position() {
            /* calculate maximum size of layers */
            int ml = maxSize(layers);
            /* store each layers' height and width */
            double[][] lh = new double[layers.length][2];
            /* set coordinates */
            double mx = 0.0, my = 0.0;
            double x = margin / 3, y = margin / 3;
            /* position each vertex in each layer */
            for (int i = 0; i < layers.length; i++) {
                double mrg = margin;
                y = margin / 3;
                //                y = margin + mrg;
                Iterator it = layers[i].iterator();
                double lmx = 0;
                /* compute running coordinates */
                while (it.hasNext()) {
                    Object v =  it.next();
                    VisualVertex vv = vg.getVisualVertex(v);
                    if (vv != null) {
                        Rectangle2D bounds = vv.getShape().getBounds();
                        vv.setLocation(x, y);
                        /* set maximum width of layer */
                        if (bounds.getWidth() > lmx)
                            lmx = bounds.getWidth();
                        y += bounds.getHeight() + interval;
                    } else { // v is a Point2D
                        ((Point2D.Double)v).setLocation(x, y);
                        y += interval; /* virtual vertices */
                    }
                }
                if (y > my)
                    my = y;
                x += lmx + margin;
                lh[i][0] = lmx;
                lh[i][1] = y;
            }
            mx = x;
            /* center each layer according to my */
            for (int i = 0; i < layers.length; i++) {
                double offset = my / 2 - lh[i][1] / 2;
                if (offset == 0)
                    continue;
                /* compute running coordinates */
                for (Iterator it = layers[i].iterator(); it.hasNext();) {
                    Object v =  it.next();
                    VisualVertex vv = vg.getVisualVertex(v);
                    if (vv != null) {
                        vv.setLocationDelta(0, offset);
                    } else {
                        Point2D point = (Point2D) v;
                        point.setLocation(point.getX(), point.getY() + offset);
                    }
                }
            }
            vg.setMaxSize(new Dimension((int) (mx + margin / 3),
                    (int) (my + margin / 3)));
        }
    };

    private final Position tdposition = new Position() {

        void position() {
            /* calculate maximum size of layers */
            int ml = maxSize(layers);
            /* store each layers' height and width */
            double[][] lh = new double[layers.length][2];
            /* set coordinates */
            double mx = 0.0, my = 0.0;
            double x = margin / 3, y = margin / 3;
            /* position each vertex in each layer */
            for (int i = 0; i < layers.length; i++) {
                double mrg = margin;
                x = margin / 3;
                //                y = margin + mrg;
                Iterator it = layers[i].iterator();
                double lmy = 0;
                /* compute running coordinates */
                while (it.hasNext()) {
                    Object v =  it.next();
                    VisualVertex vv = vg.getVisualVertex(v);
                    if (vv != null) {
                        Rectangle2D bounds = vv.getShape().getBounds();
                        vv.setLocation(x, y);
                        /* set maximum width of layer */
                        if (bounds.getHeight() > lmy)
                            lmy = bounds.getHeight();
                        x += bounds.getWidth() + interval;
                    } else {
                        ((Point2D.Double)v).setLocation(x, y);
                        x += interval; /* virtual vertices */
                    }
                }
                if (x > mx)
                    mx = x;
                y += lmy + margin;
                lh[i][1] = lmy;
                lh[i][0] = x;
            }
            my = y;
            /* center each layer according to my */
            for (int i = 0; i < layers.length; i++) {
                double offset = mx / 2 - lh[i][0] / 2;
                if (offset == 0)
                    continue;
                /* compute running coordinates */
                for (Iterator it = layers[i].iterator(); it.hasNext();) {
                    Object v =  it.next();
                    VisualVertex vv = vg.getVisualVertex(v);
                    if (vv != null) {
                        vv.setLocationDelta(offset, 0);
                    } else {
                        Point2D point = (Point2D) v;
                        point.setLocation(point.getX() + offset, point.getY());
                    }
                }
            }
            vg.setMaxSize(new Dimension((int) (mx + margin / 3),
                    (int) (my + margin / 3)));
        }
    };

    /*
     * this graph's axis. Default to LR
     */
    private int axis = LR_AXIS;

    /* the graph laid out */
    private VisualGraph vg;

    /* map for inverted edges */
    private Map /* < Edge, Edge > */invert = new HashMap();

    /* map for virtual vertices */
    private Map /* < Edge, Path > */virtuals = new HashMap();

    /* the underlying directed grpah */
    private DirectedGraph g;

    /* map from vertices to layers */
    private HashMap vidx;

    private DirectedAcyclicGraphImpl dag;

    private double interval = 30.0;

    private double margin = 100;

    private List[] layers;

    private int maxPermutation = 25;

    private Random rand = new Random();

    /* maximum interlayers permutation in one step */
    private int Cmax = 5;

    /* the traversal object */
    private GraphTraversal traversal;

    private Set roots;

    /*
     * flag to choose mode of traversal
     */
    private boolean directed;
    
    class MedianComparator implements Comparator {

        /* list of parent/child vertices */
        List source;

        /* incoming/outgoing flag */
        boolean up;

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {
            Object v1 =  o1;
            Object v2 =  o2;
            float f1 = median(v1, source, up);
            float f2 = median(v2, source, up);
            return (f1 > f2) ? 1 : (f1 < f2 ? -1 : 0);
        }

    };

    private MedianComparator comp = new MedianComparator();

    private boolean initialized;

    /**
     * Construct a layout for given visual graph with given axis.
     * 
     * @param vg
     *            the VisualGraph object to layout
     * @param axis
     *            the axis to use.
     */
    public DigraphLayeredLayout(VisualGraph vg, int axis) {
        this.vg = vg;
        this.g = (DirectedGraph) vg.getGraph();
        this.axis = axis;
    }

    /**
     * Construct a layout for given visual graph object
     * 
     * @param vg
     *            the VisualGraph to layout
     * @exception ClassCastException
     *                if the underlying graph of vg is not a DirectedGraph
     *                object
     */
    public DigraphLayeredLayout(VisualGraph vg) {
        this.vg = vg;
        this.g = (DirectedGraph) vg.getGraph();
        this.axis = LR_AXIS;
    }

    /**
     * Argument-less constructor
     *  
     */
    public DigraphLayeredLayout() {
        this.axis = LR_AXIS;
    }

    /**
     * @return Returns the traversal.
     */
    public GraphTraversal getTraversal() {
        return traversal;
    }

    /**
     * @param traversal
     *            The traversal to set.
     */
    public void setTraversal(GraphTraversal traversal) {
        this.traversal = traversal;
    }

    /**
     * The interval is the distance between each vertex on the same layer.
     * 
     * @return Returns the cellSize.
     */
    public double getInterval() {
        return interval;
    }

    /**
     * The interval is the distance between each vertex on the same layer.
     * 
     * @param cellSize
     *            The cellSize to set.
     */
    public void setInterval(double cellSize) {
        this.interval = cellSize;
    }

    /**
     * @return Returns the cmax.
     */
    public int getCmax() {
        return Cmax;
    }

    /**
     * @param cmax
     *            The cmax to set.
     */
    public void setCmax(int cmax) {
        Cmax = cmax;
    }

    /**
     * The margin is used as additional space between each layer of the layout.
     * 
     * @return Returns the margin.
     */
    public double getMargin() {
        return margin;
    }

    /**
     * The margin is used as additional space between each layer
     * 
     * @param margin
     *            The margin to set.
     */
    public void setMargin(double margin) {
        this.margin = margin;
    }

    /**
     * @return Returns the roots.
     */
    public Set getRoots() {
        return roots;
    }

    /**
     * Sets the root vertex that will direct ordering of vertices. This method,
     * together with setTraversal gives the caller some control on the order of
     * traversal of vertices in the graph and hence on the way it is displayed.
     * 
     * @param roots
     *            The roots to set.
     */
    public void setRoots(Set roots) {
        this.roots = roots;
    }

    /**
     * Sets the visual graph to be laid out
     * 
     * @param vg
     *            a VisualGraph object (may not be null)
     * @exception ClassCastException
     *                if the underlying graph of vg is not a DirectedGraph
     *                object
     */
    public void setVisualGraph(VisualGraph vg) {
        this.vg = vg;
        this.g = (DirectedGraph) vg.getGraph();
        this.initialized = false;
    }

    /**
     * Returns the graph this algorithms operates on
     * 
     * @return an instance of VisualGraph
     */
    public VisualGraph getVisualGraph() {
        return vg;
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#isInitialized()
     */
    public boolean isInitialized() {
        return vg != null && initialized;
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#layout()
     */
    public void layout() {
        try {
            /* sort vertices */
            if(!initialized)
                doLayout();
            switch (axis) {
            case LR_AXIS:
                lrposition.position();
                break;
            case TD_AXIS:
                tdposition.position();
                break;
            default:
                lrposition.position();
            }
            vg.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Computes abstract structure of the layout
     * 
     * @throws Exception
     */
    private void doLayout() throws Exception {
        layers = sortLayers();
        /* make virtual vertices and edges */
        splitPath(layers);
        /* minimize crossing between layers */
        minimizeCrossing(layers);
        this.initialized = true;
    }

    /**
     * @param layers2
     * @return
     */
    private int maxSize(List[] layers2) {
        int m = 0;
        for (int i = 0; i < layers2.length; i++) {
            if (layers[i].size() > m)
                m = layers[i].size();
        }
        return m;
    }

    /**
     * @param layers
     */
    private void minimizeCrossing(List[] layers) {
        /* phase Ia */
        boolean moved = false;
        int c = 0;
        do {
            moved = false;
            for (int i = 0; i < layers.length - 1; i++) {
                c++;
                moved = optimize2(i, i + 1, true);
            }
            /* phase Ib */
            /* phase Ia */
            for (int i = layers.length - 1; i > 0; i--) {
                c++;
                moved = optimize2(i, i - 1, false);
            }
        } while (moved && (c < Cmax));
    }

    /*
     * optimize crossings between layers using median position : the target
     * vertex is put at a position in list2 which is nearest the median position
     * of all its sources vertices. if this position is already occupied, it is
     * put upper or lower, whichever minimizes number of crossings.
     */
    private boolean optimize2(int j, int k, boolean up) {
        List list = layers[j], list2 = layers[k];
        List nl = new ArrayList(list2);
        /* sort list2 according to median positions of its members */
        comp.up = up;
        comp.source = list;
        Collections.sort(nl, comp);
        if ((up && (crossings(list, nl) < crossings(list, list2)))
                || (!up && (crossings(nl, list) < crossings(list2, list)))) {
            /* set list2 */
            layers[k] = nl;
            return true;
        }
        /* no movement */
        return false;
    }

    /**
     * @param v
     * @param list
     * @param i
     * @return
     */
    private float median(Object v, List list, boolean up) {
        float m = 0;
        int n = 0;
        Iterator it = up ? dag.getIncomingAdjacentVertices(v).iterator() : dag
                .getOutgoingAdjacentVertices(v).iterator();
        while (it.hasNext()) {
            int k = list.indexOf(it.next());
            if (k != -1) {
                m += k;
                n++;
            }
        }
        if (n > 0)
            return (m / n);
        else
            return 0;
    }

    /**
     * Compute number of crossings between two adjacent layers
     * 
     * @param list
     * @param list2
     * @return
     */
    private int crossings(List list, List list2) {
        int cross = 0;
        List edges = new ArrayList();
        int l1 = list.size(), l2 = list2.size();
        /* incidence matrix from list -> list2 */
        int[][] incident = new int[list.size()][list2.size()];
        Iterator it = list.iterator();
        /* create a list of all edges between the two layers */
        int i = 0, j = 0;
        while (it.hasNext()) {
            Object v =  it.next();
            Iterator it2 = dag.getOutgoingEdges(v).iterator();
            while (it2.hasNext()) {
                DirectedEdge de = (DirectedEdge) it2.next();
                j = list2.indexOf(de.getVertexB());
                if (j > -1)
                    incident[i][j] = 1;
            }
            i++;
        }
        /* compute crossings from matrix */
        for (i = 0; i < l1; i++)
            for (j = i; j < l1; j++) {
                int cr1 = 0, cr2 = 0;
                for (int k = 0; k < l2; k++) {
                    if (incident[j][k] == 1 && incident[i][k] == 0) {
                        cr2++;
                    } else if (incident[i][k] == 1 && incident[j][k] == 0) {
                        if (cr2 > 0)
                            cross += cr2;
                    }
                }
            }
        /* check each edge against its peers */
        /*
         * for (i = 0; i < edges.size(); i++) for (j = i + 1; j < edges.size();
         * j++) { DirectedEdge de1 = (DirectedEdge) edges.get(i); DirectedEdge
         * de2 = (DirectedEdge) edges.get(j); int v1 =
         * list.indexOf(de1.getVertexA()); int v2 =
         * list2.indexOf(de1.getVertexB()); int w1 =
         * list.indexOf(de2.getVertexA()); int w2 =
         * list2.indexOf(de2.getVertexB()); if (((v1 > w1) && (w2 > v2)) || ((v1 <
         * w1) && (w2 < v2))) cross++; }
         */
        return cross;
    }

    /**
     * @throws Exception
     *  
     */
    private void splitPath(List[] lays) throws Exception {
        /* add all vertices */
        Iterator vit = dag.getAllVertices().iterator();
        while (vit.hasNext()) {
            Object v =  vit.next();
            /* add all edges */
            Iterator eit = new ArrayList(dag.getOutgoingEdges(v)).iterator();
            while (eit.hasNext()) {
                DirectedEdge de = (DirectedEdge) eit.next();
                int i = ((Integer) vidx.get(v)).intValue();
                int j = ((Integer) vidx.get(de.getVertexB())).intValue();
                int k = j - i;
                if (k > 1) {
                    Path path = new PathImpl();
                    Object old = v;
                    Object cur = null;
                    path.add(de.getVertexA());
                    for (; k > 1; k--) {
                        cur = new Point2D.Double();
                        lays[j - k + 1].add(cur);
                        path.add(cur);
                        dag.add(cur);
                        dag.addEdge(old, cur);
                        old = cur;
                    }
                    path.add(de.getVertexB());
                    dag.addEdge(old, de.getVertexB());
                    virtuals.put(de, path);
                    dag.removeEdge(de);
                } else if (k < 0)
                    throw new CycleException(
                            "Error dag layout, found an edge between " + i
                                    + " and " + j);
            }
        }
    }

    /**
     * @return
     * @throws Exception
     */
    private List[] sortLayers() throws Exception {
        /* layers */
        List lays = new ArrayList();
        /* list of sets of connected vertices */
        g = (DirectedGraph) vg.getGraph();
        /* reconstruct a DAG from g, possibly inverting some edges 
         * in the process.
         * The set of inverted edges is put into the invert map.
         */
        dag = new DirectedAcyclicGraphImpl();
        if (traversal == null)
            if(directed)
            	traversal = new BreadthFirstDirectedGraphTraversal(g);
            else
            	traversal = new BreadthFirstTraversal(g);
        Visitor vis = new Visitor() {
            /*
             * (non-Javadoc)
             * 
             * @see salvo.jesus.graph.Visitor#visit(salvo.jesus.graph.Vertex)
             */
            public boolean visit(Object v) {
                try {
                    /* add all edges */
                    List l = g.getEdges(v);
                    Iterator eit = g.getOutgoingEdges(v).iterator();
                    while (eit.hasNext()) {
                        DirectedEdge de = (DirectedEdge) eit.next();
                        /* self loops are not put in DAG */
                        if (de.getVertexA() == de.getVertexB())
                            continue;
                        try {
                            // if (!dag.isConnected(de.getVertexA(),
                            // de.getVertexB()))
                            dag.addEdge(de);
                        } catch (CycleException cex) {
                            DirectedEdge d = new DirectedEdgeImpl(de
                                    .getVertexB(), de.getVertexA(), de
                                    .getData());
                            invert.put(de, d);
                            dag.addEdge(d);
                        }
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
        /* get first vertex */
        Iterator cs;
        if (roots == null) {
            cs = g.getConnectedSet().iterator();
            while (cs.hasNext()) {
                List l = (List) cs.next();
                traversal.traverse( l.get(0), vis);
            }
        } else {
            cs = roots.iterator();
            while (cs.hasNext()) {
                Object l =  cs.next();
                traversal.traverse(l, vis);
            }
        }
        /* first layer contain root vertices */
        List root = dag.getRoot();
        /* map from vertices to their layer */
        vidx = new HashMap();
        /* rank vertices into layers */
        layerSort(root, lays, vidx);
        return (List[]) lays.toArray(new List[0]);
    }

    /**
     * A special kind of BDT
     * 
     * @param vertex
     * @param lays
     * @param idxs
     *            map from vertices to layer number
     */
    private void layerSort(List root, List lays, Map idxs) {
        int i = 0;
        Queue queue = new Queue();
        Object vertex;
        for (Iterator it1 = root.iterator(); it1.hasNext();) {
            vertex =  it1.next();
            queue.put(vertex);
            idxs.put(vertex, new Integer(i));
        }
        if (queue.isEmpty())
            return;
        try {
            do {
                vertex =  queue.get();
                /* get index */
                i = ((Integer) idxs.get(vertex)).intValue();
                /* ... and right list */
                List l = null;
                try {
                    l = (List) lays.get(i);
                } catch (IndexOutOfBoundsException iex) {
                    l = new ArrayList();
                    lays.add(i, l);
                }
                l.add(vertex);
                /* visit connected vertices */
                Iterator it = dag.getOutgoingAdjacentVertices(vertex)
                        .iterator();
                while (it.hasNext()) {
                    Object n =  it.next();
                    if (n.equals(vertex))
                        continue;
                    /* remove vertex if already visited */
                    Integer old = (Integer) idxs.get(n);
                    if (old != null) {
                        int j = old.intValue();
                        try {
                            ((List) lays.get(j)).remove(n);
                        } catch (IndexOutOfBoundsException e) {
                            // NOOP
                        }
                    }
                    /* put in queue if not visited */
                    if (!queue.contains(n))
                        queue.put(n);
                    idxs.put(n, new Integer(i + 1));
                }
            } while (!queue.isEmpty());
        } catch (EmptyQueueException ex) {
            // nothing to do
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#addVertex(salvo.jesus.graph.visual.VisualVertex)
     */
    public void addVertex(VisualVertex vvertex) {
        // TODO : should recalculate layout
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#removeVertex(salvo.jesus.graph.visual.VisualVertex)
     */
    public void removeVertex(VisualVertex vvertex) {
        // TODO : should recalculate layout
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#addEdge(salvo.jesus.graph.visual.VisualEdge)
     */
    public void addEdge(VisualEdge vedge) {
        // TODO : should recalculate layout
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#removeEdge(salvo.jesus.graph.visual.VisualEdge)
     */
    public void removeEdge(VisualEdge vedge) {
        // TODO : should recalculate layout
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#paintEdge(java.awt.Graphics2D,
     *      salvo.jesus.graph.visual.VisualEdge)
     */
    public void paintEdge(Graphics2D g2d, VisualEdge vedge) {
        routeEdge(g2d, vedge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#routeEdge(java.awt.Graphics2D,
     *      salvo.jesus.graph.visual.VisualEdge)
     */
    public void routeEdge(Graphics2D g2d, VisualEdge vEdge) {
        Edge e = vEdge.getEdge();
        boolean inv = false;
        Edge r;
        Path path;
        Rectangle frombounds = vEdge.getVisualVertexA().getBounds();
        Rectangle tobounds = vEdge.getVisualVertexB().getBounds();
        ControlCurve curve = new NaturalCubicSpline();

        /* is e inverted ? */
        if ((r = (Edge) invert.get(e)) != null) {
            e = r;
            inv = true;
        }

        g2d.setColor(vEdge.getOutlinecolor());
        g2d.setStroke(new BasicStroke(vEdge.getLineThickness()));
        Point2D.Float fromcenter = new Point2D.Float(new Double(vEdge
                .getVisualVertexA().getBounds2D().getCenterX()).floatValue(),
                new Double(vEdge.getVisualVertexA().getBounds2D().getCenterY())
                        .floatValue());
        Point2D.Float tocenter = new Point2D.Float(new Double(vEdge
                .getVisualVertexB().getBounds2D().getCenterX()).floatValue(),
                new Double(vEdge.getVisualVertexB().getBounds2D().getCenterY())
                        .floatValue());

        /* is e split ? */
        path = (Path) virtuals.get(e);
        /* route edge with control points */
        if (path != null) {
            if (!inv)
                routePath(g2d, curve, path);
            else
                routePathInverse(g2d, curve, path);
            /* set shape of edge */
            vEdge.setShape(curve);
        } else if (!vEdge.getVisualVertexA().equals(vEdge.getVisualVertexB())) {
            /* invert end points ? */
            /*
             * if (inv) { Point2D.Float tmp = fromcenter; fromcenter = tocenter;
             * tocenter = tmp; }
             */
            boolean opp = false;
            GeneralPath gPath = new GeneralPath();
            gPath.reset();
            gPath.moveTo(fromcenter.x, fromcenter.y);
            /* check reverse path */
            Object va = vEdge.getVisualVertexA().getVertex();
            Iterator it = vEdge.getVisualGraph().getGraph().getEdges(
                    vEdge.getVisualVertexB().getVertex()).iterator();
            while (it.hasNext()) {
                Edge ed = (Edge) it.next();
                if (ed.getVertexB().equals(va)) {
                    opp = true;
                    break;
                }
            }
            if (opp) { /* there is an inverse path - draw a curve */
                /* compute center vector */
                Point2D.Float center = new Point2D.Float(
                        (tocenter.x - fromcenter.x) / 2,
                        (tocenter.y - fromcenter.y) / 2);
                /* rotate by 30? */
                AffineTransform rot = AffineTransform
                        .getRotateInstance(Math.PI / 6);
                rot.deltaTransform(center, center);

                gPath.curveTo(fromcenter.x + center.x, fromcenter.y + center.y,
                        fromcenter.x + center.x, fromcenter.y + center.y,
                        tocenter.x, tocenter.y);
            } else { /* draw a line */
                gPath.lineTo(tocenter.x, tocenter.y);
            }
            vEdge.setShape(new VisualGraphComponentPath(gPath));
            // to and from are same vertices
        } else {
            float x1, y1, x2, y2; // control points
            x1 = frombounds.x;
            y1 = frombounds.y - frombounds.height;
            x2 = frombounds.x + frombounds.width;
            y2 = y1;
            // we draw a loop
            GeneralPath gPath = new GeneralPath();
            gPath.reset();
            gPath.moveTo(fromcenter.x, fromcenter.y - frombounds.height / 2);
            gPath.curveTo(x1, y1, x2, y2, tocenter.x, tocenter.y
                    - tobounds.height / 2);
            vEdge.setShape(new VisualGraphComponentPath(gPath));
        }

    }

    /**
     * Returns the index of the layer containing given vertex, or -1 if not
     * found.
     * 
     * @param vertex
     * @return index of layer or -1
     */
    private int layerIndexOf(Object vertex) {
        for (int i = 0; i < layers.length; i++)
            if (layers[i].contains(vertex))
                return i;
        return -1;
    }

    /**
     * @param g2d
     * @param path
     * @param path2
     * @param fromcenter
     * @param tocenter
     */
    private void routePath(Graphics2D g2d, ControlCurve curve, Path vpath) {
        curve.reset();
        List l;
        /* get list of vertices in right order */
        l = vpath.traverse(vpath.getFirstVertex());
        /* route edge */
        Iterator it = l.iterator();
        while (it.hasNext()) {
            double x, y;
            Object v =  it.next();
            VisualVertex vv = vg.getVisualVertex(v);
            if (vv != null) {
                x = vv.getBounds2D().getCenterX();
                y = vv.getBounds2D().getCenterY();
            } else {
                x = ((Point2D.Double) v).x;
                y = ((Point2D.Double) v).y;
            }
            curve.addPoint((int) x, (int) y);
        }
    }

    private void routePathInverse(Graphics2D g2d, ControlCurve curve, Path vpath) {
        curve.reset();
        List l;
        boolean started = false;
        /* get list of vertices in right order */
        l = vpath.traverse(vpath.getLastVertex());
        /* route edge */
        Iterator it = l.iterator();
        while (it.hasNext()) {
            double x, y;
            Object v =  it.next();
            VisualVertex vv = vg.getVisualVertex(v);
            if (vv != null) {
                x = vv.getBounds2D().getCenterX();
                y = vv.getBounds2D().getCenterY();
            } else {
                x = ((Point2D.Double) v).x;
                y = ((Point2D.Double) v).y;
            }
            /* curve to */
            curve.addPoint((int) x, (int) y);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#drawLayout()
     */
    public void drawLayout() {
        if (isInitialized())
            vg.repaint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see salvo.jesus.graph.visual.layout.GraphLayoutManager#setRepaint(boolean)
     */
    public void setRepaint(boolean b) {
        // not used in this algorithm
    }

    /**
     * Gets the axis used for laying out this graph.
     * 
     * @return Returns the axis.
     */
    public int getAxis() {
        return axis;
    }

    /**
     * Sets the axis for laying out this graph.
     * 
     * @param axis
     *            The axis to set.
     */
    public void setAxis(int axis) {
        this.axis = axis;
    }

	/**
	 * @return Returns the directed.
	 */
	public boolean isDirected() {
		return directed;
	}

	/**
	 * @param directed The directed to set.
	 */
	public void setDirected(boolean directed) {
		this.directed = directed;
	}
}

/*
 * $Log: DigraphLayeredLayout.java,v $ Revision 1.6 2004/12/13 08:36:56 bailly
 * corrected layout algorithms parametrization of cell size in digraph layout
 * Revision 1.5 2004/11/18 08:20:10 bailly resynchro added cubic curve shape
 * handling for edgess Revision 1.4 2004/11/15 12:38:12 bailly improved layout
 * algorithm inspired from sugiyama Revision 1.3 2004/09/07 10:02:23 bailly ***
 * empty log message ***
 * 
 * Revision 1.2 2004/08/30 21:04:29 bailly continued layout algorithm (Sugiyama ?)
 * Revision 1.1 2004/08/18 07:12:10 bailly start working on layered layout
 * algorithm for directed graphs
 *  
 */
