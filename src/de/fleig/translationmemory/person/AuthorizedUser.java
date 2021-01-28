package de.fleig.translationmemory.person;

import de.fleig.translationmemory.application.Globals;
import de.fleig.translationmemory.exception.DecryptionException;
import de.fleig.translationmemory.exception.EncryptionException;
import de.fleig.translationmemory.exception.FailedToCreateCipherException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class AuthorizedUser extends User {
    private static final String DEFAULT_ADMINISTRATOR_EMAIL = "admin@admin.com"; // Hardcoded for first sign in.
    private static final String DEFAULT_TRANSLATOR_EMAIL = "translator@translator.com";
    private static final String DEFAULT_PASSWORD = "default_password";

    private static final ArrayList<AuthorizedUser> registeredUsers = new ArrayList<>();

    private String email;
    private String password;
    private SecretKey encryptionKeyStringForUser;

    /**
     * First check if a user is already registered, if so check if the password is correct,
     * if that is true return a new instance of the user type.
     * If not registered and the default email is used, check for the correct default password,
     * after setting a new password and email return a instance of the user type.
     * <p></p>
     * If the user cannot be identified throw a NoSuchUserException, after every fifth set a login timeout for
     * a increasing amount of time. If the time reaches 3 Minutes terminate the program.
     *
     * @return a new instance of the user type
     */
    public AuthorizedUser login() {
        return null;
    }

    public AuthorizedUser() {
        createAesKeyAndSetForAuthorizedUser();
    }

    /**
     * Loop over the registeredUsers
     * and return if a email of a user equal the email of the login request.
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if there is a registered user with the given parameter as email
     */
    private boolean isUserAlreadyRegistered (String emailOfLoginRequest) {
        for (AuthorizedUser eachUser : registeredUsers) {
            if (eachUser.email.equals(emailOfLoginRequest)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Answer if the email of the login request is a default email.
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if it is a default email
     */
    private boolean isDefaultEmail (String emailOfLoginRequest) {
        return emailOfLoginRequest.equals(getDefaultAdministratorEmail()) || email.equals(getDefaultTranslatorEmail());
    }

    /**
     * Answer if the emailOfLoginRequest is the default administrator email.
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if the emailOfLoginRequest is the default administrator email
     */
    private boolean isDefaultAdministratorEmail (String emailOfLoginRequest) {
        return emailOfLoginRequest.equals(getDefaultAdministratorEmail());
    }

    /**
     * Answer if the emailOfLoginRequest is the default translator email.
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if the emailOfLoginRequest is the default translator email
     */
    private boolean isDefaultTranslatorEmail (String emailOfLoginRequest) {
        return emailOfLoginRequest.equals(getDefaultTranslatorEmail());
    }

    /**
     * Encrypt the authorized user password.
     *
     * @param passwordToEncrypt a password string to encrypt
     * @return a boolean indication if the encryption was successful
     * @throws EncryptionException encrypting the password failed
     */
    public byte[] encryptPassword (String passwordToEncrypt) throws EncryptionException {
        if (encryptionKeyStringForUser != null) {
            try {
                return createCipher(Cipher.ENCRYPT_MODE).doFinal(passwordToEncrypt.getBytes());
            } catch (BadPaddingException | IllegalBlockSizeException | FailedToCreateCipherException e) {
                Globals.errorLog("Failed to encrypt password");
                Globals.errorLog(e.getMessage());
            }
        } else {
            Globals.printToConsole("Saving not possible try again");
            throw new EncryptionException();
        }
        return null;
    }

    /**
     * Try to decrypt the password given as parameter.
     *
     * @param encryptedPassword the encrypted password as byte[]
     * @return the decrypted password
     * @throws DecryptionException if decryption failed
     */
    public String decryptPassword(byte[] encryptedPassword) throws DecryptionException {
        try {
            return new String(createCipher(Cipher.DECRYPT_MODE).doFinal(encryptedPassword));
        } catch (BadPaddingException | IllegalBlockSizeException | FailedToCreateCipherException e) {
            Globals.errorLog(e.getMessage());
            throw new DecryptionException();
        }
    }

    /**
     * Creat a new secret aes key and set it for the user.
     */
    private void createAesKeyAndSetForAuthorizedUser() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            setEncryptionKeyStringForUser(keyGen.generateKey());
        } catch (NoSuchAlgorithmException e) {
            Globals.errorLog("Failed to create Secret Key");
        }
    }

    /**
     * Creat a cipher for encrypting and decrypting the password
     *
     * @param cipherMode indicates in which mode the cypher is configured
     * @return the configured cipher
     * @throws FailedToCreateCipherException if the creation or set up failed
     */
    private Cipher createCipher (int cipherMode) throws FailedToCreateCipherException {
        try {
            Key aesKey = new SecretKeySpec(encryptionKeyStringForUser.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, aesKey);
            return cipher;
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            Globals.errorLog(e.getMessage());
            throw new FailedToCreateCipherException();
        }
    }

    /**
     * Answer the default administrator email.
     *
     * @return the default administrator email
     */
    public static String getDefaultAdministratorEmail() {
        return DEFAULT_ADMINISTRATOR_EMAIL;
    }

    /**
     * Answer the default translator email.
     *
     * @return the default translator email
     */
    public static String getDefaultTranslatorEmail() {
        return DEFAULT_TRANSLATOR_EMAIL;
    }

    /**
     * Set the Password of the authorized user.
     *
     * @param password the password of the authorized user
     */
    private void setPassword(String password) {
        this.password = password;
    }

    /**
     * Answer the password of the authorized user.
     *
     * @return the password of the authorized user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the encryptionKeyStringForUser.
     *
     * @param secretAesKey a SecretKey aes key
     */
    private void setEncryptionKeyStringForUser(SecretKey secretAesKey) {
        encryptionKeyStringForUser = secretAesKey;
    }
}
