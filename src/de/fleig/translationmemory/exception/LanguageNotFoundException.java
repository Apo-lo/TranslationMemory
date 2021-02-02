package de.fleig.translationmemory.exception;

public class LanguageNotFoundException extends VocabularyException {

    /**
     * Constructor for the class
     */
    public LanguageNotFoundException() {
        super("Language not found.");
    }
}
