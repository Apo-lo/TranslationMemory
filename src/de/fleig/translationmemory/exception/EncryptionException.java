package de.fleig.translationmemory.exception;

public class EncryptionException extends CryptographyException{
    public EncryptionException() {
        super("Failed to encrypt password");
    }
}
