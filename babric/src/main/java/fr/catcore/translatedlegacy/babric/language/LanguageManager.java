package fr.catcore.translatedlegacy.babric.language;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.catcore.translatedlegacy.TranslatedLegacy;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resource.language.TranslationStorage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LanguageManager {

    public static final String DEFAULT_LANGUAGE = "en_us";
    private static String CURRENT_LANGUAGE_CODE = DEFAULT_LANGUAGE;
    public static final Map<String, OldTranslationStorage> CODE_TO_STORAGE = new TreeMap<>();

    protected static final Gson GSON = new GsonBuilder().create();

    public static OldTranslationStorage CURRENT_LANGUAGE = init();

    private static final List<LanguageSwitchCallback> CALLBACKS = new ArrayList<>();

    public static OldTranslationStorage init() {
        TranslatedLegacy.updateLanguageList();

        try {
            JsonObject jsonObject = GSON.fromJson(
                    new InputStreamReader(
                            Files.newInputStream(TranslatedLegacy.META_FILE.toPath()),
                            StandardCharsets.UTF_8),
                    JsonObject.class).getAsJsonObject("language");

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
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void loadConfig(boolean loadFile) {
        Path configDir = TranslatedLegacy.CONFIG_FOLDER.toPath();

        if (!configDir.toFile().exists()) configDir.toFile().mkdir();

        File configPath = configDir.resolve("config-b1.7.3.json").toFile();

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
            if ("en_us".equals(code)) {
                CODE_TO_STORAGE.get(code).load(TranslationStorage.class.getResourceAsStream("/lang/en_US.lang"), false);
                CODE_TO_STORAGE.get(code).load(TranslationStorage.class.getResourceAsStream("/lang/stats_US.lang"), false);
            } else {
                CODE_TO_STORAGE.get(code).load(Files.newInputStream(TranslatedLegacy.getLangFile("b1.7.3", code).toPath()), true);
            }
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
            boolean hasRegion = true;

            if (!code.contains("_")) hasRegion = false;

            String upperCode = hasRegion ? code.split("_")[0] + "_" + code.split("_")[1].toUpperCase(Locale.ENGLISH) : "dummy_uppercode";

            String[] possiblePaths = new String[]{
                    "/assets/" + modId + "/lang/" + code,
                    "/assets/" + modId + "/lang/" + upperCode,
                    "/assets/" + modId + "/stationloader/lang/" + code,
                    "/assets/" + modId + "/stationloader/lang/" + upperCode
            };

            for (String possiblePath : possiblePaths) {
                if (possiblePath.endsWith("dummy_uppercode")) continue;

                InputStream langFile = LanguageManager.class.getResourceAsStream(possiblePath + ".lang");

                if (langFile != null) {
                    try {
                        CODE_TO_STORAGE.get(code).load(modId, langFile, false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                InputStream jsonFile = LanguageManager.class.getResourceAsStream(possiblePath + ".json");

                if (jsonFile != null) {
                    try {
                        CODE_TO_STORAGE.get(code).load(modId, jsonFile, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void loadFile(String code, InputStream fileStream, boolean json) throws IOException {
        CODE_TO_STORAGE.get(code).load(fileStream, json);
    }

    public static void loadFile(String code, Reader fileStream, boolean json) throws IOException {
        CODE_TO_STORAGE.get(code).load(fileStream, json);
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
