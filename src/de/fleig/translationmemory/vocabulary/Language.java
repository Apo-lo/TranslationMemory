package de.fleig.translationmemory.vocabulary;

import java.util.Locale;
import java.util.UUID;

public class Language {
    private final Locale LANGUAGE_LOCALE;
    private final UUID LANGUAGE_ID;

    public Language(String languageName) {
        LANGUAGE_LOCALE = new Locale(languageName);
        LANGUAGE_ID = UUID.randomUUID();
    }

    public Locale getLanguageLocale() {
        return LANGUAGE_LOCALE;
    }

    public UUID getLanguageId() {
        return LANGUAGE_ID;
    }
}
