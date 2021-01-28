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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizedUser extends User {
    private static final String DEFAULT_ADMINISTRATOR_EMAIL = "admin@admin.com"; // Hardcoded for first sign in.
    private static final String DEFAULT_TRANSLATOR_EMAIL = "translator@translator.com";
    private static final String DEFAULT_PASSWORD = "default_password";

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE); //https://stackoverflow.com/questions/8204680/java-regex-email


    private static final HashMap<String, AuthorizedUser> registeredUsers = new HashMap<>();

    private String email;
    private String password;
    private SecretKey encryptionKeyStringForUser;

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
     * @throws LoginFailedException if the login fails because of a wrong email or password
     */
    public static AuthorizedUser login(String email, String password) throws LoginFailedException {
        if(isUserAlreadyRegistered(email)) {
            AuthorizedUser userToSignIn = registeredUsers.get(email);
            if (isPasswordValidForUser(userToSignIn, password)) {
                return userToSignIn;
            } else {
                throw new LoginFailedException();
            }
        } else if (isDefaultEmail(email) && isDefaultPassword(password)) {
            AuthorizedUser newUser;
            if (isDefaultAdministratorEmail(email)) {
                newUser = createAdministrator();
            } else {
                newUser = createTranslator();
            }
            registeredUsers.put(newUser.getEmail(), newUser);
            return newUser;
        }
        throw new LoginFailedException();
    }

    /**
     * Get the email and password from the user and return a new instance of translator or administrator
     *
     * @param isAdministrator indicates if the returned authorized user is an administrator or translator
     * @return a new instance of administrator of translator
     */
    private static AuthorizedUser createNewAuthorizedUser(boolean isAdministrator) {
        Scanner inputScanner = new Scanner(System.in);
        String email;
        String password;

        Globals.printToConsole("Please enter a new email address");
        String input = inputScanner.nextLine();
        while (!isValidEmail(input)) {
            Globals.printToConsole("Email not valid try again");
            input = inputScanner.nextLine();
        }
        email = input;

        Globals.printToConsole("Please enter a new password");
        password = inputScanner.nextLine();

        if (isAdministrator) {
            return new Administrator(email, password);
        } else {
            return new Translator(email, password);
        }
    }

    /**
     * Create a new translator
     *
     * @return a new instance of translator
     */
    public static Translator createTranslator() {
        return (Translator) createNewAuthorizedUser(false);
    }

    /**
     * Create a new administrator
     *
     * @return a new instance of administrator
     */
    private static Administrator createAdministrator() {
        return (Administrator) createNewAuthorizedUser(true);
    }

    /**
     * Constructor of AuthorizedUser
     *
     * @param email email of the user
     * @param password password of the user
     */
    public AuthorizedUser(String email, String password) {
        createAesKeyAndSetForAuthorizedUser();
        this.email = email;
        this.password = password;
    }

    /**
     * Loop over the registeredUsers
     * and return if a email of a user equal the email of the login request.
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if there is a registered user with the given parameter as email
     */
    private static boolean isUserAlreadyRegistered(String emailOfLoginRequest) {
        return registeredUsers.containsKey(emailOfLoginRequest);
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
        return isDefaultAdministratorEmail(emailOfLoginRequest)|| isDefaultTranslatorEmail(emailOfLoginRequest);
    }

    /**
     * Answer if the emailOfLoginRequest is the default administrator email.
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if the emailOfLoginRequest is the default administrator email
     */
    private static boolean isDefaultAdministratorEmail(String emailOfLoginRequest) {
        return emailOfLoginRequest.equals(DEFAULT_ADMINISTRATOR_EMAIL);
    }

    /**
     * Answer if the emailOfLoginRequest is the default translator email.
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if the emailOfLoginRequest is the default translator email
     */
    private static boolean isDefaultTranslatorEmail(String emailOfLoginRequest) {
        return emailOfLoginRequest.equals(DEFAULT_TRANSLATOR_EMAIL);
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
    private String decryptPassword(byte[] encryptedPassword) throws DecryptionException {
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
    private Cipher createCipher(int cipherMode) throws FailedToCreateCipherException {
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
     * Check if a email is valid
     *
     * @param email email to validate
     * @return if the given email is valid
     */
    private static boolean isValidEmail(String email) { //https://stackoverflow.com/questions/8204680/java-regex-email
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
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

    /**
     * Answer the email of the authorized user.
     *
     * @return the email of the authorized user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email of the authorized user.
     *
     * @param email the email to assign
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the encryptionKeyStringForUser.
     *
     * @param secretAesKey the SecretKey to assign
     */
    private void setEncryptionKeyStringForUser(SecretKey secretAesKey) {
        encryptionKeyStringForUser = secretAesKey;
    }
}
