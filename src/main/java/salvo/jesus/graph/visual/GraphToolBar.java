package salvo.jesus.graph.visual;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import salvo.jesus.util.PrintUtilities;


class GraphToolBar extends JToolBar {
    GraphEditor     editor;
    ButtonGroup     moderadiogroup;
    JRadioButton    selectradiobutton;
    JRadioButton    vertexradiobutton;
    JRadioButton    edgeradiobutton;
    JButton         layoutbutton;
    JButton         printButton;
    JButton         openButton;
    JButton         saveButton;



    GraphToolBar( GraphEditor editor ) {

      super();
      this.editor = editor;
      this.moderadiogroup = new ButtonGroup();

      openButton = new JButton( this.getImageIcon( "images/Zvaiofu041.gif" ));
      openButton.setToolTipText( "Open" );
      //openButton.setPressedIcon( this.getImageIcon( "images/selected_Zvaiofu041.gif" ));
      openButton.setVerticalTextPosition( AbstractButton.BOTTOM );
      openButton.setHorizontalTextPosition( AbstractButton.CENTER );
      openButton.setContentAreaFilled( false );
      openButton.setBorderPainted( true );
      openButton.addActionListener( new OpenButtonListener( this.editor ));
      this.add( openButton );

      saveButton = new JButton( this.getImageIcon( "images/Zvaiofu006.gif" ));
      saveButton.setToolTipText( "Save" );
      //saveButton.setPressedIcon( this.getImageIcon( "images/selected_Zvaiofu006.gif" ));
      saveButton.setVerticalTextPosition( AbstractButton.BOTTOM );
      saveButton.setHorizontalTextPosition( AbstractButton.CENTER );
      saveButton.setContentAreaFilled( false );
      saveButton.setBorderPainted( true );
      saveButton.addActionListener( new SaveButtonListener( this.editor ));
      this.add( saveButton );

      this.addSeparator();
      this.addSeparator();

      selectradiobutton = new JRadioButton( this.getImageIcon( "images/pointer.gif" ) );
      selectradiobutton.setToolTipText( "Select" );
      selectradiobutton.setSelectedIcon( this.getImageIcon( "images/selected_pointer.gif") );
      selectradiobutton.setVerticalTextPosition( AbstractButton.BOTTOM );
      selectradiobutton.setHorizontalTextPosition( AbstractButton.CENTER );
      selectradiobutton.setSelected( true );
      moderadiogroup.add( selectradiobutton );
      this.add( selectradiobutton );
      selectradiobutton.addActionListener( new ToolBarNormalButtonListener( this.editor ));

      vertexradiobutton = new JRadioButton( this.getImageIcon( "images/rectangle.gif" ) );
      vertexradiobutton.setToolTipText( "Vertex" );
      vertexradiobutton.setSelectedIcon( this.getImageIcon( "images/selected_rectangle.gif" ) );
      vertexradiobutton.setVerticalTextPosition( AbstractButton.BOTTOM );
      vertexradiobutton.setHorizontalTextPosition( AbstractButton.CENTER );
      moderadiogroup.add( vertexradiobutton );
      this.add( vertexradiobutton );
      vertexradiobutton.addActionListener( new ToolBarVertexButtonListener( this.editor ));

      edgeradiobutton = new JRadioButton( this.getImageIcon( "images/line.gif" ) );
      edgeradiobutton.setToolTipText( "Edge" );
      edgeradiobutton.setSelectedIcon( this.getImageIcon( "images/selected_line.gif" ) );
      edgeradiobutton.setVerticalTextPosition( AbstractButton.BOTTOM );
      edgeradiobutton.setHorizontalTextPosition( AbstractButton.CENTER );
      moderadiogroup.add( edgeradiobutton );
      this.add( edgeradiobutton );
      edgeradiobutton.addActionListener( new ToolBarEdgeButtonListener( this.editor ));

      this.addSeparator();
      this.addSeparator();

      layoutbutton = new JButton( this.getImageIcon( "images/layout.gif" ));
      layoutbutton.setToolTipText( "Layout" );
      layoutbutton.setPressedIcon( this.getImageIcon( "images/selected_layout.gif" ));
      layoutbutton.setVerticalTextPosition( AbstractButton.BOTTOM );
      layoutbutton.setHorizontalTextPosition( AbstractButton.CENTER );
      layoutbutton.setContentAreaFilled( false );
      layoutbutton.setBorderPainted( false );
      layoutbutton.addActionListener( new ToolBarLayoutButtonListener( this.editor ));
      this.add( layoutbutton );

      this.addSeparator();
      this.addSeparator();

      printButton = new JButton( this.getImageIcon( "images/printer.jpg" ) );
      printButton.setToolTipText( "Print" );
      printButton.setPressedIcon( this.getImageIcon( "images/selected_printer.jpg" ));
      printButton.setVerticalTextPosition( AbstractButton.BOTTOM );
      printButton.setHorizontalTextPosition( AbstractButton.CENTER );
      printButton.setContentAreaFilled( false );
      printButton.setBorderPainted( false );
      printButton.addActionListener( new ToolBarPrintButtonListener( this.editor ));

      this.add( printButton );

      this.setFloatable( true );
    }


    private ImageIcon getImageIcon ( String imagefilename ) {

      URL 		imageURL;
      ImageIcon   imgicon = null;

      try{
         imageURL = getClass().getResource( imagefilename );
        imgicon = new ImageIcon( imageURL );
      }
      catch (Exception e){
        e.printStackTrace();
      }

      return imgicon;
    }
}


class ToolBarNormalButtonListener implements ActionListener {
    GraphEditor    editor;

    public ToolBarNormalButtonListener( GraphEditor editor ) {
        this.editor = editor;
    }

    public void actionPerformed( ActionEvent actionevent ) {
        this.editor.processChangeStateEvent(
            new ChangeStateEvent( this,
                new GraphPanelNormalState( this.editor.graphscrollpane.gpanel )) );
    }
}


class ToolBarVertexButtonListener implements ActionListener {
    GraphEditor    editor;

    public ToolBarVertexButtonListener( GraphEditor editor ) {
        this.editor = editor;
    }

    public void actionPerformed( ActionEvent actionevent ) {
        this.editor.processChangeStateEvent(
            new ChangeStateEvent( this,
                new GraphPanelVertexState( this.editor.graphscrollpane.gpanel )) );
    }
}


class ToolBarEdgeButtonListener implements ActionListener {
    GraphEditor    editor;

    public ToolBarEdgeButtonListener( GraphEditor editor ) {
        this.editor = editor;
    }

    public void actionPerformed( ActionEvent actionevent ) {
        this.editor.processChangeStateEvent(
            new ChangeStateEvent( this,
                new GraphPanelEdgeState( this.editor.graphscrollpane.gpanel )) );

    }
}


class ToolBarLayoutButtonListener implements ActionListener {
    GraphEditor    editor;

    public ToolBarLayoutButtonListener( GraphEditor editor ) {
        this.editor = editor;
    }

    public void actionPerformed( ActionEvent actionevent ) {
        this.editor.graphscrollpane.getVisualGraph().layout();
    }
}

class ToolBarPrintButtonListener implements ActionListener {
    GraphEditor    editor;

    public ToolBarPrintButtonListener( GraphEditor editor ) {
        this.editor = editor;
    }

    public void actionPerformed( ActionEvent actionevent ) {
        PrintUtilities.printComponent( this.editor.graphscrollpane.gpanel );
    }
}


class OpenButtonListener implements ActionListener {
    GraphEditor     editor;
    File            file;
    FileReader      reader;
 
    public OpenButtonListener( GraphEditor editor ) {
        this.editor = editor;
    }

    public void actionPerformed( ActionEvent actionevent ) {
    }
}


class SaveButtonListener implements ActionListener {
    GraphEditor     editor;
    File            file;
    FileWriter      writer;

    public SaveButtonListener( GraphEditor editor ) {
        this.editor = editor;
    }

    public void actionPerformed( ActionEvent actionevent ) {
    }
}

class XMLFileFilter extends javax.swing.filechooser.FileFilter {

    private static final String XGMML_EXTENSION = ".xgmml";
    private static final String XGMML_DESCRIPTION = "XGMML ( *.xgmml )";


    public XMLFileFilter() {}

    public boolean accept( File file ) {
        if( ( file.isFile() && file.getName().endsWith( XGMML_EXTENSION )) ||
            file.isDirectory())
            return true;
        else
            return false;
    }

    public String getDescription() {
        return XMLFileFilter.XGMML_DESCRIPTION;
    }

}