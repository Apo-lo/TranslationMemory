package de.fleig.translationmemory.application;

import de.fleig.translationmemory.exception.LoginFailedException;
import de.fleig.translationmemory.person.Administrator;
import de.fleig.translationmemory.person.AuthorizedUser;
import de.fleig.translationmemory.person.Translator;
import de.fleig.translationmemory.person.User;
import de.fleig.translationmemory.vocabulary.Language;
import de.fleig.translationmemory.vocabulary.Word;

import java.util.ArrayList;

public class MainApplication {

    static User currentUser;

    /**
     * Start the application beginning with the start up sequence.
     */
    public void startTranslationMemoryApplication() {
        Globals.startUp();
        mainApplication();
    }

    /**
     * The main program loop
     */
    public void mainApplication() {
        String input;
        boolean programRunning = true;

        setCurrentUser(loginAsStandardUserOrAuthorizedUserIfEmailIsSet());
        printWelcomeMessage();

        do {
            input = Globals.inputScanner.nextLine();

            switch (input) {
                case "-help":
                case "help": // to ensure that help is given
                    printOptions(currentUser);
                    break;
                case "-exit":
                    Globals.printToConsole("Exiting application");
                    Globals.shutDown();
                case "-login":
                    if ((currentUser instanceof AuthorizedUser)) {
                        Globals.printToConsole("Already logged in!");
                    }
                    if (login()) {
                        if (currentUser instanceof Administrator) {
                            printWelcomeMessageForAdministrator();
                        } else {
                            printWelcomeMessageForTranslator();
                        }
                    } else {
                        Globals.printToConsole("Login canceled");
                    }
                    break;
                case "-search":
                    Word.searchForWord();
                    break;
                case "-create -word":
                    Word.createWord();
                    break;
                case "-create -language":
                    if (currentUser instanceof Administrator) {
                        Language.createLanguage();
                    } else {
                        Globals.printToConsole("Not allowed to perform action!");
                    }
                    break;
                case "-assign":
                    if (currentUser instanceof Administrator) {
                        Administrator administrator = (Administrator) getCurrentUser();
                        administrator.assignTranslator();
                    } else {
                        Globals.printToConsole("Not allowed to perform action!");
                    }
                    break;
                case "-translated -words":
                    if (currentUser instanceof Translator) {
                        Globals.printToConsole("You translated " + ((Translator) currentUser).getTranslatedWordCount() + " words.");
                    } else {
                        Globals.printToConsole("Not allowed to perform action!");
                    }
                    break;
                case "-word -count":
                    Globals.printToConsole("You have created " + currentUser.getCreatedWordsCount() + " words.");
                    break;
                case "-translate":
                    if (currentUser instanceof Translator) {
                        Translator translator = (Translator) getCurrentUser();
                        translator.translateWord();
                    } else {
                        Globals.printToConsole("Not allowed to perform action!");
                    }
                    break;
                case "-logout":
                    if (currentUser instanceof  AuthorizedUser) {
                        Globals.printToConsole("Logging out and exiting application");
                        programRunning = false;
                    } else {
                        Globals.printToConsole("Not allowed to perform action!");
                    }
                    break;
                default:
                    Globals.printToConsole("Unknown command try \"-help\" for a list of available commands");
            }
        } while (programRunning);
    }

    /**
     * Log in as a standard user or if email is registered as authorized user.
     *
     * @return a new user or a already registered user
     */
    private User loginAsStandardUserOrAuthorizedUserIfEmailIsSet() {
        return User.loginAsStandardUserOrAuthorizedUserIfEmailIsSet();
    }

    /**
     * Login as Administrator or Translator.
     *
     * @return if the login request was successful
     */
    private boolean login() {

        while (true) {
            Globals.printToConsole("Email: ");
            String email = Globals.inputScanner.nextLine();

            Globals.printToConsole("Password: ");
            String password = Globals.inputScanner.nextLine();

            try {
                currentUser = AuthorizedUser.login(email, password);
                return true;
            } catch (LoginFailedException e) {
                Globals.printToConsole(e.getMessage() + ". Press press enter to try again or typ \"-cancel\" to cancel");
                if (Globals.inputScanner.nextLine().equals("-cancel")) {
                    return false;
                }
            }
        }
    }

    /**
     * Print the welcome message for the user type
     */
    private void printWelcomeMessage() {
        if (currentUser instanceof Administrator) {
            printWelcomeMessageForAdministrator();
        } else if (currentUser instanceof Translator) {
            printWelcomeMessageForTranslator();
        } else {
            printWelcomeMessageForStandardUser();
        }
    }
    /**
     * Print a welcome message after signing in as a administrator.
     */
    private void printWelcomeMessageForStandardUser() {
        Globals.printToConsole("Hello " + currentUser.getEmail());
    }

    /**
     * Print a welcome message after signing in as a translator.
     */
    private void printWelcomeMessageForTranslator() {
        Globals.printToConsole("Hello, " + currentUser.getEmail());
        Globals.printToConsole("Following words need to be translated");
        Translator currentTranslator = (Translator) currentUser;
        for (String eachMissingTranslation : currentTranslator.missingTranslations()) {
            Globals.printToConsole(eachMissingTranslation);
        }
    }

    /**
     * Print a welcome message after signing in as a administrator.
     */
    private void printWelcomeMessageForAdministrator() {
        Globals.printToConsole("Hello " + currentUser.getEmail());
        Globals.printToConsole("Following languages have been requested:");
        for (String eachRequestedLanguage : Administrator.LANGUAGES_TO_CREATE) {
            Globals.printToConsole(eachRequestedLanguage);
        }
    }

    /**
     * Create an ArrayList of available options for the user.
     *
     * @return an ArrayList of available options
     */
    private ArrayList<String> optionsForUsers() {
        ArrayList<String> optionsForUsers = new ArrayList<>();

        optionsForUsers.add("-help                  - list all available options");
        optionsForUsers.add("-exit                  - exit the application");
        optionsForUsers.add("-search                - search for an Word");
        optionsForUsers.add("-create -word          - create a new word");
        optionsForUsers.add("-word -count           - show how many words you created");
        optionsForUsers.add("-login                 - sign in as Administrator or Translator");

        return optionsForUsers;
    }

    /**
     * Create an ArrayList of available options for the Translator.
     *
     * @return an ArrayList of available options
     */
    private ArrayList<String> optionsForTranslators() {
        ArrayList<String> optionsForTranslators = optionsForAuthorizedUsers();

        optionsForTranslators.add("-translate           - translate a word");
        optionsForTranslators.add("-translated -words   - show how many words you translated");

        return optionsForTranslators;
    }

    /**
     * Create an ArrayList of available options for the Administrator.
     *
     * @return an ArrayList of available options
     */
    private ArrayList<String> optionsForAdministrator() {
        ArrayList<String> optionsForAdministrator = optionsForAuthorizedUsers();

        optionsForAdministrator.add("-create -language      - create a language");
        optionsForAdministrator.add("-assign                - assign a translator to a language ");
        return optionsForAdministrator;
    }

    /**
     * Create an ArrayList of available options for authorized users.
     *
     * @return an ArrayList of available options
     */
    private ArrayList<String> optionsForAuthorizedUsers() {
        ArrayList<String> optionsForAuthorizedUsers = optionsForUsers();

        optionsForAuthorizedUsers.add("-logout                  - to log out and exit");

        return optionsForAuthorizedUsers;
    }

    /**
     * Print the options available for the user type.
     *
     * @param currentUser type of user (User, Administrator. Translator)
     */
    private void printOptions(User currentUser) {
        ArrayList<String> optionsToPrint;
        if (currentUser instanceof Administrator) {
            optionsToPrint = optionsForAdministrator();
        } else if (currentUser instanceof Translator) {
            optionsToPrint = optionsForTranslators();
        } else {
            optionsToPrint = optionsForUsers();
        }

        for(String eachOption : optionsToPrint) {
            Globals.printToConsole(eachOption);
        }
    }

    /**
     * Answer the currentUser.
     *
     * @return the currentUser
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Set the currentUser.
     *
     * @param currentUser the user set (User, Translator, Administrator)
     */
    public static void setCurrentUser(User currentUser) {
        MainApplication.currentUser = currentUser;
    }
}
