package fr.catcore.translatedlegacy.babric.mixin.client.font;

import fr.catcore.translatedlegacy.babric.TranslatedLegacyBabricClient;
import fr.catcore.translatedlegacy.babric.mixin.client.GameOptionsAccessor;
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
        if (!FabricLoader.getInstance().isModLoaded("translated-legacy-stapi"))
            TranslatedLegacyBabricClient.setGameProvider(((GameOptionsAccessor)arg).getMinecraft());
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
     * @reason We don't want any of the original code to run. Fix multiline draw height
     */
    @Overwrite
    public void method_1904(String string, int i, int j, int k, int l) {
        String[] lines = string.split("\n");
        if (lines.length > 1) {
            for(int lineIndex = 0; lineIndex < lines.length; ++lineIndex) {
                this.method_1904(lines[lineIndex], i, j, k, l);
                j += this.method_1902(lines[lineIndex], k);
            }

        } else {
            String[] segments = string.split(" ");
            int segmentIndex = 0;

            while(segmentIndex < segments.length) {
                String toDraw;
                for(toDraw = segments[segmentIndex++] + " "; segmentIndex < segments.length && this.getWidth(toDraw + segments[segmentIndex]) < k; toDraw = toDraw + segments[segmentIndex++] + " ") {
                }

                int lineBreakIndex;
                for(; this.getWidth(toDraw) > k; toDraw = toDraw.substring(lineBreakIndex)) {
                    for(lineBreakIndex = 0; this.getWidth(toDraw.substring(0, lineBreakIndex + 1)) <= k; ++lineBreakIndex) {
                    }

                    if (!toDraw.substring(0, lineBreakIndex).trim().isEmpty()) {
                        j += fr.catcore.translatedlegacy.font.TextRenderer.draw(toDraw.substring(0, lineBreakIndex), i, j, l, false);
                    }
                }

                if (!toDraw.trim().isEmpty()) {
                    j += fr.catcore.translatedlegacy.font.TextRenderer.draw(toDraw, i, j, l, false);
                }
            }

        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int method_1902(String string, int i) {
        String[] lines = string.split("\n");
        if (lines.length > 1) {
            int lineIndex = 0;
            for (int j = 0; j < lines.length; ++j) {
                lineIndex += this.method_1902(lines[j], i);
            }
            return lineIndex;
        }
        String[] segments = string.split(" ");
        int segmentIndex = 0;
        int height = 0;
        while (segmentIndex < segments.length) {
            String toDraw = segments[segmentIndex++] + " ";
            while (segmentIndex < segments.length && this.getWidth(toDraw + segments[segmentIndex]) < i) {
                toDraw = toDraw + segments[segmentIndex++] + " ";
            }
            while (this.getWidth(toDraw) > i) {
                int lineBreakIndex = 0;
                while (this.getWidth(toDraw.substring(0, lineBreakIndex + 1)) <= i) {
                    ++lineBreakIndex;
                }
                if (toDraw.substring(0, lineBreakIndex).trim().length() > 0) {
                    height += fr.catcore.translatedlegacy.font.TextRenderer.getTextHeight(toDraw.substring(0, lineBreakIndex));
                }
                toDraw = toDraw.substring(lineBreakIndex);
            }
            if (toDraw.trim().length() <= 0) continue;
            height += fr.catcore.translatedlegacy.font.TextRenderer.getTextHeight(toDraw);
        }
        if (height < 8) {
            height += fr.catcore.translatedlegacy.font.TextRenderer.getTextHeight(string);
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
