package fr.catcore.translatedlegacy.mixin;

import fr.catcore.translatedlegacy.language.LanguageManager;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(I18n.class)
public class I18nMixin {
    /**
     * @author Cat Core
     */
    @Overwrite
    public static String translate(String key) {
        return LanguageManager.CURRENT_LANGUAGE.translate(key);
    }

    /**
     * @author Cat Core
     */
    @Overwrite
    public static String translate(String key, Object... arg) {
        return LanguageManager.CURRENT_LANGUAGE.translate(key, arg);
    }
}
