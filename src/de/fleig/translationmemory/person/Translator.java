package de.fleig.translationmemory.person;

import de.fleig.translationmemory.application.Globals;
import de.fleig.translationmemory.exception.LanguageNotFoundException;
import de.fleig.translationmemory.exception.WordNotFoundException;
import de.fleig.translationmemory.vocabulary.Language;
import de.fleig.translationmemory.vocabulary.Word;

import java.util.ArrayList;

public class Translator extends AuthorizedUser {
    protected static final String DEFAULT_TRANSLATOR_EMAIL = "translator@translator.com"; // Hardcoded for first sign in.
    protected final ArrayList<Language> LANGUAGES_TO_TRANSLATE = new ArrayList<>();
    public final ArrayList<Word> TRANSLATED_WORDS = new ArrayList<>();

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

    /**
     * Translate a word, add it to all words and to the translated word of the original word.
     */
    public void translateWord() {
        Language languageOfTranslation = new Language(""); // Need this to check if the Translator is allowed to translate the language
        Word wordToTranslate = new Word("",languageOfTranslation);

        Globals.printToConsole("What word would you like to translate");
        String input = Globals.readNextLine();

        if (Word.doesWordExists(input)) {
            try {
                wordToTranslate = Word.getWord(input);
            } catch (WordNotFoundException ignored) {
                // Ignored, getWord() will only be called if word is present
            }
        } else {
            Globals.printToConsole("Word does not exist, would you like to create it? (yes/no)");
            if(Globals.readNextLine().equals("yes")) {
                Word.createWord();
                try { // Not happy with this solution, but it works
                    wordToTranslate = Word.getWord(input);
                } catch (WordNotFoundException e) {
                    Globals.printToConsole("Failed to create word, please see output.");
                    Globals.printToConsole(e.getMessage());
                    return;
                }
            }
        }

        Globals.printToConsole("In what language is your translation?");
        input = Globals.readNextLine();

        while (!Language.doesLanguageExist(input)) {
            Globals.printToConsole("Language does not exist, try again or type \"-cancel\" to cancel");
            input = Globals.readNextLine();
            if(input.equals("-cancel")) {
                break;
            }
        }

        try {
            languageOfTranslation = Language.getLanguage(input);
        } catch (LanguageNotFoundException ignored) {
            // Ignored, user is forced to type a existing language or to cancel
        }
        if (!isAllowedToTranslateLanguage(languageOfTranslation)) {
            Globals.printToConsole("Not allowed to translate a word in this language.");
            return;
        }

        Globals.printToConsole("What is the Translation of the word?");
        String translation = Globals.readNextLine();

        Word theTranslatedWord = new Word(translation, languageOfTranslation);
        Word.ALL_WORDS.add(theTranslatedWord);
        wordToTranslate.ALL_TRANSLATIONS_OF_WORD.add(theTranslatedWord);
    }

    /**
     * Answer if the Translator is allowed to translate a word in the parameter language
     *
     * @param languageToTranslate the language the word should be translated to
     * @return if the translator is allowed to translate in that language
     */
    private boolean isAllowedToTranslateLanguage(Language languageToTranslate) {
        return LANGUAGES_TO_TRANSLATE.contains(languageToTranslate);
    }
}
