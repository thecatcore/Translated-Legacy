package fr.catcore.translatedlegacy.babric.mixin.client.container;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {

    @ModifyArg(
            method = "drawForeground",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;III)V"
            )
    )
    public String renderForeground$lang(String string) {
        return I18n.getTranslation("container.crafting");
    }
}
