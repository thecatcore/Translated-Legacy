package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.util.TextUtils;
import net.minecraft.client.render.TextRenderer;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TextRenderer.class)
public class TextRendererMixin {
    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public void drawLayer(String text, int x, int y, int color, boolean shadow) {
        fr.catcore.translatedlegacy.font.TextRenderer.draw(text, x, y, color, shadow);
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
    @Conditional(minecraftVersion = @Version(">=b1.5"))
    @Overwrite
    public void splitAndDraw(String text, int x, int y, int width, int color) {
        for (String paragraph : text.split("\n")) {
            int paragraphHeight = 0;

            for (String line : TextUtils.wrapParagraph(paragraph, width, this::getWidth)) {
                if (line.trim().isEmpty()) continue;

                int lineHeight = fr.catcore.translatedlegacy.font.TextRenderer.draw(line, x, y, color, false);
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
     * @reason Must wrap exactly like {@link #splitAndDraw(String, int, int, int, int)}
     */
    @Conditional(minecraftVersion = @Version(">=b1.5"))
    @Overwrite
    public int splitAndGetHeight(String text, int width) {
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
