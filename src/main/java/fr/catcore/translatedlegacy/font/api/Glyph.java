package fr.catcore.translatedlegacy.font.api;

import fr.catcore.translatedlegacy.util.NativeImage;

public interface Glyph {
    int getWidth();
    int getHeight();
    int getOffset();
    int getXStart();
    int getYStart();

    default void upload(NativeImage from, NativeImage to, int x, int y) {
        upload(from, to, x, y, false, false);
    }

    default void upload(NativeImage from, NativeImage to, int x, int y, boolean flipX, boolean flipY) {
        from.copyRect(to, getXStart(), getYStart(), x, y, getWidth(), getHeight(), flipX, flipY);
    }

    default int getFullWidth() {
        return this.getWidth();
    }

    GlyphProvider getProvider();
    Character getChar();

    default String getId() {
        return getProvider().getId() + "::" + getChar();
    }
}
