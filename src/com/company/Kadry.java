package com.company;

import com.dropbox.core.DbxException;

import javax.swing.*;
import java.io.File;

/**
 *  Klasa która jest GUI dla kadrów
 *  W danej klasie zaimplementowane GUI.
 */
public
class Kadry
{
    private JPanel  panelKadry;
    private JButton chooserButton;
    private JButton wyslijPlikButton;
    private JTextField textField1;
    private JButton wyslijPlikDoZatwierdzeniaButton;
    private JButton    odswiezListePlikowButton;
    private JButton    pobierzWybranyPlikButton;
    private JButton usunPlikButton;
    private JButton cofnijPlikDoPoprawyButton;
    private JButton odswiezListeFolderArchiwumButton;
    private JButton cofnijPlikDoKierownikaButton;
    private JButton wyswietlDokumentButton;
    private JButton wylogujButton;
    private JList<String> list1;
    static boolean refreshKadry=true;
    void refresherPlikowKadry() throws DbxException
    {
        Main.listFiles ( "/kadry/" );
        DefaultListModel<String> list  = new DefaultListModel<> ();
        String[] items = Main.listaPlikowP.split("\n");

        for(String item : items)
        {
            list.addElement(item);
        }
        list1.setModel(list);
    }

    void refresherPlikowArchiwum() throws DbxException
    {
        Main.listFiles ( "/archiwum/" );
        DefaultListModel<String> list  = new DefaultListModel<> ();
        String[] items = Main.listaPlikowP.split("\n");

        for(String item : items)
        {
            list.addElement(item);
        }
        list1.setModel(list);
    }

    public Kadry( )
    {
        Runnable runnable =
                ( ) -> {
                    while ( true )
                    {
                        try
                        {
                            if ( refreshKadry && Main.permission == 3 && list1.isSelectionEmpty () )
                            {
                                refresherPlikowKadry ( );
                            }
                            else if( ! refreshKadry && Main.permission == 3 && list1.isSelectionEmpty ())
                            {
                                refresherPlikowArchiwum ();
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
        Thread threadKadryRefresher = new Thread ( runnable );
        threadKadryRefresher.start ( );
        chooserButton.addActionListener ( e -> Main.chooseFile () );
        wyslijPlikButton.addActionListener ( e -> {
            if(!textField1.getText().isEmpty ())
            {
                Main.uploadFile ( Main.pathPliku , "/kadry/kadry_" + textField1.getText ( ) + "." + Main.tempFileExtension );
            }
            else
            {
                Main.infoBox ( "Podaj nazwe pliku!","Error" );
            }
        } );
        wyslijPlikDoZatwierdzeniaButton.addActionListener ( e -> {
            if(!textField1.getText().isEmpty ())
            {
                try
                {
                    String tempNazwaPliku = list1.getSelectedValue().substring(list1.getSelectedValue().lastIndexOf("/") + 1).trim();
                    Main.moveFile ( list1.getSelectedValue(),"/archiwum/" + tempNazwaPliku );
                    Main.infoBox ( "Przenoszenie zakończone powodzeniem!", "Sukces!" );
                    list1.clearSelection ();
                }
                catch ( DbxException dbxException )
                {
                    dbxException.printStackTrace ( );
                }
            }
            else
            {
                Main.infoBox ( "Podaj nazwe pliku!","Error" );
            }
        } );
        odswiezListePlikowButton.addActionListener ( e -> {
            try
            {
                refreshKadry=true;
                refresherPlikowKadry ();
            }
            catch ( DbxException dbxException )
            {
                dbxException.printStackTrace ( );
            }
        } );
        pobierzWybranyPlikButton.addActionListener ( e -> {
            Main.saveFile ();
            String tempNazwaPliku = list1.getSelectedValue().substring(list1.getSelectedValue().lastIndexOf("/") + 1).trim();
            Main.downloadFile ( list1.getSelectedValue(), new File ( Main.pathFolderu + "/" + tempNazwaPliku ) );
            list1.clearSelection ();
        } );
        usunPlikButton.addActionListener ( e -> {
            try
            {
                Main.deleteFile (list1.getSelectedValue());
                list1.clearSelection ();
            }
            catch ( DbxException | IllegalArgumentException dbxException )
            {
                Main.infoBox ( "Usuwanie nie powiodło się! Czy nazwa pliku jest poprawna?", "Error" );
            }
        } );
        cofnijPlikDoPoprawyButton.addActionListener ( e -> {
            try
            {
                String tempNazwaPliku = list1.getSelectedValue().substring(list1.getSelectedValue().lastIndexOf("/") + 1).trim();
                Main.moveFile ( list1.getSelectedValue(),"/pracownik/" + tempNazwaPliku );
                Main.infoBox ( "Przenoszenie zakończone powodzeniem!", "Sukces!" );
                list1.clearSelection ();
            }
            catch ( DbxException dbxException )
            {
                dbxException.printStackTrace ( );
            }
        } );
        cofnijPlikDoKierownikaButton.addActionListener ( e -> {
            try
            {
                String tempNazwaPliku = list1.getSelectedValue().substring(list1.getSelectedValue().lastIndexOf("/") + 1).trim();
                Main.moveFile ( list1.getSelectedValue(),"/kierownik/" + tempNazwaPliku );
                Main.infoBox ( "Przenoszenie zakończone powodzeniem!", "Sukces!" );
                list1.clearSelection ();
            }
            catch ( DbxException dbxException )
            {
                dbxException.printStackTrace ( );
            }
        } );
        odswiezListeFolderArchiwumButton.addActionListener ( e -> {
            try
            {
                refreshKadry=false;
                refresherPlikowArchiwum ();
            }
            catch ( DbxException dbxException )
            {
                dbxException.printStackTrace ( );
            }
        } );
        wylogujButton.addActionListener ( e -> {
            Main.frameKadry.setVisible ( false );
            Main.zalogowanyPomyslnie=0;
            Main.frameLogowanie.setVisible ( true );
            list1.clearSelection ();
        } );
        wyswietlDokumentButton.addActionListener ( e -> {
            try
            {
                DocumentPreview.numerStrony=0;
                Main.generateFilePreview (list1.getSelectedValue());
                DocumentPreview.displayDocument();
                list1.clearSelection ();
            }
            catch ( DbxException | IllegalArgumentException dbxException )
            {
                Main.infoBox ( "Wystąpił błąd, prawdopodobnie została podana nieprawidłowa ścieżka do pliku" +
                                       " lub został wybrany nieobsługiwany format pliku.\nDozwolone formaty to: .ai, .doc," +
                                       " .docm, .docx, .eps, .gdoc, .gslides, .odp, .odt, .pps, .ppsm, .ppsx, .ppt, .pptm, .pptx," +
                                       " .rtf.","Error" );
            }
        } );
    }

    public JPanel getPanel() {
        return panelKadry;
    }
}
