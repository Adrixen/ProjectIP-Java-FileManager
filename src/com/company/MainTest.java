package com.company;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        JFrame frameKadry=new JFrame (  );
        char[] password = {'k', 'a', 'd', 'r', 'y'};
        com.company.Main.logowanie (password);
        Assertions.assertEquals(3, Main.permission );
    }

    @Test
    void infoBox ( )
    {
    }

    @Test
    void displayGUIComponents ( )
    {
    }

    @Test
    void setUpServerConnection ( )
    {
    }

    @Test
    void uploadFile ( )
    {
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