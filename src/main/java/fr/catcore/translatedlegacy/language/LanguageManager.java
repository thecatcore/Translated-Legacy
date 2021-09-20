package fr.catcore.translatedlegacy.language;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    private static final String DEFAULT_LANGUAGE = "en_us";
    private static final Map<String, OldTranslationStorage> CODE_TO_STORAGE = new HashMap<>();

    private static final Gson GSON = new GsonBuilder().create();

    public static OldTranslationStorage CURRENT_LANGUAGE = init();

    public static OldTranslationStorage init() {
        JsonObject jsonObject = GSON.fromJson(new InputStreamReader(LanguageManager.class.getResourceAsStream("/pack.mcmeta")), JsonObject.class).getAsJsonObject("language");

        for (String code : jsonObject.keySet()) {
            JsonObject languageEntry = jsonObject.getAsJsonObject(code);
            CODE_TO_STORAGE.put(code, new OldTranslationStorage(languageEntry.get("name").getAsString(), languageEntry.get("region").getAsString()));
            try {
                CODE_TO_STORAGE.get(code).load(LanguageManager.class.getResourceAsStream("/assets/modid/lang_default/" + code + ".lang"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return CODE_TO_STORAGE.get(DEFAULT_LANGUAGE);
    }
}
