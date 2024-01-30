package fr.catcore.translatedlegacy.api;

import java.util.ArrayList;
import java.util.List;

public class TextRenderer {
    private final GameProvider game;
    private final List<GlyphProvider> providers = new ArrayList<>();

    public TextRenderer(GameProvider game) {
        this.game = game;
    }
}
