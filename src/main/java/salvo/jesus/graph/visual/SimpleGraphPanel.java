package salvo.jesus.graph.visual;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A simple panel for visualizing a graph. This is a read-only UI component for
 * viewing {@link VisualGraph} instances.
 * 
 * @author nono
 */
public class SimpleGraphPanel extends JComponent implements GraphPanel {

	/*
	 * the graph
	 */
	private VisualGraph visualGraph;

	/*
	 * zooming
	 */
	private double zoomFactor = 1.0;

	public double getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	protected void paintComponent(Graphics g) {
		if (visualGraph == null)
			return;
		// custom graphics
		Graphics2D g2d = (Graphics2D) g;
		// compute insets
		Insets insets = getInsets();
		int w = getWidth() - insets.left - insets.right;
		int h = getHeight() - insets.top - insets.bottom;
		// honor opaque property
		if (isOpaque()) { // paint background
			g2d.setColor(getBackground());
			g2d.fillRect(0, 0, w, h);
		}
		/* clip to viewport */
		g2d.clipRect(insets.left, insets.top, w - insets.right, h
				- insets.bottom);
		/* rescale g2d to account for zooming */
		if (zoomFactor != 1)
			g2d.scale(zoomFactor, zoomFactor);
		// paint graph
		this.visualGraph.paint(g2d);
	}

	public Dimension getMaximumSize() {
		return visualGraph.getMaxSize();
	}

	public Dimension getMinimumSize() {
		return new Dimension(20, 20);
	}

	public Dimension getPreferredSize() {
		if (visualGraph == null)
			return new Dimension(100, 100);
		return visualGraph.getMaxSize();
	}

	public VisualGraph getVisualGraph() {
		return visualGraph;
	}

	public void setVisualGraph(VisualGraph visualGraph) {
		this.visualGraph = visualGraph;
		visualGraph.addContainer(this);
	}

}
