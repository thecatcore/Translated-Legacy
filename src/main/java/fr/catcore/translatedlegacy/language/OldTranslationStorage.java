package fr.catcore.translatedlegacy.language;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OldTranslationStorage {

    private final Map<String, String> translations = new HashMap<>();
    public final String name;
    public final String region;
    public final String code;

    public OldTranslationStorage(String name, String region, String code) {
        this.name = name;
        this.region = region;
        this.code = code;
    }

    protected void load(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String fileStr = reader.lines().collect(Collectors.joining("\n"));

            for (String line : fileStr.split("\n")) {
                if (!line.contains("=")) continue;
                String[] parts = line.split("=");
                translations.put(parts[0], parts[1]);
            }
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
        return this.translations.getOrDefault(key, defaultValue);
    }

    public String translate(String key, Object... arg) {
        String defaultValue = isDefaultLanguage() ? key : LanguageManager.CODE_TO_STORAGE
                .get(LanguageManager.DEFAULT_LANGUAGE)
                .translate(key);
        String var3 = this.translations.getOrDefault(key, defaultValue);
        return String.format(var3, arg);
    }

    @Environment(EnvType.CLIENT)
    public String method_995(String string) {
        String defaultValue = isDefaultLanguage() ? "" : LanguageManager.CODE_TO_STORAGE
                .get(LanguageManager.DEFAULT_LANGUAGE)
                .method_995(string);
        return this.translations.getOrDefault(string + ".name", defaultValue);
    }
}
