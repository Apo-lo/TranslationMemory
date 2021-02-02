package de.fleig.translationmemory.vocabulary;

import de.fleig.translationmemory.application.Globals;
import de.fleig.translationmemory.application.MainApplication;
import de.fleig.translationmemory.exception.LanguageNotFoundException;
import de.fleig.translationmemory.exception.WordNotFoundException;
import de.fleig.translationmemory.person.Administrator;

import java.util.ArrayList;
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
        Globals.printToConsole("What word would you like to create?");
        String word = Globals.inputScanner.nextLine();

        if (doesWordExists(word)) {
            wordAlreadyExistsOutput(word);
        } else {
            createWord(word);
        }
    }

    /**
     * Create a new word and add it to the all words array list
     */
    public static void createWord(String wordToCreate) {

        if (doesWordExists(wordToCreate)) {
            wordAlreadyExistsOutput(wordToCreate);
            return;
        }

        Globals.printToConsole("In which Language is the word?");
        String input = Globals.inputScanner.nextLine();

        if(Language.doesLanguageExist(input)) {
            try {
                Language languageOfWord = Language.getLanguage(input);
                Word theNewWord = new Word(wordToCreate, languageOfWord);
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

        Globals.printToConsole("What word would you like to search for");
        String input= Globals.inputScanner.nextLine();
        String word = input;
        if (Word.doesWordExists(input)) {
            try {
                Word.getWord(input).printWordWithTranslations();
            } catch (WordNotFoundException ignored) {
                //No need to do something with the exception, because the method getWord() will only be called if the word is present.
            }
        } else {
            Globals.printToConsole("Word does not exist yet, do you want to create it? (yes/no)");
            input = Globals.inputScanner.nextLine();
            if (input.equalsIgnoreCase("yes")) {
                Word.createWord(word);
            } else {
                Globals.printToConsole("Press enter to search for another word or typ \"-cancel\" to cancel");
                if (!Globals.inputScanner.nextLine().equals("-cancel")) {
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

    /**
     * Answer the percentage on how many languages the word is translated
     *
     * @return the parentage of the translations of the word compared to all languages
     */
    public int percentageTranslated() {
        return ALL_TRANSLATIONS_OF_WORD.size() / Language.ALL_LANGUAGES.size() * 100;
    }

    /**
     * Answer a list of all languages missing from word
     *
     * @return all languages the word is not translated in
     */
    public ArrayList<Language> missingTranslations() {
        ArrayList<Language> missingLanguages = new ArrayList<>();
        ArrayList<Language> eachLanguageOfTranslationOfWord = new ArrayList<>();

        for (Word eachTranslation : ALL_TRANSLATIONS_OF_WORD) {
            eachLanguageOfTranslationOfWord.add(eachTranslation.getLANGUAGE_OF_WORD());
        }

        eachLanguageOfTranslationOfWord.add(getLANGUAGE_OF_WORD());

        if (Language.ALL_LANGUAGES.size() == eachLanguageOfTranslationOfWord.size()) {
            return missingLanguages;
        }

        for (Language eachLanguage : Language.ALL_LANGUAGES) {
            if(!eachLanguageOfTranslationOfWord.contains(eachLanguage)) {
                missingLanguages.add(eachLanguage);
            }
        }
        return missingLanguages;
    }
}
