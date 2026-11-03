package fr.catcore.translatedlegacy.ornithe.client;

import fr.catcore.translatedlegacy.font.TextInfo;
import fr.catcore.translatedlegacy.font.api.GameProvider;
import fr.catcore.translatedlegacy.ornithe.common.TextComponentParser;
import net.minecraft.client.render.vertex.Tesselator;
import net.ornithemc.osl.lifecycle.api.client.MinecraftInstance;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class OrnitheGameProvider implements GameProvider {
    @Override
    public boolean anaglyph3d() {
        return MinecraftInstance.get().options.anaglyph;
    }

    @Override
    public List<TextInfo> parseText(String text) {
        return TextComponentParser.parse(text);
    }

    @Override
    public void draw(int x, int y, int width, int height, float u0, float v0, float u1, float v1, float blitOffset, boolean italic) {
        beginGlyphBatch();
        drawBatchedQuad(x, y, width, height, u0, v0, u1, v1, blitOffset, italic);
        endGlyphBatch();
    }

    @Override
    public void beginGlyphBatch() {
        startQuadsSafely(Tesselator.INSTANCE);
    }

    @Override
    public void drawBatchedQuad(float x, float y, float width, float height, float u0, float v0, float u1, float v1, float blitOffset, boolean italic) {
        float italicOffset = italic ? 1 : 0;

        Tesselator tessellator = Tesselator.INSTANCE;
        tessellator.vertex(x - italicOffset, y + height, blitOffset, u0, v1);
        tessellator.vertex(x + width - italicOffset, y + height, blitOffset, u1, v1);
        tessellator.vertex(x + width + italicOffset, y, blitOffset, u1, v0);
        tessellator.vertex(x + italicOffset, y, blitOffset, u0, v0);
    }

    @Override
    public void endGlyphBatch() {
        Tesselator.INSTANCE.end();
    }

    @Override
    public void doDecorations(int posX, int posY, float charWidth, float charHeight, boolean strikethroughStyle, boolean underlineStyle) {
        Tesselator tessellator = null;
        if (strikethroughStyle) {
            tessellator = Tesselator.INSTANCE;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            startQuadsSafely(tessellator);
            tessellator.vertex(posX, posY + (charHeight / 2), 0.0);
            tessellator.vertex(posX + charWidth, posY + (charHeight / 2), 0.0);
            tessellator.vertex(posX + charWidth, posY + (charHeight / 2) - 1.0F, 0.0);
            tessellator.vertex(posX, posY + (charHeight / 2) - 1.0F, 0.0);
            tessellator.end();
            if(!underlineStyle) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
        }

        if (underlineStyle) {
            if(!strikethroughStyle) {
                tessellator = Tesselator.INSTANCE;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
            startQuadsSafely(tessellator);
            int underlineOffset = -1;
            tessellator.vertex(posX + (float)underlineOffset, posY + charHeight, 0.0);
            tessellator.vertex(posX + charWidth, posY + charHeight, 0.0);
            tessellator.vertex(posX + charWidth, posY + charHeight - 1.0F, 0.0);
            tessellator.vertex(posX + (float)underlineOffset, posY + charHeight - 1.0F, 0.0);
            tessellator.end();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    @Override
    public InputStream getResource(String path) throws IOException {
        return ResourceManager.client().getResource(path);
    }

    private void startQuadsSafely(Tesselator tessellator) {
        try {
            tessellator.begin();
        } catch (Exception e) {
            tessellator.end();
            tessellator.begin();
        }
    }
}
