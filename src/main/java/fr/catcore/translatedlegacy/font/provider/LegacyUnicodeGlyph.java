package fr.catcore.translatedlegacy.font.provider;

import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;

public class LegacyUnicodeGlyph implements Glyph {
    private final GlyphProvider glyphProvider;
    private final Character character;
    private final int width, x, y;

    public LegacyUnicodeGlyph(GlyphProvider glyphProvider, Character character, int width, int x, int y) {
        this.glyphProvider = glyphProvider;
        this.character = character;
        this.width = width;
        this.x = x;
        this.y = y;
    }

    @Override
    public int getWidth() {
        return this.width;
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
