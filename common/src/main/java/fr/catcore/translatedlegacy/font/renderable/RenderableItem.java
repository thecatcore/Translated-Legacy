package fr.catcore.translatedlegacy.font.renderable;

import fr.catcore.translatedlegacy.font.NativeTexture;
import fr.catcore.translatedlegacy.font.api.GameProvider;

public interface RenderableItem {
    default boolean isSpace() {
        return false;
    }

    int getWidth();
    int getHeight();
    default void beforeRender() {}

    default void draw(GameProvider game, int x, int y, int blitoffset, boolean italic) {}

    /**
     * The texture this item would draw from, or null if it draws nothing (e.g. a space).
     * Used to group consecutive items sharing a texture into one tessellator batch.
     */
    default NativeTexture getTexture() {
        return null;
    }

    /**
     * Adds this item's quad(s) to an already-open batch; see {@link GameProvider#beginGlyphBatch()}.
     */
    default void drawBatched(GameProvider game, int x, int y, int blitoffset, boolean italic) {}
}
