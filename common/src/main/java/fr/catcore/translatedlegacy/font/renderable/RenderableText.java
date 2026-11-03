package fr.catcore.translatedlegacy.font.renderable;

import fr.catcore.translatedlegacy.font.GlyphContainer;
import fr.catcore.translatedlegacy.font.api.GameProvider;

import java.util.List;

public class RenderableText {
    private final List<GlyphContainer> containers;

    public RenderableText(List<GlyphContainer> containers) {
        this.containers = containers;
    }

    public void render(int x, int y, int defaultColor, int blitOffset, GameProvider game, boolean flag) {
        int currentX = x;

        for (GlyphContainer container : this.containers) {
            container.render(currentX, y, defaultColor, blitOffset, game, flag);
            currentX += container.getWidth();
        }
    }

    public int getWidth() {
        return containers.stream().mapToInt(GlyphContainer::getWidth).sum();
    }
    public int getHeight() {
        return containers.stream().mapToInt(GlyphContainer::getHeight).max().orElse(8);
    }
}
