package salvo.jesus.graph.visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import salvo.jesus.graph.javax.swing.JTabPanel;

/**
 * A JPanel that contains a GraphTabbedPane and Apply, OK, Cancel JButtons.
 * The intention of is for this class to be reused either in a JDialog or
 * as a properties panel in an internal frame.
 *
 * @author  Jesus M. Salvo Jr.
 */
class VisualGraphComponentPropertiesPanel extends JPanel implements ActionListener {
  GraphTabbedPane     tabpages;
  JButton             apply;
  JButton             ok;
  JButton             cancel;
  static final String              APPLY = "Apply";
  static final String              OK = "OK";
  static final String              CANCEL = "Cancel";

  /**
   * Creates a VisualGraphComponentPropertiesPanel object with the
   * specified VisualGraphComponent as the context.
   *
   * @param   vgcomponent   VisualGraphComponent object which will be the context.
   */
  public VisualGraphComponentPropertiesPanel( VisualGraphComponent vgcomponent ) {
    try {
      this.initVisualGraphComponentPropertiesPanel( vgcomponent );
    }
    catch( Exception e ) {
      e.printStackTrace();
    }
  }

  /**
   * Creates and layouts the components and registers itself as a listener
   * for events on the created JButtons.
   */
  private void initVisualGraphComponentPropertiesPanel( VisualGraphComponent vgcomponent ) throws Exception {
    JPanel  buttonpanel = new JPanel();

    tabpages = new GraphTabbedPane( vgcomponent );

    apply       = new JButton( VisualGraphComponentPropertiesPanel.APPLY );
    ok          = new JButton( VisualGraphComponentPropertiesPanel.OK );
    cancel      = new JButton( VisualGraphComponentPropertiesPanel.CANCEL );

    buttonpanel.setLayout( new FlowLayout( FlowLayout.RIGHT ));
    buttonpanel.add( apply );
    buttonpanel.add( ok );
    buttonpanel.add( cancel );

    apply.setActionCommand( apply.getText() );
    ok.setActionCommand( ok.getText() );
    cancel.setActionCommand( cancel.getText() );

    apply.addActionListener( this );
    ok.addActionListener( this );
    cancel.addActionListener( this );

    this.setLayout( new BorderLayout( ));
    this.add( tabpages, BorderLayout.CENTER );
    this.add( buttonpanel, BorderLayout.SOUTH );
  }

  /**
   * Convenience method so that other classes should not directly call
   * apply.addActionListener().
   *
   * @param   listener    AddActionListener that will listen for button
   *                      clicks on the apply JButton.
   */
  public void addApplyActionListener( ActionListener listener  ) {
    this.apply.addActionListener( listener );
  }

  /**
   * Convenience method so that other classes should not directly call
   * ok.addActionListener().
   *
   * @param   listener    AddActionListener that will listen for button
   *                      clicks on the ok JButton.
   */
  public void addOKActionListener( ActionListener listener  ) {
    this.ok.addActionListener( listener );
  }

  /**
   * Convenience method so that other classes should not directly call
   * cancel.addActionListener().
   *
   * @param   listener    AddActionListener that will listen for button
   *                      clicks on the cancel JButton.
   */
  public void addCancelActionListener( ActionListener listener  ) {
    this.cancel.addActionListener( listener );
  }

  /**
   * Implementation of the actionPerformed method of the ActionListener interface.
   * If the source of the event is either the ok or apply JButton, each tabpage
   * (assuming to be an instance of JTabPanel) contained in GraphTabbedPane
   * will have their ok() or apply() method called, respectively.
   */
  public void actionPerformed( ActionEvent e ) {
    String  action = e.getActionCommand();

    if( action.equals( VisualGraphComponentPropertiesPanel.APPLY ) || action.equals( VisualGraphComponentPropertiesPanel.OK ) ){
      JTabPanel tabpage;
      int       i, tabcount = tabpages.getTabCount();

      for( i = 0; i < tabcount; i++ ) {
        tabpage = (JTabPanel) tabpages.getComponentAt( i );
        if( action.equals( VisualGraphComponentPropertiesPanel.APPLY ) ) tabpage.apply();
        if( action.equals( VisualGraphComponentPropertiesPanel.OK ) ) tabpage.ok();
      }
    }
  }
}

