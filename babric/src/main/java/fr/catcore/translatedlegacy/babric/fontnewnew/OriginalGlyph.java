package fr.catcore.translatedlegacy.babric.fontnewnew;

import fr.catcore.translatedlegacy.api.Glyph;
import fr.catcore.translatedlegacy.api.GlyphProvider;

public class OriginalGlyph implements Glyph {
    private final int offset, x, y, index;
    private final GlyphProvider provider;

    public OriginalGlyph(int offset, int x, int y, int index, GlyphProvider provider) {
        this.offset = offset;
        this.x = x;
        this.y = y;
        this.index = index;
        this.provider = provider;

        System.out.println("'" + ((char) index) + "' x:" + x + " y:" + y + " offset:" + offset);
    }

    @Override
    public int getWidth() {
        return getOffset();
    }

    @Override
    public int getHeight() {
        return 8;
    }

    @Override
    public int getOffset() {
        return offset;
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
