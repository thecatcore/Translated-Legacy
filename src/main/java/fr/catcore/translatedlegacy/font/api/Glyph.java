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

        float x0 = (float) getXStart() / (float) texture.getWidth();
        float x1 = (float) (getXStart() + getWidth()) / (float) texture.getWidth();

        float y0 = (float) getYStart() / (float) texture.getHeight();
        float y1 = (float) (getYStart() + getHeight()) / (float) texture.getHeight();

        gameProvider.draw(x, y, width, height, x0, y0, x1, y1, blitOffset, italic);
    }

    /**
     * Adds this glyph's quad to an already-open batch. Caller is responsible for binding
     * {@code texture} and for {@link GameProvider#beginGlyphBatch()}/{@link GameProvider#endGlyphBatch()}.
     */
    default void drawBatched(GameProvider gameProvider, NativeTexture texture, int x, int y, int width, int height, float blitOffset, boolean italic) {
        float x0 = (float) getXStart() / (float) texture.getWidth();
        float x1 = (float) (getXStart() + getWidth()) / (float) texture.getWidth();

        float y0 = (float) getYStart() / (float) texture.getHeight();
        float y1 = (float) (getYStart() + getHeight()) / (float) texture.getHeight();

        gameProvider.drawBatchedQuad(x, y, width, height, x0, y0, x1, y1, blitOffset, italic);
    }
}
