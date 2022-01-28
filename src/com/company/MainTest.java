package com.company;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.URL;
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
            if( inputLine.equals ( new String(password) ) )
            {
                zalogowanyPomyslnie=1;

                if(inputLine.startsWith("p"))
                {
                    permission=1;
                    if(password.equals ( "pracownik_a1234" ))
                    {
                        Assertions.assertTrue (permission==1);
                    }
                }
                else if(inputLine.startsWith("ki"))
                {
                    permission=2;
                    if(password.equals ( "kierownik" ))
                    {
                        Assertions.assertTrue (permission==2);
                    }
                }
                else if(inputLine.startsWith("ka"))
                {
                    permission=3;
                    if(password.equals ( "kadry" ))
                    {
                        Assertions.assertTrue (permission==3);
                    }
                }
                break;
            }
        }
        if(zalogowanyPomyslnie==0)
        {
            Assertions.assertTrue (permission==0);
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
            Assertions.assertTrue ( ACCESS_TOKEN == "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E" );
        }
        catch ( Exception e )
        {
            Assertions.fail("Exception " + e);
        }
    }


    @Test
    void uploadFile ( )
    {
        String pathSource="C:/Users/kowal/Desktop/ProjektIPJAVA/Potrzebne.txt";
        String pathDestination="/tests/Test.txt";
        try
        {
            final String     ACCESS_TOKEN = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
            DbxRequestConfig config       = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
            DbxClientV2 client = new DbxClientV2 ( config , ACCESS_TOKEN );
            InputStream in = new FileInputStream ( pathSource);
            client.files().uploadBuilder(pathDestination).uploadAndFinish(in);

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
    }

    @Test
    void listFiles ( ) throws DbxException
    {
        final String     ACCESS_TOKEN = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
        DbxRequestConfig config       = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
        DbxClientV2      client      = new DbxClientV2 ( config , ACCESS_TOKEN );
        List<String> listaPlikow = new ArrayList<> ( );
        String displayPath="/pracownik/";
        ListFolderResult result = client.files().listFolder(displayPath);
        String listaPlikowP;
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

    @Test
    void deleteFile ( ) throws DbxException, IOException
    {
        try
        {
            String           pathSource      = "C:/Users/kowal/Desktop/ProjektIPJAVA/Potrzebne.txt";
            String           pathDestination = "/tests/Test.txt";
            final String     ACCESS_TOKEN    = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
            DbxRequestConfig config          = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
            DbxClientV2      client          = new DbxClientV2 ( config , ACCESS_TOKEN );
            InputStream      in              = new FileInputStream ( pathSource );
            client.files ( ).uploadBuilder ( pathDestination ).uploadAndFinish ( in );
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
        String pathPliku="/tests/Test.txt";
        String tempFileName = "Test.txt";
        String tempFileExtension = tempFileName.substring(tempFileName.lastIndexOf(".") + 1).trim();
        Assertions.assertTrue ( tempFileExtension.equals ( "txt" ) );
    }



    @Test
    void downloadFile ( ) throws IOException, DbxException
    {
        String           pathSource      = "C:/Users/kowal/Desktop/ProjektIPJAVA/Potrzebne.txt";
        String           pathDestination = "/tests/Test.txt";
        final String     ACCESS_TOKEN    = "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E";
        DbxRequestConfig config          = DbxRequestConfig.newBuilder ( "zarzadzanieDokumentami/0.1" ).build ( );
        DbxClientV2      client          = new DbxClientV2 ( config , ACCESS_TOKEN );
        InputStream in = new FileInputStream ( pathSource);
        client.files().uploadBuilder(pathDestination).uploadAndFinish(in);
        String destinationPath="C:/Users/kowal/Desktop/ProjektIPJAVA/Potrzebne.txt";
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
        String           destinationPath = "/tests/Test3.txt"; //zmieniac liczbe co uruchomienie testu
        String pathSource="C:/Users/kowal/Desktop/ProjektIPJAVA/Potrzebne.txt";
        String pathDestination="/tests/Test.txt";
        InputStream in = new FileInputStream ( pathSource);
        client.files().uploadBuilder(pathDestination).uploadAndFinish(in);
        try
        {
            client.files ( ).moveV2 ( sourcePath , destinationPath );
        }
        catch( DbxException e)
        {
            Assertions.fail("Exception " + e);
        }
    }
}