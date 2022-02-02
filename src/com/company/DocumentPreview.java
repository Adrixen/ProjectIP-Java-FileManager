package com.company;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class DocumentPreview
{
    static int numerStrony = 0;
    static int rzeczywistaIloscStron;
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
                    PDPage      page    = doc.getPage ( numerStrony );
                    PDRectangle cropBox = page.getCropBox ( );
                    boolean     rot     = page.getRotation ( ) == 90 || page.getRotation ( ) == 270;

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
                    rzeczywistaIloscStron = doc.getNumberOfPages ();
                    renderer.renderPageToGraphics ( numerStrony , (Graphics2D) g , scale );
                }
                catch ( IOException e )
                {
                    e.printStackTrace ( );
                }
            }
        };
    }


    static void nastepnaStrona()
    {
        try
        {
            if ( numerStrony < rzeczywistaIloscStron-1 )
            {
                frameDocument.dispose ();
                numerStrony++;
                DocumentPreview.displayDocument ( );
            }
        }
        catch( IndexOutOfBoundsException ex)
        {
            frameDocument.dispose ();
        }
    }

    static void poprzedniaStrona()
    {
        try
        {
            if ( numerStrony > 0 )
            {
                frameDocument.dispose ();
                numerStrony--;
                DocumentPreview.displayDocument ( );
            }
        }
        catch( IndexOutOfBoundsException ex)
        {
            frameDocument.dispose ();
        }
    }
    static void arrowKeysListener()
    {
        frameDocument.addKeyListener ( new KeyListener ( )
        {
            @Override
            public
            void keyTyped ( KeyEvent e )
            {
            }

            @Override
            public
            void keyPressed ( KeyEvent e )
            {
            }

            @Override
            public
            void keyReleased ( KeyEvent e )
            {
                if(e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    poprzedniaStrona();
                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    nastepnaStrona ();
                }
            }
        });
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