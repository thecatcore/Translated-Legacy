package fr.catcore.translatedlegacy.stapi.mixin;

import fr.catcore.translatedlegacy.babric.mixin.client.GameOptionsAccessor;
import fr.catcore.translatedlegacy.stapi.TranslatedLegacyStAPI;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextRenderer.class)
public class TextRendererMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void c$init(GameOptions arg, String string, TextureManager arg1, CallbackInfo ci) {
        TranslatedLegacyStAPI.setGameProvider(((GameOptionsAccessor)arg).getMinecraft());
    }
}
