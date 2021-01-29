package de.fleig.translationmemory.vocabulary;

import de.fleig.translationmemory.application.Globals;

import java.util.ArrayList;
import java.util.HashMap;
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
     *
     * @param word the word as String
     * @param languageOfWord the language of the word
     * @return the new word
     */
    public static Word createWord (String word, Language languageOfWord) {
        Word theNewWord = new Word(word, languageOfWord);
        ALL_WORDS.add(theNewWord);
        return theNewWord;
    }

    public static boolean doesWordExists(String wordToSearchFor) {
        for(Word eachWord : ALL_WORDS) {
            if(eachWord.getWORD().equals(wordToSearchFor)) {
                return true;
            }
        }
        return false;
    }

    public static Word getWord (String word) {
        for(Word eachWord : ALL_WORDS) {
            if(eachWord.getWORD().equals(word)) {
                return eachWord;
            }
        }
        return null;
    }

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
