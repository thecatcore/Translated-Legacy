package fr.catcore.translatedlegacy.font.provider;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.catcore.translatedlegacy.font.NativeTexture;
import fr.catcore.translatedlegacy.font.TextRenderer;
import fr.catcore.translatedlegacy.font.api.GameProvider;
import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;
import fr.catcore.translatedlegacy.util.NativeImage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BitmapGlyphProvider implements GlyphProvider {
    private final Map<Character, Glyph> glyphs = new HashMap<>();
    private final String texturePath;
    private final int ascent, height;
    private final List<int[]> chars = new ArrayList<>();
    private final Set<Character> charSet;
    private NativeTexture fullTexture;

    public BitmapGlyphProvider(JsonObject obj) {
        String path = obj.get("file").getAsString();

        if (path.contains(":")) {
            String[] split = path.split(":");
            path = "/assets/" + split[0] + "/textures/" + split[1];
        }

        this.texturePath = path;

        for (JsonElement element : obj.getAsJsonArray("chars")) {
            int[] is = element.getAsString().codePoints().toArray();
            this.chars.add(is);
        }

        this.ascent = obj.get("ascent").getAsInt();
        this.height = obj.has("height") ? obj.get("height").getAsInt() : 8;
        this.charSet = chars.stream().flatMapToInt(Arrays::stream).mapToObj(x -> (Character) (char) x).filter(x -> x != 0).collect(Collectors.toSet());
    }

    @Override
    public boolean provides(Character c) {
        return charSet.contains(c);
    }

    @Override
    public Glyph getGlyph(Character c) {
        return glyphs.get(c);
    }

    @Override
    public String getId() {
        return this.texturePath;
    }

    @Override
    public List<Glyph> getShowcasedGlyphs() {
        List<Glyph> showcased = new ArrayList<>();
        showcased.add(getGlyph('A'));

        return showcased;
    }

    @Override
    public boolean isLoaded() {
        return fullTexture != null;
    }

    @Override
    public void load() throws IOException {
        NativeImage fullImage = NativeImage.read(NativeImage.Format.RGBA, TextRenderer.getGameProvider().getResource(texturePath));
        fullTexture = new NativeTexture(fullImage);

        int imageWidth = fullTexture.getWidth();
        int imageHeight = fullTexture.getHeight();
        int columnWidth = imageWidth / this.chars.get(0).length;
        int rowHeight = imageHeight / this.chars.size();
        float scaleFactor = (float) this.height / (float) rowHeight;

        for (int rowIndex = 0; rowIndex < this.chars.size(); rowIndex++) {
            int[] row = this.chars.get(rowIndex);

            for (int columnIndex = 0; columnIndex < row.length; columnIndex++) {
                int currentChar = row[columnIndex];

                if (currentChar != 0 && currentChar != 32) {
                    int startingX = findCharacterStartX(fullImage, columnWidth, rowHeight, columnIndex, rowIndex) + 1;

                    int x = columnIndex * columnWidth;
                    int y = rowIndex * rowHeight;
                    int width = (int)(0.5D + (double)((float)startingX * scaleFactor)) + 1;

                    glyphs.put((char) currentChar, new BitmapGlyph(width, this.height, x, y, currentChar, this));
                }
            }
        }
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

    @Override
    public void unload() {
        if (fullTexture != null) {
            fullTexture.close();
            fullTexture = null;
            this.glyphs.clear();
        }
    }

    @Override
    public void upload(Glyph glyph, NativeImage to, int x, int y) {
//        glyph.upload(fullTexture, to, x, y);
    }

    @Override
    public void draw(Glyph glyph, GameProvider game, int x, int y, int width, int height, float blitOffset, boolean italic) {
        glyph.draw(game, fullTexture, x, y, width, height, blitOffset, italic);
    }
}
