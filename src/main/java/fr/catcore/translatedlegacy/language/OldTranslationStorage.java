package fr.catcore.translatedlegacy.language;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class OldTranslationStorage {

//    private final Properties translations = new Properties();
    private final Map<String, String> translations = new HashMap<>();
    public final String name;
    public final String region;

    public OldTranslationStorage(String name, String region) {
        this.name = name;
        this.region = region;
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

    public String translate(String key) {
        return this.translations.getOrDefault(key, key);
    }

    public String translate(String key, Object... arg) {
        String var3 = this.translations.getOrDefault(key, key);
        return String.format(var3, arg);
    }

    @Environment(EnvType.CLIENT)
    public String method_995(String string) {
        return this.translations.getOrDefault(string + ".name", "");
    }
}
