package de.fleig.translationmemory.application;

import java.util.Locale;
import java.util.Scanner;

public class Globals {
    private static Locale[] allAvailableLocales;
    public static final Scanner inputScanner = new Scanner(System.in);

    /**
     * Start up sequence of the application.
     */
    public static void startUp() {
        setAllAvailableLocales(Locale.getAvailableLocales());
    }

    /**
     * Shut down the application
     */
    public static void shutDown() {

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
}
