package salvo.jesus.graph.visual;

import salvo.jesus.graph.javax.swing.JTabPanel;

/**
 * An interface defining a factory that returns an array of <tt>JTabPanel</tt>.
 * The returned <tt>JTabPanel</tt>s are added to the <tt>GraphTabbedPane</tt>
 * as tab pages when right-clicking on a <tt>VisualGraphComponent</tt>.
 * It is up to the implementor to write custom <tt>JTabPanel</tt>s that will
 * allow the end-user to modify the properties of the right-clicked
 * <tt>VisualGraphComponent</tt>.
 *
 * @author  Jesus M. Salvo Jr.
 */

public interface VisualGraphComponentEditorFactory {

    /**
     * Implementation of this method should returns an array of
     * <tt>JTabPanel</tt>s that will be added to <tt>GraphTabbedPane</tt>.
     */
    public JTabPanel[] getTabEditors( VisualGraphComponent component );

}