package fr.catcore.translatedlegacy.ornithe.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import fr.catcore.translatedlegacy.util.TextUtils;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(TextRenderer.class)
public abstract class TextRendererMixin {
    @Shadow
    public abstract int getWidth(String text);

    @Unique
    private String texturePath;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void tl$init(GameOptions options, String fontPath, TextureManager textureManager, CallbackInfo ci) {
        this.texturePath = fontPath;
    }

    private boolean overwrite() {
        return this.texturePath == null || Objects.equals(texturePath, "/font/default.png");
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @WrapMethod(method = "drawLayer")
    public void tl$drawLayer(String text, int x, int y, int color, boolean shadow, Operation<Void> original) {
        if (overwrite()) {
            fr.catcore.translatedlegacy.font.TextRenderer.draw(text, x, y, color, shadow);
            return;
        }

        original.call(text, x, y, color, shadow);
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @WrapMethod(method = "getWidth")
    public int tl$getWidth(String text, Operation<Integer> original) {
        if (overwrite()) {
            return fr.catcore.translatedlegacy.font.TextRenderer.getTextWidth(text);
        }

        return original.call(text);
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run. Fix multiline draw height,
     * wrap at proper line break opportunities (Thai has no spaces between words) and never
     * split a letter from its combining marks.
     */
    @WrapMethod(method = "splitAndDraw(Ljava/lang/String;IIIIZ)V")
    public void tl$splitAndDraw(String text, int x, int y, int width, int color, boolean shadow, Operation<Void> original) {
        if (!this.overwrite()) {
            original.call(text, x, y, width, color, shadow);
            return;
        }

        for (String paragraph : text.split("\n")) {
            int paragraphHeight = 0;

            for (String line : TextUtils.wrapParagraph(paragraph, width, this::getWidth)) {
                if (line.trim().isEmpty()) continue;

                int lineHeight = fr.catcore.translatedlegacy.font.TextRenderer.draw(line, x, y, color, shadow);
                y += lineHeight;
                paragraphHeight += lineHeight;
            }

            if (paragraphHeight < 8) {
                y += fr.catcore.translatedlegacy.font.TextRenderer.getTextHeight(paragraph);
            }
        }
    }

    /**
     * @author CatCore
     */
    @WrapMethod(method = "splitAndGetHeight")
    public int tl$splitAndGetHeight(String text, int width, Operation<Integer> original) {
        if (!this.overwrite()) {
            return original.call(text, width);
        }

        int height = 0;

        for (String paragraph : text.split("\n")) {
            int paragraphHeight = 0;

            for (String line : TextUtils.wrapParagraph(paragraph, width, this::getWidth)) {
                if (line.trim().isEmpty()) continue;

                paragraphHeight += fr.catcore.translatedlegacy.font.TextRenderer.getTextHeight(line);
            }

            if (paragraphHeight < 8) {
                paragraphHeight += fr.catcore.translatedlegacy.font.TextRenderer.getTextHeight(paragraph);
            }

            height += paragraphHeight;
        }

        return height;
    }
}
