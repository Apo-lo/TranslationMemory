package de.fleig.translationmemory.person;

import de.fleig.translationmemory.application.Globals;
import de.fleig.translationmemory.exception.LoginFailedException;
import de.fleig.translationmemory.vocabulary.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class User {

    protected String email;

    public static final HashMap<String, User> REGISTERED_NORMAL_USERS = new HashMap<>(); //TODO save and load
    private final ArrayList<Word> CREATED_WORDS = new ArrayList<>(); //TODO save and load

    protected static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE); //https://stackoverflow.com/questions/8204680/java-regex-email

    /**
     * Constructor for Class User
     *
     * @param email email of the User
     */
    public User(String email) {
        this.email = email;
    }

    /**
     * Log in as standard user.
     *
     * @return a new user or a already registered user
     */
    public static User loginAsStandardUserOrAuthorizedUserIfEmailIsSet() {
        User theNewOrAlreadyExistingUser;

        Globals.printToConsole("Please enter your Email.");
        String input = Globals.inputScanner.nextLine();
        while (isInvalidEmail(input)) {
            Globals.printToConsole("Email not valid try again.");
            input = Globals.inputScanner.nextLine();
        }

        if (doesUserExist(input)) {
            theNewOrAlreadyExistingUser = REGISTERED_NORMAL_USERS.get(input);
        } else if (AuthorizedUser.isUserAlreadyRegistered(input) || AuthorizedUser.isDefaultEmail(input)) {
            theNewOrAlreadyExistingUser = tryLoginAsAuthorizedUser(input);
        } else {
            theNewOrAlreadyExistingUser = new User(input);
            REGISTERED_NORMAL_USERS.put(input, theNewOrAlreadyExistingUser);
        }
        return theNewOrAlreadyExistingUser;
    }

    /**
     * When logging in at start this method is used to check the user.
     *
     * @param email email of login
     * @return the AuthorizedUser user associated to the email
     */
    private static AuthorizedUser tryLoginAsAuthorizedUser(String email) {
        try {
            return AuthorizedUser.askForPasswordAndTryLogin(email);
        } catch (LoginFailedException e) {
            Globals.printToConsole("Login failed, please try again or type \"-exit\" to close the program.");
            if(Globals.inputScanner.nextLine().equals("-exit")) {
                Globals.shutDown();
            }
            loginAsStandardUserOrAuthorizedUserIfEmailIsSet();
        }
        return null; // null is ok in this case, it will never be called because the user is forced to retry or to exit
    }

    /**
     * Answer if the user is already registered
     *
     * @param email email of the user
     * @return if the user is already registered
     */
    private static boolean doesUserExist(String email) {
        return REGISTERED_NORMAL_USERS.containsKey(email);
    }

    /**
     * Check if a email is invalid
     *
     * @param email email to validate
     *
     * @return if the given email is invalid
     */
    protected static boolean isInvalidEmail(String email) {
        return !VALID_EMAIL_ADDRESS_REGEX.matcher(email).find();
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
     * Answer the createdWOrds of the user
     *
     * @return the created words of the user
     */
    public ArrayList<Word> getCreatedWords() {
        return CREATED_WORDS;
    }

    /**
     * Answer the number of the words created by the user
     *
     * @return the number of words created by the user
     */
    public int getCreatedWordsCount() {
        return getCreatedWords().size();
    }
}
