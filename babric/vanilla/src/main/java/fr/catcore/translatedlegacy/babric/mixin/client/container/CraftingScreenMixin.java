package fr.catcore.translatedlegacy.babric.mixin.client.container;

import fr.catcore.translatedlegacy.babric.TranslationHelper;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CraftingScreen.class)
public abstract class CraftingScreenMixin {

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
        return TranslationHelper.get("backport.container.crafting", "container.crafting");
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
        return TranslationHelper.get("backport.container.inventory", "container.inventory");
    }
}
