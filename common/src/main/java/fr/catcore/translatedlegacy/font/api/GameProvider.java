package fr.catcore.translatedlegacy.font.api;

import fr.catcore.translatedlegacy.font.TextInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface GameProvider {
    boolean anaglyph3d();

    /**
     * Splits a string containing formatting codes into runs of identically styled text, in logical order.
     */
    List<TextInfo> parseText(String text);

    default void draw(int x, int y, int width, int height, float blitOffset, boolean italic) {
        draw(x, y, width, height, 0, 0, 1, 1, blitOffset, italic);
    }

    void draw(int x, int y, int width, int height, float u0, float v0, float u1, float v1, float blitOffset, boolean italic);

    /**
     * Starts a batch of glyph quads sharing one bound texture; must be paired with {@link #endGlyphBatch()}.
     */
    void beginGlyphBatch();

    /**
     * Adds a quad to the currently open glyph batch. Does not bind a texture or flush on its own.
     */
    void drawBatchedQuad(float x, float y, float width, float height, float u0, float v0, float u1, float v1, float blitOffset, boolean italic);

    /**
     * Flushes the quads accumulated since {@link #beginGlyphBatch()} in a single draw call.
     */
    void endGlyphBatch();

    void doDecorations(int posX, int posY, float charWidth, float charHeight, boolean strikethroughStyle, boolean underlineStyle);

    InputStream getResource(String path) throws IOException;
}
