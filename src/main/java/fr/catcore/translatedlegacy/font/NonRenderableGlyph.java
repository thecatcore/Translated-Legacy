package fr.catcore.translatedlegacy.font;

public interface NonRenderableGlyph extends Glyph {
    @Override
    default int getId() {
        return ' ';
    }

    @Override
    default int getImagePointer() {
        return 0;
    }
}
