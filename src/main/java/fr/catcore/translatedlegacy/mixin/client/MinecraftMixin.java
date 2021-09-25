package fr.catcore.translatedlegacy.mixin.client;

import fr.catcore.translatedlegacy.language.LanguageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;openScreen(Lnet/minecraft/client/gui/Screen;)V"))
    public void init$reloadLang(CallbackInfo ci) {
        LanguageManager.switchLanguage(LanguageManager.CURRENT_LANGUAGE.code);
    }

    @ModifyArg(method = "createOrLoadWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;notifyStatus(Lnet/minecraft/level/Level;Ljava/lang/String;)V"), index = 1)
    public String createOrLoadWorld$lang(String string) {
        return Objects.equals(string, "Generating level")
                ? I18n.translate("menu.generatingLevel")
                : I18n.translate("menu.loadingLevel");
    }

    @ModifyArg(method = "method_2130", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ProgressListenerImpl;notifySubMessage(Ljava/lang/String;)V", ordinal = 0))
    public String method_2130$lang1(String string) {
        return I18n.translate("menu.generatingTerrain");
    }

    @ModifyArg(method = "method_2130", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ProgressListenerImpl;notifySubMessage(Ljava/lang/String;)V", ordinal = 1))
    public String method_2130$lang2(String string) {
        return I18n.translate("menu.simulating");
    }

    @ModifyArg(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;method_2130(Ljava/lang/String;)V"))
    public String respawn$lang(String string) {
        return I18n.translate("menu.respawning");
    }
}
