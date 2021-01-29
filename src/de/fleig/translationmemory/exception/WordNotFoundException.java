package de.fleig.translationmemory.exception;

public class WordNotFoundException extends VocabularyException {

    public WordNotFoundException() {
        super("Word not found.");
    }
}
