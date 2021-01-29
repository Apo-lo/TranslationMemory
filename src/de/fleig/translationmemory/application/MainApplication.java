package de.fleig.translationmemory.application;

import de.fleig.translationmemory.exception.LoginFailedException;
import de.fleig.translationmemory.person.Administrator;
import de.fleig.translationmemory.person.AuthorizedUser;
import de.fleig.translationmemory.person.Translator;
import de.fleig.translationmemory.person.User;
import de.fleig.translationmemory.vocabulary.Language;
import de.fleig.translationmemory.vocabulary.Word;

import java.util.ArrayList;
import java.util.Scanner;

public class MainApplication {

    static User currentUser;

    /**
     * Start the application beginning with the start up sequence.
     */
    public void startTranslationMemoryApplication() {
        Globals.startUp();
        mainApplication();
    }

    public void mainApplication() {
        Scanner inputScanner = new Scanner(System.in);
        String input;
        boolean programRunning = true;

        setCurrentUser(loginAsStandardUser());
        printWelcomeMessageForStandardUser();

        do {
            input = inputScanner.nextLine();

            switch (input) {
                case "-help":
                case "help": //to ensure that help is given
                    printOptions(currentUser);
                    break;
                case "-exit":
                    Globals.printToConsole("Exiting application");
                    programRunning = false;
                    break;
                case "-login":
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
                    Word.createWord("");
                    break;
                case "-create -language":
                    Language.createLanguage("");
                    break;
                case "-logout":
                    Globals.printToConsole("Logging out and exiting application");
                    programRunning = false;
                    break;
                default:
                    Globals.printToConsole("Unknown command try \"-help\" for a list of available commands");
            }
        } while (programRunning);
    }

    /**
     * Log in as a standard user
     *
     * @return a new user or a already registered user
     */
    private User loginAsStandardUser() {
        return User.loginAsStandardUser();
    }

    /**
     * Login as Administrator or Translator.
     *
     * @return if the login request was successful
     */
    private boolean login() {
        Scanner inputScanner = new Scanner(System.in);

        while (true) {
            Globals.printToConsole("Email: ");
            String email = inputScanner.nextLine();

            Globals.printToConsole("Password: ");
            String password = inputScanner.nextLine();

            try {
                currentUser = AuthorizedUser.login(email, password);
                return true;
            } catch (LoginFailedException e) {
                Globals.printToConsole(e.getMessage() + ". Press press enter to try again or typ \"-cancel\" to cancel");
                if (inputScanner.nextLine().equals("-cancel")) {
                    return false;
                }
            }
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
    }

    /**
     * Print a welcome message after signing in as a administrator.
     */
    private void printWelcomeMessageForAdministrator() {
        Globals.printToConsole("Hello " + currentUser.getEmail());
    }
    /**
     * Create an ArrayList of available options for the user
     *
     * @return an ArrayList of available options
     */
    private ArrayList<String> optionsForUsers() {
        ArrayList<String> optionsForUsers = new ArrayList<>();

        optionsForUsers.add("-help                  - list all available options");
        optionsForUsers.add("-exit                  - exit the application");
        optionsForUsers.add("-search                - search for an Word");
        optionsForUsers.add("-create -word          - create a new word");
        optionsForUsers.add("-login                 - sign in as Administrator or Translator");

        return optionsForUsers;
    }

    /**
     * Create an ArrayList of available options for the Translator
     *
     * @return an ArrayList of available options
     */
    private ArrayList<String> optionsForTranslators() {
        ArrayList<String> optionsForTranslators = optionsForAuthorizedUsers();

        return optionsForTranslators;
    }

    private ArrayList<String> optionsForAdministrator() {
        ArrayList<String> optionsForAdministrator = optionsForAuthorizedUsers();

        optionsForAdministrator.add("-create -language             - create a language");
        return optionsForAdministrator;
    }

    private ArrayList<String> optionsForAuthorizedUsers() {
        ArrayList<String> optionsForAuthorizedUsers = optionsForUsers();

        optionsForAuthorizedUsers.add("-show                - list all translated words");
        optionsForAuthorizedUsers.add("-logout              - to log out");

        return optionsForAuthorizedUsers;
    }

    /**
     * Print the options available for the user type
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
     * Answer the currentUser
     *
     * @return the currentUser
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Set the currentUser
     *
     * @param currentUser the user set (User, Translator, Administrator)
     */
    public static void setCurrentUser(User currentUser) {
        MainApplication.currentUser = currentUser;
    }
}
