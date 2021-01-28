package de.fleig.translationmemory.application;

import java.util.Locale;

public class Globals {
    private static Locale[] allAvailableLocales;

    /**
     * Start up sequence of the application.
     */
    public static void startUp() {
        setAllAvailableLocales(Locale.getAvailableLocales());
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
