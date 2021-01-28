package de.fleig.translationmemory.exception;

public class DecryptionException extends CryptographyException{
    public DecryptionException() {
        super("Failed to decrypt password");
    }
}
