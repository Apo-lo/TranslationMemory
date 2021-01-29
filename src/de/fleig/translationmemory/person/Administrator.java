package de.fleig.translationmemory.person;

public class Administrator extends AuthorizedUser {
    protected static final String DEFAULT_ADMINISTRATOR_EMAIL = "admin@admin.com"; // Hardcoded for first sign in.

    public Administrator (String email, String password) {
        super(email, password);
    }

    /**
     * Create a new administrator
     *
     * @return a new instance of administrator
     */
    protected static Administrator createAdministrator() {
        return (Administrator) createNewAuthorizedUser(true);
    }

    /**
     * Answer if the emailOfLoginRequest is the default administrator email.
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if the emailOfLoginRequest is the default administrator email
     */
    protected static boolean isDefaultAdministratorEmail(String emailOfLoginRequest) {
        return emailOfLoginRequest.equals(Administrator.DEFAULT_ADMINISTRATOR_EMAIL);
    }
}
