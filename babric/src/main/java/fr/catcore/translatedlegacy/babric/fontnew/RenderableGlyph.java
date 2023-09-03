package fr.catcore.translatedlegacy.babric.fontnew;

public interface RenderableGlyph {
    public int getWidth();

    public int getHeight();

    public void upload(int var1, int var2);

    public boolean hasColor();

    public float getOversample();

    default public float getXMin() {
        return this.getBearingX();
    }

    default public float getXMax() {
        return this.getXMin() + (float)this.getWidth() / this.getOversample();
    }

    default public float getYMin() {
        return this.getAscent();
    }

    default public float getYMax() {
        return this.getYMin() + (float)this.getHeight() / this.getOversample();
    }

    default public float getBearingX() {
        return 0.0f;
    }

    default public float getAscent() {
        return 3.0f;
    }
}
