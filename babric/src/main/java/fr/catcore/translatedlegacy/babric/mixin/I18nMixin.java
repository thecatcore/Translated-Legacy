package fr.catcore.translatedlegacy.babric.mixin;

import fr.catcore.translatedlegacy.babric.language.LanguageManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(I18n.class)
public class I18nMixin {
    @Unique
    private static boolean skipStatFix;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void init(CallbackInfo ci) {
        skipStatFix = FabricLoader.getInstance().isModLoaded("stationapi");
    }

    /**
     * @author Cat Core
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public static String getTranslation(String key) {
        return !skipStatFix && key.startsWith("stat.") ? key.replace("stat.", "") : LanguageManager.CURRENT_LANGUAGE.translate(key);
    }

    /**
     * @author Cat Core
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public static String getTranslation(String key, Object... objects) {
        return LanguageManager.CURRENT_LANGUAGE.translate(key, objects);
    }
}
