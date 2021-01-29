package de.fleig.translationmemory.vocabulary;

import de.fleig.translationmemory.application.Globals;
import de.fleig.translationmemory.exception.LanguageNotFoundException;
import de.fleig.translationmemory.exception.WordNotFoundException;

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
    public static void createWord() {
        Scanner inputScanner = new Scanner(System.in);
        String word;

        Globals.printToConsole("What word would you like to create?");
        word = inputScanner.nextLine();

        Globals.printToConsole("In which Language is the word?");
        String input = inputScanner.nextLine();

        if(Language.doesLanguageExists(input)) {
            try {
                Word theNewWord = new Word(word, Language.getLanguage(input));
                ALL_WORDS.add(theNewWord);
            } catch (LanguageNotFoundException ignored) {
                // Exception is ignored, getLanguage() will only be called if the language is present.
            }

        } else {
            Globals.printToConsole("Language does not exist. Please contact an Administrator to create the language");
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
            Globals.printToConsole(translationOfWord.getLANGUAGE_OF_WORD() + " : " + translationOfWord.getWORD());
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
}
