package fr.catcore.translatedlegacy.babric.mixin.client;

import fr.catcore.translatedlegacy.babric.language.LanguageManager;
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

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    public void init$reloadLang(CallbackInfo ci) {
        LanguageManager.switchLanguage(LanguageManager.CURRENT_LANGUAGE.code);
    }

    @ModifyArg(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setWorld(Lnet/minecraft/world/World;Ljava/lang/String;)V"), index = 1)
    public String createOrLoadWorld$lang(String string) {
        return Objects.equals(string, "Generating level")
                ? I18n.getTranslation("menu.generatingLevel")
                : I18n.getTranslation("menu.loadingLevel");
    }

    @ModifyArg(method = "prepareWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/ProgressRenderer;progressStage(Ljava/lang/String;)V", ordinal = 0))
    public String method_2130$lang1(String string) {
        return I18n.getTranslation("menu.generatingTerrain");
    }

    @ModifyArg(method = "prepareWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/ProgressRenderer;progressStage(Ljava/lang/String;)V", ordinal = 1))
    public String method_2130$lang2(String string) {
        return I18n.getTranslation("menu.simulating");
    }

    @ModifyArg(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;prepareWorld(Ljava/lang/String;)V"))
    public String respawn$lang(String string) {
        return I18n.getTranslation("menu.respawning");
    }
}
