package fr.catcore.translatedlegacy.babric.mixin.client.font;

import fr.catcore.translatedlegacy.babric.TranslatedLegacyBabricClient;
import fr.catcore.translatedlegacy.babric.mixin.client.GameOptionsAccessor;
import fr.catcore.translatedlegacy.util.TextUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextRenderer.class)
public abstract class TextRendererMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void c$init(GameOptions arg, String string, TextureManager arg1, CallbackInfo ci) {
        if (!FabricLoader.getInstance().isModLoaded("translated-legacy-stapi")) {
            TranslatedLegacyBabricClient.setGameProvider(((GameOptionsAccessor) arg).getMinecraft());
        }
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public void draw(String string, int i, int j, int k, boolean flag) {
        fr.catcore.translatedlegacy.font.TextRenderer.draw(string, i, j, k, flag);
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public int getWidth(String string) {
        return fr.catcore.translatedlegacy.font.TextRenderer.getTextWidth(string);
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run. Fix multiline draw height,
     * wrap at proper line break opportunities (Thai has no spaces between words) and never
     * split a letter from its combining marks.
     */
    @Overwrite
    public void drawSplit(String string, int i, int j, int k, int l) {
        for (String paragraph : string.split("\n")) {
            int paragraphHeight = 0;

            for (String line : TextUtils.wrapParagraph(paragraph, k, this::getWidth)) {
                if (line.trim().isEmpty()) continue;

                int lineHeight = fr.catcore.translatedlegacy.font.TextRenderer.draw(line, i, j, l, false);
                j += lineHeight;
                paragraphHeight += lineHeight;
            }

            if (paragraphHeight < 8) {
                j += fr.catcore.translatedlegacy.font.TextRenderer.getTextHeight(paragraph);
            }
        }
    }

    /**
     * @author CatCore
     * @reason Must wrap exactly like {@link #drawSplit(String, int, int, int, int)}
     */
    @Overwrite
    public int splitAndGetHeight(String string, int i) {
        int height = 0;

        for (String paragraph : string.split("\n")) {
            int paragraphHeight = 0;

            for (String line : TextUtils.wrapParagraph(paragraph, i, this::getWidth)) {
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
//    /**
//     * @author CatCore
//     * @reason We don't want any of the original code to run.
//     */
//    @Overwrite
//    public void method_1904(String string, int i, int j, int k, int i1) {
//        TranslatedLegacyBabric.TEXT_RENDERER.drawText(string, i, j, k, i1);
//    }
//
//    /**
//     * @author CatCore
//     * @reason We don't want any of the original code to run.
//     */
//    @Overwrite
//    public int method_1902(String string, int i) {
//        return TranslatedLegacyBabric.TEXT_RENDERER.getLineWidth(string, i);
//    }
}
