package fr.catcore.translatedlegacy.babric.font;

public enum BlankGlyph implements RenderableGlyph {
    INSTANCE;

    private static final int field_32228 = 5;
    private static final int field_32229 = 8;

    private BlankGlyph() {
    }

    public int getWidth() {
        return 5;
    }

    public int getHeight() {
        return 8;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int getImagePointer() {
        return 0;
    }

    public float getAdvance() {
        return 6.0F;
    }

    public float getOversample() {
        return 1.0F;
    }

    public void upload(int x, int y) {
//        IMAGE.upload(0, x, y, false);
    }

    public boolean hasColor() {
        return true;
    }
}
