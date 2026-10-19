package fr.catcore.translatedlegacy.ornithe.common.mixin;

import fr.catcore.translatedlegacy.TranslatedLegacy;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.localization.impl.Locale;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Locale.class)
public class LocaleMixin {
    @Inject(method = "loadLanguage", at = @At("HEAD"))
    private void tl$preLoadLanguageFile(ResourceManager resourceManager, String language, CallbackInfo ci) {
        if (language != null && !Objects.equals(language, "en_us") && !Objects.equals(language, "en_US")) {
            TranslatedLegacy.getLangFile(MinecraftVersion.resolve().gameVersion(), language.toLowerCase(java.util.Locale.ENGLISH));
        }
    }
}
