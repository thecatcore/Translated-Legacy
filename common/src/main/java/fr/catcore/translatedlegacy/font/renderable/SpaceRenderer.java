package fr.catcore.translatedlegacy.font.renderable;

import fr.catcore.translatedlegacy.font.TextRenderer;

public class SpaceRenderer implements RenderableItem {
    @Override
    public boolean isSpace() {
        return true;
    }

    @Override
    public int getWidth() {
        return TextRenderer.getSpaceWidth();
    }

    @Override
    public int getHeight() {
        return 8;
    }
}
