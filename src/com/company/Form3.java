package com.company;

import com.dropbox.core.DbxException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public
class Form3
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

    public
    Form3 ( )
    {
        chooserButton.addActionListener ( e -> Main.chooseFile () );
        wyslijPlikButton.addActionListener ( e -> {
            if(!textField1.getText().isEmpty ())
            {
                Main.uploadFile ( Main.pathPliku , "/kadry/kadry_" + textField1.getText ( ) + ".txt" );
            }
            else
            {
                Main.infoBox ( "Podaj nazwe pliku!","Error" );
            }
        } );
        wyslijPlikDoZatwierdzeniaButton.addActionListener ( e -> {
            if(!textField1.getText().isEmpty ())
            {
                Main.uploadFile ( Main.pathPliku , "/archiwum/kadry_" + textField1.getText ( ) + ".txt" );
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
        pobierzWybranyPlikButton.addActionListener ( new ActionListener ( )
        {
            @Override
            public
            void actionPerformed ( ActionEvent e )
            {
                Main.saveFile ();
                String tempNazwaPliku = nazwaPlikuDoPobraniaTextField.getText ( ).substring(nazwaPlikuDoPobraniaTextField.getText ( ).lastIndexOf("/") + 1).trim();
                Main.downloadFile ( nazwaPlikuDoPobraniaTextField.getText ( ), new File ( Main.pathFolderu + "/" + tempNazwaPliku ) );
            }
        } );
        usunPlikButton.addActionListener ( new ActionListener ( )
        {
            @Override
            public
            void actionPerformed ( ActionEvent e )
            {
                try
                {
                    Main.deleteFile (nazwaPlikuDoPobraniaTextField.getText ( ));
                }
                catch ( DbxException | IllegalArgumentException dbxException )
                {
                    Main.infoBox ( "Usuwanie nie powiodło się! Czy nazwa pliku jest poprawna?", "Error" );
                }
            }
        } );
        cofnijPlikDoPoprawyButton.addActionListener ( new ActionListener ( )
        {
            @Override
            public
            void actionPerformed ( ActionEvent e )
            {
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
            }
        } );
        cofnijPlikDoKierownikaButton.addActionListener ( new ActionListener ( )
        {
            @Override
            public
            void actionPerformed ( ActionEvent e )
            {
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
            }
        } );
        odswiezListeFolderArchiwumButton.addActionListener ( new ActionListener ( )
        {
            @Override
            public
            void actionPerformed ( ActionEvent e )
            {
                try
                {
                    Main.listFiles ( "/archiwum/" );
                    textArea1.setText(Main.listaPlikowP);
                }
                catch ( DbxException dbxException )
                {
                    dbxException.printStackTrace ( );
                }
            }
        } );
    }

    public JPanel getPanel() {
        return panelKadry;
    }
}
