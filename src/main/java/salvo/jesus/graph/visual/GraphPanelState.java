package salvo.jesus.graph.visual;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.Serializable;

/**
 * Abstract state object of GraphPanel, applying the state design pattern.
 * <p>
 * Implementations of this class now also acts as state machines, in that
 * an input to a state may return a new state. The input can be
 * mouse and keyboard events or a <tt>ChangeStateEvent</tt>. <p>
 * <p>
 * For mouse and keyboard events, after a state has processed the input,
 * the input is then passed on to the VisualGraphComponent, if any, to process the input.
 * These are done via the mouseXXX() and keyEvent() methods.
 * <p>
 * For <tt>ChangeStateEvent</tt>s, they can be passed to the method
 * <tt>recommendState()</tt> where a new state may or may not be returned.
 *
 * @author  Jesus M. Salvo Jr.
 * @author  Armin Groll
 */

public abstract class GraphPanelState implements Serializable {
	/**
	 * The GraphPanel object that has the specified state.
	 */
	DefaultGraphPanel gpanel;

	/**
	 * Creates a GraphPanelState object for the specified GraphPanel object.
	 */
	GraphPanelState(DefaultGraphPanel gpanel) {
		this.gpanel = gpanel;
	}

	/**
	 * With this constructor, you have to make sure to set the GraphPanel associated with the state.
	 * This is done by GraphPanel automatically, if it is constructed with new GraphPanel(GraphPanelState gps);
	 */
	GraphPanelState() {
	}

	public void setGraphPanel(DefaultGraphPanel gpanel) {
		this.gpanel = gpanel;
	}

	/**
	 * Returns a state depending on the <tt>ChangeStateEvent</tt>. Implementations
	 * may ignore the event, by simply returning itself. Implementations may also
	 * return a different state than that indicated by the event. Callers of this
	 * method should therefore check the new state returned by this method.
	 * <p>
	 * This method is called by GraphPanel from within the GraphPanel's processChangeStateEvent method.
	 * @param cse an event object that gives this state a hint on the direction of the transition.
	 * @return the resulting state, either this state again, or a new state. GraphPanel will take this as the new state.
	 *
	 */
	public GraphPanelState recommendState(ChangeStateEvent cse) {
		return cse.getRequestedState();
	}

	/**
	 * GraphPanel delegates all MouseEvents to its current state. The state will do the actions desired.
	 * It will inform the targeted VisualGraphComponent via informTargetVisualGraphComponentOfMouseEvent(e) and
	 * return itself as the resulting state, if you don't override it.
	 * @param e the MouseEvent, as it was received by the GraphPanel.
	 * @return the resulting state, either this state again, or a new state. GraphPanel will take this as the new state.
	 *
	 */
	public GraphPanelState mousePressed(MouseEvent e) {
		informTargetVisualGraphComponentOfMouseEvent(e);
		return this;
	}

	/**
	 * GraphPanel delegates all MouseEvents to its current state. The state will do the actions desired.
	 * It will inform the targeted VisualGraphComponent via informTargetVisualGraphComponentOfMouseEvent(e) and
	 * return itself as the resulting state, if you don't override it.
	 * @param e the MouseEvent, as it was received by the GraphPanel.
	 * @return the resulting state, either this state again, or a new state. GraphPanel will take this as the new state.
	 *
	 */
	public GraphPanelState mouseReleased(MouseEvent e) {
		informTargetVisualGraphComponentOfMouseEvent(e);
		return this;
	}

	/**
	 * GraphPanel delegates all MouseEvents to its current state. The state will do the actions desired.
	 * It will inform the targeted VisualGraphComponent via informTargetVisualGraphComponentOfMouseEvent(e) and
	 * return itself as the resulting state, if you don't override it.
	 * @param e the MouseEvent, as it was received by the GraphPanel.
	 * @return the resulting state, either this state again, or a new state. GraphPanel will take this as the new state.
	 *
	 */
	public GraphPanelState mouseDragged(MouseEvent e) {
		informTargetVisualGraphComponentOfMouseEvent(e);
		return this;
	}

	/**
	 * GraphPanel delegates all MouseEvents to its current state. The state will do the actions desired.
	 * It will inform the targeted VisualGraphComponent via informTargetVisualGraphComponentOfMouseEvent(e) and
	 * return itself as the resulting state, if you don't override it.
	 * @param e the MouseEvent, as it was received by the GraphPanel.
	 * @return the resulting state, either this state again, or a new state. GraphPanel will take this as the new state.
	 *
	 */
	public GraphPanelState mouseEntered(MouseEvent e) {
		informTargetVisualGraphComponentOfMouseEvent(e);
		return this;
	}

	/**
	 * GraphPanel delegates all MouseEvents to its current state. The state will do the actions desired.
	 * It will inform the targeted VisualGraphComponent via informTargetVisualGraphComponentOfMouseEvent(e) and
	 * return itself as the resulting state, if you don't override it.
	 * @param e the MouseEvent, as it was received by the GraphPanel.
	 * @return the resulting state, either this state again, or a new state. GraphPanel will take this as the new state.
	 *
	 */
	public GraphPanelState mouseExited(MouseEvent e) {
		informTargetVisualGraphComponentOfMouseEvent(e);
		return this;
	}

	/**
	 * GraphPanel delegates all MouseEvents to its current state. The state will do the actions desired.
	 * It will inform the targeted VisualGraphComponent via informTargetVisualGraphComponentOfMouseEvent(e) and
	 * return itself as the resulting state, if you don't override it.
	 * @param e the MouseEvent, as it was received by the GraphPanel.
	 * @return the resulting state, either this state again, or a new state. GraphPanel will take this as the new state.
	 *
	 */
	public GraphPanelState mouseClicked(MouseEvent e) {
		informTargetVisualGraphComponentOfMouseEvent(e);
		return this;
	}

	/**
	 * GraphPanel delegates all MouseEvents to its current state. The state will do the actions desired.
	 * It will inform the targeted VisualGraphComponent via informTargetVisualGraphComponentOfMouseEvent(e) and
	 * return itself as the resulting state, if you don't override it.
	 * @param e the MouseEvent, as it was received by the GraphPanel.
	 * @return the resulting state, either this state again, or a new state. GraphPanel will take this as the new state.
	 *
	 */
	public GraphPanelState mouseMoved(MouseEvent e) {
		informTargetVisualGraphComponentOfMouseEvent(e);
		return this;
	}

	public GraphPanelState keyPressed(KeyEvent e) {
		return this;
	}

	public GraphPanelState keyReleased(KeyEvent e) {
		return this;
	}

	public GraphPanelState keyTyped(KeyEvent e) {
		return this;
	}

	/**
	 * This method calls processMouseEvent(e) upon the target (if any) VisualGraphComponent
	 * of a mouse action.
	 *
	 * @param e the MouseEvent.
	 */
	public void informTargetVisualGraphComponentOfMouseEvent(MouseEvent e) {
		VisualGraphComponent component;

		// Notify the VisualGraphComponent of the event
		component = this.gpanel.getVisualGraph().getNode(e.getX(), e.getY());
		if (component == null) {
			component =
				this.gpanel.getVisualGraph().getVisualEdge(e.getX(), e.getY());
		}
		if (component != null) {
			switch (e.getID()) {
				case MouseEvent.MOUSE_DRAGGED :
				case MouseEvent.MOUSE_MOVED :
					component.processMouseMotionEvent(e);
					break;
				default :
					component.processMouseEvent(e);
			}
		}
	}

	/**
	 * This method calls processKeyEvent(e) upon the target (if any) VisualGraphComponent
	 * of a key action.
	 * <p>
	 * Since this method is only called when the focus is on
	 * <tt>GraphPanel</tt>, and <tt>VisualGraphComponent</tt>s are themselves
	 * not AWT or Swing components, it is up to this class to determine which
	 * <tt>VisualGraphComponent</tt> is the current "focus". Therefore, currently,
	 * this method is empty, and even though there is a <tt>processKeyEvent()</tt> method
	 * in <tt>VisualGraphComponent</tt>, we still have a devise a scheme
	 * to determine the currently focused <tt>VisualGraphComponent</tt> based on
	 * the key event. A simple solution may be to simply query
	 * <tt>GraphPanelNormalState</tt> for the component in focus.
	 *
	 * @param e the KeyEvent.
	 */
	public void informTargetVisualGraphComponentOfKeyEvent(KeyEvent e) {
		return;
	}

	/**
	 * Different painting actions may occur depending on the GraphPanel's state.
	 * 
	 * this paint method should be called by all state subclasses
	 * as it resizes the panel
	 */
	public abstract void paint(Graphics2D ged);
}
