package de.fleig.translationmemory.vocabulary;

import de.fleig.translationmemory.application.Globals;

import java.util.Locale;
import java.util.UUID;

public class Language {
    private final String LANGUAGE_NAME;
    private final UUID LANGUAGE_ID;

    /**
     * Constructor for Language Class.
     * Creates a UUID for the Language.
     *
     * @param languageName name of the language
     */
    public Language(String languageName) {
        LANGUAGE_NAME = languageName;
        LANGUAGE_ID = UUID.randomUUID();
    }

    /**
     * Answer the name of the language.
     *
     * @return the name if the language
     */
    public String getLANGUAGE_NAME() {
        return LANGUAGE_NAME;
    }

    /**
     * Answer the language id of the language.
     *
     * @return the id of the language
     */
    public UUID getLANGUAGE_ID() {
        return LANGUAGE_ID;
    }

    /**
     * Iterates over all available locals and checks if the given language name string
     * is a valid language, by comparing the display language string of each Locale to the given parameter.
     *
     * @param languageNameToCheck language name to check
     * @return if a language is valid
     */
    public static boolean isLanguage (String languageNameToCheck) {
        for (Locale eachLocale : Globals.getAllAvailableLocales()) {
            if (eachLocale.getDisplayLanguage().equalsIgnoreCase(languageNameToCheck)) {
                return true;
            }
        }
        return false;
    }
}
