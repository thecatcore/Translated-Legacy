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
    public int[] imageInt = new int[256];
    private int[] anInt = new int[256];
    private IntBuffer intBuffer = class_214.method_745(1024);

    private static final int GLYPH_HEIGHT = 8;
    private static final int GLYPH_WIDTH = 8;
    private byte[] CHARS_WIDTH = new byte[65536];
    private static final String str = "0123456789abcdef";
    private static final String str1 = "0123456789abcdef";

    private void loadFonts(GameOptions arg, TextureManager arg1) {
        String a = str;

        for (int a1 = 0; a1 < a.length(); a1++) {
            for (int a2 = 0; a2 < str1.length(); a2++) {
                loadFont(arg, arg1, a.charAt(a1), str1.charAt(a2));
            }
        }
    }

    private void loadFont(GameOptions arg, TextureManager arg1, char a, char b) {
        BufferedImage fontImage;
        try {
            fontImage = ImageIO.read(BetterTextRenderer.class.getResourceAsStream("/assets/minecraft/font/unicode_page_" + a + b + ".png"));
        } catch (IOException var18) {
            throw new RuntimeException(var18);
        } catch (IllegalArgumentException e) {
            return;
        }

        int imageWidth = fontImage.getWidth();
        int imageHeight = fontImage.getHeight();
        int[] pixels = new int[imageWidth * imageHeight];
        fontImage.getRGB(0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);

        int fontBlockIndex = (str.indexOf(a) * 16) + str1.indexOf(b);
        int fontBlock = fontBlockIndex * 256;
        for(int unicodeId = 0; unicodeId < 256; ++unicodeId) {
            int euclidean = unicodeId % GLYPH_HEIGHT;
            int nonEuclidean = unicodeId / GLYPH_WIDTH;

            int charWidth;
            for(charWidth = 7; charWidth >= 0; --charWidth) {
                int var12 = euclidean * 8 + charWidth;
                boolean wipLine = true;

                for(int charHeight = 0; charHeight < 8; ++charHeight) {
                    int var15 = (nonEuclidean * 8 + charHeight) * imageWidth;
                    int outOfBound = pixels[var12 + var15] & 255;
                    if (outOfBound > 0) {
                        wipLine = false;
                        break;
                    }
                }

                if (!wipLine) {
                    break;
                }
            }

            int unicode = unicodeId + fontBlock;
            if (unicode == 32) {
                charWidth = 2;
            }

            this.CHARS_WIDTH_ASCII[unicode] = charWidth + 2;
        }

        this.imageInt[fontBlockIndex] = arg1.glLoadImage(fontImage);
        this.anInt[fontBlockIndex] = class_214.method_741(288 + fontBlockIndex);
        Tessellator tessellator = Tessellator.INSTANCE;

        for(int unicodeId = 0; unicodeId < 256; ++unicodeId) {
            GL11.glNewList(this.anInt[fontBlockIndex] + unicodeId + fontBlock, 4864);
            tessellator.start();
            int height = unicodeId % 16 * GLYPH_HEIGHT;
            int width = unicodeId / 16 * GLYPH_WIDTH;
            float var26 = 7.99F;
            float var28 = 0.0F;
            float var30 = 0.0F;

            tessellator.vertex(
                    0.0D,
                    0.0F + var26,
                    0.0D,
                    (float)height / 128.0F + var28,
                    ((float)width + var26) / 128.0F + var30
            );

            tessellator.vertex(
                    0.0F + var26,
                    0.0F + var26,
                    0.0D,
                    ((float)height + var26) / 128.0F + var28,
                    ((float)width + var26) / 128.0F + var30
            );

            tessellator.vertex(
                    0.0F + var26,
                    0.0D,
                    0.0D,
                    ((float)height + var26) / 128.0F + var28,
                    (float)width / 128.0F + var30
            );

            tessellator.vertex(
                    0.0D,
                    0.0D,
                    0.0D,
                    (float)height / 128.0F + var28,
                    (float)width / 128.0F + var30
            );

            tessellator.draw();

            GL11.glTranslatef(this.getCharWidth(unicodeId + fontBlock), 0.0F, 0.0F);
            GL11.glEndList();
        }

        for(int var21 = 0; var21 < 32; ++var21) {
            int var23 = (var21 >> 3 & 1) * 85;
            int red = (var21 >> 2 & 1) * 170 + var23;
            int green = (var21 >> 1 & 1) * 170 + var23;
            int blue = (var21 & 1) * 170 + var23;
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

            GL11.glNewList(this.anInt[fontBlockIndex] + 256 + var21, 4864);
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
                ((Buffer)this.intBuffer).clear();
                int var141 = BetterCharacterUtils.getId(string.charAt(var12));
                for (int a1 = 0; a1 < str.length(); a1++) {
                    boolean breack = false;
                    for (int a2 = 0; a2 < str1.length(); a2++) {
                        if ((var141 - ((a1*16) + a2)*256) <= 256) {
                            GL11.glBindTexture(3553, this.imageInt[((a1*16) + a2)]);
                            breack = true;
                            break;
                        }
                    }
                    if (breack) break;
                }

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
                    for (int a1 = 0; a1 < str.length(); a1++) {
                        boolean breack = false;
                        for (int a2 = 0; a2 < str1.length(); a2++) {
                            if ((var14 - ((a1*16) + a2)*256) <= 256) {
                                this.intBuffer.put(this.anInt[((a1*16) + a2)] + var14);
                                breack = true;
                                break;
                            }
                        }
                        if (breack) break;
                    }
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
