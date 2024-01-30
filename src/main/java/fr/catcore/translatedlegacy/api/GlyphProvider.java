package fr.catcore.translatedlegacy.api;

public interface GlyphProvider {
    boolean provides(Character c);
    Glyph getGlyph(Character c);

    String getId();
}
