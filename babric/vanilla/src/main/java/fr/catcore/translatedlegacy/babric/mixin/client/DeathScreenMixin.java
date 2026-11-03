package fr.catcore.translatedlegacy.babric.mixin.client;

import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V", ordinal = 0))
    public String init$lang1(String string) {
        return I18n.getTranslation("deathScreen.respawn");
    }

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V", ordinal = 1))
    public String init$lang2(String string) {
        return I18n.getTranslation("deathScreen.titleScreen");
    }

    @ModifyArg(method = "render", index = 1, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/DeathScreen;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
            ordinal = 0
    ))
    public String render$lang1(String string) {
        return I18n.getTranslation("deathScreen.title");
    }

    @ModifyArg(method = "render", index = 1, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/DeathScreen;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
            ordinal = 1
    ))
    public String render$lang2(String string) {
        return I18n.getTranslation("deathScreen.score") + ": §e" + string.replace("Score: &e", "");
    }
}
