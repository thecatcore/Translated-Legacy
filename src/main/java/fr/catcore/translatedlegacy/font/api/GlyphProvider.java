package fr.catcore.translatedlegacy.font.api;

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
}
