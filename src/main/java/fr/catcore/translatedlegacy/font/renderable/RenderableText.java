package fr.catcore.translatedlegacy.font.renderable;

import fr.catcore.translatedlegacy.font.api.GameProvider;

import java.util.List;

public class RenderableText {
    private final List<Renderable> renderables;

    public RenderableText(List<Renderable> renderables) {
        this.renderables = renderables;
    }

    public void render(int x, int y, int defaultColor, int blitOffset, GameProvider game, boolean flag) {
        int currentX = x;

        for (Renderable renderable : this.renderables) {
            renderable.render(currentX, y, defaultColor, blitOffset, game, flag);
            currentX += renderable.getWidth();
        }
    }
}
