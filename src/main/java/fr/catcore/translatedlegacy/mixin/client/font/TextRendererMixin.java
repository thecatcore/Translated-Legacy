package fr.catcore.translatedlegacy.mixin.client.font;

import fr.catcore.translatedlegacy.ExampleMod;
import fr.catcore.translatedlegacy.font.BetterTextRenderer;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.TextRenderer;
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
        ExampleMod.TEXT_RENDERER = new BetterTextRenderer(arg, arg1);
    }

    /**
     * @author CatCore
     */
    @Overwrite
    public void drawTextWithShadow(String string, int i, int j, int k) {
        ExampleMod.TEXT_RENDERER.drawTextWithShadow(string, i, j, k);
    }

    /**
     * @author CatCore
     */
    @Overwrite
    public void drawText(String string, int i, int j, int k) {
        ExampleMod.TEXT_RENDERER.drawText(string, i, j, k);
    }

    /**
     * @author CatCore
     */
    @Overwrite
    public void drawText(String string, int i, int j, int k, boolean flag) {
        ExampleMod.TEXT_RENDERER.drawText(string, i, j, k, flag);
    }

    /**
     * @author CatCore
     */
    @Overwrite
    public int getTextWidth(String string) {
        return ExampleMod.TEXT_RENDERER.getTextWidth(string);
    }

    /**
     * @author CatCore
     */
    @Overwrite
    public void method_1904(String string, int i, int j, int k, int i1) {
        ExampleMod.TEXT_RENDERER.drawText(string, i, j, k, i1);
    }

    /**
     * @author CatCore
     */
    @Overwrite
    public int method_1902(String string, int i) {
        return ExampleMod.TEXT_RENDERER.getLineWidth(string, i);
    }
}
