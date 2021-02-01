package de.fleig.translationmemory.exception;

public class WordNotFoundException extends VocabularyException {

    /**
     * Constructor for the class
     */
    public WordNotFoundException() {
        super("Word not found.");
    }
}
