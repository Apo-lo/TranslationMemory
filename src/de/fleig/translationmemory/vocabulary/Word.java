package de.fleig.translationmemory.vocabulary;

import java.util.UUID;

public class Word {
    private final String WORD;
    private final UUID WORD_ID;

    private final Language LANGUAGE_OF_WORD;

    public Word (String word, Language languageOfWord) {
        WORD = word;
        LANGUAGE_OF_WORD = languageOfWord;

        WORD_ID = UUID.randomUUID();
    }

    public Language getLANGUAGE_OF_WORD() {
        return LANGUAGE_OF_WORD;
    }

    public String getWORD() {
        return WORD;
    }

    public UUID getWORD_ID() {
        return WORD_ID;
    }
}
