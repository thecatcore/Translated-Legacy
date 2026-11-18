package fr.catcore.translatedlegacy.babric;

import net.minecraft.client.resource.language.I18n;

import java.util.Objects;

public class TranslationHelper {
    public static String get(String key, String fallbackKey) {
        if (!Objects.equals(I18n.getTranslation(key), key)) return I18n.getTranslation(key);

        return I18n.getTranslation(fallbackKey);
    }
}
