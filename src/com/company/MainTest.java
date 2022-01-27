package com.company;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.URL;

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
            Assertions.assertTrue ( ACCESS_TOKEN == "obsUvJMAmrIAAAAAAAAAAaG5pNRcyh36yQNA8kOOQCpamhMX3TcDwjKVg3tFrm_E" );

        }
        catch ( IOException | DbxException e )
        {
            Assertions.fail("Exception " + e);
        }
    }

    @Test
    void listFilesForPracownik ( )
    {
    }

    @Test
    void listFiles ( )
    {
    }

    @Test
    void deleteFile ( )
    {
    }

    @Test
    void chooseFile ( )
    {
    }

    @Test
    void saveFile ( )
    {
    }

    @Test
    void centreWindow ( )
    {
    }

    @Test
    void downloadFile ( )
    {
    }

    @Test
    void moveFile ( )
    {
    }
}