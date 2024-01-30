package fr.catcore.translatedlegacy.api;

public interface Glyph extends AutoCloseable {
    int getWidth();
    int getHeight();

    GlyphProvider getProvider();
    Character getChar();

    default String getId() {
        return getProvider().getId() + "::" + getChar();
    }
}
