/*
 * Created on Apr 1, 2004
 */
package salvo.jesus.graph.visual.print;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JOptionPane;

import salvo.jesus.graph.visual.VisualGraph;

/**
 * A class for printing VisualGraph objects
 * <p>
 * Instance of this class can be created to print a given graph.The graph is
 * simply resized to fit into pageFormat and then its paint method is called
 * with given graphics object
 * 
 * @author nono
 * @version $Log: VisualGraphPrinter.java,v $
 * @version Revision 1.3 2004/11/15 12:38:12 bailly
 * @version improved layout algorithm inspired from sugiyama
 * @version
 * @version Revision 1.2 2004/07/28 16:05:50 bonte
 * @version *** empty log message ***
 * @version
 * @version Revision 1.1 2004/04/05 15:27:10 bailly
 * @version ADded printing capability
 * @version
 */
public class VisualGraphPrinter implements Printable {

  /**
   * @param vg
   */
  public VisualGraphPrinter(VisualGraph vg) {
    this.visual = vg;
  }

  private VisualGraph visual;

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.print.Printable#print(java.awt.Graphics,
   *      java.awt.print.PageFormat, int)
   */
  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
      throws PrinterException {
    /* print only one page */
    if (pageIndex > 0)
      return NO_SUCH_PAGE;
    Rectangle clip = graphics.getClipBounds();
    Dimension mdim = visual.getMaxSize();
    graphics.translate(clip.x, clip.y);
    double sc = Math.min((double) (clip.width - clip.x) / (double) mdim.width,
        (double) (clip.height - clip.y) / (double) mdim.height);
    ((Graphics2D) graphics).scale(sc, sc);
    /* call visual graph paint method */
    visual.paint((Graphics2D) graphics);
    return PAGE_EXISTS;
  }

  /**
   * Prints the graph displayed
   * 
   * @param a
   */
  public void showPrint(int x, int y) {
    if (visual == null)
      return;
    Dimension mdim = visual.getMaxSize();
    /* print */
    DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
    aset.add(MediaSizeName.ISO_A4);
    if (mdim.width > mdim.height)
      aset.add(OrientationRequested.LANDSCAPE);
    aset.add(new MediaPrintableArea(5, 5, 200, 287, MediaPrintableArea.MM));
    aset.add(new Copies(1));
    PrintService[] pservices = PrintServiceLookup.lookupPrintServices(flavor,
        aset);
    PrintService service = null;
    if (pservices.length == 0) {
      /*
       * no print service , try to find a streamprintservice
       */
      /* create outputstream */
      FileOutputStream fos;
      try {
        fos = new FileOutputStream("print.ps");
      } catch (FileNotFoundException e) {
        System.err.println("Unable to open print.ps for writing :"
            + e.getMessage() + ", abort printing");
        return;
      }
      StreamPrintServiceFactory[] factories = StreamPrintServiceFactory
          .lookupStreamPrintServiceFactories(flavor, "application/postscript");
      if (factories.length > 0) {
        pservices = new StreamPrintService[factories.length];
        for (int i = 0; i < factories.length; i++)
          pservices[i] = factories[i].getPrintService(fos);
      }
    }
    if (pservices.length == 0) {
      JOptionPane.showMessageDialog(null,
          "Unable to find services for printing");
      return;
    }
    service = ServiceUI.printDialog(null, x, y, pservices, pservices[0],
        flavor, aset);
    if (service != null) {
      try {
        DocPrintJob pj = service.createPrintJob();
        HashDocAttributeSet docset = new HashDocAttributeSet();
//        docset.add(MediaSizeName.ISO_A4);
//        docset
//            .add(new MediaPrintableArea(5, 5, 200, 287, MediaPrintableArea.MM));
        Doc doc = new SimpleDoc(new VisualGraphPrinter(visual), flavor, docset);
        pj.print(doc, aset);
      } catch (PrintException e) {
        System.err.println("Error in printing graph :" + e.getMessage());
      }
    }
  }

}