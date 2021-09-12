package fr.catcore.translatedlegacy.font;

import net.minecraft.class_214;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.IntBuffer;

public class BetterTextRenderer {
    private int[] CHARS = new int[256];
    public int imageInt = 0;
    private int anInt;
    private IntBuffer intBuffer = class_214.method_745(1024);

    private static final int GLYPH_HEIGHT = 8;
    private static final int GLYPH_WIDTH = 8;

    public BetterTextRenderer(GameOptions arg, TextureManager arg1) {
        BufferedImage fontImage;
        try {
            fontImage = ImageIO.read(BetterTextRenderer.class.getResourceAsStream("/assets/modid/font/unicode_page_00.png"));
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

            int charHeight;
            for(charHeight = 7; charHeight >= 0; --charHeight) {
                int var12 = euclidean * 8 + charHeight;
                boolean wipLine = true;

                for(int charWidth = 0; charWidth < 8 && wipLine; ++charWidth) {
                    int var15 = (nonEuclidean * 8 + charWidth) * imageWidth;
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
                charHeight = 2;
            }

            this.CHARS[unicodeId] = charHeight + 2;
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

            GL11.glTranslatef((float)this.CHARS[unicodeId], 0.0F, 0.0F);
            GL11.glEndList();
        }

        for(int var21 = 0; var21 < 32; ++var21) {
            int var23 = (var21 >> 3 & 1) * 85;
            int var25 = (var21 >> 2 & 1) * 170 + var23;
            int var27 = (var21 >> 1 & 1) * 170 + var23;
            int var29 = (var21 >> 0 & 1) * 170 + var23;
            if (var21 == 6) {
                var25 += 85;
            }

            boolean var31 = var21 >= 16;
            if (arg.anaglyph3d) {
                int var32 = (var25 * 30 + var27 * 59 + var29 * 11) / 100;
                int var33 = (var25 * 30 + var27 * 70) / 100;
                int var17 = (var25 * 30 + var29 * 70) / 100;
                var25 = var32;
                var27 = var33;
                var29 = var17;
            }

            if (var31) {
                var25 /= 4;
                var27 /= 4;
                var29 /= 4;
            }

            GL11.glNewList(this.anInt + 256 + var21, 4864);
            GL11.glColor3f((float)var25 / 255.0F, (float)var27 / 255.0F, (float)var29 / 255.0F);
            GL11.glEndList();
        }

    }

    public void drawTextWithShadow(String string, int i, int j, int k) {
        this.drawText(string, i + 1, j + 1, k, true);
        this.drawText(string, i, j, k);
    }

    public void drawText(String string, int i, int j, int k) {
        this.drawText(string, i, j, k, false);
    }

    public void drawText(String string, int i, int j, int k, boolean flag) {
        if (string != null) {
            if (flag) {
                int var6 = k & -16777216;
                k = (k & 16579836) >> 2;
                k = k + var6;
            }

            GL11.glBindTexture(3553, this.imageInt);
            float var11 = (float)(k >> 16 & 255) / 255.0F;
            float var7 = (float)(k >> 8 & 255) / 255.0F;
            float var8 = (float)(k & 255) / 255.0F;
            float var9 = (float)(k >> 24 & 255) / 255.0F;
            if (var9 == 0.0F) {
                var9 = 1.0F;
            }

            GL11.glColor4f(var11, var7, var8, var9);
            ((Buffer)this.intBuffer).clear();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)i, (float)j, 0.0F);

            for(int var12 = 0; var12 < string.length(); ++var12) {
                for(; string.length() > var12 + 1 && string.charAt(var12) == 167; var12 += 2) {
                    int var13 = "0123456789abcdef".indexOf(string.toLowerCase().charAt(var12 + 1));
                    if (var13 < 0 || var13 > 15) {
                        var13 = 15;
                    }

                    this.intBuffer.put(this.anInt + /*256*/ 64 + var13 + (flag ? 16 : 0));
                    if (this.intBuffer.remaining() == 0) {
                        ((Buffer)this.intBuffer).flip();
                        GL11.glCallLists(this.intBuffer);
                        ((Buffer)this.intBuffer).clear();
                    }
                }

                if (var12 < string.length()) {
                    int var14 = BetterCharacterUtils.field_298.indexOf(string.charAt(var12));
                    if (var14 >= 0) {
                        this.intBuffer.put(this.anInt + var14 + /*32*/ 0);
                    }
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
            int var2 = 0;

            for(int var3 = 0; var3 < string.length(); ++var3) {
                if (string.charAt(var3) == 167) {
                    ++var3;
                } else {
                    int var4 = BetterCharacterUtils.field_298.indexOf(string.charAt(var3));
                    if (var4 >= 0) {
                        var2 += this.CHARS[var4 + 32];
                    }
                }
            }

            return var2;
        }
    }

    public void method_1904(String string, int i, int j, int k, int i1) {
        String[] var6 = string.split("\n");
        if (var6.length > 1) {
            for(int var11 = 0; var11 < var6.length; ++var11) {
                this.method_1904(var6[var11], i, j, k, i1);
                j += this.method_1902(var6[var11], k);
            }

        } else {
            String[] var7 = string.split(" ");
            int var8 = 0;

            while(var8 < var7.length) {
                String var9;
                for(var9 = var7[var8++] + " "; var8 < var7.length && this.getTextWidth(var9 + var7[var8]) < k; var9 = var9 + var7[var8++] + " ") {
                }

                int var10;
                for(; this.getTextWidth(var9) > k; var9 = var9.substring(var10)) {
                    for(var10 = 0; this.getTextWidth(var9.substring(0, var10 + 1)) <= k; ++var10) {
                    }

                    if (var9.substring(0, var10).trim().length() > 0) {
                        this.drawText(var9.substring(0, var10), i, j, i1);
                        j += 8;
                    }
                }

                if (var9.trim().length() > 0) {
                    this.drawText(var9, i, j, i1);
                    j += 8;
                }
            }

        }
    }

    public int method_1902(String string, int i) {
        String[] var3 = string.split("\n");
        if (var3.length > 1) {
            int var9 = 0;

            for(int var10 = 0; var10 < var3.length; ++var10) {
                var9 += this.method_1902(var3[var10], i);
            }

            return var9;
        } else {
            String[] var4 = string.split(" ");
            int var5 = 0;
            int var6 = 0;

            while(var5 < var4.length) {
                String var7;
                for(var7 = var4[var5++] + " "; var5 < var4.length && this.getTextWidth(var7 + var4[var5]) < i; var7 = var7 + var4[var5++] + " ") {
                }

                int var8;
                for(; this.getTextWidth(var7) > i; var7 = var7.substring(var8)) {
                    for(var8 = 0; this.getTextWidth(var7.substring(0, var8 + 1)) <= i; ++var8) {
                    }

                    if (var7.substring(0, var8).trim().length() > 0) {
                        var6 += 8;
                    }
                }

                if (var7.trim().length() > 0) {
                    var6 += 8;
                }
            }

            if (var6 < 8) {
                var6 += 8;
            }

            return var6;
        }
    }
}
