package fr.catcore.translatedlegacy.babric.fontnew;

import net.minecraft.client.render.Tessellator;

public interface Glyph {
    public float getAdvance();

    default public float getAdvance(boolean bold) {
        return this.getAdvance() + (bold ? this.getBoldOffset() : 0.0f);
    }

    default public float getBoldOffset() {
        return 1.0f;
    }

    default public float getShadowOffset() {
        return 1.0f;
    }

    public void preDraw(boolean italic, float y, float x, float indent, float end, Tessellator tessellator);

    public interface EmptyGlyph extends Glyph {
        @Override
        default void preDraw(boolean italic, float y, float x, float indent, float end, Tessellator tessellator) {}
    }
}
