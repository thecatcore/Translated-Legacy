package fr.catcore.translatedlegacy.babric.language;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.icu.text.Bidi;
import fr.catcore.translatedlegacy.babric.mixin.TranslationStorageAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resource.language.TranslationStorage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OldTranslationStorage {

    private final Map<String, String> translations = new HashMap<>();
    public final String name;
    public final String region;
    public final String code;
    public final boolean rightToLeft;

    private static boolean checkedVanilla = false;

    public OldTranslationStorage(String name, String region, String code, boolean rightToLeft) {
        this.name = name;
        this.region = region;
        this.code = code;
        this.rightToLeft = rightToLeft;
    }

    protected void load(InputStream inputStream, boolean json) throws IOException {
        this.load(null, inputStream, json);
    }

    protected void load(String modid, InputStream inputStream, boolean json) throws IOException {
        this.load(modid, new InputStreamReader(inputStream, StandardCharsets.UTF_8), json);
    }

    protected void load(Reader reader, boolean json) throws IOException {
        this.load(null, reader, json);
    }

    protected void load(String modid, Reader inputStream, boolean json) throws IOException {
        if (json) {
            JsonObject object = LanguageManager.GSON.fromJson(inputStream, JsonObject.class);

            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().getAsString();

                this.add(modid, key, value);
            }
        } else {
            try (BufferedReader reader = new BufferedReader(inputStream)) {
                String fileStr = reader.lines().collect(Collectors.joining("\n"));

                for (String line : fileStr.split("\n")) {
                    if (!line.contains("=") || line.startsWith("#")) continue;
                    String[] parts = line.split("=");
                    if (parts.length < 2) continue;
                    this.add(modid, parts[0], parts[1]);
                }
            }
        }
    }

    protected void add(String key, String value) {
        this.add(null, key, value);
    }

    protected void add(String modid, String key, String value) {
        key = key.replace("\\:", ":");
        translations.put(key, value);

        // Station API compatibility
        if (modid != null && !modid.equals("minecraft") && (
                FabricLoader.getInstance().isModLoaded("station-localization-api-v0")
                || FabricLoader.getInstance().isModLoaded("stationloader")
        )) {
            String newKey = String.copyValueOf(key.toCharArray());

            String[] strings = newKey.split("\\.");
            if (strings.length > 1)
                strings[1] = modid + ":" + strings[1];
            newKey = String.join(".", strings);

            translations.put(newKey, value);
        }
    }

    protected void clear() {
        this.translations.clear();
    }

    private boolean isDefaultLanguage() {
        return this == LanguageManager.CODE_TO_STORAGE.get(LanguageManager.DEFAULT_LANGUAGE);
    }

    public String translate(String key) {
        String defaultValue = isDefaultLanguage() ? key : LanguageManager.CODE_TO_STORAGE
                .get(LanguageManager.DEFAULT_LANGUAGE)
                .translate(key);
        return this.getOrDefault(key, defaultValue);
    }

    public String translate(String key, Object... arg) {
        String defaultValue = isDefaultLanguage() ? key : LanguageManager.CODE_TO_STORAGE
                .get(LanguageManager.DEFAULT_LANGUAGE)
                .translate(key);
        String var3 = this.getOrDefault(key, defaultValue);
        return String.format(var3, arg);
    }

    @Environment(EnvType.CLIENT)
    public String method_995(String string) {
        String defaultValue = isDefaultLanguage() ? "" : LanguageManager.CODE_TO_STORAGE
                .get(LanguageManager.DEFAULT_LANGUAGE)
                .method_995(string);
        return this.getOrDefault(string + ".name", defaultValue);
    }

    private String getOrDefault(String key, String defaultValue) {
        if (checkedVanilla) {
            String translation = this.translations.getOrDefault(key, defaultValue);
            if (this.rightToLeft && this.hasKey(key)) {
                Bidi bidi = new Bidi(translation, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);
                translation = bidi.getTextAsString();
            }
            return translation;
        } else if (key.equals(defaultValue) || defaultValue.isEmpty()){
            ((TranslationStorageAccessor)TranslationStorage.getInstance()).getTranslations().forEach((vKey, value) -> {
                if (!LanguageManager.CODE_TO_STORAGE
                        .get(LanguageManager.DEFAULT_LANGUAGE).hasKey((String) vKey)) {
                    LanguageManager.CODE_TO_STORAGE
                            .get(LanguageManager.DEFAULT_LANGUAGE).add((String) vKey, (String) value);
                }
            });
            checkedVanilla = true;
            return this.getOrDefault(key, isDefaultLanguage() ? "" : LanguageManager.CODE_TO_STORAGE
                    .get(LanguageManager.DEFAULT_LANGUAGE)
                    .method_995(key));
        }
        return defaultValue;
    }

    private boolean hasKey(String key) {
        return this.translations.containsKey(key);
    }
}
