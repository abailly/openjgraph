package salvo.jesus.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.RepaintManager;

/**
 * Generic printing utility.
 * <p>
 * The original source code for this class only was obtained from
 * http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-Printing.html
 * without restrictions as specified on their web site. To quote the link above:
 * <p>
 * <i>All source code freely available for unrestricted use.</i>
 * <p>
 * However, additional changes have been made to the original.
 */


public class PrintUtilities implements Printable {

    private Component componentToBePrinted;

 
    public static void printComponent(Component c) {
        new PrintUtilities(c).print();
    }

    public PrintUtilities(Component componentToBePrinted) {
        this.componentToBePrinted = componentToBePrinted;
    }

    public void print() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        if (printJob.printDialog())
        try {
            printJob.print();
        } catch(PrinterException pe) {
        }
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {

        if (pageIndex > 0) {
            return(NO_SUCH_PAGE);
        } else {
            Graphics2D g2d = (Graphics2D)g;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            disableDoubleBuffering(componentToBePrinted);
            componentToBePrinted.paint(g2d);
            enableDoubleBuffering(componentToBePrinted);
            return(PAGE_EXISTS);
        }
    }

    public static void disableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }

    public static void enableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }
}
