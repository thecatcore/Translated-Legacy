package fr.catcore.translatedlegacy.font;

public class UnicodeTextureGlyph implements RenderableGlyph {
    private final int width;
    private final int height;

    UnicodeTextureGlyph(/*int i, int j, */int k, int l/*, NativeImage nativeImage*/) {
        this.width = k;
        this.height = l;
//        this.unpackSkipPixels = i;
//        this.unpackSkipRows = j;
//        this.image = nativeImage;
    }

    @Override
    public float getAdvance() {
        return (float)(this.width / 2 + 1);
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
