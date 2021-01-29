package de.fleig.translationmemory.vocabulary;

import de.fleig.translationmemory.application.Globals;
import de.fleig.translationmemory.exception.LanguageNotFoundException;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class Language {
    private final String LANGUAGE_NAME;
    private final UUID LANGUAGE_ID;

    public static final ArrayList<Language> ALL_LANGUAGES = new ArrayList<>();

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
    public static boolean isLanguage(String languageNameToCheck) {
        for (Locale eachLocale : Globals.getAllAvailableLocales()) {
            if (eachLocale.getDisplayLanguage().equalsIgnoreCase(languageNameToCheck)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Answer if a language exists.
     *
     * @param languageNameToCheck the name of teh language to check
     * @return if the the language exists
     */
    public static boolean doesLanguageExists(String languageNameToCheck) {
        for(Language eachLanguage : ALL_LANGUAGES) {
            if(eachLanguage.getLANGUAGE_NAME().equals(languageNameToCheck)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Answer the language with the name from the parameter
     *
     * @param nameOfLanguage name of language to search
     * @return the Language associated with the name
     * @throws LanguageNotFoundException if the language cannot be found
     */
    public static Language getLanguage(String nameOfLanguage) throws LanguageNotFoundException {
        for(Language eachLanguage : ALL_LANGUAGES) {
            if(eachLanguage.getLANGUAGE_NAME().equals(nameOfLanguage)) {
                return eachLanguage;
            }
        }
        throw new LanguageNotFoundException();
    }
}
