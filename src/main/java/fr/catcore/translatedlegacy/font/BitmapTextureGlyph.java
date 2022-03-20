package fr.catcore.translatedlegacy.font;

import net.minecraft.client.render.Tessellator;

public class BitmapTextureGlyph implements RenderableGlyph {
    private final float scaleFactor;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int advance;
    private final int ascent;

    protected BitmapTextureGlyph(float scaleFactor, int x, int y, int width, int height, int advance, int ascent) {
        this.scaleFactor = scaleFactor;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.advance = advance;
        this.ascent = ascent;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int getImagePointer() {
        return 0;
    }

    @Override
    public float getAdvance() {
        return this.advance;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void upload(int x, int y) {

    }

    @Override
    public boolean hasColor() {
        return true;
    }

    @Override
    public float getOversample() {
        return 1.0F / this.scaleFactor;
    }

    @Override
    public float getAscent() {
        return RenderableGlyph.super.getAscent() + 7.0F - (float)this.ascent;
    }

    @Override
    public void preDraw(boolean italic, float y, float x, float indent, float end, Tessellator tessellator) {
        float var26 = 7.99F;

//        y = this.getHeight()/8F;
//        x = this.getWidth()/8F;
//        indent = this.getAdvance();

        tessellator.vertex(
                0.0D,
                0.0F + var26,
                0.0D,
                (float) (x + indent) / 128.0F,
                ((float) y + var26) / 128.0F
        );

        tessellator.vertex(
                0.0F + var26,
                0.0F + var26,
                0.0D,
                (float) (x + var26) / 128.0F,
                ((float) y + var26) / 128.0F
        );

        tessellator.vertex(
                0.0F + var26,
                0.0D,
                0.0D,
                (float) (x + var26) / 128.0F,
                (float) y / 128.0F
        );

        tessellator.vertex(
                0.0D,
                0.0D,
                0.0D,
                (float) (x + indent) / 128.0F,
                (float) y / 128.0F
        );
    }
}
