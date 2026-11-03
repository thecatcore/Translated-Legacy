package fr.catcore.translatedlegacy.babric.mixin.client.container;

import net.minecraft.client.gui.screen.ingame.DispenserScreen;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DispenserScreen.class)
public abstract class DispenserScreenMixin {

    @ModifyArg(
            method = "drawForeground",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;III)V",
                    ordinal = 0
            )
    )
    public String renderForeground$lang1(String string) {
        return I18n.getTranslation("container.dispenser");
    }

    @ModifyArg(
            method = "drawForeground",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;III)V",
                    ordinal = 1
            )
    )
    public String renderForeground$lang2(String string) {
        return I18n.getTranslation("container.inventory");
    }
}
