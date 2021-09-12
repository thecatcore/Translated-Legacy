package fr.catcore.translatedlegacy.font;

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

public class BetterTextRenderer {
    private int[] CHARS_WIDTH_ASCII = new int[65536];
    public int imageInt = 0;
    private int anInt;
    private IntBuffer intBuffer = class_214.method_745(1024);

    private static final int GLYPH_HEIGHT = 8;
    private static final int GLYPH_WIDTH = 8;
    private byte[] CHARS_WIDTH = new byte[65536];

    public BetterTextRenderer(GameOptions arg, TextureManager arg1) {
        this.loadGlyphSizes();
        BufferedImage fontImage;
        try {
            fontImage = ImageIO.read(BetterTextRenderer.class.getResourceAsStream("/assets/modid/font/unicode_page_00 (copie).png"));
        } catch (IOException var18) {
            throw new RuntimeException(var18);
        }

        int imageWidth = fontImage.getWidth();
        int imageHeight = fontImage.getHeight();
        int[] pixels = new int[imageWidth * imageHeight];
        fontImage.getRGB(0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);

        for(int unicodeId = 0; unicodeId < 256; ++unicodeId) {
            int euclidean = unicodeId % GLYPH_HEIGHT;
            int nonEuclidean = unicodeId / GLYPH_WIDTH;

            int charWidth;
            for(charWidth = 7; charWidth >= 0; --charWidth) {
                int var12 = euclidean * 8 + charWidth;
                boolean wipLine = true;

                for(int charHeight = 0; charHeight < 8 && wipLine; ++charHeight) {
                    int var15 = (nonEuclidean * 8 + charHeight) * imageWidth;
                    int outOfBound = pixels[var12 + var15] & 255;
                    if (outOfBound > 0) {
                        wipLine = false;
                    }
                }

                if (!wipLine) {
                    break;
                }
            }

            if (unicodeId == 32) {
                charWidth = 2;
            }

            this.CHARS_WIDTH_ASCII[unicodeId] = charWidth + 2;
        }

        this.imageInt = arg1.glLoadImage(fontImage);
        this.anInt = class_214.method_741(288);
        Tessellator tessellator = Tessellator.INSTANCE;

        for(int unicodeId = 0; unicodeId < 256; ++unicodeId) {
            GL11.glNewList(this.anInt + unicodeId, 4864);
            tessellator.start();
            int height = unicodeId % 16 * GLYPH_HEIGHT;
            int width = unicodeId / 16 * GLYPH_WIDTH;
            float var26 = 7.99F;
            float var28 = 0.0F;
            float var30 = 0.0F;

            tessellator.vertex(
                    0.0D,
                    (double)(0.0F + var26),
                    0.0D,
                    (double)((float)height / 128.0F + var28),
                    (double)(((float)width + var26) / 128.0F + var30)
            );

            tessellator.vertex(
                    (double)(0.0F + var26),
                    (double)(0.0F + var26),
                    0.0D,
                    (double)(((float)height + var26) / 128.0F + var28),
                    (double)(((float)width + var26) / 128.0F + var30)
            );

            tessellator.vertex(
                    (double)(0.0F + var26),
                    0.0D,
                    0.0D,
                    (double)(((float)height + var26) / 128.0F + var28),
                    (double)((float)width / 128.0F + var30)
            );

            tessellator.vertex(
                    0.0D,
                    0.0D,
                    0.0D,
                    (double)((float)height / 128.0F + var28),
                    (double)((float)width / 128.0F + var30)
            );

            tessellator.draw();

            GL11.glTranslatef(this.getCharWidth(unicodeId), 0.0F, 0.0F);
            GL11.glEndList();
        }

        for(int var21 = 0; var21 < 32; ++var21) {
            int var23 = (var21 >> 3 & 1) * 85;
            int red = (var21 >> 2 & 1) * 170 + var23;
            int green = (var21 >> 1 & 1) * 170 + var23;
            int blue = (var21 >> 0 & 1) * 170 + var23;
            if (var21 == 6) {
                red += 85;
            }

            boolean var31 = var21 >= 16;
            if (arg.anaglyph3d) {
                int var32 = (red * 30 + green * 59 + blue * 11) / 100;
                int var33 = (red * 30 + green * 70) / 100;
                int var17 = (red * 30 + blue * 70) / 100;
                red = var32;
                green = var33;
                blue = var17;
            }

            if (var31) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }

            GL11.glNewList(this.anInt + 256 + var21, 4864);
            GL11.glColor3f((float)red / 255.0F, (float)green / 255.0F, (float)blue / 255.0F);
            GL11.glEndList();
        }

    }

    public void drawTextWithShadow(String string, int x, int y, int z) {
        this.drawText(string, x + 1, y + 1, z, true);
        this.drawText(string, x, y, z);
    }

    public void drawText(String string, int x, int y, int z) {
        this.drawText(string, x, y, z, false);
    }

    public void drawText(String string, int x, int y, int z, boolean flag) {
        if (string != null) {
            if (flag) {
                int var6 = z & -16777216;
                z = (z & 16579836) >> 2;
                z = z + var6;
            }

            GL11.glBindTexture(3553, this.imageInt);
            float red = (float)(z >> 16 & 255) / 255.0F;
            float green = (float)(z >> 8 & 255) / 255.0F;
            float blue = (float)(z & 255) / 255.0F;
            float alpha = (float)(z >> 24 & 255) / 255.0F;
            if (alpha == 0.0F) {
                alpha = 1.0F;
            }

            GL11.glColor4f(red, green, blue, alpha);
            ((Buffer)this.intBuffer).clear();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, 0.0F);

            for(int var12 = 0; var12 < string.length(); ++var12) {
                for(; string.length() > var12 + 1 && string.charAt(var12) == 167; var12 += 2) {
                    int var13 = "0123456789abcdef".indexOf(string.toLowerCase().charAt(var12 + 1));
                    if (var13 < 0 || var13 > 15) {
                        var13 = 15;
                    }

                    this.intBuffer.put(this.anInt + 256 + var13 + (flag ? 16 : 0));
                    if (this.intBuffer.remaining() == 0) {
                        ((Buffer)this.intBuffer).flip();
                        GL11.glCallLists(this.intBuffer);
                        ((Buffer)this.intBuffer).clear();
                    }
                }

                if (var12 < string.length()) {
                    int var14 = BetterCharacterUtils.getId(string.charAt(var12));
                    this.intBuffer.put(this.anInt + var14);
                }

                if (this.intBuffer.remaining() == 0) {
                    ((Buffer)this.intBuffer).flip();
                    GL11.glCallLists(this.intBuffer);
                    ((Buffer)this.intBuffer).clear();
                }
            }

            ((Buffer)this.intBuffer).flip();
            GL11.glCallLists(this.intBuffer);
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
    
    private int getCharWidth(int index) {
        if (index == 32) return this.CHARS_WIDTH_ASCII[index];

        int j = this.CHARS_WIDTH[index] >>> 4;
        int k = this.CHARS_WIDTH[index] & 15;
        if (k > 7) {
            k = 15;
            j = 0;
        }

        ++k;
        return (k - j) / 2 + 1;
    }

    private void loadGlyphSizes() {
        InputStream inputStream = null;

        try {
            inputStream = BetterTextRenderer.class.getResourceAsStream("/assets/modid/font/glyph_sizes.bin");
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

    public void drawText(String string, int x, int y, int z, int z1) {
        String[] lines = string.split("\n");
        if (lines.length > 1) {
            for(int lineIndex = 0; lineIndex < lines.length; ++lineIndex) {
                this.drawText(lines[lineIndex], x, y, z, z1);
                y += this.getLineWidth(lines[lineIndex], z);
            }

        } else {
            String[] words = string.split(" ");
            int wordIndex = 0;

            while(wordIndex < words.length) {
                String word;
                for(word = words[wordIndex++] + " ";
                    wordIndex < words.length && this.getTextWidth(word + words[wordIndex]) < z;
                    word = word + words[wordIndex++] + " ") {
                }

                int var10;
                for(; this.getTextWidth(word) > z; word = word.substring(var10)) {
                    for(var10 = 0; this.getTextWidth(word.substring(0, var10 + 1)) <= z; ++var10) {
                    }

                    if (word.substring(0, var10).trim().length() > 0) {
                        this.drawText(word.substring(0, var10), x, y, z1);
                        y += 8;
                    }
                }

                if (word.trim().length() > 0) {
                    this.drawText(word, x, y, z1);
                    y += 8;
                }
            }

        }
    }

    public int getLineWidth(String string, int z) {
        String[] lines = string.split("\n");
        if (lines.length > 1) {
            int var9 = 0;

            for(int lineIndex = 0; lineIndex < lines.length; ++lineIndex) {
                var9 += this.getLineWidth(lines[lineIndex], z);
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
