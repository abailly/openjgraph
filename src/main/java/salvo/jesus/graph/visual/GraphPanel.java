/**
 * 
 */
package salvo.jesus.graph.visual;

/**
 * Interface for all kind of panels displaying visual graphs.
 * 
 * @author nono
 *
 */
public interface GraphPanel {

	void setZoomFactor(double d);
	
	void repaint();

	double getZoomFactor();
	
}
