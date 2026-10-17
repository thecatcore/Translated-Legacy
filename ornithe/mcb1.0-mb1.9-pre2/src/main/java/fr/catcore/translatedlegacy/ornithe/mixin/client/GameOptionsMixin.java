package fr.catcore.translatedlegacy.ornithe.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.options.GameOptions;
import net.ornithemc.osl.localization.api.L10n;
import net.ornithemc.osl.localization.impl.Localization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.PrintWriter;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Inject(method = "load", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z", ordinal = 0))
    private void tl$loadLanguage(CallbackInfo ci, @Local String[] strings) {
        if (strings[0].equals("lang")) {
            Localization.getLanguageManager().setSelectedLanguage(strings[1]);
            Localization.reloadLocale();
        }
    }

    @Inject(method = "save", at = @At(value = "INVOKE", target = "Ljava/io/PrintWriter;println(Ljava/lang/String;)V", ordinal = 0))
    private void tl$saveLanguage(CallbackInfo ci, @Local PrintWriter printWriter) {
        printWriter.println("lang:" + L10n.getLanguage().code());
    }
}
