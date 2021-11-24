package fr.catcore.translatedlegacy.font;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.class_214;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.texture.TextureManager;
import org.lwjgl.opengl.GL11;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class BetterTextRenderer {
    private static final Gson GSON = new Gson();
    private final IntBuffer intBuffer = class_214.method_745(1024);
    private static final List<Font> FONTS = new ArrayList<>();

    private void loadFonts(GameOptions arg, TextureManager arg1) {
        for (Font font : FONTS) {
            font.loadTextures(arg, arg1);
        }
    }

    public BetterTextRenderer(GameOptions arg, TextureManager arg1) {
        this.loadFontJson();
        this.loadFonts(arg, arg1);
    }

    private void loadFontJson() {
        InputStream stream = BetterTextRenderer.class.getResourceAsStream("/assets/minecraft/font/uniform.json");
        InputStreamReader reader = new InputStreamReader(stream);

        JsonObject object = GSON.fromJson(reader, JsonObject.class);
        JsonArray list = object.getAsJsonArray("providers");

        for (JsonElement element : list) {
            FONTS.add(FontType.create(element.getAsJsonObject()));
        }
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
            for(int charIndex = 0; charIndex < string.length(); ++charIndex) {
                char c = string.charAt(charIndex);
                Glyph g = this.getGlyph(c);
                if (g != null) this.drawChar(string, g, charIndex, flag);
            }
            GL11.glPopMatrix();
        }
    }

    private Glyph getGlyph(char c) {
        Font font = null;
        for (Font ft: FONTS) {
            if (ft.contains(c)) font = ft; break;
        }

        return font != null ? font.getGlyph(c) : null;
    }

    private int getImageInt(char c) {
        Font font = null;
        for (Font ft: FONTS) {
            if (ft.contains(c)) font = ft; break;
        }

        return font != null ? font.getImagePointer(c) : -1;
    }

    private int getAnInt(char c) {
        Font font = null;
        for (Font ft: FONTS) {
            if (ft.contains(c)) font = ft; break;
        }

        return font != null ? font.getOtherPointer(c) : -1;
    }

    private byte getCharWidth(char c) {
        Font font = null;
        for (Font ft: FONTS) {
            if (ft.contains(c)) font = ft; break;
        }

        return font != null ? font.getWidth(c) : 0;
    }

    private void drawChar(String string, Glyph c, int charIndex, boolean flag) {
        if (this.getCharWidth((char) c.getId()) == 0) return;
        ((Buffer)this.intBuffer).clear();
        GL11.glBindTexture(3553, this.getImageInt((char) c.getId()));

        for(; string.length() > charIndex + 1 && c.getId() == 167; charIndex += 2) {
            int var13 = "0123456789abcdef".indexOf(string.toLowerCase().charAt(charIndex + 1));
            if (var13 < 0 || var13 > 15) {
                var13 = 15;
            }

            this.intBuffer.put(this.getAnInt((char) 0) + 256 + var13 + (flag ? 16 : 0));
            if (this.intBuffer.remaining() == 0) {
                ((Buffer)this.intBuffer).flip();
                GL11.glCallLists(this.intBuffer);
                ((Buffer)this.intBuffer).clear();
            }
        }

        if (charIndex < string.length()) {
            c = this.getGlyph(string.charAt(charIndex));
            if (this.getCharWidth((char) c.getId()) <= 0) return;
            this.intBuffer.put(this.getAnInt((char) c.getId()) + c.getId());
        }

        if (this.intBuffer.remaining() == 0) {
            ((Buffer)this.intBuffer).flip();
            GL11.glCallLists(this.intBuffer);
            ((Buffer)this.intBuffer).clear();
        }
        ((Buffer)this.intBuffer).flip();
        GL11.glCallLists(this.intBuffer);
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
                    Glyph glyph = this.getGlyph((char) index);
                    width += glyph != null ? glyph.getAdvance() : 8.0F;
                }
            }

            return width;
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
