package fr.catcore.translatedlegacy.babric;

import fr.catcore.translatedlegacy.font.api.GameProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;

public abstract class BabricGameProvider implements GameProvider {
    private final Minecraft minecraft;

    public BabricGameProvider(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public boolean anaglyph3d() {
        return minecraft.options.anaglyph3d;
    }

    @Override
    public void draw(int x, int y, int width, int height, float u0, float v0, float u1, float v1, float blitOffset, boolean italic) {
        int italicOffset = italic ? 1 : 0;

        Tessellator tessellator = Tessellator.INSTANCE;
        startQuadsSafely(tessellator);
        tessellator.vertex((float)x - (float) italicOffset, (float)(y + height), blitOffset, u0, v1);
        tessellator.vertex((float)(x + width) - (float) italicOffset, (float)(y + height), blitOffset, u1, v1);
        tessellator.vertex((float)(x + width) + (float) italicOffset, (float)y, blitOffset, u1, v0);
        tessellator.vertex((float)x + (float) italicOffset, (float)y, blitOffset, u0, v0);
        tessellator.draw();
    }

    @Override
    public void doDecorations(int posX, int posY, float charWidth, float charHeight, boolean strikethroughStyle, boolean underlineStyle) {
        Tessellator tessellator = null;
        if (strikethroughStyle) {
            tessellator = Tessellator.INSTANCE;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            startQuadsSafely(tessellator);
            tessellator.vertex(posX, posY + (charHeight / 2), 0.0);
            tessellator.vertex(posX + charWidth, posY + (charHeight / 2), 0.0);
            tessellator.vertex(posX + charWidth, posY + (charHeight / 2) - 1.0F, 0.0);
            tessellator.vertex(posX, posY + (charHeight / 2) - 1.0F, 0.0);
            tessellator.draw();
            if(!underlineStyle) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
        }

        if (underlineStyle) {
            if(!strikethroughStyle) {
                tessellator = Tessellator.INSTANCE;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
            startQuadsSafely(tessellator);
            int underlineOffset = -1;
            tessellator.vertex(posX + (float)underlineOffset, posY + charHeight, 0.0);
            tessellator.vertex(posX + charWidth, posY + charHeight, 0.0);
            tessellator.vertex(posX + charWidth, posY + charHeight - 1.0F, 0.0);
            tessellator.vertex(posX + (float)underlineOffset, posY + charHeight - 1.0F, 0.0);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    private void startQuadsSafely(Tessellator tessellator) {
        try {
            tessellator.startQuads();
        } catch (Exception e) {
            tessellator.draw();
            tessellator.startQuads();
        }
    }
}
