package com.company;

import com.dropbox.core.DbxException;
import javax.swing.*;
import java.io.File;

public
class Kierownik
{
    private JPanel  panelKierownik;
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

    public Kierownik( )
    {
        chooserButton.addActionListener ( e -> Main.chooseFile () );
        wyslijPlikButton.addActionListener ( e -> {
            if(!textField1.getText().isEmpty ())
            {
                Main.uploadFile ( Main.pathPliku , "/kierownik/kierownik_" + textField1.getText ( ) + ".txt" );
            }
            else
            {
                Main.infoBox ( "Podaj nazwe pliku!","Error" );
            }
        } );
        wyslijPlikDoZatwierdzeniaButton.addActionListener ( e -> {
            if(!textField1.getText().isEmpty ())
            {
                Main.uploadFile ( Main.pathPliku , "/kadry/kierownik_" + textField1.getText ( ) + ".txt" );
            }
            else
            {
                Main.infoBox ( "Podaj nazwe pliku!","Error" );
            }
        } );
        odswiezListePlikowButton.addActionListener ( e -> {
            try
            {
                Main.listFiles ( "/kierownik/" );
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
    }

    public JPanel getPanel() {
        return panelKierownik;
    }
}
