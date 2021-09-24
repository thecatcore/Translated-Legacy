package fr.catcore.translatedlegacy.mixin.compat;

import fr.catcore.translatedlegacy.language.LanguageManager;
import io.github.minecraftcursedlegacy.api.registry.Translations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Objects;

@Mixin(Translations.class)
public class CursedLegacyAPITranslationsMixin {

    /**
     * @author Cat Core
     */
    @Overwrite(remap = false)
    public static void addTranslation(String key, String translation) {
        LanguageManager.registerCallback(code -> {
            if (Objects.equals(code, LanguageManager.DEFAULT_LANGUAGE)) LanguageManager.addTranslation(LanguageManager.DEFAULT_LANGUAGE, key, translation);
        });
    }

    /**
     * @author Cat Core
     */
    @Overwrite(remap = false)
    public static void loadLangFile(String file) throws IOException {
        LanguageManager.registerCallback(code -> {
            if (Objects.equals(code, LanguageManager.DEFAULT_LANGUAGE)) {
                try (InputStream in = Translations.class.getResourceAsStream(file)) {
                    LanguageManager.loadFile(LanguageManager.DEFAULT_LANGUAGE, in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @author Cat Core
     */
    @Overwrite(remap = false)
    public static void loadLangFile(Reader reader) throws IOException {
        LanguageManager.registerCallback(code -> {
            if (Objects.equals(code, LanguageManager.DEFAULT_LANGUAGE)) {
                try {
                    LanguageManager.loadFile(LanguageManager.DEFAULT_LANGUAGE, reader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
