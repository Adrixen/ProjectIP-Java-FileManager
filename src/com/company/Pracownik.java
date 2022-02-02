package com.company;

import com.dropbox.core.DbxException;

import javax.swing.*;
import java.io.File;


/**
 *  Klasa która jest GUI dla pracownika
 *  W danej klasie zaimplementowane GUI.
 */
public
class Pracownik
{
    private JPanel        panelPracownik;
    private JButton       chooserButton;
    private JButton       wyslijPlikButton;
    private JTextField    textField1;
    private JButton       wyslijPlikDoZatwierdzeniaButton;
    private JButton       odswiezListePlikowButton;
    private JButton       pobierzWybranyPlikButton;
    private JButton       usunPlikButton;
    private JButton       wyswietlDokumentButton;
    private JButton       wylogujButton;
    private JList<String> list1;

    void refresherPlikow ( ) throws DbxException
    {
        Main.listFilesForPracownik ( );
        DefaultListModel<String> list  = new DefaultListModel<> ( );
        String[]                 items = Main.listaPlikowP.split ( "\n" );
        for ( String item : items )
        {
            list.addElement ( item );
        }
        list1.setModel ( list );
    }


    public
    Pracownik ( )
    {
        Runnable runnable =
                ( ) -> {
                    while ( true )
                    {
                        try
                        {
                            if ( Main.permission == 1 && list1.isSelectionEmpty () )
                            {
                                refresherPlikow ( );
                            }
                        }
                        catch ( DbxException dbxException )
                        {
                            dbxException.printStackTrace ( );
                        }
                        try
                        {
                            Thread.sleep ( 2000 );
                        }
                        catch ( InterruptedException interruptedException )
                        {
                            interruptedException.printStackTrace ( );
                            Thread.currentThread ( ).interrupt ( );
                        }
                    }
                };
        Thread threadPracownikRefresher = new Thread ( runnable );
        threadPracownikRefresher.start ( );
        chooserButton.addActionListener ( e -> Main.chooseFile ( ) );
        wyslijPlikButton.addActionListener ( e -> {
            if ( ! textField1.getText ( ).isEmpty ( ) )
            {
                Main.uploadFile ( Main.pathPliku , "/pracownik/" + Main.idPracownika + " - " + textField1.getText ( ) + "." + Main.tempFileExtension );
            }
            else
            {
                Main.infoBox ( "Podaj nazwe pliku!" , "Error" );
            }
        } );
        wyslijPlikDoZatwierdzeniaButton.addActionListener ( e -> {
            if ( ! textField1.getText ( ).isEmpty ( ) )
            {
                String tempNazwaPliku = list1.getSelectedValue ( ).substring ( list1.getSelectedValue ( ).lastIndexOf ( "/" ) + 1 ).trim ( );
                try
                {
                    Main.moveFile ( list1.getSelectedValue ( ) , "/kierownik/" + tempNazwaPliku );
                    list1.clearSelection ();
                }
                catch ( DbxException dbxException )
                {
                    dbxException.printStackTrace ( );
                }
                Main.infoBox ( "Przenoszenie zakończone powodzeniem!" , "Sukces!" );
            }
            else
            {
                Main.infoBox ( "Podaj nazwe pliku!" , "Error" );
            }
        } );
        odswiezListePlikowButton.addActionListener ( e -> {
            try
            {
                refresherPlikow ( );
            }
            catch ( DbxException dbxException )
            {
                dbxException.printStackTrace ( );
            }
        } );
        pobierzWybranyPlikButton.addActionListener ( e -> {
            Main.saveFile ( );
            String tempNazwaPliku = list1.getSelectedValue ( ).substring ( list1.getSelectedValue ( ).lastIndexOf ( "/" ) + 1 ).trim ( );
            Main.downloadFile ( list1.getSelectedValue ( ) , new File ( Main.pathFolderu + "/" + tempNazwaPliku ) );
            list1.clearSelection ();
        } );
        usunPlikButton.addActionListener ( e -> {
            try
            {
                Main.deleteFile ( list1.getSelectedValue ( ) );
                list1.clearSelection ();
            }
            catch ( DbxException | IllegalArgumentException dbxException )
            {
                Main.infoBox ( "Usuwanie nie powiodło się! Czy nazwa pliku jest poprawna?" , "Error" );
            }
        } );
        wyswietlDokumentButton.addActionListener ( e -> {
            try
            {
                DocumentPreview.numerStrony = 0;
                Main.generateFilePreview ( list1.getSelectedValue ( ) );
                DocumentPreview.displayDocument ( );
                list1.clearSelection ();
            }
            catch ( DbxException | IllegalArgumentException dbxException )
            {
                Main.infoBox ( "Wystąpił błąd, prawdopodobnie została podana nieprawidłowa ścieżka do pliku" +
                                       " lub został wybrany nieobsługiwany format pliku.\nDozwolone formaty to: .ai, .doc," +
                                       " .docm, .docx, .eps, .gdoc, .gslides, .odp, .odt, .pps, .ppsm, .ppsx, .ppt, .pptm, .pptx," +
                                       " .rtf." , "Error" );
            }
        } );
        wylogujButton.addActionListener ( e -> {
            Main.framePracownik.setVisible ( false );
            Main.zalogowanyPomyslnie = 0;
            Main.frameLogowanie.setVisible ( true );
            list1.clearSelection ();
        } );
    }

    public
    JPanel getPanel ( )
    {
        return panelPracownik;
    }
}
