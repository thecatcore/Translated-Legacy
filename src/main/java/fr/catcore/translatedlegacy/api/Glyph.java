package fr.catcore.translatedlegacy.api;

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
        int destX = x;

        if (destX < 0) destX = 0;

        from.copyRect(to, getXStart(), getYStart(), destX, y, getWidth(), getHeight(), flipX, flipY);
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
