package fr.catcore.translatedlegacy.ornithe.common;

import net.ornithemc.osl.localization.api.L10n;

public class TranslationHelper {
    public static String get(String key, String fallbackKey) {
        if (L10n.has(key)) return L10n.get(key);

        return L10n.get(fallbackKey);
    }
}
