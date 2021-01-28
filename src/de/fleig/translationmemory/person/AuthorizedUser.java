package de.fleig.translationmemory.person;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class AuthorizedUser extends User {
    private static final String DEFAULT_ADMINISTRATOR_EMAIL = "admin@admin.com"; // Hardcoded for first sign in.
    private static final String DEFAULT_TRANSLATOR_EMAIL = "translator@translator.com";
    private static final String DEFAULT_PASSWORD = "default_password";

    private static ArrayList<AuthorizedUser> registeredUsers = new ArrayList<>();

    private String email;
    private String password;

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

    /**
     * Loop over the registeredUsers
     * and return if a email of a user equal the email of the login request.
     * <p></p>
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
     * Answer if the email of the login request is a default email
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if it is a default email
     */
    private boolean isDefaultEmail (String emailOfLoginRequest) {
        return emailOfLoginRequest.equals(getDefaultAdministratorEmail()) || email.equals(getDefaultTranslatorEmail());
    }

    /**
     * Answer if the emailOfLoginRequest is the default administrator email
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if the emailOfLoginRequest is the default administrator email
     */
    private boolean isDefaultAdministratorEmail (String emailOfLoginRequest) {
        return emailOfLoginRequest.equals(getDefaultAdministratorEmail());
    }

    /**
     * Answer if the emailOfLoginRequest is the default translator email
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if the emailOfLoginRequest is the default translator email
     */
    private boolean isDEfaultTranslatorEmail (String emailOfLoginRequest) {
        return emailOfLoginRequest.equals(getDefaultTranslatorEmail());
    }

    /**
     * Answer the default administrator email
     *
     * @return the default administrator email
     */
    public static String getDefaultAdministratorEmail() {
        return DEFAULT_ADMINISTRATOR_EMAIL;
    }

    /**
     * Answer the default translator email
     *
     * @return the default translator email
     */
    public static String getDefaultTranslatorEmail() {
        return DEFAULT_TRANSLATOR_EMAIL;
    }
}
