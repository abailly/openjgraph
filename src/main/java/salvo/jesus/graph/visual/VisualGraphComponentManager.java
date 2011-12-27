package salvo.jesus.graph.visual;

import salvo.jesus.graph.Edge;


/**
 * An abstract class providing an entry point for creating
 * VisualGraphComponent objects.
 *
 * This class provides a framework for interfacing
 * a custom visual componetn factory and methods for VisualGraph 
 * to create components 
 *
 * @author Arnaud Bailly
 * @version 31082002
 */
public final class VisualGraphComponentManager
	implements VisualGraphComponentFactory {

	//////////////////////////////////////////////
	// STATIC MEMBERS
	/////////////////////////////////////////////

	private static VisualGraphComponentFactory theFactory;

	//////////////////////////////////////////////
	// STATIC METHODS
	/////////////////////////////////////////////

	/**
	 * Retrieve the current graph component factory
	 *
	 * This method returns a factory object according to
	 * predefined properties :
	 * <ol>
	 * <li>if there exists a resource named META-INF/services/salvo.jesus.graph.visual.VisualGraphComponentFactory,
	 * the content of this resource is taken as a class name to instantiate as the factory ;</li>
	 * <li> else, if the system property salvo.jesus.graph.visual.VisualGraphComponentFactory is defined, its
	 * value is taken as a class name to instantiate as the factory ;</li>
	 * <li> else, an instance of VisualGraphComponentManager is returned as a default factory</li>
	 *</ol>
	 *
	 * @return a instance of VisualGraphComponentFactory
	 */
	public static VisualGraphComponentFactory getFactory() {
		String factname = null;
		if (theFactory == null) {
			try {
				//  try to find resource in META-INF/services directory
				java.io.InputStream is =
					VisualGraphComponentFactory
						.class
						.getClassLoader()
						.getResourceAsStream(
						"META-INF/services/"
							+ VisualGraphComponentFactory.class.getName());
				if (is != null) {
					java.io.BufferedReader reader =
						new java.io.BufferedReader(
							new java.io.InputStreamReader(is));
					factname = reader.readLine();
				}
				if (factname == null) // try system property
					factname =
						System.getProperty(
							VisualGraphComponentFactory.class.getName());
				if (factname == null) // return default factory
					return theFactory = new VisualGraphComponentManager();
				// instantiate given class
				Class clazz = Class.forName(factname);
				theFactory = (VisualGraphComponentFactory) clazz.newInstance();
			} catch (Exception ex) {
				System.err.println("Unable to instantiate factory " + factname);
				return theFactory = new VisualGraphComponentManager();
			}
		}
		return theFactory;
	}


	/**
	 * Defines the factory to use
	 * 
	 * This method sets the factory that will be used for all visual graph components
	 * creation
	 * 
	 * @param fact a VisualGraphComponentFactory object
	 */
	public static void setFactory(VisualGraphComponentFactory fact ) {
		theFactory  = fact;
	}
	
	//////////////////////////////////////////////
	// CONSTRUCTOR
	/////////////////////////////////////////////

	/**
	 * The one and only private constructor
	 */
	private VisualGraphComponentManager() {
	}

	//////////////////////////////////////////////
	// PUBLIC METHODS
	/////////////////////////////////////////////

	/**
	 * Creates a VisualVertex given a Object and
	 * a VisualGraph.
	 *
	 * This default implementation simply returns a newly allocated
	 * VisualVertex object
	 * 
	 * @see salvo.jesus.graph.visual.VisualGraphComponentFactory
	 */
	public VisualVertex createVisualVertex(Object vertex, VisualGraph graph) {
		return new VisualVertex(vertex, graph);
	}

	/**
	 * Creates a VisualEdge given a Edge and
	 * a VisualGraph.
	 *
	 * This default implementation simply returns a newly allocated
	 * VisualEdge object
	 * 
	 * @see salvo.jesus.graph.visual.VisualGraphComponentFactory
	 */
	public VisualEdge createVisualEdge(Edge edge, VisualGraph graph) {
		return new VisualEdge(edge, graph);
	}

	/**
	 * Creates an instance of Arrowhead.
	 *
	 * This method should be overriden as needed to provide
	 * customized Arrowhead shapes
	 *
	 * @return an instance of Arrowhead
	 */
	public Arrowhead createArrowhead() {
		return new DefaultArrowhead();
	}

}
