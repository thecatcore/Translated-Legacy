package fr.catcore.translatedlegacy.font;

import net.minecraft.client.render.Tessellator;

public interface RenderableGlyph extends Glyph {
    int getWidth();

    int getHeight();

    void upload(int x, int y);

    boolean hasColor();

    float getOversample();

    default float getXMin() {
        return this.getBearingX();
    }

    default float getXMax() {
        return this.getXMin() + (float)this.getWidth() / this.getOversample();
    }

    default float getYMin() {
        return this.getAscent();
    }

    default float getYMax() {
        return this.getYMin() + (float)this.getHeight() / this.getOversample();
    }

    default float getAscent() {
        return 3.0F;
    }

    @Override
    default void preDraw(boolean italic, float y, float x, float indent, float end, Tessellator tessellator) {
        float var26 = 7.99F;

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
