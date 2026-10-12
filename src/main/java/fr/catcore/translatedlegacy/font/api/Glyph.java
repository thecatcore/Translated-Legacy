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

    /**
     * Leftmost inked column, in texels relative to {@link #getXStart()}.
     */
    default float getInkLeft() {
        return 0;
    }

    /**
     * Column just past the rightmost inked one, in texels relative to {@link #getXStart()}.
     */
    default float getInkRight() {
        return this.getWidth();
    }

    /**
     * Horizontal point, in texels, that combining marks are centered on (for a base glyph)
     * or centered with (for a mark glyph).
     */
    default float getAnchorX() {
        return (this.getInkLeft() + this.getInkRight()) / 2.0F;
    }

    /**
     * For a combining mark drawn around a dotted-circle placeholder (as Unifont does), the
     * leftmost texel column of that circle, which spans 6 columns. -1 if there is none.
     */
    default int getMarkCircleX() {
        return -1;
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
    default void drawBatched(GameProvider gameProvider, NativeTexture texture, float x, float y, float width, float height, float blitOffset, boolean italic) {
        drawBatched(gameProvider, texture, 0, getWidth(), x, y, width, height, blitOffset, italic);
    }

    /**
     * Same as {@link #drawBatched(GameProvider, NativeTexture, float, float, float, float, float, boolean)},
     * but only draws texel columns {@code [fromX, toX)} of the glyph.
     */
    default void drawBatched(GameProvider gameProvider, NativeTexture texture, int fromX, int toX, float x, float y, float width, float height, float blitOffset, boolean italic) {
        float x0 = (float) (getXStart() + fromX) / (float) texture.getWidth();
        float x1 = (float) (getXStart() + toX) / (float) texture.getWidth();

        float y0 = (float) getYStart() / (float) texture.getHeight();
        float y1 = (float) (getYStart() + getHeight()) / (float) texture.getHeight();

        gameProvider.drawBatchedQuad(x, y, width, height, x0, y0, x1, y1, blitOffset, italic);
    }
}
