package fr.catcore.translatedlegacy.font.provider;

import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;

public class BitmapGlyph implements Glyph {
    private final int width, height, x, y, index;
    private final GlyphProvider provider;

    public BitmapGlyph(int width, int height, int x, int y, int index, GlyphProvider provider) {
        this.width = width;
        this.height = height;
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
        return this.height;
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
