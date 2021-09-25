package fr.catcore.translatedlegacy.mixin.client.container;

import net.minecraft.client.gui.screen.container.PlayerInventoryScreen;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerInventoryScreen.class)
public abstract class PlayerInventoryScreenMixin {

    @ModifyArg(
            method = "renderForeground",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/TextRenderer;drawText(Ljava/lang/String;III)V"
            )
    )
    public String renderForeground$lang(String string) {
        return I18n.translate("container.crafting");
    }
}
