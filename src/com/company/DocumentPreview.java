package com.company;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DocumentPreview
{
    static JFrame frameDocument = new JFrame();
    private static
    JPanel getTestPanel ( )
    {
        final PDDocument doc;
        try
        {
            doc = Loader.loadPDF ( new File ( "output.pdf" ) );

        }
        catch ( IOException e )
        {
            e.printStackTrace ( );
            return null;
        }
        final PDFRenderer renderer = new PDFRenderer ( doc );
        return new JPanel ( )
        {

            @Override
            protected
            void paintComponent ( Graphics g )
            {
                try
                {
                    g.setColor ( Color.black );
                    g.fillRect ( 0 , 0 , getWidth ( ) , getHeight ( ) );
                    PDPage      page    = doc.getPage ( 0 );
                    PDRectangle cropBox = page.getCropBox ( );
                    boolean     rot     = page.getRotation ( ) == 90 || page.getRotation ( ) == 270;

                    // https://stackoverflow.com/questions/1106339/resize-image-to-fit-in-bounding-box
                    float imgWidth  = rot ? cropBox.getHeight ( ) : cropBox.getWidth ( );
                    float imgHeight = rot ? cropBox.getWidth ( ) : cropBox.getHeight ( );
                    float xf        = getWidth ( ) / imgWidth;
                    float yf        = getHeight ( ) / imgHeight;
                    float scale     = Math.min ( xf , yf );
                    if ( yf < xf )
                    {
                        g.translate ( (int) ( ( getWidth ( ) - imgWidth * yf ) / 2 ) , 0 );
                    }
                    else
                    {
                        g.translate ( 0 , (int) ( ( getHeight ( ) - imgHeight * xf ) / 2 ) );
                    }
                    renderer.renderPageToGraphics ( 0 , (Graphics2D) g , scale );
                }
                catch ( IOException e )
                {
                    e.printStackTrace ( );
                }
            }
        };
    }
    static void displayDocument()
    {
        frameDocument.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frameDocument.add(DocumentPreview.getTestPanel());
        frameDocument.pack();
        frameDocument.setSize(800, 800);
        Dimension paneSize = frameDocument.getSize();
        Dimension screenSize = frameDocument.getToolkit().getScreenSize();
        frameDocument.setLocation((screenSize.width - paneSize.width) / 2, (screenSize.height - paneSize.height) / 2);
        frameDocument.setTitle("Podgląd dokumentu");
        frameDocument.setVisible(true);
    }
}