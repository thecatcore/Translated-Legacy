package fr.catcore.translatedlegacy.font.provider;

import com.google.gson.JsonObject;
import fr.catcore.translatedlegacy.font.NativeTexture;
import fr.catcore.translatedlegacy.font.TextRenderer;
import fr.catcore.translatedlegacy.font.api.GameProvider;
import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;
import fr.catcore.translatedlegacy.util.NativeImage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegacyUnicodeProvider implements GlyphProvider {
    private final Map<Character, Glyph> glyphs = new HashMap<>();
    private final Map<Integer, NativeTexture> textures = new HashMap<>();
    private byte[] CHARS_WIDTH = null;
    private final List<Integer> ignoredBlocks = new ArrayList<>();

    private final String sizesLocation;
    private final String template;

    public LegacyUnicodeProvider(JsonObject object) {
        this.sizesLocation = "/assets/" + object.get("sizes").getAsString().replace(":", "/");
        this.template = "/assets/" + object.get("template").getAsString().replace(":", "/textures/");
    }

    @Override
    public boolean provides(Character c) {
        int blockIndex = c/256;

        if (this.ignoredBlocks.contains(blockIndex)) return false;

        return c <= 65536;
    }

    @Override
    public Glyph getGlyph(Character c) {
        int blockIndex = c/256;

        if (!this.textures.containsKey(blockIndex)) {
            try {
                this.loadTexture(blockIndex);
            } catch (IOException e) {

            }
        }

        return glyphs.get(c);
    }

    @Override
    public String getId() {
        return "minecraft:legacy_unicode";
    }

    @Override
    public List<Glyph> getShowcasedGlyphs() {
        return new ArrayList<>();
    }

    @Override
    public boolean isLoaded() {
        return CHARS_WIDTH != null;
    }

    @Override
    public void load() {
        InputStream inputStream = null;

        try {
            this.CHARS_WIDTH = new byte[65536];
            inputStream = TextRenderer.getGameProvider().getResource(this.sizesLocation);
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

    @Override
    public void unload() {
        this.CHARS_WIDTH = null;

        this.textures.values().forEach(NativeTexture::close);
        this.textures.clear();
        this.glyphs.clear();
        this.ignoredBlocks.clear();
    }

    @Override
    public float scalingFactor() {
        return 0.5F;
    }

    private void loadTexture(int blockIndex) throws IOException {
        int startIndex = blockIndex * 256;

        String path = String.format(String.format(this.template, "%02x"), blockIndex);

        InputStream inputStream = TextRenderer.getGameProvider().getResource(path);

        if (inputStream == null) {
            System.err.println("Failed to find font texture: " + path);
            this.ignoredBlocks.add(blockIndex);
            return;
        }

        NativeImage nativeImage = NativeImage.read(NativeImage.Format.RGBA, inputStream);
        NativeTexture nativeTexture = new NativeTexture(nativeImage);

        int imageWidth = nativeTexture.getWidth();
        int imageHeight = nativeTexture.getHeight();

        if (imageWidth != 256 || imageHeight != 256) return;

        for (int k = 0; k < 256; k++) {
            byte width = this.CHARS_WIDTH[startIndex + k];
            if (width != 0 && getStart(width) > getEnd(width)) {
                this.CHARS_WIDTH[startIndex + k] = 0;
            }
        }

        int columnWidth = imageWidth / 16;
        int rowHeight = imageHeight / 16;

        for (int rowIndex = 0; rowIndex < 16; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 16; columnIndex++) {
                int charIndex = startIndex + rowIndex * 16 + columnIndex;

                int width = findCharacterStartX(nativeImage, columnWidth, rowHeight, columnIndex, rowIndex) + 1;
                int x = columnIndex * columnWidth;
                int y = rowIndex * rowHeight;

                this.glyphs.put((char) charIndex, new LegacyUnicodeGlyph(
                        this,
                        (char) charIndex,
                        width,
                        x,
                        y
                ));
            }
        }

        this.textures.put(blockIndex, nativeTexture);
    }

    private int findCharacterStartX(NativeImage image, int characterWidth, int characterHeight, int columnIndex, int rowIndex) {
        int i;

        for (i = characterWidth - 1; i >= 0; --i) {
            int j = columnIndex * characterWidth + i;

            for (int k = 0; k < characterHeight; ++k) {
                int l = rowIndex * characterHeight + k;
                if (image.getPixelOpacity(j, l) != 0) {
                    return i;
                }
            }
        }

        return i;
    }

    private static int getStart(byte size) {
        return size >> 4 & 15;
    }

    private static int getEnd(byte size) {
        return (size & 15) + 1;
    }

    @Override
    public void draw(Glyph glyph, GameProvider game, int x, int y, int width, int height, float blitOffset, boolean italic) {
        int blockIndex = glyph.getChar()/256;
        glyph.draw(game, this.textures.get(blockIndex), x, y, width, height, blitOffset, italic);
    }
}
