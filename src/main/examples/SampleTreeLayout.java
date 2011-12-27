package examples;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import salvo.jesus.graph.Tree;
import salvo.jesus.graph.TreeImpl;
import salvo.jesus.graph.Vertex;
import salvo.jesus.graph.VertexImpl;
import salvo.jesus.graph.visual.GraphEditor;
import salvo.jesus.graph.visual.VisualGraph;
import salvo.jesus.graph.visual.layout.GraphLayoutManager;
import salvo.jesus.graph.visual.layout.LayeredTreeLayout;

/**
 * @author  Jesus M. Salvo Jr.
 */

public class SampleTreeLayout extends JFrame {
    VisualGraph         vgraph;

    public SampleTreeLayout() {}

    private Tree initTree() throws Exception {
        Tree    tree = new TreeImpl();
        Vertex  v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11;

        v1 = new VertexImpl( "1" );
        v2 = new VertexImpl( "2" );
        v3 = new VertexImpl( "3" );
        v4 = new VertexImpl( "4" );
        v5 = new VertexImpl( "5" );
        v6 = new VertexImpl( "6" );
        v7 = new VertexImpl( "7" );
        v8 = new VertexImpl( "8" );
        v9 = new VertexImpl( "9" );
        v10 = new VertexImpl( "10" );
        v11 = new VertexImpl( "11" );

        tree.addNode( null, v1 );

        tree.addNode( v1, v2 );
        tree.addNode( v1, v3 );
        tree.addNode( v1, v4 );

        tree.addNode( v2, v5 );
        tree.addNode( v2, v6 );

        tree.addNode( v3, v7 );

        tree.addNode( v7, v8 );
        tree.addNode( v7, v9 );
        tree.addNode( v7, v10 );
        tree.addNode( v7, v11 );

        return tree;
    }

    private void showTree( Tree tree ) {
        GraphEditor         editor;
        GraphLayoutManager  layoutManager;

        // Get a VisualGraph
        editor = new GraphEditor();
        editor.setGraph( tree );
        vgraph = editor.getVisualGraph();

        // Initialise a Tree layout manager
        layoutManager = new LayeredTreeLayout( editor.getVisualGraph() );
        editor.setGraphLayoutManager( layoutManager );

        // Make it all visible
        this.getContentPane().setLayout( new GridLayout(1,2));
        this.getContentPane().add( editor );

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension( screenSize.width - 80, screenSize.height - 80 );

        this.setSize( frameSize );
        this.setLocation((int)(screenSize.getWidth() - frameSize.getWidth()) / 2, (int)(screenSize.getHeight() - frameSize.getHeight()) / 2);

        // Terminate the application when the window closes
        this.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) { System.exit(0); }
          });
    }

    public static void main( String args[] ) throws Exception {
        SampleTreeLayout    app = new SampleTreeLayout();
        Tree tree = app.initTree();
        app.showTree( tree );
        app.setTitle( "SampleTreeLayout" );
        app.setVisible( true );
        app.vgraph.layout();
    }
}