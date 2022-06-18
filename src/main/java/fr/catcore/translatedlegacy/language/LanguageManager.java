package fr.catcore.translatedlegacy.language;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

public class LanguageManager {

    public static final String DEFAULT_LANGUAGE = "en_us";
    private static String CURRENT_LANGUAGE_CODE = DEFAULT_LANGUAGE;
    public static final Map<String, OldTranslationStorage> CODE_TO_STORAGE = new TreeMap<>();

    private static final Gson GSON = new GsonBuilder().create();

    public static OldTranslationStorage CURRENT_LANGUAGE = init();

    private static final List<LanguageSwitchCallback> CALLBACKS = new ArrayList<>();

    public static OldTranslationStorage init() {
        JsonObject jsonObject = GSON.fromJson(new InputStreamReader(LanguageManager.class.getResourceAsStream("/pack.mcmeta"), StandardCharsets.UTF_8), JsonObject.class).getAsJsonObject("language");

        for (String code : jsonObject.keySet()) {
            JsonObject languageEntry = jsonObject.getAsJsonObject(code);
            CODE_TO_STORAGE.put(code, new OldTranslationStorage(
                    languageEntry.get("name").getAsString(),
                    languageEntry.get("region").getAsString(),
                    code,
                    languageEntry.get("bidirectional").getAsBoolean()
            ));
        }

        loadConfig(true);

        loadLanguage(DEFAULT_LANGUAGE);
        loadLanguage(CURRENT_LANGUAGE_CODE);

        return CODE_TO_STORAGE.get(CURRENT_LANGUAGE_CODE);
    }

    private static void loadConfig(boolean loadFile) {
        Path configDir = FabricLoader.getInstance().getConfigDir();

        if (!configDir.toFile().exists()) configDir.toFile().mkdir();

        File configPath = configDir.resolve("translated-legacy.json").toFile();

        if (configPath.exists() && loadFile) {
            try {
                FileReader fileReader = new FileReader(configPath);
                JsonObject object = GSON.fromJson(fileReader, JsonObject.class);
                CURRENT_LANGUAGE_CODE = object.has("code") ? object.get("code").getAsString() : DEFAULT_LANGUAGE;
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                configPath.createNewFile();
                FileWriter fileWriter = new FileWriter(configPath);
                JsonObject object = new JsonObject();
                object.addProperty("code", CURRENT_LANGUAGE_CODE);
                fileWriter.write(GSON.toJson(object));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadLanguage(String code) {
        loadDefaultLangFiles(code);
        loadLangFiles(code);
    }

    private static void loadDefaultLangFiles(String code) {
        try {
            CODE_TO_STORAGE.get(code).load(LanguageManager.class.getResourceAsStream("/assets/minecraft/lang_default/" + code + ".lang"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadLangFiles(String code) {
        List<String> modidList = new ArrayList<>();
        FabricLoader.getInstance().getAllMods().forEach(modContainer -> {
            modidList.add(modContainer.getMetadata().getId());
        });

        for (String modId : modidList) {
            URL langFolder = LanguageManager.class.getResource("/assets/" + modId + "/lang/");

            if (langFolder != null) {
                InputStream langFile = LanguageManager.class.getResourceAsStream("/assets/" + modId + "/lang/" + code + ".lang");

                if (langFile != null) {
                    try {
                        CODE_TO_STORAGE.get(code).load(modId, langFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String upperCode = code.split("_")[0] + "_" + code.split("_")[1].toUpperCase(Locale.ROOT);

                InputStream langFile1 = LanguageManager.class.getResourceAsStream("/assets/" + modId + "/lang/" + upperCode + ".lang");

                if (langFile1 != null) {
                    try {
                        CODE_TO_STORAGE.get(code).load(modId, langFile1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                InputStream langFile2 = LanguageManager.class.getResourceAsStream("/assets/" + modId + "/stationloader/lang/" + code + ".lang");

                if (langFile2 != null) {
                    try {
                        CODE_TO_STORAGE.get(code).load(modId, langFile2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                InputStream langFile3 = LanguageManager.class.getResourceAsStream("/assets/" + modId + "/stationloader/lang/" + upperCode + ".lang");

                if (langFile3 != null) {
                    try {
                        CODE_TO_STORAGE.get(code).load(modId, langFile3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void loadFile(String code, InputStream fileStream) throws IOException {
        CODE_TO_STORAGE.get(code).load(fileStream);
    }

    public static void loadFile(String code, Reader fileStream) throws IOException {
        CODE_TO_STORAGE.get(code).load(fileStream);
    }

    public static void addTranslation(String code, String key, String value) {
        CODE_TO_STORAGE.get(code).add(key, value);
    }

    public static void switchLanguage(String newCode) {
        CURRENT_LANGUAGE.clear();
        CODE_TO_STORAGE.get(DEFAULT_LANGUAGE).clear();

        CURRENT_LANGUAGE_CODE = newCode;

        loadConfig(false);

        loadLanguage(DEFAULT_LANGUAGE);
        loadLanguage(CURRENT_LANGUAGE_CODE);

        CURRENT_LANGUAGE = CODE_TO_STORAGE.get(CURRENT_LANGUAGE_CODE);

        for (LanguageSwitchCallback languageSwitchCallback : CALLBACKS) {
            languageSwitchCallback.changed(CURRENT_LANGUAGE_CODE);
        }
    }

    public static void registerCallback(LanguageSwitchCallback callback) {
        CALLBACKS.add(callback);
    }

    public interface LanguageSwitchCallback {
        void changed(String code);
    }
}
