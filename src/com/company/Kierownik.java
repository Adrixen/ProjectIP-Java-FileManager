package com.company;

import com.dropbox.core.DbxException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;


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
    private JButton    pobierzWybranyPlikButton;
    private JButton usunPlikButton;
    private JButton cofnijPlikDoPoprawyButton;
    private JButton wyswietlDokumentButton;
    private JButton wylogujButton;
    private JList<String> list1;
    private JTextArea notatkaTextArea;
    private JButton   odczytajNotatkePlikuButton;

    void refresherPlikow() throws DbxException
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

    public Kierownik( )
    {
        Runnable runnable =
                ( ) -> {
                    while ( true )
                    {
                        try
                        {
                            if ( Main.permission == 2 && list1.isSelectionEmpty () )
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
        Thread threadKierownikRefresher = new Thread ( runnable );
        threadKierownikRefresher.start ( );
        chooserButton.addActionListener ( e -> {Main.chooseFile ( );list1.clearSelection (); });
        wyslijPlikButton.addActionListener ( e -> {
            if(!textField1.getText().isEmpty ())
            {
                Main.uploadFile ( Main.pathPliku , "/kierownik/kierownik_" + textField1.getText ( ) + "." + Main.tempFileExtension );
                list1.clearSelection ();
            }
            else
            {
                Main.infoBox ( "Podaj nazwe pliku!","Error" );
            }
        } );
        wyslijPlikDoZatwierdzeniaButton.addActionListener ( e -> {
            if(list1.getSelectedValue ()!=null)
            {
                String tempNazwaPliku = list1.getSelectedValue ( ).substring ( list1.getSelectedValue ( ).lastIndexOf ( "/" ) + 1 ).trim ( );
                try
                {   Main.note = "Data: "+ LocalDateTime.now().withNano( 0).withSecond ( 0 )+"\nTyp: Przesłany do zatwierdzenia\n"+"Wiadomość od "+Main.idPracownika+":\n"+notatkaTextArea.getText()+"\n";
                    Main.moveFile ( list1.getSelectedValue ( ) , "/kadry/" + tempNazwaPliku );
                    String temp=tempNazwaPliku.substring(0, tempNazwaPliku.lastIndexOf('.'));
                    Main.generateNote ( "/notatki/"+"notatka"+temp+".txt" );
                    list1.clearSelection ();
                    Main.infoBox ( "Przenoszenie zakończone powodzeniem!" , "Sukces!" );
                }
                catch ( DbxException | IOException dbxException )
                {
                    System.out.println ( dbxException );
                    Main.infoBox ( "Taka nazwa pliku już istnieje!" , "Error" );
                }
            }
            else
            {
                Main.infoBox ( "Brak wybranego pliku!" , "Error" );
            }
        } );
        pobierzWybranyPlikButton.addActionListener ( e -> {
            if(list1.getSelectedValue ()==null)
            {
                Main.infoBox ( "Brak wybranego pliku!" , "Error" );
            }
            else
            {
                Main.saveFile ( );
                String tempNazwaPliku = list1.getSelectedValue ( ).substring ( list1.getSelectedValue ( ).lastIndexOf ( "/" ) + 1 ).trim ( );
                Main.downloadFile ( list1.getSelectedValue ( ) , new File ( Main.pathFolderu + "/" + tempNazwaPliku ) );
                list1.clearSelection ( );
            }
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
            if(list1.getSelectedValue ()==null)
            {
                Main.infoBox ( "Brak wybranego pliku!" , "Error" );
            }
            else
            {
                String tempNazwaPliku = list1.getSelectedValue ( ).substring ( list1.getSelectedValue ( ).lastIndexOf ( "/" ) + 1 ).trim ( );
                try
                {
                    Main.note = "Data: " + LocalDateTime.now ( ).withNano ( 0 ).withSecond ( 0 ) + "\nTyp: Przesłany do poprawy\n" + "Wiadomość od " + Main.idPracownika + ":\n" + notatkaTextArea.getText ( ) + "\n";
                    Main.moveFile ( list1.getSelectedValue ( ) , "/pracownik/" + tempNazwaPliku );
                    String temp = tempNazwaPliku.substring ( 0 , tempNazwaPliku.lastIndexOf ( '.' ) );
                    Main.generateNote ( "/notatki/" + "notatka" + temp + ".txt" );
                    list1.clearSelection ( );
                    Main.infoBox ( "Przenoszenie zakończone powodzeniem!" , "Sukces!" );
                }
                catch ( DbxException | IOException dbxException )
                {
                    System.out.println ( dbxException );
                    Main.infoBox ( "Taka nazwa pliku już istnieje!" , "Error" );
                }
            }
        } );
        wylogujButton.addActionListener ( e -> {
            Main.frameKierownik.setVisible ( false );
            Main.zalogowanyPomyslnie=0;
            Main.frameLogowanie.setVisible ( true );
            list1.clearSelection ();
        } );
        wyswietlDokumentButton.addActionListener ( e -> {
            try
            {
                DocumentPreview.numerStrony=0;
                Main.generateFilePreview ( list1.getSelectedValue() );
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
        odczytajNotatkePlikuButton.addActionListener ( e -> {
            if(list1.getSelectedValue ()==null)
            {
                Main.infoBox ( "Brak wybranego pliku!" , "Error" );
            }
            else
            {
                String tempNazwaPliku = list1.getSelectedValue ( ).substring ( list1.getSelectedValue ( ).lastIndexOf ( "/" ) + 1 ).trim ( );
                String temp           = tempNazwaPliku.substring ( 0 , tempNazwaPliku.lastIndexOf ( '.' ) );
                Main.readNote ( "/notatki/" + "notatka" + temp + ".txt" );
                notatkaTextArea.setText ( Main.note );
                list1.clearSelection ( );
            }
        } );
    }
    /**
     * metoda zwracająca panel kierownika
     */
    public JPanel getPanel() {
        return panelKierownik;
    }
}
