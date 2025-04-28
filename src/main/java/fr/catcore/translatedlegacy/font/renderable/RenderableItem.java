package fr.catcore.translatedlegacy.font.renderable;

import fr.catcore.translatedlegacy.font.api.GameProvider;

public interface RenderableItem {
    default boolean isSpace() {
        return false;
    }

    int getWidth();
    int getHeight();
    default void beforeRender() {}

    default void draw(GameProvider game, int x, int y, int blitoffset, boolean italic) {}
}
