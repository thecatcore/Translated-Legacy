package fr.catcore.translatedlegacy.font.renderable;

import fr.catcore.translatedlegacy.font.TextRenderer;
import fr.catcore.translatedlegacy.font.api.Glyph;

public class ObfuscatedGlyphRenderer extends GlyphRenderer {
    private final Glyph originalGlyph;

    public ObfuscatedGlyphRenderer(Glyph glyph) {
        super(glyph);
        this.originalGlyph = glyph;
    }

    @Override
    public void beforeRender() {
        this.glyph = TextRenderer.getObfuscated(originalGlyph.getWidth(), originalGlyph.getHeight());
    }
}
