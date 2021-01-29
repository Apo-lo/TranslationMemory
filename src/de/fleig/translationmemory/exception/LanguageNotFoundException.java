package de.fleig.translationmemory.exception;

public class LanguageNotFoundException extends VocabularyException {
    public LanguageNotFoundException() {
        super("Language not found.");
    }
}
