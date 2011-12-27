package salvo.jesus.graph;


/**
 * A vertex in a graph. This class encapsulates an object that the vertex will represent.
 * Hence, a Vertex can represent any object that extends java.lang.Object by simply
 * calling setObject() or specifying the objet on the constructor.
 *
 * @author		Jesus M. Salvo Jr.
 */
public class VertexImpl implements Vertex {

	/**
		* The object that the vertex represents.
		*/
	protected Object	object;

	/**
	 * The string returned when toString() is called.
	 */
	protected String    str;

	/**
		* Constructor that initializes this.object to null
		*/
	public VertexImpl( ){
		object = null;
	}

	/**
		* Creates a new Vertex object that initializes this.object to newobject
		*
		* @param	newobject	The object that the Vertex will encapsulate
		*/
	public VertexImpl( Object newobject ) {
		this.object = newobject;
		this.str = this.object.toString();
	}

	/**
		* Getter method that returns the object that the Vertex represents
		*
		* @return	The object that this Vertex encapsulates
		*/
	public Object getObject( ){
		return object;
	}

	/**
		* Setter method sets this.object to newobject
		*/
	public void setObject( Object newobject ){
		this.object = newobject;
	}


	public void setString( String string ) {
		this.str = string;
	}

	/**
		* If <tt>setString()</tt> has never been called, this
		* method then simply calls <tt>this.object.toString()</tt>.
		* Otherwise, it returns the string that was set during the
		* call to <tt>setString()</tt>.
		*
		* @return   String representation of the Vertex object
		*/
	public String toString( ){
		if( this.object != null )
				return this.object.toString();
		else
		  return "";
	}
  
}
