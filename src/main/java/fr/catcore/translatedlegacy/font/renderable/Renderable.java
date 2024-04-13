package fr.catcore.translatedlegacy.font.renderable;

import fr.catcore.translatedlegacy.font.Style;
import fr.catcore.translatedlegacy.font.TextImage;
import fr.catcore.translatedlegacy.font.TextRenderer;
import fr.catcore.translatedlegacy.font.api.GameProvider;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class Renderable {
    private final List<TextImage> textures;
    private final Style style;

    public Renderable(List<TextImage> textures, Style style) {
        this.textures = textures;
        this.style = style;
    }

    public void render(int x, int y, int defaultColor, int blitOffset, GameProvider game, boolean flag) {
        this.textures.stream().reduce(x, (currentX, textImage) -> {
            if (currentX != x) {
                currentX += TextRenderer.getSpaceWidth();
            }

            if (textImage.getWidth() > 0) render(textImage, currentX, y, defaultColor, blitOffset, game, flag);

            return currentX + textImage.getWidth();
        }, (integer, integer2) -> integer2);
    }

    public void render(TextImage texture, int x, int y, int defaultColor, int blitOffset, GameProvider game, boolean flag) {
        texture.bind();
        float alpha = (float)(defaultColor >> 24 & 255) / 255.0F;

        if (alpha == 0.0F) {
            alpha = 1.0F;
        }

        float red = (float)(defaultColor >> 16 & 255) / 255.0F;
        float green = (float)(defaultColor >> 8 & 255) / 255.0F;
        float blue = (float)(defaultColor & 255) / 255.0F;

        if (style != null && style.color != null) {
            red = style.color.r;
            green = style.color.g;
            blue = style.color.b;

            if (flag) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
        }

        GL11.glColor4f(red, green, blue, alpha);

        game.draw(x, y, texture.getWidth(), texture.getHeight(), blitOffset);
    }

    public int getWidth() {
        return textures.stream().mapToInt(TextImage::getWidth)
                .reduce(0,
                        (left, right) -> left == 0 ? right : left + TextRenderer.getSpaceWidth() + right
                );
    }
}
