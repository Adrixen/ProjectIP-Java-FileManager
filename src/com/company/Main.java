package com.company;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.util.*;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;
import com.dropbox.core.v2.files.*;

public class Main
{
    static int zalogowanyPomyslnie=0;
    static int permission=0;
    private JPanel         panelMain;
    static private JFrame frameLogowanie;
    private JButton        zalogujSieButton;
    private JPasswordField passwordField1;

    static private JPanel panelP;
    static private JFrame frameP;

    static DbxClientV2 client;

    public Main ( )
    {
        zalogujSieButton.addActionListener ( e -> {
            try
            {
                logowanie( passwordField1.getPassword ( ) );
            }
            catch ( IOException ioException )
            {
                ioException.printStackTrace ( );
            }
        } );
    }

    static void logowanie(char[] password) throws IOException
    {
        URL url = new URL("https://gist.githubusercontent.com/Adrixen/95e1a2a6bfad61933d5de5b3190e4f32/raw/55e31b1b75e4d7a80622476e3a176eeadf407571");
        BufferedReader in = new BufferedReader( new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
        {
            //System.out.println ( inputLine );//Wypisywanie zawartosci pliku z haslami
            if( inputLine.equals ( new String(password) ) )
            {
                zalogowanyPomyslnie=1;
                System.out.println ( "Zalogowany jako " + inputLine + "!" );
                if(inputLine.startsWith("p"))
                {
                    permission=1;
                    frameP.setVisible(true);
                }
                else if(inputLine.startsWith("ki"))
                {
                    permission=2;
                }
                else if(inputLine.startsWith("ka"))
                {
                    permission=3;
                }
                System.out.println ( "Twoj poziom uprawnien to: " + permission );
                frameLogowanie.setVisible(false);
                break;
            }
        }
        if(zalogowanyPomyslnie==0)
        {
            infoBox ( "Złe hasło! Spróbuj ponownie..", "Brak dostępu!" );
            System.out.println ( "Brak dostepu!");
        }
        in.close();
    }

    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    static void displayGUIComponents()
    {
        panelP = new Form1().getPanel();

        frameLogowanie = new JFrame("Zarzadzanie Dokumentami");
        frameLogowanie.setContentPane ( new Main().panelMain );
        frameLogowanie.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameLogowanie.pack();
        frameLogowanie.setVisible(true);

        frameP = new JFrame("Zarzadzanie Dokumentami");
        frameP.setContentPane ( panelP );
        frameP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameP.pack();
    }

    static void setUpDropboxConnection()
    {
        final String ACCESS_TOKEN = "sl.BApK2mFVfGPXept36ZKij9N_C2izEv9TBlwfX79a58NyzRwitY5Af5KOgbPHgFM4bPtUXmxiI8Uz89LzMvCPZrC4ej6EF-XBWiizAIlGCtdBtcaQ3R3MtbSsBgTuovUDUzPMec8";
        DbxRequestConfig config = DbxRequestConfig.newBuilder("zarzadzanieDokumentami/0.1").build();
        client  = new DbxClientV2(config, ACCESS_TOKEN);
        //FullAccount account = client.users().getCurrentAccount(); // sprawdzanie do jakiego konta dropbox jest przypisany token
        //System.out.println(account.getName().getDisplayName()); // sprawdzanie do jakiego konta dropbox jest przypisany token
    }

    static void uploadFile()
    {
        try {
            InputStream in = new FileInputStream ("C:/Users/kowal/Desktop/testerjavaIP/pliktestowy.txt");
            client.files().uploadBuilder("/pliczektestowy.txt").uploadAndFinish(in);
            System.out.println ( "file uploaded" );
        }
        catch ( IOException | DbxException e )
        {
            e.printStackTrace ( );
        }
    }

    static void listFiles() throws DbxException
    {
        ListFolderResult result = client.files().listFolder("");
        while (true)
        {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }
    }

    public static void main(String[] args)
    {
        System.out.println ( "Twoj poziom uprawnien to: " + permission );
        displayGUIComponents();
        setUpDropboxConnection();

    }
}