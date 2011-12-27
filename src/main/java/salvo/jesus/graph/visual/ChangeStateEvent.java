package salvo.jesus.graph.visual;

/**
 * This event acts as a request to change the state of a <tt>GraphPanel</tt>.
 *
 * @author  Armin Groll
 */
public class ChangeStateEvent extends java.util.EventObject {

    private GraphPanelState requestedState;

    public ChangeStateEvent(Object source, GraphPanelState requestedState) {
        super(source);
        this.requestedState = requestedState;
    }

    /**
     * Returns the requested state to change to
     */
    public GraphPanelState getRequestedState() {
        return this.requestedState;
    }

}
