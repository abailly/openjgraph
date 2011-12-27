package salvo.jesus.graph.visual;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

import salvo.jesus.graph.Graph;
import salvo.jesus.graph.visual.layout.GraphLayoutManager;

/**
 * GraphScrollPane encapsulates GraphPanelSizeable so that the
 * visual representation of the graph has scrollbars to allow the user
 * to view other portions of the graph that are not in the direct view of
 * the viewport of GraphScrollPane.
 *
 * @author		Jesus M. Salvo Jr.
 */
public class GraphScrollPane extends JScrollPane {
	/**
	 * The GraphPanel that GraphScrollPane encapsulates
	 */
	protected DefaultGraphPanel gpanel;

	/**
	* calls GraphScrollPane(new GraphPanelNormalState(), new VisualGraph());
	*/
	public GraphScrollPane() {
		this(new GraphPanelNormalState(), new VisualGraph(), false);
	}

	/**
	 * calls GraphScrollPane(gps, new VisualGraph()) with the given GraphPanelState and sets the given graph.
	 */
	public GraphScrollPane(GraphPanelState gps) {
		this(gps, new VisualGraph(), false);
	}

	/**
	 * calls GraphScrollPane(new GraphPanelNormalState(), vgraph)
	 */
	public GraphScrollPane(VisualGraph vgraph) {
		this(new GraphPanelNormalState(), vgraph, false);
	}

	/**
	 * Creates a GraphScrollPane object with the given GraphPanelState and initializes the scrollbars and the
	 * GraphPanelSizeable which it encapsulates. As a note,
	 * the scrollbars only appear if the policy rules are set to ***_ALWAYS
	 * in the call to the JScrollPane's constructor method. The scrollbars
	 * would not appear if we call the constructor method with the policy
	 * rules set to other than ***_ALWAYS, even if we subsequently set
	 * the policy rules to ***_ALWAYS later on.
	 *
	 */
	public GraphScrollPane(
		GraphPanelState gps,
		VisualGraph vgraph,
		boolean antialias) {
		super(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.gpanel = new DefaultGraphPanel(gps, vgraph, antialias);
		this.gpanel.gpcontainer = this;
		this.GraphScrollPane_init();
		this.setOpaque(true);
	}

	private void GraphScrollPane_init() {
		JViewport viewport;

		viewport = new JViewport();
		viewport.setView(this.gpanel);
		viewport.setOpaque(true);
		viewport.setBackground(Color.white);
		viewport.setSize(gpanel.getPreferredScrollableViewportSize());
		this.setViewport(viewport);
	}

	/**
	 * Returns the VisualGraph that is encapsulated within
	 * GraphScrollPane.GraphPanelSizeable. This is simply
	 * a wrapper method around gpanel's getVisualGraph() method
	 *
	 * @return	This VisualGraph that is encapsulated.
	 */
	public VisualGraph getVisualGraph() {
		return this.gpanel.getVisualGraph();
	}

	/**
	  * Sets the VisualGraph that is encapsulated within
	  * GraphScrollPane.GraphPanelSizeable. Calling this will
	  * automatically repaint the contents of the pane. This is simply
	  * a wrapper method around gpanel's setVisualGraph() method.
	  *
	  * @param vg	The visual graph to be encapsulated and drawn.
	 */
	public void setVisualGraph(VisualGraph vg) {
		this.gpanel.setVisualGraph(vg, this);
	}

	/**
	  * Sets the Graph that is encapsulated within
	  * GraphScrollPane.GraphPanelSizeable. Calling this will
	  * automatically repaint the contents of the pane. This is simply
	  * a wrapper method around gpanel's setGraph() method.
	  *
	  * @param g	The graph to be encapsulated and drawn.
	 */
	public void setGraph(Graph g) {
		this.gpanel.setGraph(g, this);
	}

	/**
	  * Sets the layout manager to use to layout the vertices of the graph.
	  *
	  * @param  layoutmanager   An object implementing the GraphLayoutManager interface.
	  */
	public void setGraphLayoutManager(GraphLayoutManager layoutmanager) {
		this.gpanel.setGraphLayoutManager(layoutmanager);
	}

	/**
	 * This is simply a wrapper method around <tt>GraphPanel</tt>'s
	 * <tt>processChangeStateEvent()</tt> method.
	 */
	public GraphPanelState processChangeStateEvent(ChangeStateEvent cse) {
		return this.gpanel.processChangeStateEvent(cse);
	}

	/**
	 * toggles antialias rendering for this graph scroll pane. this method
	 * is merely a wrapper for the same method in GraphPanel
	 * 
	 * @param antialias a boolean flag, true if antialias should be on ,false otherwise
	 * @see salvo.jesus.graph.visual.DefaultGraphPanel
	 */
	public void setAntialias(boolean antialias) {
		gpanel.setAntialias(antialias);
	}
//  /**
//   * @return
//   */
//  public GraphPanel getGpanel() {
//    return gpanel;
//  }

}
