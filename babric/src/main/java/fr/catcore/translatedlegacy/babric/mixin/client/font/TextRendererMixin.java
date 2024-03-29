package fr.catcore.translatedlegacy.babric.mixin.client.font;

import fr.catcore.translatedlegacy.babric.TranslatedLegacyBabric;
import fr.catcore.translatedlegacy.babric.font.BetterTextRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextRenderer.class)
public class TextRendererMixin {


    @Inject(method = "<init>", at = @At("RETURN"))
    public void c$init(GameOptions arg, String string, TextureManager arg1, CallbackInfo ci) {
        TranslatedLegacyBabric.TEXT_RENDERER = new BetterTextRenderer(arg, arg1);
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public void drawWithShadow(String string, int i, int j, int k) {
        TranslatedLegacyBabric.TEXT_RENDERER.drawTextWithShadow(string, i, j, k);
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public void draw(String string, int i, int j, int k) {
        TranslatedLegacyBabric.TEXT_RENDERER.drawText(string, i, j, k);
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public void draw(String string, int i, int j, int k, boolean flag) {
        TranslatedLegacyBabric.TEXT_RENDERER.drawText(string, i, j, k, flag);
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public int getWidth(String string) {
        return TranslatedLegacyBabric.TEXT_RENDERER.getTextWidth(string);
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public void method_1904(String string, int i, int j, int k, int i1) {
        TranslatedLegacyBabric.TEXT_RENDERER.drawText(string, i, j, k, i1);
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public int method_1902(String string, int i) {
        return TranslatedLegacyBabric.TEXT_RENDERER.getLineWidth(string, i);
    }
}
