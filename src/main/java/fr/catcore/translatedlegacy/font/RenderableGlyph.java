package fr.catcore.translatedlegacy.font;

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
}
