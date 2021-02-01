package de.fleig.translationmemory.person;

import de.fleig.translationmemory.application.Globals;
import de.fleig.translationmemory.exception.LanguageNotFoundException;
import de.fleig.translationmemory.vocabulary.Language;

import java.util.ArrayList;
import java.util.HashSet;

public class Administrator extends AuthorizedUser {
    protected static final String DEFAULT_ADMINISTRATOR_EMAIL = "admin@admin.com"; // Hardcoded for first sign in.
    public static final HashSet<String> LANGUAGES_TO_CREATE = new HashSet<>(); //TODO Also save

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

    public void assignTranslator() {
        Globals.printToConsole("Email of the translator to assign");
        String input = Globals.inputScanner.nextLine();

        while(!isUserAlreadyRegistered(input)) { //TODO check when user is found if it is a Translator
            Globals.printToConsole("No Translator with this email found try again, or typ \"-cancel\" to cancel.");
            input = Globals.inputScanner.nextLine();
            if (input.equals("-cancel")) {
                return;
            }
        }
        Translator translatorToAssign = (Translator) AuthorizedUser.registeredAuthorizedUsers.get(input);

        Globals.printToConsole("To what language should the translator be assigned?");
        String language = Globals.inputScanner.nextLine();

        while (!Language.doesLanguageExist(language)) {
            Globals.printToConsole("No language with that name found. Do you want to create it? (yes/no)");
            language = Globals.inputScanner.nextLine();
            if(language.equals("yes")) {
                Language.createLanguage(language);
            } else {
                Globals.printToConsole("Try again, or type \"-cancel\" to cancel.");
                language = Globals.inputScanner.nextLine();
                if(language.equals("-cancel")) {
                    return;
                }
            }
        }
        try {
            Language theLanguageToAssign = Language.getLanguage(language);
            theLanguageToAssign.TRANSLATORS_OF_LANGUAGE.add(translatorToAssign);
            translatorToAssign.LANGUAGES_TO_TRANSLATE.add(theLanguageToAssign);
            Globals.printToConsole("Translator " + translatorToAssign.getEmail() + " successfully assigned to language " + theLanguageToAssign.getLANGUAGE_NAME());
        } catch (LanguageNotFoundException ignored) {
            // Can be ignored because getLanguage() will only be called if the language already exist.
        }
    }
}
