package de.fleig.translationmemory.exception;

public class CryptographyException extends Exception {

    /**
     * Constructor for the class
     *
     * @param errorMessage the error message to show the user
     */
    public CryptographyException (String errorMessage) {
        super(errorMessage);
    }
}
