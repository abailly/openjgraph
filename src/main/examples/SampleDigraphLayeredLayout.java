package examples;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import salvo.jesus.graph.DirectedGraphImpl;
import salvo.jesus.graph.Graph;
import salvo.jesus.graph.Vertex;
import salvo.jesus.graph.VertexImpl;
import salvo.jesus.graph.visual.GraphEditor;
import salvo.jesus.graph.visual.VisualGraph;
import salvo.jesus.graph.visual.layout.DigraphLayeredLayout;


public class SampleDigraphLayeredLayout extends JFrame {
    Vertex        v1, v2, v3, v4, v5, v6, v7;
    Vertex        v8, v9, v10, v11, v12, v13, v14;
    Vertex        v15, v16, v17, v18, v19;
    Graph         graph;
    VisualGraph	  vGraph;
    GraphEditor	  gEdit;
    DigraphLayeredLayout  layout;

    public SampleDigraphLayeredLayout() throws Exception {
        graph = new DirectedGraphImpl();
        vGraph = new VisualGraph();
        gEdit = new GraphEditor();

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
        v12 = new VertexImpl( "12" );
        v13 = new VertexImpl( "13" );
        v14 = new VertexImpl( "14" );

        v15 = new VertexImpl( "15" );
        v16 = new VertexImpl( "16" );
        v17 = new VertexImpl( "17" );
        v18 = new VertexImpl( "18" );
        v19 = new VertexImpl( "19" );

        graph.add( v1 );
        graph.add( v2 );
        graph.add( v3 );
        graph.add( v4 );
        graph.add( v5 );
        graph.add( v6 );
        graph.add( v7 );

        graph.add( v8 );
        graph.add( v9 );
        graph.add( v10 );
        graph.add( v11 );
        graph.add( v12 );
        graph.add( v13 );
        graph.add( v14 );

        graph.add( v15 );
        graph.add( v16 );
        graph.add( v17 );
        graph.add( v18 );
        graph.add( v19 );

        graph.addEdge( v1, v2 );
        graph.addEdge( v2, v3 );
        graph.addEdge( v3, v4 );
        graph.addEdge( v4, v5 );
        graph.addEdge( v5, v6 );
        graph.addEdge( v6, v7 );
        graph.addEdge( v7, v1 );

        graph.addEdge( v1, v8 );
        graph.addEdge( v8, v9 );
        graph.addEdge( v9, v10 );
        graph.addEdge( v10, v11 );
        graph.addEdge( v11, v12 );
        graph.addEdge( v12, v13 );
        graph.addEdge( v13, v1 );

        graph.addEdge( v1, v14 );
        graph.addEdge( v14, v15 );
        graph.addEdge( v15, v16 );
        graph.addEdge( v16, v17 );
        graph.addEdge( v17, v18 );
        graph.addEdge( v18, v19 );
        graph.addEdge( v19, v1 );

        vGraph.setGraph( graph );
        gEdit.setVisualGraph( vGraph );

        this.getContentPane().setLayout( new GridLayout(1,2));
        this.getContentPane().add( gEdit );

        layout = new DigraphLayeredLayout( vGraph );
        vGraph.setGraphLayoutManager( layout );

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension( screenSize.width - 80, screenSize.height - 80 );

        this.setSize( frameSize );
        this.setLocation((int)(screenSize.getWidth() - frameSize.getWidth()) / 2, (int)(screenSize.getHeight() - frameSize.getHeight()) / 2);

        layout.layout();
    }

    public static void main(String[] args) throws Exception {
        SampleDigraphLayeredLayout frame = new SampleDigraphLayeredLayout();
        frame.setTitle( "SampleForceDirectedLayout" );
        frame.setVisible(true);
    }
}


