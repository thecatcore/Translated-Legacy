package fr.catcore.translatedlegacy.font.renderable;

import fr.catcore.translatedlegacy.font.Style;
import fr.catcore.translatedlegacy.font.TextImage;
import fr.catcore.translatedlegacy.font.api.GameProvider;
import org.lwjgl.opengl.GL11;

import java.io.Closeable;
import java.io.IOException;

public class Renderable implements Closeable {
    private final TextImage texture;
    private final Style style;

    public Renderable(TextImage texture, Style style) {
        this.texture = texture;
        this.style = style;
    }

    public void render(int x, int y, int defaultColor, int blitOffset, GameProvider game, boolean flag) {
        this.texture.bind();
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
        return texture.getWidth();
    }

    @Override
    public void close() throws IOException {
        texture.close();
    }
}
