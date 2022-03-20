package fr.catcore.translatedlegacy.font;

import com.google.gson.JsonObject;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.GlAllocationUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LegacyUnicodeFont extends Font {

    private static final NonRenderableGlyph SPACE = () -> 4.0F;

    private final int[] imageInt = new int[256];
    private final int[] anInt = new int[256];

    private static final int GLYPH_HEIGHT = 8;
    private static final int GLYPH_WIDTH = 8;
    private final byte[] CHARS_WIDTH = new byte[65536];
    private final Glyph[] GLYPHS = new Glyph[65536];

    private final String sizesLocation;
    private final String template;

    protected LegacyUnicodeFont(JsonObject object) {
        this.sizesLocation = "/assets/" + object.get("sizes").getAsString().replace(":", "/");
        this.template = "/assets/" + object.get("template").getAsString().replace(":", "/textures/");
    }

    @Override
    public boolean contains(char c) {
        return c <= 65536;
    }

    @Override
    protected Glyph getGlyph(char c) {
        return this.GLYPHS[c];
    }

    @Override
    protected void loadTextures(GameOptions arg, TextureManager arg1) {
        this.loadGlyphSizes();

        for (int blockIndex = 0; blockIndex < 256; blockIndex++) {
            loadTexture(arg, arg1, blockIndex);
        }
    }

    @Override
    protected int getImagePointer(char c) {
        return this.imageInt[c/256];
    }

    @Override
    protected int getOtherPointer(char c) {
        return this.anInt[c/256];
    }

    @Override
    protected byte getWidth(char c) {
        return this.CHARS_WIDTH[c];
    }

    private void loadTexture(GameOptions arg, TextureManager arg1, int blockIndex) {
        int j = blockIndex * 256;

        BufferedImage fontImage;
        try {
            fontImage = ImageIO.read(LegacyUnicodeFont.class.getResourceAsStream(String.format(String.format(this.template, "%02x"), blockIndex)));
        } catch (IOException var18) {
            throw new RuntimeException(var18);
        } catch (IllegalArgumentException e) {
            return;
        }

        int imageWidth = fontImage.getWidth();
        int imageHeight = fontImage.getHeight();

        if (imageWidth != 256 || imageHeight != 256) return;

        for (int k = 0; k < 256; k++) {
            byte b = this.CHARS_WIDTH[j + k];
            if (b != 0 && getStart(b) > getEnd(b)) {
                this.CHARS_WIDTH[j + k] = 0;
            }
        }

        this.imageInt[blockIndex] = arg1.method_1088(fontImage);
        this.anInt[blockIndex] = GlAllocationUtils.generateDisplayLists(300 + blockIndex);
        Tessellator tessellator = Tessellator.INSTANCE;

        for (int u = 0; u < 256; u++) {
            int codepoint = j + u;
            byte b = this.CHARS_WIDTH[codepoint];
            if (b != 0) this.GLYPHS[codepoint] = new UnicodeTextureGlyph(getEnd(b) - getStart(b), 16, codepoint, blockIndex);
            else this.GLYPHS[codepoint] = null;
        }

        this.GLYPHS[32] = SPACE;

        for(int unicodeId = 0; unicodeId < 256; ++unicodeId) {
            int codepoint = j + unicodeId;
            GL11.glNewList(this.anInt[blockIndex] + codepoint, 4864);
            tessellator.startQuads();

            Glyph glyph = this.GLYPHS[codepoint];

            if (glyph != null) {
                int width = unicodeId % 16 * GLYPH_HEIGHT;
                int height = unicodeId / 16 * GLYPH_WIDTH;
                glyph.preDraw(false, height, width, getStart(this.CHARS_WIDTH[codepoint])/2.0F, getEnd(this.CHARS_WIDTH[codepoint]), tessellator);
            }

            tessellator.draw();

            float width = this.getCharWidth(unicodeId + j);

            GL11.glTranslatef(width, 0.0F, 0.0F);
            GL11.glEndList();
        }

        this.registerColorCodes(arg.anaglyph3d);
    }

    private void registerColorCodes(boolean anaglyph3d) {
        for(int color = 0; color < 32; ++color) {
            int var23 = (color >> 3 & 1) * 85;
            int red = (color >> 2 & 1) * 170 + var23;
            int green = (color >> 1 & 1) * 170 + var23;
            int blue = (color & 1) * 170 + var23;
            if (color == 6) {
                red += 85;
            }

            boolean flag = color >= 16;
            if (anaglyph3d) {
                int var32 = (red * 30 + green * 59 + blue * 11) / 100;
                int var33 = (red * 30 + green * 70) / 100;
                int var17 = (red * 30 + blue * 70) / 100;
                red = var32;
                green = var33;
                blue = var17;
            }

            if (flag) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }

            GL11.glNewList(this.anInt[0] + 256 + color, 4864);
            GL11.glColor3f((float)red / 255.0F, (float)green / 255.0F, (float)blue / 255.0F);
            GL11.glEndList();
        }
    }

    private void loadGlyphSizes() {
        InputStream inputStream = null;

        try {
            inputStream = LegacyUnicodeFont.class.getResourceAsStream(this.sizesLocation);
            inputStream.read(this.CHARS_WIDTH);
        } catch (IOException var6) {
            throw new RuntimeException(var6);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static int getStart(byte size) {
        return size >> 4 & 15;
    }

    private static int getEnd(byte size) {
        return (size & 15) + 1;
    }

    private float getCharWidth(int index) {
        return this.GLYPHS[index] != null ? this.GLYPHS[index].getAdvance() : 8.0F;
    }
}
