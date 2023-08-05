package fr.catcore.translatedlegacy.mixin;

import fr.catcore.translatedlegacy.language.LanguageManager;
import net.minecraft.class_629;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(class_629.class)
public class I18nMixin {
    /**
     * @author Cat Core
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public static String method_2049(String key) {
        return key.startsWith("stat.") ? key.replace("stat.", "") : LanguageManager.CURRENT_LANGUAGE.translate(key);
    }

    /**
     * @author Cat Core
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public static String method_2050(String key, Object... objects) {
        return LanguageManager.CURRENT_LANGUAGE.translate(key, objects);
    }
}
