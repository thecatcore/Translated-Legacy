package fr.catcore.translatedlegacy.font;

import fr.catcore.translatedlegacy.util.Int2ObjectMap;
import net.minecraft.class_214;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BetterTextRenderer {
    private static final Glyph SPACE = () -> {
        return 4.0F;
    };

    public int[] imageInt = new int[256];
    private int[] anInt = new int[256];
    private IntBuffer intBuffer = class_214.method_745(1024);

    private static final int GLYPH_HEIGHT = 8;
    private static final int GLYPH_WIDTH = 8;
    private byte[] CHARS_WIDTH = new byte[65536];
    private Glyph[] GLYPHS = new Glyph[65536];

    private void loadFonts(GameOptions arg, TextureManager arg1) {
        for (int a1 = 0; a1 < 256; a1++) {
            int j = a1 * 256;
            loadFont(arg, arg1, j);
        }
    }

    private static int getStart(byte size) {
        return size >> 4 & 15;
    }

    private static int getEnd(byte size) {
        return (size & 15) + 1;
    }

    private void loadFont(GameOptions arg, TextureManager arg1, int j) {
        BufferedImage fontImage;
        try {
            fontImage = ImageIO.read(BetterTextRenderer.class.getResourceAsStream("/assets/minecraft/textures/font/unicode_page_" + String.format("%02x", j / 256) + ".png"));
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

        int fontBlockIndex = j / 256;

        this.imageInt[fontBlockIndex] = arg1.glLoadImage(fontImage);
        this.anInt[fontBlockIndex] = class_214.method_741(288 + fontBlockIndex);
        Tessellator tessellator = Tessellator.INSTANCE;

        for (int u = 0; u < 256; u++) {
            int codepoint = j + u;
            byte b = this.CHARS_WIDTH[codepoint];
            if (b != 0) this.GLYPHS[codepoint] = new UnicodeTextureGlyph(getEnd(b) - getStart(b), 16);
            else this.GLYPHS[codepoint] = null;
        }

        this.GLYPHS[32] = SPACE;

        for(int unicodeId = 0; unicodeId < 256; ++unicodeId) {
            int codepoint = j + unicodeId;
            GL11.glNewList(this.anInt[fontBlockIndex] + codepoint, 4864);
            tessellator.start();

            Glyph glyph = this.GLYPHS[codepoint];

            if (glyph != null) {
                int width = unicodeId % 16 * GLYPH_HEIGHT;
                int height = unicodeId / 16 * GLYPH_WIDTH;
                glyph.preDraw(false, height, width, getStart(this.CHARS_WIDTH[codepoint])/2.0F, getEnd(this.CHARS_WIDTH[codepoint]), tessellator);
            }

            tessellator.draw();

            GL11.glTranslatef(this.getCharWidth(unicodeId + j), 0.0F, 0.0F);
            GL11.glEndList();
        }

        for(int color = 0; color < 32; ++color) {
            int var23 = (color >> 3 & 1) * 85;
            int red = (color >> 2 & 1) * 170 + var23;
            int green = (color >> 1 & 1) * 170 + var23;
            int blue = (color & 1) * 170 + var23;
            if (color == 6) {
                red += 85;
            }

            boolean flag = color >= 16;
            if (arg.anaglyph3d) {
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

    public BetterTextRenderer(GameOptions arg, TextureManager arg1) {
        this.loadGlyphSizes();
        this.loadFonts(arg, arg1);
    }

    public void drawTextWithShadow(String string, int x, int y, int color) {
        this.drawText(string, x + 1, y + 1, color, true);
        this.drawText(string, x, y, color);
    }

    public void drawText(String string, int x, int y, int color) {
        this.drawText(string, x, y, color, false);
    }

    public void drawText(String string, int x, int y, int color, boolean flag) {
        if (string != null) {
            if (flag) {
                int var6 = color & -16777216;
                color = (color & 16579836) >> 2;
                color = color + var6;
            }

            float red = (float)(color >> 16 & 255) / 255.0F;
            float green = (float)(color >> 8 & 255) / 255.0F;
            float blue = (float)(color & 255) / 255.0F;
            float alpha = (float)(color >> 24 & 255) / 255.0F;
            if (alpha == 0.0F) {
                alpha = 1.0F;
            }

            GL11.glColor4f(red, green, blue, alpha);

            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, 0.0F);
            for(int var12 = 0; var12 < string.length(); ++var12) {
                int var141 = BetterCharacterUtils.getId(string.charAt(var12));
                if (this.CHARS_WIDTH[var141] == 0) continue;
                ((Buffer)this.intBuffer).clear();
                GL11.glBindTexture(3553, this.imageInt[var141 / 256]);

                for(; string.length() > var12 + 1 && string.charAt(var12) == 167; var12 += 2) {
                    int var13 = "0123456789abcdef".indexOf(string.toLowerCase().charAt(var12 + 1));
                    if (var13 < 0 || var13 > 15) {
                        var13 = 15;
                    }

                    this.intBuffer.put(this.anInt[0] + 256 + var13 + (flag ? 16 : 0));
                    if (this.intBuffer.remaining() == 0) {
                        ((Buffer)this.intBuffer).flip();
                        GL11.glCallLists(this.intBuffer);
                        ((Buffer)this.intBuffer).clear();
                    }
                }

                if (var12 < string.length()) {
                    int var14 = BetterCharacterUtils.getId(string.charAt(var12));
                    if (this.CHARS_WIDTH[var14] <= 0) continue;
                    this.intBuffer.put(this.anInt[var14 / 256] + var14);
                }

                if (this.intBuffer.remaining() == 0) {
                    ((Buffer)this.intBuffer).flip();
                    GL11.glCallLists(this.intBuffer);
                    ((Buffer)this.intBuffer).clear();
                }
                ((Buffer)this.intBuffer).flip();
                GL11.glCallLists(this.intBuffer);
            }
            GL11.glPopMatrix();
        }
    }

    public int getTextWidth(String string) {
        if (string == null) {
            return 0;
        } else {
            int width = 0;

            for(int pos = 0; pos < string.length(); ++pos) {
                if (string.charAt(pos) == 167) {
                    ++pos;
                } else {
                    int index = BetterCharacterUtils.getId(string.charAt(pos));
                    width += this.getCharWidth(index);
                }
            }

            return width;
        }
    }
    
    private float getCharWidth(int index) {
        return this.GLYPHS[index] != null ? this.GLYPHS[index].getAdvance() : 8.0F;
    }

    private void loadGlyphSizes() {
        InputStream inputStream = null;

        try {
            inputStream = BetterTextRenderer.class.getResourceAsStream("/assets/minecraft/font/glyph_sizes.bin");
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

    public void drawText(String string, int x, int y, int width, int color) {
        String[] lines = string.split("\n");
        if (lines.length > 1) {
            for (String line : lines) {
                this.drawText(line, x, y, width, color);
                y += this.getLineWidth(line, width);
            }

        } else {
            String[] words = string.split(" ");
            int wordIndex = 0;

            while(wordIndex < words.length) {
                String word;
                for(word = words[wordIndex++] + " ";
                    wordIndex < words.length && this.getTextWidth(word + words[wordIndex]) < width;
                    word = word + words[wordIndex++] + " ") {
                }

                int var10;
                for(; this.getTextWidth(word) > width; word = word.substring(var10)) {
                    for(var10 = 0; this.getTextWidth(word.substring(0, var10 + 1)) <= width; ++var10) {
                    }

                    if (word.substring(0, var10).trim().length() > 0) {
                        this.drawText(word.substring(0, var10), x, y, color);
                        y += 8;
                    }
                }

                if (word.trim().length() > 0) {
                    this.drawText(word, x, y, color);
                    y += 8;
                }
            }

        }
    }

    public int getLineWidth(String string, int z) {
        String[] lines = string.split("\n");
        if (lines.length > 1) {
            int var9 = 0;

            for (String line : lines) {
                var9 += this.getLineWidth(line, z);
            }

            return var9;
        } else {
            String[] words = string.split(" ");
            int wordIndex = 0;
            int lineWidth = 0;

            while(wordIndex < words.length) {
                String word;
                for(word = words[wordIndex++] + " ";
                    wordIndex < words.length && this.getTextWidth(word + words[wordIndex]) < z;
                    word = word + words[wordIndex++] + " ") {
                }

                int var8;
                for(; this.getTextWidth(word) > z; word = word.substring(var8)) {
                    for(var8 = 0; this.getTextWidth(word.substring(0, var8 + 1)) <= z; ++var8) {
                    }

                    if (word.substring(0, var8).trim().length() > 0) {
                        lineWidth += 8;
                    }
                }

                if (word.trim().length() > 0) {
                    lineWidth += 8;
                }
            }

            if (lineWidth < 8) {
                lineWidth += 8;
            }

            return lineWidth;
        }
    }
}
