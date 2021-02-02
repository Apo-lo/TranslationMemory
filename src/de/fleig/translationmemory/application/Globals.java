package de.fleig.translationmemory.application;

import de.fleig.translationmemory.database.TranslationDatabase;
import de.fleig.translationmemory.person.AuthorizedUser;
import de.fleig.translationmemory.person.User;
import de.fleig.translationmemory.vocabulary.Language;
import de.fleig.translationmemory.vocabulary.Word;

import java.util.Locale;
import java.util.Scanner;

public class Globals {
    private static Locale[] allAvailableLocales;
    public static final Scanner inputScanner = new Scanner(System.in);

    private static final TranslationDatabase translationDatabase = TranslationDatabase.getInstance();

    /**
     * Start up sequence of the application.
     */
    public static void startUp() {
        setAllAvailableLocales(Locale.getAvailableLocales());
        translationDatabase.loadDataFromFile();
    }

    /**
     * Shut down the application
     */
    public static void shutDown() {
        translationDatabase.setListsToSave(Word.ALL_WORDS, Language.ALL_LANGUAGES, AuthorizedUser.REGISTERED_AUTHORIZED_USERS, User.REGISTERED_NORMAL_USERS);
        translationDatabase.saveDataToFile();
    }

    /**
     * Prints the parameter on the console
     *
     * @param stringToPrint a string to print on the console
     */
    public static void printToConsole(String stringToPrint) {
        System.out.println(stringToPrint);
    }

    /**
     * Logs errors
     *
     * @param errorStringToPrint a string to print on the console
     */
    public static void errorLog(String errorStringToPrint) {
        System.err.println(errorStringToPrint);
    }

    /**
     * Sets the allAvailableLocales class variable of Globals
     *
     * @param allAvailableLocales array of all available locales
     */
    public static void setAllAvailableLocales(Locale[] allAvailableLocales) {
        Globals.allAvailableLocales = allAvailableLocales;
    }

    /**
     * Answer the allAvailableLocales variable of Globals
     *
     * @return allAvailableLocales variable of Globals
     */
    public static Locale[] getAllAvailableLocales() {
        return allAvailableLocales;
    }

    public static String readNextLine() {
        return inputScanner.nextLine();
    }
}
