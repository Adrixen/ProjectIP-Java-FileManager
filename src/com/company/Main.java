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
import javax.swing.JFrame;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Window;

public class Main
{
    static String listaPlikowP;
    static ListFolderResult result;
    static File pathFolderu;
    static File pathPliku;
    static int  zalogowanyPomyslnie=0;
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

        frameP = new JFrame("Zarzadzanie Dokumentami - Pracownik");
        frameP.setContentPane ( panelP );
        frameP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameP.pack();
        centreWindow ( frameLogowanie );
        centreWindow ( frameP );
    }

    static void setUpDropboxConnection()
    {
        final String ACCESS_TOKEN = "sl.BApnhhFVEuNPPdmMs3O9zr354GYGb2nj3wimO7CxoHvHb2yB6cdBpp5TcXB_LXATytTsmeGg4sYbhsSaAEehGP4sKH8y-yMI-WsLUtPMMT28QlzhB1yKIhxhm_VRCchVgR_YI-Y";
        DbxRequestConfig config = DbxRequestConfig.newBuilder("zarzadzanieDokumentami/0.1").build();
        client  = new DbxClientV2(config, ACCESS_TOKEN);
        //FullAccount account = client.users().getCurrentAccount(); // sprawdzanie do jakiego konta dropbox jest przypisany token
        //System.out.println(account.getName().getDisplayName()); // sprawdzanie do jakiego konta dropbox jest przypisany token
    }

    static void uploadFile(File pathSource, String pathDestination)
    {
        try {
            InputStream in = new FileInputStream (pathSource);
            client.files().uploadBuilder(pathDestination).uploadAndFinish(in);
            System.out.println ( "file uploaded successfully" );
        }
        catch ( IOException | DbxException e )
        {
            e.printStackTrace ( );
        }
    }

    static void listFiles(String displayPath) throws DbxException
    {
        List<String> listaPlikow = new ArrayList<String>();
        result = client.files().listFolder(displayPath);
        while (true)
        {
            for (Metadata metadata : result.getEntries())
            {
                //System.out.println(metadata.getPathLower());
                listaPlikow.add(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }
        StringBuilder sb = new StringBuilder();
        for (String s : listaPlikow)
        {
            sb.append(s);
            sb.append("\n");
        }
        listaPlikowP = sb.toString();
    }

    static void deleteFile(String pathDeleteDestination) throws DbxException
    {
        DeleteResult metadata = client.files().deleteV2(pathDeleteDestination);
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
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
            pathPliku=chooser.getSelectedFile();
        }
        else
        {
            System.out.println("No Selection ");
        }
    }

    static void saveFile()
    {
        JFileChooser chooser1 = new JFileChooser();
        chooser1.setCurrentDirectory(new java.io.File("."));
        chooser1.setDialogTitle("Wybierz katalog");
        chooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser1.setAcceptAllFileFilterUsed(true);

        if (chooser1.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            System.out.println("getCurrentDirectory(): " + chooser1.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + chooser1.getSelectedFile());
            pathFolderu=chooser1.getSelectedFile();
        }
        else
        {
            System.out.println("No Selection");
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
            try
            {
                //output file for download --> storage location on local system to download file
                OutputStream downloadFile = new FileOutputStream(destinationPath);
                try
                {
                    FileMetadata metadata = client.files().downloadBuilder(sourcePath)
                            .download(downloadFile);
                }
                finally
                {
                    downloadFile.close();
                }
            }
            catch (DbxException e)
            {
                JOptionPane.showMessageDialog(null, "Unable to download file to local system\n Error: " + e);
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(null, "Unable to download file to local system\n Error: " + e);
            }
        }

    public static void main(String[] args) throws Exception
    {
        System.out.println ( "Twoj poziom uprawnien to: " + permission );
        displayGUIComponents();
        setUpDropboxConnection();
        //uploadFile ("C:/Users/kowal/Desktop/testerjavaIP/pliktestowy.txt","/pliczektestowy31.txt");
        //deleteFile ("/pliczektestowy1.txt");
    }
}