/**
 * 
 */
package salvo.jesus.graph.visual.layout;

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
 * Control panel for parameters of SimulatedAnnealingLayout. Instances of this
 * class may be used in GUI in conjunction with {@link SimulatedAnnealingLayout}
 * layouts to control the latter behavior.
 * 
 * @author nono
 * 
 */
public class SimulatedAnnealingUI extends JPanel {
	/*
	 * the controlled layout
	 */
	private SimulatedAnnealingLayout layoutManager;

	private JTextField attractiveForce;

	private JTextField temperature;

	private JTextField expectedDistance;

	private JTextField coolFactor;

	/**
	 * Initializes the UI
	 * 
	 */
	public SimulatedAnnealingUI() {
		/* listener for content update */
		ActionListener l = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				updateLayout();
			}
		};
		/* border and gaps */
		this.setBorder(new EmptyBorder(3, 3, 3, 3));
		/* sizes */
		this.attractiveForce = new JTextField(5);
		this.attractiveForce
				.setToolTipText("Sets the attraction factor between nodes");
		this.attractiveForce.addActionListener(l);
		this.add(new JLabel("Attraction"));
		this.add(attractiveForce);

		this.temperature = new JTextField(5);
		this.temperature.setToolTipText("Sets the initial temperature level");
		this.temperature.addActionListener(l);
		this.add(new JLabel("Temperature"));
		this.add(temperature);

		this.expectedDistance = new JTextField(5);
		this.expectedDistance
				.setToolTipText("Sets the expected distance between nodes");
		this.expectedDistance.addActionListener(l);
		this.add(new JLabel("expectedDistance"));
		this.add(expectedDistance);

		this.coolFactor = new JTextField(5);
		this.coolFactor.setToolTipText("Sets the cooling factor");
		this.coolFactor.addActionListener(l);
		this.add(new JLabel("coolFactor"));
		this.add(coolFactor);

		JButton reset = new JButton("reset");
		reset.setToolTipText("Reset current temperature to initial value");
		reset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				layoutManager.setCurrentTemperature(Double
						.parseDouble(temperature.getText()));
			}

		});
		this.add(reset);
	}

	/**
	 * @return Returns the layout.
	 */
	public SimulatedAnnealingLayout getLayoutManager() {
		return layoutManager;
	}

	/**
	 * @param layout
	 *            The layout to set.
	 */
	public void setLayoutManager(SimulatedAnnealingLayout layout) {
		this.layoutManager = layout;
		updateWidgets();
	}

	/**
	 * Update the value of the widgets to reflect the parameters of current
	 * layout.
	 * 
	 */
	private void updateWidgets() {
		this.attractiveForce.setText(Double.toString(layoutManager
				.getAttractiveForce()));
		this.temperature.setText(Double
				.toString(layoutManager.getTemperature()));
		this.expectedDistance.setText(Double.toString(layoutManager
				.getExpectedDist()));
		this.coolFactor.setText(Double.toString(layoutManager.getCoolFactor()));
	}

	/**
	 * Update the layout.
	 * 
	 */
	private void updateLayout() {
		layoutManager.setAttractiveForce(Double
				.parseDouble(this.attractiveForce.getText()));
		layoutManager.setTemperature(Double.parseDouble(this.temperature
				.getText()));
		layoutManager.setExpectedDist(Double.parseDouble(this.expectedDistance
				.getText()));
		layoutManager.setCoolFactor(Double.parseDouble(this.coolFactor
				.getText()));
	}
}
