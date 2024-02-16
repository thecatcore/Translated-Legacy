package fr.catcore.translatedlegacy.api;

import fr.catcore.translatedlegacy.util.NativeImage;

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

    void upload(Glyph glyph, NativeImage to, int x, int y);
}
