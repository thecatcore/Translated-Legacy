package fr.catcore.translatedlegacy.font.api;

import java.io.IOException;
import java.io.InputStream;

public interface GameProvider {
    boolean anaglyph3d();

    default void draw(int x, int y, int width, int height, float blitOffset) {
        draw(x, y, width, height, 0, 0, 1, 1, blitOffset);
    }

    void draw(int x, int y, int width, int height, float u0, float v0, float u1, float v1, float blitOffset);

    InputStream getResource(String path) throws IOException;
}
