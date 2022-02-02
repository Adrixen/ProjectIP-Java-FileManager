package com.company;

import com.dropbox.core.DbxException;

import javax.swing.*;
import java.io.File;


/**
 *  Klasa która jest GUI dla kierownika
 *  W danej klasie zaimplementowane GUI.
 */
public
class Kierownik
{
    private JPanel  panelKierownik;
    private JButton chooserButton;
    private JButton wyslijPlikButton;
    private JTextField textField1;
    private JButton wyslijPlikDoZatwierdzeniaButton;
    private JButton    odswiezListePlikowButton;
    private JButton    pobierzWybranyPlikButton;
    private JButton usunPlikButton;
    private JButton cofnijPlikDoPoprawyButton;
    private JButton wyswietlDokumentButton;
    private JButton wylogujButton;
    private JList<String> list1;

    public Kierownik( )
    {
        chooserButton.addActionListener ( e -> Main.chooseFile () );
        wyslijPlikButton.addActionListener ( e -> {
            if(!textField1.getText().isEmpty ())
            {
                Main.uploadFile ( Main.pathPliku , "/kierownik/kierownik_" + textField1.getText ( ) + "." + Main.tempFileExtension );
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
                    Main.moveFile ( list1.getSelectedValue(),"/kadry/" + tempNazwaPliku );
                    Main.infoBox ( "Przenoszenie zakończone powodzeniem!", "Sukces!" );
                }
                catch ( DbxException dbxException )
                {
                    dbxException.printStackTrace ( );
                }
            }
        } );
        odswiezListePlikowButton.addActionListener ( e -> {
            try
            {
                Main.listFiles ( "/kierownik/" );
                DefaultListModel<String> list  = new DefaultListModel<> ();
                String[] items = Main.listaPlikowP.split("\n");

                for(String item : items)
                {
                    list.addElement(item);
                }
                list1.setModel(list);
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
        } );
        usunPlikButton.addActionListener ( e -> {
            try
            {
                Main.deleteFile (list1.getSelectedValue());
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
            }
            catch ( DbxException dbxException )
            {
                dbxException.printStackTrace ( );
            }
        } );
        wylogujButton.addActionListener ( e -> {
            Main.frameKierownik.setVisible ( false );
            Main.zalogowanyPomyslnie=0;
            Main.frameLogowanie.setVisible ( true );
        } );
        wyswietlDokumentButton.addActionListener ( e -> {
            try
            {
                DocumentPreview.numerStrony=0;
                Main.generateFilePreview ( list1.getSelectedValue() );
                DocumentPreview.displayDocument();
            }
            catch ( DbxException | IllegalArgumentException dbxException )
            {
                Main.infoBox ( "Wystąpił błąd, prawdopodobnie została podana nieprawidłowa ścieżka do pliku lub został wybrany nieobsługiwany format pliku.\nDozwolone formaty to: .ai, .doc, .docm, .docx, .eps, .gdoc, .gslides, .odp, .odt, .pps, .ppsm, .ppsx, .ppt, .pptm, .pptx, .rtf.","Error" );
            }
        } );
    }

    public JPanel getPanel() {
        return panelKierownik;
    }
}
