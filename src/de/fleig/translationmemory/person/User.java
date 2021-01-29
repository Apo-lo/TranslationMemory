package de.fleig.translationmemory.person;

import de.fleig.translationmemory.application.Globals;
import de.fleig.translationmemory.vocabulary.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    protected String email;

    private static final HashMap<String, User> registeredUsers = new HashMap<>(); //TODO save and load
    private final ArrayList<Word> createdWords = new ArrayList<>(); //TODO save and load

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
    public static User loginAsStandardUser() {
        Scanner inputScanner = new Scanner(System.in);
        User theNewOrAlreadyExistingUser;

        Globals.printToConsole("Hello, please enter your Email.");
        String input = inputScanner.nextLine();
        while (isInvalidEmail(input)) {
            Globals.printToConsole("Email not valid try again.");
            input = inputScanner.nextLine();
        }

        if (doesUserExist(input)) {
            theNewOrAlreadyExistingUser = registeredUsers.get(input);
        } else {
            theNewOrAlreadyExistingUser = new User(input);
        }
        registeredUsers.put(input, theNewOrAlreadyExistingUser);
        return theNewOrAlreadyExistingUser;
    }

    /**
     * Answer if the user is already registered
     *
     * @param email email of the user
     * @return if the user is already registered
     */
    private static boolean doesUserExist(String email) {
        return registeredUsers.containsKey(email);
    }

    /**
     * Check if a email is valid
     *
     * @param email email to validate
     *
     * @return if the given email is valid
     */
    protected static boolean isInvalidEmail(String email) { //https://stackoverflow.com/questions/8204680/java-regex-email
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return !matcher.find();
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
        return createdWords;
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
