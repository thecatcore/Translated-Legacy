package fr.catcore.translatedlegacy.font;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.class_214;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitmapFont extends Font {

    private final String fileLocation;
    private final int ascent;
    private int height = 8;
    private final List<int[]> chars = new ArrayList<>();
    private final Map<Integer, Glyph> GLYPHS = new HashMap<>();
    private int imagePointer;
    private int anInt;

    protected BitmapFont(JsonObject object) {
        this.fileLocation = "/assets/" + object.get("file").getAsString().replace(":", "/textures/");
        System.out.println(this.fileLocation);
        this.ascent = object.get("ascent").getAsInt();

        for (JsonElement element : object.getAsJsonArray("chars")) {
            int[] is = element.getAsString().codePoints().toArray();
            chars.add(is);
        }

        if (object.has("height")) {
            this.height = object.get("height").getAsInt();
        }
    }

    @Override
    public boolean contains(char c) {
        return GLYPHS.containsKey((int)c);
    }

    @Override
    protected Glyph getGlyph(char c) {
        return GLYPHS.get((int)c);
    }

    @Override
    protected void loadTextures(GameOptions arg, TextureManager arg1) {
        BufferedImage fontImage;
        try {
            fontImage = ImageIO.read(LegacyUnicodeFont.class.getResourceAsStream(this.fileLocation));
        } catch (IOException var18) {
            throw new RuntimeException(var18);
        } catch (IllegalArgumentException e) {
            return;
        }

        this.imagePointer = arg1.glLoadImage(fontImage);
        this.anInt = class_214.method_741(this.getImagePointer());
        Tessellator tessellator = Tessellator.INSTANCE;

        int imageWidth = fontImage.getWidth();
        int imageHeight = fontImage.getHeight();
        int k = imageWidth / this.chars.get(0).length;
        int l = imageHeight / this.chars.size();
        float f = (float)this.height / (float)l;

        int m = 0;

        while(true) {
            if (m >= this.chars.size()) break;

            int n = 0;
            int[] var12 = this.chars.get(m);
            int var13 = var12.length;

            for(int var14 = 0; var14 < var13; ++var14) {
                int o = var12[var14];
                int p = n++;
                if (o != 0 && o != 32) {
                    int q = this.findCharacterStartX(fontImage, k, l, p, m);
                    GLYPHS.put(o, new BitmapTextureGlyph(fontImage, f, p * k, m * l, k, l, (int)(0.5D + (double)((float)q * f)) + 1, this.ascent, o, this.imagePointer));
                }
            }

            ++m;
        }

        for (Map.Entry<Integer, Glyph> entry : GLYPHS.entrySet()) {
            GL11.glNewList(this.anInt + entry.getKey(), 4864);

            tessellator.start();
            RenderableGlyph glyph = (RenderableGlyph) entry.getValue();

            if (glyph != null) {
                glyph.preDraw(false, 0,0,0,0, tessellator);
            }

            tessellator.draw();

            GL11.glTranslatef(glyph.getWidth(), 0.0F, 0.0F);
            GL11.glEndList();
        }
    }

    private int findCharacterStartX(BufferedImage image, int characterWidth, int characterHeight, int charPosX, int charPosY) {
        int i;
        for(i = characterWidth - 1; i >= 0; --i) {
            int j = charPosX * characterWidth + i;

            for(int k = 0; k < characterHeight; ++k) {
                int l = charPosY * characterHeight + k;
                if (image.getRGB(j, l) != 0) {
                    return i + 1;
                }
            }
        }

        return i + 1;
    }

    private int getImagePointer() {
        return 288 + (
                this.fileLocation.contains("ascii") ? 1 :
                        this.fileLocation.contains("nonlatin_european") ? 2 : 3
                );
    }

    @Override
    protected int getImagePointer(char c) {
        return this.imagePointer;
    }

    @Override
    protected int getOtherPointer(char c) {
        return this.anInt;
    }

    @Override
    protected byte getWidth(char c) {
        return (byte) (GLYPHS.containsKey(c) ? ((RenderableGlyph)GLYPHS.get(c)).getAdvance() : 8);
    }
}
