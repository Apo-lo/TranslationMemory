package de.fleig.translationmemory.exception;

public class FailedToCreateCipherException extends CryptographyException {
    public FailedToCreateCipherException () {
        super("Failed to create cipher");
    }
}
