package fr.catcore.translatedlegacy.mixin.client.container;

import net.minecraft.client.gui.screen.container.CraftingScreen;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CraftingScreen.class)
public abstract class CraftingScreenMixin {

    @ModifyArg(
            method = "renderForeground",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/TextRenderer;drawText(Ljava/lang/String;III)V",
                    ordinal = 0
            )
    )
    public String renderForeground$lang1(String string) {
        return I18n.translate("container.crafting");
    }

    @ModifyArg(
            method = "renderForeground",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/TextRenderer;drawText(Ljava/lang/String;III)V",
                    ordinal = 1
            )
    )
    public String renderForeground$lang2(String string) {
        return I18n.translate("container.inventory");
    }
}
