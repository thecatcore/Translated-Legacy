package fr.catcore.translatedlegacy.babric.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import fr.catcore.translatedlegacy.babric.language.LanguageManager;
import net.minecraft.client.option.GameOptions;
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
            LanguageManager.switchLanguage(strings[1]);
        }
    }

    @Inject(method = "save", at = @At(value = "INVOKE", target = "Ljava/io/PrintWriter;println(Ljava/lang/String;)V", ordinal = 0))
    private void tl$saveLanguage(CallbackInfo ci, @Local PrintWriter printWriter) {
        printWriter.println("lang:" + LanguageManager.CURRENT_LANGUAGE.code);
    }
}
