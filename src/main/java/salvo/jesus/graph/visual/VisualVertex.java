package salvo.jesus.graph.visual;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.Random;
import java.util.StringTokenizer;


import salvo.jesus.graph.visual.drawing.VisualVertexPainter;
import salvo.jesus.util.VisualGraphComponentPath;
import salvo.jesus.util.VisualGraphComponentShape;

/**
 * The VisualVertex class encapsulates a Object with attributes used for visual
 * rendering of the vertex.
 * 
 * @author Jesus M. Salvo Jr.
 */
public class VisualVertex extends AbstractVisualGraphComponent {

    /**
     * Random object used to give an initial random coordinates for newly
     * created instances of VisualVertex if no Shape has been provided.
     */
    static Random rand = new Random();


    /**
     * Creates a new VisualVertex object that encapsulates the given Vertex
     * object This defaults the font to Lucida Sans, the outline color to black
     * and the fill or background colot to Color( 0, 255, 255 ).
     * 
     * @param vertex
     *            The Object object that the VisualVertex will encapsulate.
     */
    public VisualVertex(Object vertex, VisualGraph vGraph) {
        this.component = vertex;
        this.painter = vGraph.getVisualVertexPainterFactory().getPainter(this);
        this.visualGraph = vGraph;
        this.label = vertex.toString();
        this.setFont(new Font("Lucida Sans", Font.PLAIN, 10));

        this.initLocation();
        // Force adjustment of width and height.
        this.rescale();

        this.setOutlinecolor(Color.black);
        this.setFillcolor(new Color(0, 225, 255));
    }

    /**
     * Creates a new VisualVertex object that encapsulates the given Vertex
     * object using the Font specified to draw the string inside the
     * VisualVertex.
     * 
     * @param vertex
     *            The Object object that the VisualVertex will encapsulate.
     * @param f
     *            The font that will be used to draw the String representation
     *            of the vertex
     */
    public VisualVertex(Object vertex, Font f, VisualGraph vGraph) {
        this.component = vertex;
        this.painter = vGraph.getVisualVertexPainterFactory().getPainter(this);
        this.visualGraph = vGraph;
        this.label = vertex.toString();
        this.setFont(f);

        this.initLocation();
        // Force adjustment of width and height.
        //    this.rescale();

        outlinecolor = Color.black;
        this.setFillcolor(new Color(0, 225, 255));
    }

    /**
     * Creates a new VisualVertex object that encapsulates the given Vertex
     * object with the given visual attributes.
     * 
     * @param vertex
     *            The Object object that the VisualVertex will encapsulate.
     * @param shape
     *            The shape used to render the VisualVertex.
     * @param c
     *            The color used to draw the outline of the VisualVertex's
     *            shape.
     * @param bgcolor
     *            The color used to fill the VisualVertex's shape.
     * @param f
     *            The font that will be used to draw the String representation
     *            of the vertex
     */
    public VisualVertex(Object vertex, Shape shape, Color c, Color bgcolor,
            Font f, VisualGraph vGraph) {
        this.component = vertex;
        this.painter = vGraph.getVisualVertexPainterFactory().getPainter(this);
        this.visualGraph = vGraph;
        this.label = vertex.toString();
        this.setFont(f);

        componentShape = new VisualGraphComponentPath(new GeneralPath(shape));
        this.setOutlinecolor(c);
        this.setFillcolor(bgcolor);
    }

    /**
     * Initialize the location of the VisualVertex by generating a random number
     * for its x and y coordinates.
     */
    private void initLocation() {
        StringTokenizer sttokenizer;
        int height = 0, width, maxwidth = 0;
        int lineheight;

        componentShape = new VisualGraphComponentPath(new GeneralPath(
                (Shape) new Rectangle.Double(5, 5, 30, 30)));
        this.setLocation(rand.nextInt(500), rand.nextInt(400));
    }

    /**
     * Returns the Object that VisualVertex encapsulates.
     * 
     * @return The Object object that the VisualVertex encapsulates.
     */
    public Object getVertex() {
        return  this.component;
    }

    /**
     * Sets geometry used to draw the VisualVertex.
     * 
     * @param path
     *            A GeneralPath object used to draw the VisualVertex. If the
     *            GeneralPath is not a closed polygon, this method will close
     *            the path.
     */
    public void setShape(VisualGraphComponentShape path) {
        // Make sure the path is closed
        //  path.closePath();
        super.setShape(path);
        //  this.rescale();
    }

    /**
     * Sets the vertex that the VisualVertex encapsulates. The next call to
     * paint() will redraw the string inside the VisualVertex' shape.
     * 
     * @param vertex
     *            The new Object object that VisualVertex encapsulates.
     */
    public void setVertex(Object vertex) {
        this.component = vertex;
    }

    /**
     * Tests if the coordinate is inside the VisualVertex's shape. Do not call
     * this method if paint() has not been called at least once, because the
     * first call to paint() will initialize the shape used to draw the
     * VisualVertex.
     * 
     * This method is simply a wrapper on GeneralPath.contains( x, y ).
     * 
     * @param x
     *            x-coordinate of the point you want to test
     * @param y
     *            y-coordinate of the point you want to test
     * @return True if the x and y coordinate is inside the shape of the
     *         VisualVertex.
     */
    public boolean contains(double x, double y) {
        return componentShape.contains(x, y);
    }

    /**
     * Moves the location of the VisualVertex to the new coordinate. Do not call
     * this method if paint() has not been called at least once, because the
     * first call to paint() will initialize the shape used to draw the
     * VisualVertex.
     * 
     * @param x
     *            The new x-coordinate for the VisualVertex
     * @param y
     *            The new y-coordinate for the VisualVertex
     */
    public void setLocation(int x, int y) {
        this.setLocationDelta((int) (x - componentShape.getBounds()
                .getCenterX()), (int) (y - componentShape.getBounds()
                .getCenterY()));

    }

    public void setLocation(double x, double y) {
        this.setLocationDelta((x - componentShape.getBounds().getCenterX()),
                (y - componentShape.getBounds().getCenterY()));

    }

    /**
     * Moves the location of the VisualVertex' shape by the specified delta
     * values. Do not call this method if paint() has not been called at least
     * once, because the first call to paint() will initialize the shape used to
     * draw the VisualVertex.
     * 
     * This method is simply a wrapper on Rectangle.translate( x, y ).
     * 
     * @param x
     *            The delta x-coordinate for the VisualVertex
     * @param y
     *            The delta y-coordinate for the VisualVertex
     */
    public void setLocationDelta(int dx, int dy) {
        AffineTransform transform = new AffineTransform();
        transform.translate(dx, dy);
        componentShape.transform(transform);
    }

    public void setLocationDelta(double dx, double dy) {
        if (Double.isNaN(dx) || Double.isNaN(dy))
            return;
        if (dx == 0 && dy == 0)
            return;
        AffineTransform transform = new AffineTransform();
        transform.translate(dx, dy);
        componentShape.transform(transform);
    }

    /**
     * Wrapper method that simply returns this.displaytext.
     * 
     * @return Display string of the VisualVertex object
     */
    public String toString() {
        return this.getText();
    }

    /**
     * Draw the VisualVertex with the specified 2D graphics context. Each call
     * to this method will draw the fill color, the outline color, and the
     * string inside the shape, in that order.
     * 
     * @param g2d
     *            The Graphics2D graphics context object used to draw the
     *            VisualVertex.
     */
    public void paint(Graphics2D g2d) {
        this.painter.paint(this, g2d);
    }

    /**
     * Forces a rescale of the internal shape used to represent VisualVertex.
     */
    public void rescale() {
        ((VisualVertexPainter) this.painter).rescale(this);
    }

}
