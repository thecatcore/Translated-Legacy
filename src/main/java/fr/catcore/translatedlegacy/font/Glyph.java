package fr.catcore.translatedlegacy.font;

import net.minecraft.client.render.Tessellator;

public interface Glyph {
    float getAdvance();

    default float getAdvance(boolean bold) {
        return this.getAdvance() + (bold ? this.getBoldOffset() : 0.0F);
    }

    default float getBearingX() {
        return 0.0F;
    }

    default float getAscent() {
        return 0.0F;
    }

    default float getBoldOffset() {
        return 1.0F;
    }

    default float getShadowOffset() {
        return 1.0F;
    }

    default void preDraw(boolean italic, float y, float x, float indent, float end, Tessellator tessellator) {}
}
