package fr.catcore.translatedlegacy.language;

import com.ibm.icu.text.Bidi;
import fr.catcore.translatedlegacy.mixin.TranslationStorageAccessor;
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

    protected void load(InputStream inputStream) throws IOException {
        this.load(null, inputStream);
    }

    protected void load(String modid, InputStream inputStream) throws IOException {
        this.load(modid, new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    protected void load(Reader reader) throws IOException {
        this.load(null, reader);
    }

    protected void load(String modid, Reader inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(inputStream)) {
            String fileStr = reader.lines().collect(Collectors.joining("\n"));

            for (String line : fileStr.split("\n")) {
                if (!line.contains("=")) continue;
                String[] parts = line.split("=");
                this.add(modid, parts[0], parts[1]);
            }
        }
    }

    protected void add(String key, String value) {
        this.add(null, key, value);
    }

    protected void add(String modid, String key, String value) {
        translations.put(key, value);

        // Station API compatibility
        if (modid != null && !modid.equals("minecraft") && (
                FabricLoader.getInstance().isModLoaded("station-localization-api-v0")
                || FabricLoader.getInstance().isModLoaded("stationloader")
        )) {
            String newKey = String.copyValueOf(key.toCharArray());

            String[] keyParts = newKey.split("\\.");
            if (keyParts.length > 1) {
                keyParts[1] = modid + ":" + keyParts[1];
            }

            newKey = String.join(".", keyParts);

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
