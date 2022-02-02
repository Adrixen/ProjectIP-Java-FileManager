package com.company;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class PasswordHashTest
{

    @Test
    void IntegrationTest ( )
    {
        try
        {
            // Print out 10 hashes
            for(int i = 0; i < 10; i++)
                System.out.println(PasswordHash.createHash("p\r\nassw0Rd!"));

            // Test password validation
            System.out.println("Running tests...");
            for(int i = 0; i < 100; i++)
            {
                String password = ""+i;
                String hash = PasswordHash.createHash(password);
                String secondHash = PasswordHash.createHash(password);
                if(hash.equals(secondHash)) {
                    System.out.println("FAILURE: TWO HASHES ARE EQUAL!");
                    Assertions.fail("Failed");
                }
                String wrongPassword = ""+(i+1);
                if(PasswordHash.validatePassword(wrongPassword, hash)) {
                    System.out.println("FAILURE: WRONG PASSWORD ACCEPTED!");
                    Assertions.fail("Failed");
                }
                if(!PasswordHash.validatePassword(password, hash)) {
                    System.out.println("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
                    Assertions.fail("Failed");
                }
            }
                System.out.println("TESTS PASSED!");
        }
        catch(Exception ex)
        {
            Assertions.fail( "Exception " + ex);
        }
    }

}