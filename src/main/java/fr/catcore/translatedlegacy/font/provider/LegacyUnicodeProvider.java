package fr.catcore.translatedlegacy.font.provider;

import com.google.gson.JsonObject;
import fr.catcore.translatedlegacy.font.NativeTexture;
import fr.catcore.translatedlegacy.font.TextRenderer;
import fr.catcore.translatedlegacy.font.api.GameProvider;
import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;
import fr.catcore.translatedlegacy.util.NativeImage;
import fr.catcore.translatedlegacy.util.TextUtils;

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

        int imageWidth = nativeImage.getWidth();
        int imageHeight = nativeImage.getHeight();

        if (imageWidth != 256 || imageHeight != 256) {
            nativeImage.close();
            this.ignoredBlocks.add(blockIndex);
            return;
        }

        for (int k = 0; k < 256; k++) {
            byte width = this.CHARS_WIDTH[startIndex + k];
            if (width != 0 && getStart(width) > getEnd(width)) {
                this.CHARS_WIDTH[startIndex + k] = 0;
            }
        }

        int columnWidth = imageWidth / 16;
        int rowHeight = imageHeight / 16;

        int[] markCircles = new int[256];

        // Must happen before the texture is uploaded and before widths are measured
        for (int k = 0; k < 256; k++) {
            markCircles[k] = TextUtils.isCombiningMark((char) (startIndex + k))
                    ? stripMarkCircle(nativeImage, (k % 16) * columnWidth, (k / 16) * rowHeight)
                    : -1;
        }

        NativeTexture nativeTexture = new NativeTexture(nativeImage);

        for (int rowIndex = 0; rowIndex < 16; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 16; columnIndex++) {
                int charIndex = startIndex + rowIndex * 16 + columnIndex;

                int width = findCharacterStartX(nativeImage, columnWidth, rowHeight, columnIndex, rowIndex) + 1;
                int inkLeft = findCharacterInkLeft(nativeImage, columnWidth, rowHeight, columnIndex, rowIndex);
                int x = columnIndex * columnWidth;
                int y = rowIndex * rowHeight;

                this.glyphs.put((char) charIndex, new LegacyUnicodeGlyph(
                        this,
                        (char) charIndex,
                        width,
                        x,
                        y,
                        inkLeft,
                        markCircles[charIndex - startIndex]
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

    private int findCharacterInkLeft(NativeImage image, int characterWidth, int characterHeight, int columnIndex, int rowIndex) {
        for (int i = 0; i < characterWidth; i++) {
            int j = columnIndex * characterWidth + i;

            for (int k = 0; k < characterHeight; ++k) {
                if (image.getPixelOpacity(j, rowIndex * characterHeight + k) != 0) {
                    return i;
                }
            }
        }

        return 0;
    }

    /**
     * Unifont draws combining marks around a dotted circle standing in for the base letter.
     * The circle is always the same 6 dots on rows 6, 8 and 10 of the cell, only its column varies:
     * <pre>
     * .#..#.
     * ......
     * #....#
     * ......
     * .#..#.
     * </pre>
     * Finds it, erases the dots that don't touch the mark's own strokes, and returns its leftmost
     * column so the mark can be positioned against the real base letter. -1 if there is no circle.
     */
    private static int stripMarkCircle(NativeImage image, int cellX, int cellY) {
        int bestX = -1;
        int bestScore = 0;
        int bestOverlap = Integer.MAX_VALUE;

        for (int a = 0; a + 5 < 16; a++) {
            int score = 0;

            for (int[] dot : CIRCLE_DOTS) {
                if (isInked(image, cellX, cellY, a + dot[1], CIRCLE_TOP + dot[0])) score++;
            }

            if (score < 4) continue;

            // Prefer the candidate with an empty middle, i.e. the one that really is a circle
            int overlap = 0;

            for (int row = 0; row < 5; row++) {
                for (int column = 1; column < 5; column++) {
                    if (isInked(image, cellX, cellY, a + column, CIRCLE_TOP + row) && !isCircleDot(row, column)) overlap++;
                }
            }

            if (score > bestScore || (score == bestScore && overlap < bestOverlap)) {
                bestX = a;
                bestScore = score;
                bestOverlap = overlap;
            }
        }

        if (bestX < 0) return -1;

        for (int[] dot : CIRCLE_DOTS) {
            int x = bestX + dot[1];
            int y = CIRCLE_TOP + dot[0];

            if (!isInked(image, cellX, cellY, x, y) || hasInkedNeighbour(image, cellX, cellY, x, y)) continue;

            image.setColor(cellX + x, cellY + y, 0);
        }

        return bestX;
    }

    private static final int CIRCLE_TOP = 6;
    // {row, column} relative to the top-left of the circle
    private static final int[][] CIRCLE_DOTS = {{0, 1}, {0, 4}, {2, 0}, {2, 5}, {4, 1}, {4, 4}};

    private static boolean isCircleDot(int row, int column) {
        for (int[] dot : CIRCLE_DOTS) {
            if (dot[0] == row && dot[1] == column) return true;
        }

        return false;
    }

    private static boolean isInked(NativeImage image, int cellX, int cellY, int x, int y) {
        if (x < 0 || y < 0 || x >= 16 || y >= 16) return false;

        return image.getPixelOpacity(cellX + x, cellY + y) != 0;
    }

    private static boolean hasInkedNeighbour(NativeImage image, int cellX, int cellY, int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if ((dx != 0 || dy != 0) && isInked(image, cellX, cellY, x + dx, y + dy)) return true;
            }
        }

        return false;
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

    @Override
    public NativeTexture getTexture(Glyph glyph) {
        return this.textures.get(glyph.getChar()/256);
    }
}
