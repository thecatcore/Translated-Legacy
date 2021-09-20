package fr.catcore.translatedlegacy.mixin;

import fr.catcore.translatedlegacy.language.LanguageManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin {

    /**
     * @author Cat Core
     */
    @Overwrite
    public String translate(String key) {
        return LanguageManager.CURRENT_LANGUAGE.translate(key);
    }

    /**
     * @author Cat Core
     */
    @Overwrite
    public String translate(String key, Object... arg) {
        return LanguageManager.CURRENT_LANGUAGE.translate(key, arg);
    }

    /**
     * @author Cat Core
     */
    @Overwrite
    @Environment(EnvType.CLIENT)
    public String method_995(String string) {
        return LanguageManager.CURRENT_LANGUAGE.method_995(string);
    }
}
