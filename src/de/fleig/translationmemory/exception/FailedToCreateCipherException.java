package de.fleig.translationmemory.exception;

public class FailedToCreateCipherException extends CryptographyException {

    /**
     * Constructor for the class
     */
    public FailedToCreateCipherException () {
        super("Failed to create cipher");
    }
}
