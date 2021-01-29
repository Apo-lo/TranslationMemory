package de.fleig.translationmemory.person;

import de.fleig.translationmemory.application.Globals;
import de.fleig.translationmemory.exception.DecryptionException;
import de.fleig.translationmemory.exception.EncryptionException;
import de.fleig.translationmemory.exception.FailedToCreateCipherException;
import de.fleig.translationmemory.exception.LoginFailedException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

public class AuthorizedUser extends User {
    private static final String DEFAULT_PASSWORD = "default_password"; // Hardcoded for first sign in.

    private static final HashMap<String, AuthorizedUser> registeredAuthorizedUsers = new HashMap<>();

    private String password;

    /**
     * Constructor of AuthorizedUser
     *
     * @param email email of the user
     * @param password password of the user
     */
    public AuthorizedUser(String email, String password) {
        super(email);
        this.password = password;
    }

    /**
     * First check if a user is already registered, if so check if the password is correct,
     * if that is true return the user.
     * If not registered and the default email is used, check for the correct default password,
     * after setting a new password and email return a instance of the user type.
     * <p></p>
     * Throws Exception if
     * <ul>
     *     <li>User is not already registered and does not use default email</li>
     *     <li>Registered user with wrong password</li>
     *     <li>Default email without default password</li>
     * </ul>
     *
     * @return a new instance of the user type
     * @throws LoginFailedException if the login fails
     */
    public static AuthorizedUser login(String email, String password) throws LoginFailedException {
        AuthorizedUser userToSignIn;
        if(isUserAlreadyRegistered(email)) {
            userToSignIn = registeredAuthorizedUsers.get(email);
            if (isPasswordValidForUser(userToSignIn, password)) {
                return userToSignIn;
            } else {
                throw new LoginFailedException();
            }
        } else if (isDefaultEmail(email) && isDefaultPassword(password)) {
            if (Administrator.isDefaultAdministratorEmail(email)) {
                userToSignIn = Administrator.createAdministrator();
            } else {
                userToSignIn = Translator.createTranslator();
            }
            registeredAuthorizedUsers.put(userToSignIn.getEmail(), userToSignIn);
            return userToSignIn;
        }
        throw new LoginFailedException();
    }

    /**
     * Get the email and password from the user and return a new instance of translator or administrator
     *
     * @param isAdministrator indicates if the returned authorized user is an administrator or translator
     * @return a new instance of administrator or translator
     */
    protected static AuthorizedUser createNewAuthorizedUser(boolean isAdministrator) {
        Scanner inputScanner = new Scanner(System.in);
        String email;
        String password;
        Globals.printToConsole("Welcome, this is your first login. Please change your email and password.");
        Globals.printToConsole("Please enter a new email address.");
        String input = inputScanner.nextLine();
        while (isInvalidEmail(input)) {
            Globals.printToConsole("Email not valid try again.");
            input = inputScanner.nextLine();
        }
        email = input;

        Globals.printToConsole("Please enter a new password.");
        password = inputScanner.nextLine();

        if(isAdministrator) {
            return new Administrator(email, password);
        } else {
            return new Translator(email, password);
        }
    }

    /**
     * Loop over the registeredUsers
     * and return if a email of a user equal the email of the login request.
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if there is a registered user with the given parameter as email
     */
    private static boolean isUserAlreadyRegistered(String emailOfLoginRequest) {
        return registeredAuthorizedUsers.containsKey(emailOfLoginRequest);
    }

    private static boolean isPasswordValidForUser(AuthorizedUser userToSignIn, String passwordOfLoginRequest) {
        return userToSignIn.getPassword().equals(passwordOfLoginRequest);
    }
    /**
     * Answer if the email of the login request is a default email.
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if it is a default email
     */
    private static boolean isDefaultEmail(String emailOfLoginRequest) {
        return Administrator.isDefaultAdministratorEmail(emailOfLoginRequest)|| Translator.isDefaultTranslatorEmail(emailOfLoginRequest);
    }

    /**
     * Answer if the password is the default password
     *
     * @param password the password of the login request
     * @return if the password of the login request is the default password
     */
    private static boolean isDefaultPassword(String password) {
        return password.equals(DEFAULT_PASSWORD);
    }
    /**
     * Encrypt the authorized user password.
     *
     * @param passwordToEncrypt a password string to encrypt
     * @return a boolean indication if the encryption was successful
     * @throws EncryptionException encrypting the password failed
     */
    private byte[] encryptPassword(String passwordToEncrypt) throws EncryptionException {
        try {
            return createCipher(Cipher.ENCRYPT_MODE).doFinal(passwordToEncrypt.getBytes());
        } catch (BadPaddingException | IllegalBlockSizeException | FailedToCreateCipherException e) {
            Globals.errorLog("Failed to encrypt password");
            Globals.errorLog(e.getMessage());
            throw new EncryptionException();
        }
    }

    /**
     * Try to decrypt the password given as parameter.
     *
     * @param encryptedPassword the encrypted password as byte[]
     * @return the decrypted password
     * @throws DecryptionException if decryption failed
     */
    private String decryptPassword(byte[] encryptedPassword) throws DecryptionException {
        try {
            return new String(createCipher(Cipher.DECRYPT_MODE).doFinal(encryptedPassword));
        } catch (BadPaddingException | IllegalBlockSizeException | FailedToCreateCipherException e) {
            Globals.errorLog(e.getMessage());
            throw new DecryptionException();
        }
    }

    /**
     * Creat a cipher for encrypting and decrypting the password
     *
     * @param cipherMode indicates in which mode the cypher is configured
     * @return the configured cipher
     * @throws FailedToCreateCipherException if the creation or set up failed
     */
    private Cipher createCipher(int cipherMode) throws FailedToCreateCipherException {
        try {
            String aesKeyString = "t6w9z$C&F)J@McQf"; // Don't know if this is good practise, but better than saving the password in plain text
            Key aesKey = new SecretKeySpec(aesKeyString.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, aesKey);
            return cipher;
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            Globals.errorLog(e.getMessage());
            throw new FailedToCreateCipherException();
        }
    }


    /**
     * Set the Password of the authorized user.
     *
     * @param password the password to assign
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

}