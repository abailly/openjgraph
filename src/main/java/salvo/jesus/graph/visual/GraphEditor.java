package salvo.jesus.graph.visual;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import salvo.jesus.graph.Graph;
import salvo.jesus.graph.visual.layout.GraphLayoutManager;

/**
 * GraphEditor encapsulates GraphScrollPane so that a toolbar is provided
 * for the user to add and remove vertices and edges dynamically.
 *
 * @author		Jesus M. Salvo Jr.
 */
public class GraphEditor extends JPanel {
  /**
    * The LayoutManager used by GraphEditor is by default BorderLayout.
    */
  BorderLayout			borderlayout;

  /**
    * The object representing the toolbar used by GraphEditor.
    */
  GraphToolBar			toolbar;

  /**
    * The GraphScrollPane object encapsulated by GraphEditor.
    */
  GraphScrollPane		graphscrollpane;

    JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

  /**
    * Creates a GraphEditor object without a specified GraphScrollPane
    * object to draw.
    */
  public GraphEditor() {
        this(new GraphPanelNormalState());
    }

    /**
     * Creates a GraphEditor without a specified GraphScrollPane but with
     * a given initial GraphPanelState, the GraphPanel property of GraphPanelState will be set.
     */
    public GraphEditor(GraphPanelState gps) {
        this(new GraphScrollPane(gps));
  }

  /**
    * Creates a GraphEditor object with a given GraphScrollPane
    * object to draw.
    *
    * @param		graphscrollpane	GraphScrollPane object whose vertices and edges will be drawn.
    */
    public GraphEditor(GraphScrollPane graphscrollpane) {
    this.graphscrollpane = graphscrollpane;
    this.borderlayout = new BorderLayout();
        this.toolbar = new GraphToolBar(this);
    doGraphEditorLayout();
  }

  /**
    * Creates a GraphEditor object with a given VisualGraph to draw
    *
    * @param		vgraph	VisualGraph object whose vertices and edges will be drawn.
    */
    public GraphEditor(VisualGraph vgraph) {
        this(new GraphScrollPane(vgraph));
  }

  /**
    * Creates a GraphEditor object with a given Graph
    *
    * @param		graph		Graph object whose vertices and edges will be drawn.
    */
    public GraphEditor(Graph graph) {
        this(new VisualGraph(graph));
    }

    /**
     * Creates a GraphEditor object with a given VisualGraph and a given state,
     * the GraphPanel property of GraphPanelState will be set.
     *
     * @param		graph		Graph object whose vertices and edges will be drawn.
     */
    public GraphEditor(GraphPanelState gps, VisualGraph vgraph){
      this(new GraphScrollPane(gps,vgraph,false));
    }

    /**
     * Creates a GraphEditor object with a given Graph and a given state,
     * the GraphPanel property of GraphPanelState will be set.
     *
     * @param		graph		Graph object whose vertices and edges will be drawn.
     */
    public GraphEditor(GraphPanelState gps, Graph g){
      this(gps,new VisualGraph(g));
  }


  /**
    * Lays out the components so that the toolbar is on the top. Do not
    * call this method more than once.
    */
  private void doGraphEditorLayout() {
        this.splitpane.setRightComponent(this.graphscrollpane);
        this.splitpane.setOneTouchExpandable(true);

        this.setLayout(this.borderlayout);

        this.add(this.toolbar, BorderLayout.NORTH);
        this.add(this.splitpane);
  }

    public void setLeftPanel(Component component) {
        this.splitpane.setLeftComponent(component);
  }

  /**
    * Returns the Graph object that is encapsulated in GraphEditor.
    *
    * @return	Graph object encapsulated by GraphEditor.
    */
    public Graph getGraph() {
    return this.graphscrollpane.getVisualGraph().getGraph();
  }

  /**
    * Returns the VisualGraph object that is encapsulated in GraphEditor.
    *
    * @return	VisualGraph object encapsulated by GraphEditor.
    */
    public VisualGraph getVisualGraph() {
    return this.graphscrollpane.getVisualGraph();
  }

  /**
    * Sets the new VisualGraph object that is encapsulated by GraphEditor.
    *
    * @param		vg		VisualGraph object that will be encapsulated by GraphEditor
    */
    public void setVisualGraph(VisualGraph vg) {
        this.graphscrollpane.setVisualGraph(vg);
  }

  /**
    * Sets the new Graph object that is encapsulated by GraphEditor.
    *
    * @param		graph		Graph object that will be encapsulated by GraphEditor
    */
    public void setGraph(Graph graph) {
        this.graphscrollpane.getVisualGraph().setGraph(graph);
  }

    public void paint(Graphics g) {
        super.paint(g);
  }

  /**
    * Sets the layout manager to use to layout the vertices of the graph.
    *
    * @param  layoutmanager   An object implementing the GraphLayoutManager interface.
    */
    public void setGraphLayoutManager(GraphLayoutManager layoutmanager) {
        this.graphscrollpane.setGraphLayoutManager(layoutmanager);
    }

    /**
     * This is simply a wrapper method around <tt>GraphScrollPane</tt>'s
     * <tt>processChangeStateEvent()</tt> method.
     */
    public GraphPanelState processChangeStateEvent(ChangeStateEvent cse){
      return this.graphscrollpane.processChangeStateEvent(cse);
  }

}


