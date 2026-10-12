package fr.catcore.translatedlegacy.font.api;

import fr.catcore.translatedlegacy.font.NativeTexture;

import java.io.IOException;
import java.util.List;

public interface GlyphProvider {
    boolean provides(Character c);
    Glyph getGlyph(Character c);

    String getId();

    List<Glyph> getShowcasedGlyphs();

    boolean isLoaded();
    void load() throws IOException;
    void unload();

    default float scalingFactor() {
        return 1.0F;
    }

    void draw(Glyph glyph, GameProvider game, int x, int y, int width, int height, float blitOffset, boolean italic);

    /**
     * The texture a glyph from this provider would be drawn from, without drawing it.
     * Lets callers batch consecutive glyphs sharing a texture instead of binding per glyph.
     */
    NativeTexture getTexture(Glyph glyph);

    default void drawBatched(Glyph glyph, GameProvider game, int x, int y, int width, int height, float blitOffset, boolean italic) {
        glyph.drawBatched(game, getTexture(glyph), x, y, width, height, blitOffset, italic);
    }
}
