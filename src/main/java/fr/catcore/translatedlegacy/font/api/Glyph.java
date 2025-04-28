package fr.catcore.translatedlegacy.font.api;

import fr.catcore.translatedlegacy.font.NativeTexture;
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
        return getProvider().getId() + ":" + ((int) getChar());
    }

    default void draw(GameProvider gameProvider, NativeTexture texture, int x, int y, int width, int height, float blitOffset, boolean italic) {
        texture.bind();

        int textureWidth = texture.getWidth();
        int textureHeight = texture.getHeight();

        float x0 = (float) getXStart() / (float) textureWidth;
        float x1 = (float) (getXStart() + getWidth()) / (float) textureWidth;

        float y0 = (float) getYStart() / (float) textureHeight;
        float y1 = (float) (getYStart() + getHeight()) / (float) textureHeight;

        gameProvider.draw(x, y, width, height, x0, y0, x1, y1, blitOffset, italic);
    }
}
