package de.fleig.translationmemory.exception;

public class EncryptionException extends CryptographyException{

    /**
     * Constructor for the class
     */
    public EncryptionException() {
        super("Failed to encrypt password");
    }
}
