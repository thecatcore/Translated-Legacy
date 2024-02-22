package fr.catcore.translatedlegacy.font.renderable;

import fr.catcore.translatedlegacy.font.Style;
import fr.catcore.translatedlegacy.font.api.GameProvider;
import org.lwjgl.opengl.GL11;

import java.io.Closeable;
import java.io.IOException;

public class Renderable implements Closeable {
    private final TextTexture texture;
    private final Style style;

    public Renderable(TextTexture texture, Style style) {
        this.texture = texture;
        this.style = style;
    }

    public void render(int x, int y, int defaultColor, int blitOffset, GameProvider game) {
        this.texture.bind();

        if (style == null || style.color == null) {
            float var10 = (float)(defaultColor >> 16 & 255) / 255.0F;
            float var7 = (float)(defaultColor >> 8 & 255) / 255.0F;
            float var8 = (float)(defaultColor & 255) / 255.0F;
            float var9 = (float)(defaultColor >> 24 & 255) / 255.0F;
            if (var9 == 0.0F) {
                var9 = 1.0F;
            }

            GL11.glColor4f(var10, var7, var8, var9);
        } else {
            GL11.glColor3f(style.color.r, style.color.g, style.color.b);
        }

        game.draw(x, y, texture.getWidth(), texture.getHeight(), blitOffset);
    }

    @Override
    public void close() throws IOException {
        texture.close();
    }
}
