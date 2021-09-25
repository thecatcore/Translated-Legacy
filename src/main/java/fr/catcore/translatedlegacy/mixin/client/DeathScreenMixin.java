package fr.catcore.translatedlegacy.mixin.client;

import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widgets/Button;<init>(IIILjava/lang/String;)V", ordinal = 0))
    public String init$lang1(String string) {
        return I18n.translate("deathScreen.respawn");
    }

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widgets/Button;<init>(IIILjava/lang/String;)V", ordinal = 1))
    public String init$lang2(String string) {
        return I18n.translate("deathScreen.titleScreen");
    }

    @ModifyArg(method = "render", index = 1, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/DeathScreen;drawTextWithShadowCentred(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V",
            ordinal = 0
    ))
    public String render$lang1(String string) {
        return I18n.translate("deathScreen.title");
    }

    @ModifyArg(method = "render", index = 1, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/DeathScreen;drawTextWithShadowCentred(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V",
            ordinal = 0
    ))
    public String render$lang2(String string) {
        return I18n.translate("deathScreen.score") + ": §e" + string.replace("Score: &e", "");
    }
}
