package examples;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import salvo.jesus.graph.Vertex;
import salvo.jesus.graph.VertexImpl;
import salvo.jesus.graph.WeightedGraph;
import salvo.jesus.graph.WeightedGraphImpl;
import salvo.jesus.graph.visual.GraphEditor;
import salvo.jesus.graph.visual.VisualGraph;
import salvo.jesus.graph.visual.layout.GraphLayoutManager;
import salvo.jesus.graph.visual.layout.StraightLineLayout;

/**
 * A sample application demonstrating serialization of a VisualGraph
 * @author      Jesus M. Salvo Jr.
 */

public class SampleVisualGraphSerialization extends JFrame {
    Vertex	v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13;
    VisualGraph         vgraph;
    WeightedGraph       wgraph;
    GraphEditor         editor = new GraphEditor();;
    GraphLayoutManager  layoutManager;
    String              fileName;
    JDesktopPane        desktop = new JDesktopPane();
    JInternalFrame      internalFrame = new JInternalFrame();

    public SampleVisualGraphSerialization() {}

    private void showGraph() {
        // Create an instance of an alternate paitner for the edges
        AlternateVisualEdgePainter newEdgePainter = new AlternateVisualEdgePainter();

        // Initialise a layout manager, though not really part of this eample
        layoutManager = new StraightLineLayout( vgraph );
        editor.setGraphLayoutManager( layoutManager );

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Make it all visible
        this.internalFrame.getContentPane().add( editor );
        this.internalFrame.setTitle( "SampleVisualGraphSerialization" );
        this.internalFrame.setSize(
            new Dimension( screenSize.width - 200, screenSize.height - 200 ) );
        this.internalFrame.setIconifiable( true );
        this.internalFrame.setVisible( true );
        this.internalFrame.setResizable( true );
        this.internalFrame.setEnabled( true );

        this.desktop.add( this.internalFrame );
        this.desktop.setVisible( true );

        this.getContentPane().setLayout( new GridLayout(1,2));
        this.getContentPane().add( this.desktop );

        Dimension frameSize = new Dimension( screenSize.width - 80, screenSize.height - 80 );

        this.setSize( frameSize );
        this.setLocation((int)(screenSize.getWidth() - frameSize.getWidth()) / 2, (int)(screenSize.getHeight() - frameSize.getHeight()) / 2);

        // Terminate the application when the window closes
        this.addWindowListener( new CustomWindowAdapter( this ) );
    }

    /**
     * Initialise the graph. Only called if the file to which the VisualGraph
     * was serialzed to does not exist.
     */
    private void initGraph() throws Exception {
        wgraph = new WeightedGraphImpl();

        v1 = new VertexImpl( "A" );
        v2 = new VertexImpl( "B" );
        v3 = new VertexImpl( "C" );
        v4 = new VertexImpl( "D" );
        v5 = new VertexImpl( "E" );
        v6 = new VertexImpl( "F" );
        v7 = new VertexImpl( "G" );
        v8 = new VertexImpl( "H" );
        v9 = new VertexImpl( "I" );
        v10 = new VertexImpl( "J" );
        v11 = new VertexImpl( "K" );
        v12 = new VertexImpl( "L" );
        v13 = new VertexImpl( "M" );

        wgraph.add( v1 );
        wgraph.add( v2 );
        wgraph.add( v3 );
        wgraph.add( v4 );
        wgraph.add( v5 );
        wgraph.add( v6 );
        wgraph.add( v7 );
        wgraph.add( v8 );
        wgraph.add( v9 );
        wgraph.add( v10 );
        wgraph.add( v11 );
        wgraph.add( v12 );
        wgraph.add( v13 );

        wgraph.addEdge( v1, v2, 1.0 );
        wgraph.addEdge( v1, v6, 2.0 );
        wgraph.addEdge( v1, v7, 6.0 );
        wgraph.addEdge( v2, v3, 1.0 );
        wgraph.addEdge( v2, v4, 2.0 );
        wgraph.addEdge( v2, v5, 4.0 );
        wgraph.addEdge( v3, v5, 4.0 );
        wgraph.addEdge( v4, v5, 2.0 );
        wgraph.addEdge( v4, v6, 1.0 );
        wgraph.addEdge( v6, v5, 2.0 );
        wgraph.addEdge( v7, v5, 1.0 );
        wgraph.addEdge( v7, v8, 3.0 );
        wgraph.addEdge( v8, v9, 2.0 );
        wgraph.addEdge( v9, v11, 1.0 );
        wgraph.addEdge( v11, v10, 1.0 );
        wgraph.addEdge( v10, v12, 3.0 );
        wgraph.addEdge( v10, v13, 2.0 );
        wgraph.addEdge( v7, v10, 1.0 );
        wgraph.addEdge( v12, v13, 1.0 );
        wgraph.addEdge( v12, v7, 5.0 );
        wgraph.addEdge( v12, v5, 4.0 );
        wgraph.addEdge( v12, v6, 2.0 );

        // Get a VisualGraph
        editor.setGraph( wgraph );
        vgraph = editor.getVisualGraph();
    }

    /**
     * Force the layout of the VisualGraph. Only called when the file
     * the VisualGraph was serialized to does not exist.
     */
    private void forceLayout() {
        vgraph.getVisualVertex( v1 ).setLocation( 50, 50 );
        vgraph.getVisualVertex( v2 ).setLocation( 150, 150 );
        vgraph.getVisualVertex( v3 ).setLocation( 250, 150 );
        vgraph.getVisualVertex( v4 ).setLocation( 150, 250 );
        vgraph.getVisualVertex( v5 ).setLocation( 250, 250 );
        vgraph.getVisualVertex( v6 ).setLocation( 50, 350 );

        vgraph.getVisualVertex( v7 ).setLocation( 350, 150 );

        vgraph.getVisualVertex( v8 ).setLocation( 450, 50 );
        vgraph.getVisualVertex( v9 ).setLocation( 550, 50 );
        vgraph.getVisualVertex( v10 ).setLocation( 450, 250 );
        vgraph.getVisualVertex( v11 ).setLocation( 550, 250 );
        vgraph.getVisualVertex( v12 ).setLocation( 450, 350 );
        vgraph.getVisualVertex( v13 ).setLocation( 550, 350 );
    }

    /**
     * Serialize the VisualGraph to a file
     */
    public void write() throws IOException {
        System.out.println( "Serializing to file " + fileName );
        FileOutputStream    fileOut = new FileOutputStream( fileName );
        ObjectOutputStream  out = new ObjectOutputStream( fileOut );

        out.writeObject( vgraph );
        out.flush();
        out.close();
    }

    /**
     * Restore the VisualGraph from the file
     */
    public void read() throws IOException, ClassNotFoundException {
        System.out.println( "Reading from file " + fileName );
        FileInputStream     fileIn = new FileInputStream( fileName );
        ObjectInputStream   in = new ObjectInputStream( fileIn );

        vgraph = (VisualGraph) in.readObject();
        in.close();
        wgraph = (WeightedGraph) vgraph.getGraph();
        editor.setVisualGraph( vgraph );
    }

    public static void main(String[] args) throws Exception {
        if( args.length < 1 ) {
            System.out.println( "Usage: SampleVisualGraphVisualization [ filename ]" );
            System.exit( 0 );
        }

        SampleVisualGraphSerialization frame = new SampleVisualGraphSerialization();
        frame.fileName = args[0];

        try {
            frame.read();
        }
        catch( FileNotFoundException ex ) {
            frame.initGraph();
            frame.forceLayout();
        }
        catch( Exception ex ) {
            ex.printStackTrace();
            System.exit(0);
        }
        frame.showGraph();
        frame.setTitle( "SampleVisualGraphSerialization" );
        frame.setVisible( true );

        JOptionPane.showMessageDialog( frame,
            "This demonstrates serialization of a VisualGraph.\n" +
            "Try changing the position, color, or text of any vertices or edges,\n" +
            "or even try adding and deleting vertices and edges.\n" +
            "Upon exit, the VisualGraph will be serliazed to the file specified at the command line.\n" +
            "Rerunning this application will restore the VisualGraph from the file." );
    }

}


/**
 * WindowAdapter such that when the window closes, the VisualGraph is serialized to a file.
 */
class CustomWindowAdapter extends WindowAdapter {

    SampleVisualGraphSerialization frame;

    public CustomWindowAdapter( SampleVisualGraphSerialization frame ) {
        this.frame = frame;
    }

    public void windowClosing( WindowEvent e ) {
        try {
            this.frame.write();
        }
        catch( IOException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }
}
