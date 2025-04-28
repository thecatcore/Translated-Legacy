package fr.catcore.translatedlegacy.font.renderable;

import fr.catcore.translatedlegacy.font.api.GameProvider;
import fr.catcore.translatedlegacy.font.api.Glyph;

public class GlyphRenderer implements RenderableItem {
    public Glyph glyph;

    public GlyphRenderer(Glyph glyph) {
        this.glyph = glyph;
    }

    public void draw(GameProvider game, int x, int y, int blitoffset, boolean italic) {
        this.glyph.getProvider()
                .draw(
                        this.glyph,
                        game,
                        x,
                        y,
                        getWidth(),
                        getHeight(),
                        blitoffset,
                        italic
                );
    }

    @Override
    public int getWidth() {
        return (int) (glyph.getWidth() * glyph.getProvider().scalingFactor());
    }

    @Override
    public int getHeight() {
        return (int) (glyph.getHeight() * glyph.getProvider().scalingFactor());
    }
}
