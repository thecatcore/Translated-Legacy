package fr.catcore.translatedlegacy.babric.font;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.catcore.translatedlegacy.babric.util.SimpleNativeImage;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.GlAllocationUtils;
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
        SimpleNativeImage nativeImage;
        try {
            fontImage = ImageIO.read(LegacyUnicodeFont.class.getResourceAsStream(this.fileLocation));
            nativeImage = SimpleNativeImage.read(LegacyUnicodeFont.class.getResourceAsStream(this.fileLocation));
        } catch (IOException var18) {
            throw new RuntimeException(var18);
        } catch (IllegalArgumentException e) {
            return;
        }

        this.imagePointer = arg1.method_1088(fontImage);
        this.anInt = GlAllocationUtils.generateDisplayLists(this.getImagePointer());
        Tessellator tessellator = Tessellator.INSTANCE;

        int imageWidth = fontImage.getWidth();
        int imageHeight = fontImage.getHeight();
        int columnWidth = imageWidth / this.chars.get(0).length;
        int rowHeight = imageHeight / this.chars.size();
        float scaleFactor = (float)this.height / (float)rowHeight;

//        String builder = "\nImage: " + this.fileLocation +
//                "\nImage width: " + imageWidth +
//                "\nImage height: " + imageHeight +
//                "\nColumn width: " + columnWidth +
//                "\nRow height: " + rowHeight +
//                "\nScale factor: " + scaleFactor +
//                "\nLines: " + this.chars.size() +
//                "\nRows: " + this.chars.get(0).length +
//                "\nAscent: " + this.ascent +
//                "\nHeight: " + this.height;
//
//
//        System.out.println(builder);

        int processedLine = 0;

        while(true) {
            if (processedLine >= this.chars.size()) break;

            int charIndex = 0;
            int[] lineChars = this.chars.get(processedLine);
            int charsAmount = lineChars.length;

//            System.out.println(
//                    "\nLine: " + processedLine +
//                            "\nChars: " + charsAmount
//            );

            for(int index = 0; index < charsAmount; ++index) {
                int processedChar = lineChars[index];
                int currentIndex = charIndex++;
                if (processedChar != 0 && processedChar != 32) {
                    int startingX = this.findCharacterStartX(nativeImage, columnWidth, rowHeight, currentIndex, processedLine);

                    int x = currentIndex * columnWidth;
                    int y = processedLine * rowHeight;
                    int advance = (int)(0.5D + (double)((float)startingX * scaleFactor)) + 1;

//                    System.out.println(
//                            "\n'" + (char) processedChar + "' " +
//                                    "\nIndex: " + index +
//                                    "\nId: " + processedChar +
//                                    "\nStarting x: " + startingX +
//                                    "\nX: " + x +
//                                    "\nY: " + y +
//                                    "\nAdvance: " + advance
//                    );

                    GLYPHS.put(processedChar, new BitmapTextureGlyph(
                            fontImage, scaleFactor, x, y,
                            columnWidth, rowHeight, advance,
                            this.ascent, processedChar, this.imagePointer));
                }
            }

            ++processedLine;
        }

        for (Map.Entry<Integer, Glyph> entry : GLYPHS.entrySet()) {
            GL11.glNewList(this.anInt + entry.getKey(), 4864);

            tessellator.startQuads();
            RenderableGlyph glyph = (RenderableGlyph) entry.getValue();

            if (glyph != null) {
                glyph.preDraw(false, 0,0,0,0, tessellator);
            }

            tessellator.draw();

            float width = glyph.getAdvance();

            GL11.glTranslatef(width, 0.0F, 0.0F);
            GL11.glEndList();
        }
    }

    private int findCharacterStartX(SimpleNativeImage image, int characterWidth, int characterHeight, int charPosX, int charPosY) {
        int i;
        for(i = characterWidth - 1; i >= 0; --i) {
            int j = charPosX * characterWidth + i;

            for(int k = 0; k < characterHeight; ++k) {
                int l = charPosY * characterHeight + k;
                if (image.getPixelOpacity(j, l) != 0) {
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
        return (byte) (GLYPHS.containsKey((int) c) ?
                GLYPHS.get((int) c).getAdvance()
                : 8);
    }
}
