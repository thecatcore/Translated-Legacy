package fr.catcore.translatedlegacy.ornithe.common.mixin;

import fr.catcore.translatedlegacy.TranslatedLegacy;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.localization.api.language.Language;
import net.ornithemc.osl.localization.impl.language.LanguageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;
import java.util.Objects;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {
    @Inject(method = "setSelectedLanguage(Ljava/lang/String;)V", at = @At("RETURN"))
    private void tl$fetchLangFileString(String language, CallbackInfo ci) {
        if (!Objects.equals(language, "en_us") && !Objects.equals(language, "en_US")) {
            TranslatedLegacy.getLangFile(MinecraftVersion.resolve().gameVersion(), language.toLowerCase(Locale.ENGLISH));
        }
    }

    @Inject(method = "setSelectedLanguage(Lnet/ornithemc/osl/localization/api/language/Language;)V", at = @At("RETURN"))
    private void tl$fetchLangFileLanguage(Language language, CallbackInfo ci) {
        if (!Objects.equals(language.code(), "en_us") && !Objects.equals(language.code(), "en_US")) {
            TranslatedLegacy.getLangFile(MinecraftVersion.resolve().gameVersion(), language.code().toLowerCase(Locale.ENGLISH));
        }
    }
}
