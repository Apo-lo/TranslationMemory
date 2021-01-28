package de.fleig.translationmemory;

import de.fleig.translationmemory.application.Globals;
import de.fleig.translationmemory.application.MainApplication;
import de.fleig.translationmemory.exception.LoginFailedException;
import de.fleig.translationmemory.person.AuthorizedUser;

import java.util.Arrays;


public class Main {
    /**
     * Entry method of the application, just calls the start method of the actual application.
     *
     * @param args console parameter, not used in this application
     */
    public static void main(String[] args) {
        new MainApplication().startTranslationMemoryApplication();

        try {
            AuthorizedUser a = AuthorizedUser.login("translator@translator.com", "default_password");
            System.out.println(a.toString());
        } catch (Exception e) {
            Globals.errorLog(e.getMessage());
        }

    }
}