package de.fleig.translationmemory.person;

import de.fleig.translationmemory.vocabulary.Language;

import java.util.ArrayList;

public class Translator extends AuthorizedUser {
    protected static final String DEFAULT_TRANSLATOR_EMAIL = "translator@translator.com"; // Hardcoded for first sign in.
    protected final ArrayList<Language> LANGUAGES_TO_TRANSLATE = new ArrayList<>();

    public Translator (String email, String password) {
        super(email, password);
    }

    /**
     * Create a new translator
     *
     * @return a new instance of translator
     */
    protected static Translator createTranslator() {
        return (Translator) createNewAuthorizedUser(false);
    }

    /**
     * Answer if the emailOfLoginRequest is the default translator email.
     *
     * @param emailOfLoginRequest the email of the login request
     * @return if the emailOfLoginRequest is the default translator email
     */
    protected static boolean isDefaultTranslatorEmail(String emailOfLoginRequest) {
        return emailOfLoginRequest.equals(Translator.DEFAULT_TRANSLATOR_EMAIL);
    }


}
