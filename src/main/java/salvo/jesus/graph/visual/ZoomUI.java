/*______________________________________________________________________________
 * 
 * Copyright 2005 Arnaud Bailly - NORSYS/LIFL
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * (1) Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 * (2) Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 * (3) The name of the author may not be used to endorse or promote
 *     products derived from this software without specific prior
 *     written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on Dec 7, 2005
 *
 */
package salvo.jesus.graph.visual;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * User interface for manipulating zoom on a graph panel.
 * Instances of this class allow manipulation of the zoom factoors 
 * of a {@link salvo.jesus.graph.visual.GraphScrollPane} object.
 * It provide the following operations:
 * <ul>
 * <li>A combo box for selecting directly a zoom factor</li>
 * <li>Zoom-in and zoom-out buttons that increase and decrease current
 * factor by 2</li>
 * <li>Text field for setting value of zoom (in percents of normal size)</li>
 * </ul>
 * @author nono
 * @version $Id: ZoomUI.java 1204 2006-05-24 14:09:42Z nono $
 */
public class ZoomUI extends JPanel {

    /*
     * 
     */
    private JComboBox zoomList;

    /*
     * the controlled panel
     */
    private GraphPanel graphPanel;

    private JTextField zoom;

    /**
     * Create zoom ui and attaches it to panel
     * @param p
     */
    public ZoomUI(GraphPanel p) {
        this();
        setGraphPanel(p);
    }

    /**
     * Creates user interface objects
     *
     */
    public ZoomUI() {
        this.setBorder(new EmptyBorder(3, 3, 3, 3));
        /* zoom field */
        this.zoom = new JTextField(5);
        this.zoom.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updatePanel();
            }

        });
        /* zoom list */
        this.zoomList = new JComboBox(new Integer[] { new Integer(10),
                new Integer(20), new Integer(50), new Integer(75),
                new Integer(100), new Integer(125), new Integer(150),
                new Integer(200), new Integer(400) });
        this.zoomList.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem() != null) {
                        zoom.setText(e.getItem().toString());
                        updatePanel();
                    }
                }
            }

        });
        this.zoomList.setEditable(false);
        this.add(new JLabel("Zoom"));
        this.add(zoomList);
        this.add(zoom);
        /* zoom buttons */
        JButton but = new JButton(new ImageIcon(getClass().getResource(
                "/salvo/jesus/graph/visual/images/ZoomIn16.gif")));
        but.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                zoom.setText(Double
                        .toString(Double.parseDouble(zoom.getText()) * 2));
                updatePanel();
            }

        });
        this.add(but);
        but = new JButton(new ImageIcon(getClass().getResource(
                "/salvo/jesus/graph/visual/images/ZoomOut16.gif")));
        but.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                zoom.setText(Double
                        .toString(Double.parseDouble(zoom.getText()) / 2));
                updatePanel();
            }

        });
        this.add(but);
        reset();
    }

    /**
     * updates current panel zoom factor
     *
     */
    private void updatePanel() {
        if(graphPanel == null)
            return;
        double z = Double.parseDouble(zoom.getText());
        graphPanel.setZoomFactor(z / 100.0);
        graphPanel.repaint();
    }

    /**
     * @return Returns the graphPanel.
     */
    public GraphPanel getGraphPanel() {
        return graphPanel;
    }

    /**
     * @param graphPanel The graphPanel to set.
     */
    public void setGraphPanel(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
        /* update scale factor */
        this.zoom.setText(Double.toString(graphPanel.getZoomFactor()*100));
    }

    /**
     * Reset values of zoom factors
     *
     */
    public void reset() {
        this.zoom.setText("100");
        this.zoomList.setSelectedIndex(4);
    }
}
