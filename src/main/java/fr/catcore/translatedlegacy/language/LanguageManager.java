package fr.catcore.translatedlegacy.language;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class LanguageManager {

    public static final String DEFAULT_LANGUAGE = "en_us";
    public static final Map<String, OldTranslationStorage> CODE_TO_STORAGE = new TreeMap<>();

    private static final Gson GSON = new GsonBuilder().create();

    public static OldTranslationStorage CURRENT_LANGUAGE = init();

    private static final List<LanguageSwitchCallback> CALLBACKS = new ArrayList<>();

    public static OldTranslationStorage init() {
        JsonObject jsonObject = GSON.fromJson(new InputStreamReader(LanguageManager.class.getResourceAsStream("/pack.mcmeta")), JsonObject.class).getAsJsonObject("language");

        for (String code : jsonObject.keySet()) {
            JsonObject languageEntry = jsonObject.getAsJsonObject(code);
            CODE_TO_STORAGE.put(code, new OldTranslationStorage(languageEntry.get("name").getAsString(), languageEntry.get("region").getAsString()));
            try {
                CODE_TO_STORAGE.get(code).load(LanguageManager.class.getResourceAsStream("/assets/minecraft/lang_default/" + code + ".lang"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return CODE_TO_STORAGE.get(DEFAULT_LANGUAGE);
    }

    public static void switchLanguage(OldTranslationStorage newL) {
        CURRENT_LANGUAGE = newL;
        for (LanguageSwitchCallback languageSwitchCallback : CALLBACKS) {
            languageSwitchCallback.changed(newL.name);
        }
    }

    public static void registerCallback(LanguageSwitchCallback callback) {
        CALLBACKS.add(callback);
    }

    public interface LanguageSwitchCallback {
        void changed(String code);
    }
}
