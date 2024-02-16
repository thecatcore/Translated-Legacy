package fr.catcore.translatedlegacy.babric.fontnewnew;

import fr.catcore.translatedlegacy.api.Glyph;
import fr.catcore.translatedlegacy.api.GlyphProvider;

public class OriginalGlyph implements Glyph {
    private final int width, x, y, index;
    private final GlyphProvider provider;

    public OriginalGlyph(int width, int x, int y, int index, GlyphProvider provider) {
        this.width = width;
        this.x = x;
        this.y = y;
        this.index = index;
        this.provider = provider;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return 8;
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public int getXStart() {
        return x;
    }

    @Override
    public int getYStart() {
        return y;
    }

    @Override
    public GlyphProvider getProvider() {
        return this.provider;
    }

    @Override
    public Character getChar() {
        return (char) this.index;
    }
}
