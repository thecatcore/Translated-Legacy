package fr.catcore.translatedlegacy.font;

public class UnicodeTextureGlyph implements RenderableGlyph {
    private final int width;
    private final int height;

    private final int id;
    private final int imagePointer;

    UnicodeTextureGlyph(int width, int height, int id, int imagePointer) {
        this.width = width;
        this.height = height;

        this.id = id;
        this.imagePointer = imagePointer;
    }

    @Override
    public float getAdvance() {
        return (float)(this.width / 2 + 1);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getImagePointer() {
        return this.imagePointer;
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
        return 2.0F;
    }

    @Override
    public float getBoldOffset() {
        return 0.5F;
    }

    @Override
    public float getShadowOffset() {
        return 0.5F;
    }
}
