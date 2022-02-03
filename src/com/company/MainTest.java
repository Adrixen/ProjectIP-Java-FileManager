package com.company;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class MainTest
{

    @Test
    @DisplayName("Logowanie")
    void logowanie ( ) throws IOException
    {
        URL    url = new URL( "https://gist.githubusercontent.com/Adrixen/95e1a2a6bfad61933d5de5b3190e4f32/raw/55e31b1b75e4d7a80622476e3a176eeadf407571");
        BufferedReader in  = new BufferedReader( new InputStreamReader ( url.openStream()));
        String         inputLine;
        int zalogowanyPomyslnie = 0;
        int permission=0;
        String password = "kierownik";
        while ((inputLine = in.readLine()) != null)
        {
            if( inputLine.equals ( password ) )
            {
                zalogowanyPomyslnie=1;

                permission=2;
                Assertions.assertEquals ( 2 , permission );
                break;
            }
        }
        if(zalogowanyPomyslnie==0)
        {
            Assertions.assertEquals ( 0 , permission );
        }
        in.close();
    }



    @Test
    void setUpServerConnection ( )
    {
        try
        {
            final String     ACCESS_TOKEN = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
            DbxRequestConfig config       = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
            DbxClientV2 client = new DbxClientV2 ( config , ACCESS_TOKEN );
        }
        catch ( Exception e )
        {
            Assertions.fail("Exception " + e);
        }
    }


    @Test
    void uploadFile ( )
    {
        String pathSource="Potrzebne.txt";
        String pathDestination="/tests/Test.txt";
        try
        {
            final String     ACCESS_TOKEN = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
            DbxRequestConfig config       = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
            DbxClientV2 client = new DbxClientV2 ( config , ACCESS_TOKEN );
            InputStream in = new FileInputStream ( pathSource);
            client.files().uploadBuilder(pathDestination).withMode(WriteMode.OVERWRITE).uploadAndFinish( in);

        }
        catch ( IOException | DbxException e )
        {
            Assertions.fail("Exception " + e);
        }
    }

    @Test
    void listFilesForPracownik ( ) throws DbxException
    {
        final String     ACCESS_TOKEN = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
        DbxRequestConfig config       = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
        DbxClientV2      client      = new DbxClientV2 ( config , ACCESS_TOKEN );
        List<String>     listaPlikow = new ArrayList<> ();
        ListFolderResult result      = client.files().listFolder( "/pracownik/");
        String idPracownika = "a1234";
        while (true)
        {
            for ( Metadata metadata : result.getEntries())
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
    }

    @Test
    void listFiles ( ) throws DbxException
    {
        final String     ACCESS_TOKEN = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
        DbxRequestConfig config       = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
        DbxClientV2      client      = new DbxClientV2 ( config , ACCESS_TOKEN );
        List<String> listaPlikow = new ArrayList<> ( );
        String displayPath="/kierownik/";
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
    }

    @Test
    void deleteFile ( )
    {
        try
        {
            String           pathSource      = "Potrzebne.txt";
            String           pathDestination = "/tests/Test.txt";
            final String     ACCESS_TOKEN    = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
            DbxRequestConfig config          = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
            DbxClientV2      client          = new DbxClientV2 ( config , ACCESS_TOKEN );
            InputStream      in              = new FileInputStream ( pathSource );
            client.files().uploadBuilder(pathDestination).withMode(WriteMode.OVERWRITE).uploadAndFinish( in);
            String pathDeleteDestination = "/tests/Test.txt";
            client.files ( ).deleteV2 ( pathDeleteDestination );
        }
        catch(IOException | DbxException e )
        {
            Assertions.fail("Exception " + e);
        }
    }

    @Test
    void chooseFile ( )
    {
        String tempFileName = "Test.txt";
        String tempFileExtension = tempFileName.substring(tempFileName.lastIndexOf(".") + 1).trim();
        Assertions.assertEquals ( "txt" , tempFileExtension );
    }



    @Test
    void downloadFile ( ) throws IOException, DbxException
    {
        String           pathSource      = "Potrzebne.txt";
        String           pathDestination = "/tests/Test.txt";
        final String     ACCESS_TOKEN    = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
        DbxRequestConfig config          = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
        DbxClientV2      client          = new DbxClientV2 ( config , ACCESS_TOKEN );
        InputStream in = new FileInputStream ( pathSource);
        client.files().uploadBuilder(pathDestination).withMode(WriteMode.OVERWRITE).uploadAndFinish( in);
        String destinationPath="Potrzebne.txt";
        String sourcePath="/tests/Test.txt";
        try (OutputStream downloadFile = new FileOutputStream ( destinationPath ))
        {
            client.files().downloadBuilder ( sourcePath ).download ( downloadFile );
        }
        catch (DbxException | IOException e)
        {
            Assertions.fail("Exception " + e);
        }
    }

    @Test
    void moveFile ( ) throws IOException, DbxException
    {
        final String     ACCESS_TOKEN    = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
        DbxRequestConfig config          = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
        DbxClientV2      client          = new DbxClientV2 ( config , ACCESS_TOKEN );
        String           sourcePath      = "/tests/Test.txt";
        String           destinationPath = "/tests/Test1.txt"; //zmieniac liczbe co uruchomienie testu
        String pathSource="Potrzebne.txt";
        String pathDestination="/tests/Test.txt";
        InputStream in = new FileInputStream ( pathSource);
        client.files().uploadBuilder(pathDestination).withMode(WriteMode.OVERWRITE).uploadAndFinish( in);
        try
        {
            client.files ( ).moveV2 ( sourcePath , destinationPath );
        }
        catch( DbxException e)
        {
            Assertions.fail("Exception " + e);
        }
        String pathDeleteDestination = "/tests/Test1.txt";
        client.files ( ).deleteV2 ( pathDeleteDestination );
    }
    @Test
    void generatePreviewFile() throws DbxException, IOException
    {
        final String     ACCESS_TOKEN    = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
        DbxRequestConfig config          = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
        DbxClientV2      client          = new DbxClientV2 ( config , ACCESS_TOKEN );
        String sourcePath="/tests/Test.docx";
        String pathSource="Potrzebne1.docx";
        String pathDestination="/tests/Test.docx";
        InputStream in1 = new FileInputStream ( pathSource);
        client.files().uploadBuilder(pathDestination).withMode(WriteMode.OVERWRITE).uploadAndFinish( in1);
        DbxDownloader<FileMetadata> test = client.files().getPreview ( sourcePath);
        try (InputStream in = test.getInputStream(); OutputStream out = Files.newOutputStream( Paths.get( "outputTest.pdf")))
        {
            long length = in.transferTo(out);
            System.out.println("Bytes transferred: " + length);
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    @Test
    void generateNote() throws IOException
    {
        final String     ACCESS_TOKEN    = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
        DbxRequestConfig config          = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
        DbxClientV2      client          = new DbxClientV2 ( config , ACCESS_TOKEN );
        String noteTemp="test";
        String note="test1";
        String noteDestination="/notatki/Potrzebne2.txt";
        try (OutputStream downloadFile = new FileOutputStream ( "Potrzebne2.txt" ))
        {
            client.files ( ).downloadBuilder ( noteDestination ).download ( downloadFile ); //pobiera
        }
        catch ( IOException | DbxException e )
        {
            System.out.println ( "Nie ma jeszcze notatki do tego pliku.. Generowanie.." );
        }
        File file = new File("Potrzebne2.txt");
        try (InputStream in = new FileInputStream(file))
        {
            noteTemp = IOUtils.toString( in, StandardCharsets.UTF_8); //zawartosc do stringa
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try (PrintStream out = new PrintStream(new FileOutputStream("Potrzebne2.txt")))
        {
            out.print(noteTemp+note);
        }
        try
        {
            InputStream in = new FileInputStream ("Potrzebne2.txt");
            client.files().uploadBuilder(noteDestination).withMode ( WriteMode.OVERWRITE ).uploadAndFinish( in);
        }
        catch ( IOException | DbxException e )
        {
            e.printStackTrace ( );
        }
    }
    @Test
    void readNote()
    {
        final String     ACCESS_TOKEN    = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
        DbxRequestConfig config          = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
        DbxClientV2      client          = new DbxClientV2 ( config , ACCESS_TOKEN );
        String note="test1";
        String noteDestination="/notatki/Potrzebne2.txt";
        try (OutputStream downloadFile = new FileOutputStream ( "notatka.txt" ))
        {
            client.files ( ).downloadBuilder ( noteDestination ).download ( downloadFile ); //pobiera
        }
        catch ( IOException | DbxException e )
        {
            Main.infoBox ( "Odczytywanie nie powiodło się! Nazwa pliku jest niepoprawna albo plik nie zawiera notatki!" , "Error" );
        }
        File file = new File("notatka.txt");
        try (InputStream in = new FileInputStream(file))
        {
            note = IOUtils.toString( in, StandardCharsets.UTF_8); //zawartosc do stringa
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Test
    void refresherPlikow ( ) throws DbxException
    {


        final String     ACCESS_TOKEN = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
        DbxRequestConfig config       = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
        DbxClientV2      client      = new DbxClientV2 ( config , ACCESS_TOKEN );
        List<String>     listaPlikow = new ArrayList<> ();
        ListFolderResult result      = client.files().listFolder( "/pracownik/");
        String idPracownika = "a1234";
        String listaPlikowP;
        while (true)
        {
            for ( Metadata metadata : result.getEntries())
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

        DefaultListModel<String> listYYY = new DefaultListModel<> ( );
        String[]                 items = listaPlikowP.split ( "\n" );
        for ( String item : items )
        {
            listYYY.addElement ( item );
        }
    }
}