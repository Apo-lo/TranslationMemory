package de.fleig.translationmemory.vocabulary;

import java.util.UUID;

public class Word {
    private final String WORD;
    private final UUID WORD_ID;

    private final Language LANGUAGE_OF_WORD;

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
