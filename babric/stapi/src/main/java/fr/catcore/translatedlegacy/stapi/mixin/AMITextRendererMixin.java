package fr.catcore.translatedlegacy.stapi.mixin;

import fr.catcore.translatedlegacy.font.TextRenderer;
import net.glasslauncher.mods.alwaysmoreitems.gui.AMITextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(value = AMITextRenderer.class, remap = false)
public class AMITextRendererMixin {
    @Inject(method = "renderStringAtPos", at = @At("HEAD"), remap = false, cancellable = true)
    private void overwriteAMITextRenderer(String text, int posX, int posY, Color color, boolean shadow, CallbackInfo ci) {
        TextRenderer.draw(text, posX, posY, color.getRGB(), shadow);
        ci.cancel();
    }
}
