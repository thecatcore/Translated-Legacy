package fr.catcore.translatedlegacy.mixin;

import fr.catcore.translatedlegacy.language.LanguageManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TranslationStorage.class, priority = 800)
public class TranslationStorageMixin {

    @Inject(method = "translate(Ljava/lang/String;)Ljava/lang/String;", cancellable = true, at = @At("HEAD"))
    public void tl$translate(String key, CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(LanguageManager.CURRENT_LANGUAGE.translate(key));
    }

    @Inject(method = "translate(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", cancellable = true, at = @At("HEAD"))
    public void tl$translate(String key, Object[] args, CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(LanguageManager.CURRENT_LANGUAGE.translate(key, args));
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "method_995", cancellable = true, at = @At("HEAD"))
    public void tl$method_995(String key, CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(LanguageManager.CURRENT_LANGUAGE.translate(key));
    }
}
