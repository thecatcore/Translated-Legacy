package fr.catcore.translatedlegacy.font;

import net.minecraft.client.render.Tessellator;

public class EmptyGlyphRenderer extends GlyphRenderer {
    public EmptyGlyphRenderer() {
        super(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void draw(boolean italic, float x, float y, Tessellator vertexConsumer) {
    }
}
