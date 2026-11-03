package fr.catcore.translatedlegacy.font.renderable;

import fr.catcore.translatedlegacy.font.NativeTexture;
import fr.catcore.translatedlegacy.font.api.GameProvider;
import fr.catcore.translatedlegacy.font.api.Glyph;

public class GlyphRenderer implements RenderableItem {
    public Glyph glyph;

    // Where the glyph is drawn relative to the pen position, used to place combining marks
    private float offsetX = 0;
    // Texel columns of the glyph to draw, or -1 to draw all of it
    private final int fromX, toX;
    // How far the pen moves after this item, or -1 for the glyph's own width
    private int advance = -1;

    public GlyphRenderer(Glyph glyph) {
        this(glyph, 0, -1, -1);
    }

    public GlyphRenderer(Glyph glyph, float offsetX) {
        this(glyph, offsetX, -1, -1);
    }

    public GlyphRenderer(Glyph glyph, float offsetX, int fromX, int toX) {
        this.glyph = glyph;
        this.offsetX = offsetX;
        this.fromX = fromX;
        this.toX = toX;
    }

    public void shift(float dx) {
        this.offsetX += dx;
    }

    public void setAdvance(int advance) {
        this.advance = advance;
    }

    public void draw(GameProvider game, int x, int y, int blitoffset, boolean italic) {
        this.glyph.getProvider()
                .draw(
                        this.glyph,
                        game,
                        Math.round(x + this.offsetX),
                        y,
                        getGlyphWidth(),
                        getHeight(),
                        blitoffset,
                        italic
                );
    }

    @Override
    public NativeTexture getTexture() {
        return this.glyph.getProvider().getTexture(this.glyph);
    }

    @Override
    public void drawBatched(GameProvider game, int x, int y, int blitoffset, boolean italic) {
        if (this.fromX < 0) {
            this.glyph.getProvider()
                    .drawBatched(
                            this.glyph,
                            game,
                            x + this.offsetX,
                            y,
                            getGlyphWidth(),
                            getHeight(),
                            blitoffset,
                            italic
                    );
        } else if (this.toX > this.fromX) {
            float scale = this.glyph.getProvider().scalingFactor();

            this.glyph.drawBatched(
                    game,
                    getTexture(),
                    this.fromX,
                    this.toX,
                    x + this.offsetX + this.fromX * scale,
                    y,
                    (this.toX - this.fromX) * scale,
                    getHeight(),
                    blitoffset,
                    italic
            );
        }
    }

    @Override
    public int getWidth() {
        return this.advance >= 0 ? this.advance : getGlyphWidth();
    }

    private int getGlyphWidth() {
        return (int) (glyph.getWidth() * glyph.getProvider().scalingFactor());
    }

    @Override
    public int getHeight() {
        return (int) (glyph.getHeight() * glyph.getProvider().scalingFactor());
    }
}
