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
package salvo.jesus.graph.visual.layout;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


/**
 * User interface for selecting layout parameters.
 * This interface allows the user to set various parameters of
 * the digraph layout of an automaton:
 * <ul>
 * <li>Interlayers space and intra layer spaces</li>
 * <li>Size of virtual vertices </li>
 * <LI>direction of layout</li>
 * </ul>
 * 
 * @author nono
 * @version $Id: DigraphLayoutUI.java 1182 2005-12-07 22:44:40Z nono $
 */
public class DigraphLayoutUI extends JPanel {

    /*
     * the controlled layout
     */
    private DigraphLayeredLayout layoutManager;

    private JTextField intraLayer;

    private JTextField interLayer;

    private JComboBox direction;

    /**
     * Initializes the UI
     *
     */
    public DigraphLayoutUI() {
        /* listener for content update */
        ActionListener l = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateLayout();
            }
        };
        /* border and gaps */
        this.setBorder(new EmptyBorder(3,3,3,3));
        /* sizes */
        this.intraLayer = new JTextField(5);
        this.intraLayer.setToolTipText("Sets the space between vertices");
        this.intraLayer.addActionListener(l);

        this.add(new JLabel("Interval"));
        this.add(intraLayer);
        this.interLayer = new JTextField(5);
        this.interLayer.setToolTipText("Sets the space between layers");
        this.interLayer.addActionListener(l);
        this.add(new JLabel("Margin"));
        this.add(interLayer);
        /* direction */
        this.direction = new JComboBox(new String[] { "Left to Right",
                "Right to left", "Top to Bottom", "Bottom to top" });
        this.direction.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                updateLayout();
            }

        });
        this.add(new JLabel("Direction"));
        this.add(this.direction);
    }

    /**
     * @return Returns the layout.
     */
    public DigraphLayeredLayout getLayoutManager() {
        return layoutManager;
    }

    /**
     * @param layout The layout to set.
     */
    public void setLayoutManager(DigraphLayeredLayout layout) {
        this.layoutManager = layout;
        updateWidgets();
    }

    /**
     * Update the value of the widgets to reflect the parameters 
     * of current layout.
     *
     */
    private void updateWidgets() {
        this.interLayer.setText(Double.toString(layoutManager.getMargin()));
        this.intraLayer.setText(Double.toString(layoutManager.getInterval()));
        this.direction.setSelectedIndex(layoutManager.getAxis());
    }

    /**
     * Update the layout.
     *
     */
    private void updateLayout() {
        layoutManager.setAxis(this.direction.getSelectedIndex());
        layoutManager
                .setInterval(Double.parseDouble(this.intraLayer.getText()));
        layoutManager.setMargin(Double.parseDouble(this.interLayer.getText()));
        // redo layout
        layoutManager.layout();
    }

}
