package de.fleig.translationmemory.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.fleig.translationmemory.application.Globals;
import de.fleig.translationmemory.person.Administrator;
import de.fleig.translationmemory.person.AuthorizedUser;
import de.fleig.translationmemory.person.Translator;
import de.fleig.translationmemory.person.User;
import de.fleig.translationmemory.vocabulary.Language;
import de.fleig.translationmemory.vocabulary.Word;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslationDatabase {
    private static TranslationDatabase instance;

    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public ArrayList<Word> allWords = new ArrayList<>();
    public ArrayList<Language> allLanguages = new ArrayList<>();
    public HashMap<String, Translator> allTranslators = new HashMap<>();
    public HashMap<String, Administrator> allAdministrators = new HashMap<>();
    public HashMap<String, User>allNormalUsers = new HashMap<>();

    public final String WORDS_FILE_PATH = "words.json";
    public final String LANGUAGE_FILE_PATH = "languages.json";
    public final String TRANSLATORS_FILE_PATH = "translators.json";
    public final String ADMINISTRATORS_FILE_PATH = "administrators.json";
    public final String NORMAL_USER_FILE_PATH = "normalUsers.json";


    /**
     * Constructor for TranslationDatabase empty because its a Singleton
     */
    private TranslationDatabase() {}

    /**
     * Get Instance for Singleton pattern
     *
     * @return an instance of TranslationDatabase
     */
    public static TranslationDatabase getInstance () {
        if (TranslationDatabase.instance == null) {
            TranslationDatabase.instance = new TranslationDatabase ();
        }
        return TranslationDatabase.instance;
    }

    /**
     * Set the lists that should be saved
     *  @param words list of all words
     * @param languages list of all languages
     * @param authorizedUsers list of all authorized users
     * @param normalUsers list of all normal users
     */
    public void setListsToSave(ArrayList<Word> words, ArrayList<Language> languages, HashMap<String, AuthorizedUser> authorizedUsers, HashMap<String, User> normalUsers) {
        allWords = words;
        allLanguages = languages;
        allNormalUsers = normalUsers;

        for (Map.Entry<String, AuthorizedUser> eachAuthorizedUser: authorizedUsers.entrySet()) {
            if(eachAuthorizedUser.getValue() instanceof Administrator) {
                allAdministrators.put(eachAuthorizedUser.getKey(), (Administrator) eachAuthorizedUser.getValue());
            } else {
                allTranslators.put(eachAuthorizedUser.getKey(), (Translator) eachAuthorizedUser.getValue());
            }
        }
    }

    /**
     * Save all data into four files
     */
    public void saveDataToFile() {
        try {
            FileWriter fileWriter = new FileWriter(WORDS_FILE_PATH);
            GSON.toJson(allWords, fileWriter);
            fileWriter.close();

            fileWriter = new FileWriter(LANGUAGE_FILE_PATH);
            GSON.toJson(allLanguages, fileWriter);
            fileWriter.close();

            fileWriter = new FileWriter(ADMINISTRATORS_FILE_PATH);
            GSON.toJson(allAdministrators, fileWriter);
            fileWriter.close();

            fileWriter = new FileWriter(TRANSLATORS_FILE_PATH);
            GSON.toJson(allTranslators, fileWriter);
            fileWriter.close();

            fileWriter = new FileWriter(NORMAL_USER_FILE_PATH);
            GSON.toJson(allNormalUsers, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            Globals.printToConsole("Failed to save!");
            Globals.printToConsole(e.getMessage());
        }
    }

    /**
     * Load all data from files
     */
    public void loadDataFromFile() {
        try { //https://attacomsian.com/blog/gson-read-json-file
            Reader reader = Files.newBufferedReader(Paths.get(WORDS_FILE_PATH));
            Word.ALL_WORDS.addAll(GSON.fromJson(reader, new TypeToken<List<Word>>() {}.getType()));
            reader.close();

            reader = Files.newBufferedReader(Paths.get(LANGUAGE_FILE_PATH));
            Language.ALL_LANGUAGES.addAll(GSON.fromJson(reader, new TypeToken<List<Language>>() {}.getType()));
            reader.close();

            reader = Files.newBufferedReader(Paths.get(ADMINISTRATORS_FILE_PATH));
            AuthorizedUser.REGISTERED_AUTHORIZED_USERS.putAll(GSON.fromJson(reader, new TypeToken<HashMap<String, Administrator>>() {}.getType()));
            reader.close();

            reader = Files.newBufferedReader(Paths.get(TRANSLATORS_FILE_PATH));
            AuthorizedUser.REGISTERED_AUTHORIZED_USERS.putAll(GSON.fromJson(reader, new TypeToken<HashMap<String, Translator>>() {}.getType()));
            reader.close();

            reader = Files.newBufferedReader(Paths.get(NORMAL_USER_FILE_PATH));
            User.REGISTERED_NORMAL_USERS.putAll(GSON.fromJson(reader, new TypeToken<HashMap<String, User>>() {}.getType()));
            reader.close();

        } catch (Exception e) {
            Globals.printToConsole("No data found, files will be created after first shut down.");
        }
    }

}
