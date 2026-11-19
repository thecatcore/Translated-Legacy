package fr.catcore.translatedlegacy.ornithe.common.mixin.client.translations;

import net.minecraft.client.Minecraft;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Conditional(minecraftVersion = @Version("<1.2"))
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Conditional(minecraftVersion = @Version(">=b1.8-pre2 <11w49a"))
    @ModifyArg(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/ProgressRenderer;progressStart(Ljava/lang/String;)V", ordinal = 0), index = 0)
    public String switchingLevel$lang(String string) {
        return L10n.get("backport.menu.switchingLevel");
    }

    @Conditional(minecraftVersion = @Version("<11w49a"))
    @ModifyArg(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setWorld(Lnet/minecraft/world/World;Ljava/lang/String;)V", ordinal = 0), index = 1)
    public String createWorld$lang(String string) {
        return L10n.get("backport.menu.generatingLevel");
    }

    @Conditional(minecraftVersion = @Version("<11w49a"))
    @ModifyArg(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setWorld(Lnet/minecraft/world/World;Ljava/lang/String;)V", ordinal = 1), index = 1)
    public String loadWorld$lang(String string) {
        return L10n.get("backport.menu.loadingLevel");
    }

    @Conditional(minecraftVersion = @Version("<11w49a"))
    @ModifyArg(method = "prepareWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/ProgressRenderer;progressStage(Ljava/lang/String;)V", ordinal = 0))
    public String method_2130$lang1(String string) {
        return L10n.get("backport.menu.generatingTerrain");
    }

    @Conditional(minecraftVersion = @Version("<11w49a"))
    @ModifyArg(method = "prepareWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/ProgressRenderer;progressStage(Ljava/lang/String;)V", ordinal = 1))
    public String method_2130$lang2(String string) {
        return L10n.get("backport.menu.simulating");
    }

    @Conditional(minecraftVersion = @Version("<11w49a"))
    @ModifyArg(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;prepareWorld(Ljava/lang/String;)V"))
    public String respawn$lang(String string) {
        return L10n.get("backport.menu.respawning");
    }
}
