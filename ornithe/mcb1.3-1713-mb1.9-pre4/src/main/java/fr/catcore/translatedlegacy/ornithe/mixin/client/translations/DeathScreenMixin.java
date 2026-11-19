package fr.catcore.translatedlegacy.ornithe.mixin.client.translations;

import net.minecraft.client.gui.screen.game.DeathScreen;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Conditional(minecraftVersion = @Version("<b1.9-pre2"))
@Mixin(DeathScreen.class)
public class DeathScreenMixin {

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V", ordinal = 0))
    public String init$lang1(String string) {
        return L10n.get("backport.deathScreen.respawn");
    }

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V", ordinal = 1))
    public String init$lang2(String string) {
        return L10n.get("deathScreen.titleMenu");
    }

    @ModifyArg(method = "render", index = 1, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/game/DeathScreen;drawCenteredString(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V",
            ordinal = 0
    ))
    public String render$lang1(String string) {
        return L10n.get("backport.deathScreen.title");
    }

    @ModifyArg(method = "render", index = 1, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/game/DeathScreen;drawCenteredString(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V",
            ordinal = 1
    ))
    public String render$lang2(String string) {
        return  string.replace("Score", L10n.get("backport.deathScreen.score")).replace("&e", "§e");
    }
}
