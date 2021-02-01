package de.fleig.translationmemory.exception;

public class DecryptionException extends CryptographyException{

    /**
     * Constructor for the class
     */
    public DecryptionException() {
        super("Failed to decrypt password");
    }
}
