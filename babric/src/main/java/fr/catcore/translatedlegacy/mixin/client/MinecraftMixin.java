package fr.catcore.translatedlegacy.mixin.client;

import fr.catcore.translatedlegacy.language.LanguageManager;
import net.minecraft.class_629;
import net.minecraft.client.Minecraft;
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

    @ModifyArg(method = "method_2120", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;method_2114(Lnet/minecraft/world/World;Ljava/lang/String;)V"), index = 1)
    public String createOrLoadWorld$lang(String string) {
        return Objects.equals(string, "Generating level")
                ? class_629.method_2049("menu.generatingLevel")
                : class_629.method_2049("menu.loadingLevel");
    }

    @ModifyArg(method = "method_2130", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_452;method_1796(Ljava/lang/String;)V", ordinal = 0))
    public String method_2130$lang1(String string) {
        return class_629.method_2049("menu.generatingTerrain");
    }

    @ModifyArg(method = "method_2130", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_452;method_1796(Ljava/lang/String;)V", ordinal = 1))
    public String method_2130$lang2(String string) {
        return class_629.method_2049("menu.simulating");
    }

    @ModifyArg(method = "method_2122", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;method_2130(Ljava/lang/String;)V"))
    public String respawn$lang(String string) {
        return class_629.method_2049("menu.respawning");
    }
}
