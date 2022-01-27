package com.company;

import java.net.*;
import java.io.*;
import javax.swing.*;
import java.util.*;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Window;

public class Main
{
    static DbxClientV2 client;

    static int zalogowanyPomyslnie=0;
    static int permission=0;

    private JPanel panelMain;
    static private JFrame frameLogowanie;
    static private JFrame framePracownik;
    static private JFrame frameKierownik;
    static private JFrame frameKadry;

    private JButton zalogujSieButton;
    private JPasswordField passwordField;

    static String listaPlikowP;
    static File pathFolderu;
    static File pathPliku;
    static String tempFileName;
    static String tempFileExtension;
    static String idPracownika;

    public Main ( )
    {
        zalogujSieButton.addActionListener ( e -> {
            try
            {
                logowanie( passwordField.getPassword ( ) );
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
            if( inputLine.equals ( new String(password) ) )
            {
                zalogowanyPomyslnie=1;
                System.out.println ( "Zalogowany jako " + inputLine + "!" );
                idPracownika = inputLine.substring(inputLine.lastIndexOf("_") + 1).trim();

                if(inputLine.startsWith("p"))
                {
                    permission=1;
                    framePracownik.setVisible(true);
                }
                else if(inputLine.startsWith("ki"))
                {
                    permission=2;
                    frameKierownik.setVisible(true);
                }
                else if(inputLine.startsWith("ka"))
                {
                    permission=3;
                    frameKadry.setVisible(true);
                }
                System.out.println ( "Twoj poziom uprawnien to: " + permission );
                frameLogowanie.setVisible(false);
                break;
            }
        }
        if(zalogowanyPomyslnie==0)
        {
            infoBox ( "Złe hasło! Spróbuj ponownie..", "Brak dostępu!" );
        }
        in.close();
    }

    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    static void displayGUIComponents()
    {
        JPanel panelPracownik  = new Pracownik( ).getPanel ( );
        JPanel panelKierownik  = new Kierownik( ).getPanel ( );
        JPanel panelKadry = new Kadry( ).getPanel ( );

        frameLogowanie = new JFrame("Zarzadzanie Dokumentami");
        frameLogowanie.setContentPane ( new Main().panelMain );
        frameLogowanie.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameLogowanie.pack();
        frameLogowanie.setVisible(true);

        framePracownik = new JFrame("Zarzadzanie Dokumentami - Pracownik");
        framePracownik.setContentPane ( panelPracownik );
        framePracownik.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framePracownik.pack();

        frameKierownik = new JFrame("Zarzadzanie Dokumentami - Kierownik");
        frameKierownik.setContentPane ( panelKierownik );
        frameKierownik.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameKierownik.pack();

        frameKadry = new JFrame("Zarzadzanie Dokumentami - Kadry");
        frameKadry.setContentPane ( panelKadry );
        frameKadry.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameKadry.pack();

        centreWindow ( frameLogowanie );
        centreWindow ( framePracownik );
        centreWindow ( frameKierownik );
        centreWindow ( frameKadry );
    }

    static void setUpServerConnection()
    {
        final String ACCESS_TOKEN = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
        DbxRequestConfig config = DbxRequestConfig.newBuilder("zarzadzanieDokumentami/0.1").build();
        client  = new DbxClientV2(config, ACCESS_TOKEN);
    }

    static void uploadFile(File pathSource, String pathDestination)
    {
        try
        {
            InputStream in = new FileInputStream (pathSource);
            client.files().uploadBuilder(pathDestination).uploadAndFinish(in);
            infoBox ( "Wysyłanie zakończone powodzeniem!", "Sukces!" );
        }
        catch ( IOException | DbxException e )
        {
            e.printStackTrace ( );
        }
    }

    static void listFilesForPracownik() throws DbxException
    {
        List<String> listaPlikow = new ArrayList<>();
        ListFolderResult result = client.files().listFolder("/pracownik/");

        while (true)
        {
            for (Metadata metadata : result.getEntries())
            {
                if(metadata.getPathLower().contains(idPracownika))
                {
                    listaPlikow.add (metadata.getPathLower());
                }
            }

            if (!result.getHasMore())
            {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }

        StringBuilder sb = new StringBuilder();

        for (String s : listaPlikow)
        {
                sb.append ( s );
                sb.append ( "\n" );
        }
        listaPlikowP = sb.toString();
    }

    static void listFiles(String displayPath) throws DbxException
    {
        List<String> listaPlikow = new ArrayList<> ( );
        ListFolderResult result = client.files().listFolder(displayPath);

        while (true)
        {
            for (Metadata metadata : result.getEntries())
            {
                listaPlikow.add ( metadata.getPathLower ( ) );
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }

        StringBuilder sb = new StringBuilder();

        for (String s : listaPlikow)
        {
            sb.append ( s );
            sb.append ( "\n" );
        }
        listaPlikowP = sb.toString();
    }

    static void deleteFile(String pathDeleteDestination) throws DbxException
    {
            client.files().deleteV2(pathDeleteDestination);
            infoBox ( "Usuwanie zakończone powodzeniem!", "Sukces!" );
    }

    static void chooseFile()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Wybierz dokument");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setAcceptAllFileFilterUsed(true);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            pathPliku=chooser.getSelectedFile();
            tempFileName = chooser.getSelectedFile().getName();
            tempFileExtension = tempFileName.substring(tempFileName.lastIndexOf(".") + 1).trim();
        }
        else
        {
            System.out.println("Brak wyboru");
        }
    }

    static void saveFile()
    {
        JFileChooser saver = new JFileChooser();
        saver.setCurrentDirectory(new java.io.File("."));
        saver.setDialogTitle("Wybierz katalog");
        saver.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        saver.setAcceptAllFileFilterUsed(true);

        if (saver.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            pathFolderu=saver.getSelectedFile();
        }
        else
        {
            System.out.println("Brak wyboru");
        }
    }

    static void centreWindow(Window frame)
    {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    static void downloadFile(String sourcePath, File destinationPath)
    {
        try (OutputStream downloadFile = new FileOutputStream ( destinationPath ))
        {
            client.files().downloadBuilder ( sourcePath ).download ( downloadFile );
        }
        catch (DbxException | IOException e)
        {
            JOptionPane.showMessageDialog(null, "Nie mozna pobrac pliku! \n Blad: " + e);
        }
    }
    
    static void moveFile(String sourcePath, String destinationPath) throws DbxException
    {
        client.files().moveV2(sourcePath, destinationPath);
    }

    public static void main(String[] args)
    {
        displayGUIComponents();
        setUpServerConnection();
    }
}