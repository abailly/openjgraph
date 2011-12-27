/**
 * 
 */
package salvo.jesus.graph.algorithm;

import java.awt.Graphics;

import salvo.jesus.graph.visual.DefaultGraphPanel;
import salvo.jesus.graph.visual.GraphPanelState;
import salvo.jesus.graph.visual.VisualGraph;

/**
 * A graph panel that adds some information about data dependency
 * 
 * @author nono
 *
 */
public class InfoGraphPanel extends DefaultGraphPanel {

	public InfoGraphPanel() {
		super();
	}

	public InfoGraphPanel(GraphPanelState gps, VisualGraph vgraph, boolean antialias) {
		super(gps, vgraph, antialias);
	}

	public InfoGraphPanel(GraphPanelState gps) {
		super(gps);
	}

	public InfoGraphPanel(VisualGraph vgraph) {
		super(vgraph);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// add table info
	}

	
}
