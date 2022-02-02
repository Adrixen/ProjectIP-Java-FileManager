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
    private JTextArea textArea1;
    private JButton    pobierzWybranyPlikButton;
    private JTextField nazwaPlikuDoPobraniaTextField;
    private JButton usunPlikButton;
    private JButton cofnijPlikDoPoprawyButton;
    private JButton odswiezListeFolderArchiwumButton;
    private JButton cofnijPlikDoKierownikaButton;
    private JButton wyswietlDokumentButton;
    private JButton wylogujButton;

    public Kadry( )
    {
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
                    String tempNazwaPliku = nazwaPlikuDoPobraniaTextField.getText ( ).substring(nazwaPlikuDoPobraniaTextField.getText ( ).lastIndexOf("/") + 1).trim();
                    Main.moveFile ( nazwaPlikuDoPobraniaTextField.getText ( ),"/archiwum/" + tempNazwaPliku );
                    Main.infoBox ( "Przenoszenie zakończone powodzeniem!", "Sukces!" );
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
                Main.listFiles ( "/kadry/" );
                textArea1.setText(Main.listaPlikowP);
            }
            catch ( DbxException dbxException )
            {
                dbxException.printStackTrace ( );
            }
        } );
        pobierzWybranyPlikButton.addActionListener ( e -> {
            Main.saveFile ();
            String tempNazwaPliku = nazwaPlikuDoPobraniaTextField.getText ( ).substring(nazwaPlikuDoPobraniaTextField.getText ( ).lastIndexOf("/") + 1).trim();
            Main.downloadFile ( nazwaPlikuDoPobraniaTextField.getText ( ), new File ( Main.pathFolderu + "/" + tempNazwaPliku ) );
        } );
        usunPlikButton.addActionListener ( e -> {
            try
            {
                Main.deleteFile (nazwaPlikuDoPobraniaTextField.getText ( ));
            }
            catch ( DbxException | IllegalArgumentException dbxException )
            {
                Main.infoBox ( "Usuwanie nie powiodło się! Czy nazwa pliku jest poprawna?", "Error" );
            }
        } );
        cofnijPlikDoPoprawyButton.addActionListener ( e -> {
            try
            {
                String tempNazwaPliku = nazwaPlikuDoPobraniaTextField.getText ( ).substring(nazwaPlikuDoPobraniaTextField.getText ( ).lastIndexOf("/") + 1).trim();
                Main.moveFile ( nazwaPlikuDoPobraniaTextField.getText ( ),"/pracownik/" + tempNazwaPliku );
                Main.infoBox ( "Przenoszenie zakończone powodzeniem!", "Sukces!" );
            }
            catch ( DbxException dbxException )
            {
                dbxException.printStackTrace ( );
            }
        } );
        cofnijPlikDoKierownikaButton.addActionListener ( e -> {
            try
            {
                String tempNazwaPliku = nazwaPlikuDoPobraniaTextField.getText ( ).substring(nazwaPlikuDoPobraniaTextField.getText ( ).lastIndexOf("/") + 1).trim();
                Main.moveFile ( nazwaPlikuDoPobraniaTextField.getText ( ),"/kierownik/" + tempNazwaPliku );
                Main.infoBox ( "Przenoszenie zakończone powodzeniem!", "Sukces!" );
            }
            catch ( DbxException dbxException )
            {
                dbxException.printStackTrace ( );
            }
        } );
        odswiezListeFolderArchiwumButton.addActionListener ( e -> {
            try
            {
                Main.listFiles ( "/archiwum/" );
                textArea1.setText(Main.listaPlikowP);
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
        } );
        wyswietlDokumentButton.addActionListener ( e -> {
            try
            {
                DocumentPreview.numerStrony=0;
                Main.generateFilePreview ( nazwaPlikuDoPobraniaTextField.getText (  ) );
                DocumentPreview.displayDocument();
            }
            catch ( DbxException | IllegalArgumentException dbxException )
            {
                Main.infoBox ( "Wystąpił błąd, prawdopodobnie została podana nieprawidłowa ścieżka do pliku lub został wybrany nieobsługiwany format pliku.\nDozwolone formaty to: .ai, .doc, .docm, .docx, .eps, .gdoc, .gslides, .odp, .odt, .pps, .ppsm, .ppsx, .ppt, .pptm, .pptx, .rtf.","Error" );
            }
        } );
    }

    public JPanel getPanel() {
        return panelKadry;
    }
}
