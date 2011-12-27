/*
 * Created on Jul 9, 2004
 * 
 */
package salvo.jesus.graph.visual.drawing;

import java.awt.Color;

import salvo.jesus.graph.visual.VisualGraphComponent;

/**
 * This subclass allows change on the colors used for drawing vertices
 * 
 * @author nono
 * @version $Id: AltColorVertexPainter.java 1157 2005-12-01 10:45:46Z nono $
 */
public class AltColorVertexPainter extends VisualVertexPainterImpl {

  private Color fillColor;
  private Color outlineColor;
  private Color fontColor;

  /**
   * @return Returns the fillColor.
   */
  public Color getFillColor() {
    return fillColor;
  }
  /**
   * @param fillColor The fillColor to set.
   */
  public void setFillColor(Color fillColor) {
    this.fillColor = fillColor;
  }
  /**
   * @return Returns the fontColor.
   */
  public Color getFontColor() {
    return fontColor;
  }
  /**
   * @param fontColor The fontColor to set.
   */
  public void setFontColor(Color fontColor) {
    this.fontColor = fontColor;
  }
  /**
   * @return Returns the outlineColor.
   */
  public Color getOutlineColor() {
    return outlineColor;
  }
  /**
   * @param outlineColor The outlineColor to set.
   */
  public void setOutlineColor(Color outlineColor) {
    this.outlineColor = outlineColor;
  }
  
  /**
   * Sets all the colors used for painting vertices
   * 
   * @param fill color for filling the vertex
   * @param outl color for bordering the vertex
   * @param font color for printing the text
   */
	public AltColorVertexPainter(Color fill,Color outl,Color font) {
		this.fillColor = fill;
		this.outlineColor = outl;
		this.fontColor = font;
	}

	/**
	 * Sets only the filling color 
	 * 
	 * @param fill color for filling vertices
	 */
	public AltColorVertexPainter(Color fill) {
		this.fillColor = fill;
	}

	/**
	 * Use default colors for painting vertices
	 *
	 */
	public AltColorVertexPainter(){
	}
	
	public void paintFill(
		VisualGraphComponent component,
		java.awt.Graphics2D g2d) {
		if(fillColor != null)
		  component.setFillcolor(fillColor);
		if(outlineColor != null)
		  component.setOutlinecolor(outlineColor);
		if(fontColor != null)
		  component.setFontcolor(fontColor);
		super.paintFill(component, g2d);
	}
}
/* 
 * $Log: AltColorVertexPainter.java,v $
 * Revision 1.2  2004/07/28 16:05:50  bonte
 * *** empty log message ***
 *
 * Revision 1.1  2004/07/13 15:40:14  bailly
 * added an interface for visualgraphoutput to a file system and implementation class for image output
 *
*/