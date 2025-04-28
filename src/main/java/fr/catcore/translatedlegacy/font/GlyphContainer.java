package fr.catcore.translatedlegacy.font;

import fr.catcore.translatedlegacy.font.api.GameProvider;
import fr.catcore.translatedlegacy.font.renderable.RenderableItem;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GlyphContainer {
    private final List<RenderableItem> renderables;
    private final Style style;

    public GlyphContainer(List<RenderableItem> renderables, Style style) {
        this.renderables = renderables;
        this.style = style;
    }

    public void render(int x, int y, int defaultColor, int blitOffset, GameProvider game, boolean flag) {
        int currentX = x;

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

        for (RenderableItem renderable : renderables) {
            int currentY = y;

            int height = renderable.getHeight();

            if (height > 8) {
                currentY -= height - 9;
            }

            renderable.beforeRender();

            renderable.draw(game, currentX, currentY, blitOffset, style != null ? style.italic : false);

            int currentWidth = renderable.getWidth();

            if (style != null) {
                if (style.bold) {
                    renderable.draw(game, currentX + 1, currentY, blitOffset, style.italic);
                    currentWidth++;
                }

                game.doDecorations(currentX, y, currentWidth, 9, style.strikethrough, style.underline);
            }

            currentX += currentWidth;
        }
    }

    public int getWidth() {
        return renderables.stream().mapToInt(RenderableItem::getWidth).sum();
    }
    public int getHeight() {
        return renderables.stream().mapToInt(RenderableItem::getHeight).max().orElse(8);
    }
}
