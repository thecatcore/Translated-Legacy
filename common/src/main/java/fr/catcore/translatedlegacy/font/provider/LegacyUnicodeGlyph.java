package fr.catcore.translatedlegacy.font.provider;

import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;

public class LegacyUnicodeGlyph implements Glyph {
    private final GlyphProvider glyphProvider;
    private final Character character;
    private final int width, x, y, inkLeft, markCircleX;

    public LegacyUnicodeGlyph(GlyphProvider glyphProvider, Character character, int width, int x, int y) {
        this(glyphProvider, character, width, x, y, 0, -1);
    }

    public LegacyUnicodeGlyph(GlyphProvider glyphProvider, Character character, int width, int x, int y, int inkLeft, int markCircleX) {
        this.glyphProvider = glyphProvider;
        this.character = character;
        this.width = width;
        this.x = x;
        this.y = y;
        this.inkLeft = inkLeft;
        this.markCircleX = markCircleX;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public float getInkLeft() {
        return this.inkLeft;
    }

    @Override
    public float getAnchorX() {
        // Unifont centers marks on the middle of the cell (half-width: 8 texels, full-width: 16),
        // not on the ink, so asymmetric letters still get their marks where the font expects them.
        if (this.markCircleX >= 0) return this.markCircleX + 3;

        return this.width > 8 ? 8 : 4;
    }

    @Override
    public int getMarkCircleX() {
        return this.markCircleX;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public int getXStart() {
        return this.x;
    }

    @Override
    public int getYStart() {
        return this.y;
    }

    @Override
    public GlyphProvider getProvider() {
        return this.glyphProvider;
    }

    @Override
    public Character getChar() {
        return this.character;
    }
}
