package de.fleig.translationmemory.vocabulary;

import de.fleig.translationmemory.application.Globals;
import de.fleig.translationmemory.application.MainApplication;
import de.fleig.translationmemory.exception.LanguageNotFoundException;
import de.fleig.translationmemory.exception.WordNotFoundException;
import de.fleig.translationmemory.person.Administrator;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class Word {
    private final String WORD;
    private final UUID WORD_ID;

    private final Language LANGUAGE_OF_WORD;

    public static final ArrayList<Word> ALL_WORDS = new ArrayList<>();
    public final ArrayList<Word> ALL_TRANSLATIONS_OF_WORD = new ArrayList<>();

    /**
     * Constructor for Word Class.
     * Creates a UUID for the Word as well.
     *
     * @param word the word as String
     * @param languageOfWord the language of the word
     */
    public Word (String word, Language languageOfWord) {
        WORD = word;
        LANGUAGE_OF_WORD = languageOfWord;

        WORD_ID = UUID.randomUUID();
    }

    /**
     * Create a new word and add it to the all words array list
     */
    public static void createWord(String wordToCreate) {
        Scanner inputScanner = new Scanner(System.in);
        String word;

        if (doesWordExists(wordToCreate)) {
            wordAlreadyExistsOutput(wordToCreate);
            return;
        }

        if (wordToCreate.isEmpty()) {
            Globals.printToConsole("What word would you like to create?");
            word = inputScanner.nextLine();
        } else {
            word = wordToCreate;
        }

        if (doesWordExists(word)) {
            wordAlreadyExistsOutput(word);
            return;
        }

        Globals.printToConsole("In which Language is the word?");
        String input = inputScanner.nextLine();

        if(Language.doesLanguageExists(input)) {
            try {
                Word theNewWord = new Word(word, Language.getLanguage(input));
                ALL_WORDS.add(theNewWord);
                MainApplication.getCurrentUser().getCreatedWords().add(theNewWord);
                Globals.printToConsole("Word successfully created");
            } catch (LanguageNotFoundException ignored) {
                // Exception is ignored, getLanguage() will only be called if the language is present.
            }
        } else {
            Globals.printToConsole("Language does not exist. An Administrator will be notified of your request");
            Administrator.LANGUAGES_TO_CREATE.add(input);
        }
    }

    /**
     * Search for a word if it exist print the word and all its translations.
     * Otherwise ask if the User want to creat the word.
     * The User can search for another word if the word does not exist.
     */
    public static void searchForWord() {
        Scanner inputScanner = new Scanner(System.in);

        Globals.printToConsole("What word would you like to search for");
        String input= inputScanner.nextLine();
        String word = input;
        if (Word.doesWordExists(input)) {
            try {
                Word.getWord(input).printWordWithTranslations();
            } catch (WordNotFoundException ignored) {
                //No need to do something with the exception, because the method getWord() will only be called if the word is present.
            }
        } else {
            Globals.printToConsole("Word does not exist yet, do you want to create it? (yes/no)");
            input = inputScanner.nextLine();
            if (input.equalsIgnoreCase("yes")) {
                Word.createWord(word);
            } else {
                Globals.printToConsole("Press enter to search for another word typ \"-cancel\" to cancel");
                if (!inputScanner.nextLine().equals("-cancel")) {
                    searchForWord();
                }
            }
        }
    }

    /**
     * Answer if the given word exist in the database
     *
     * @param wordToSearchFor word to search in the database
     * @return if the word exists
     */
    public static boolean doesWordExists(String wordToSearchFor) {
        for(Word eachWord : ALL_WORDS) {
            if(eachWord.getWORD().equals(wordToSearchFor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Answer the instance of the word class with the word from the parameter
     *
     * @param word as string to search for the instance of the word
     * @return the instance of the word class
     * @throws WordNotFoundException when no Word is found
     */
    public static Word getWord (String word) throws WordNotFoundException {
        for(Word eachWord : ALL_WORDS) {
            if(eachWord.getWORD().equals(word)) {
                return eachWord;
            }
        }
        throw new WordNotFoundException();
    }

    /**
     * Prints the words and all its available translations
     */
    public void printWordWithTranslations() {
        Globals.printToConsole(getLANGUAGE_OF_WORD() + " : " + getWORD());
        for(Word translationOfWord : ALL_TRANSLATIONS_OF_WORD) {
            Globals.printToConsole(translationOfWord.getLANGUAGE_OF_WORD().getLANGUAGE_NAME() + " : " + translationOfWord.getWORD());
        }
    }

    /**
     * Console output if the word already exist
     * @param wordThatExists the word to print the translations
     */
    private static void wordAlreadyExistsOutput(String wordThatExists) {
        try {
            Globals.printToConsole("Word already exists, here are the translations.");
            getWord(wordThatExists).printWordWithTranslations();
        } catch (WordNotFoundException ignored) {
            // No need to do something with the exception, because the method getWord() will only be called if the word is present.
        }
    }
    /**
     * Answer the language of the word
     *
     * @return the language of the word
     */
    public Language getLANGUAGE_OF_WORD() {
        return LANGUAGE_OF_WORD;
    }

    /**
     * Answer the word as String.
     *
     * @return the word as String
     */
    public String getWORD() {
        return WORD;
    }

    /**
     * Answer the in of the word
     *
     * @return the id of the word
     */
    public UUID getWORD_ID() {
        return WORD_ID;
    }

    /**
     * Answer the number of word in database
     *
     * @return the number of all words
     */
    public static int getAllWordsCount() {
        return ALL_WORDS.size();
    }
}
