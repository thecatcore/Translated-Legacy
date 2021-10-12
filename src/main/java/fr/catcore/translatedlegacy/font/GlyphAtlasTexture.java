package fr.catcore.translatedlegacy.font;

import org.jetbrains.annotations.Nullable;

public class GlyphAtlasTexture {
    private final GlyphAtlasTexture.Slot rootSlot;

    public GlyphAtlasTexture() {
        this.rootSlot = new GlyphAtlasTexture.Slot(0, 0, 256, 256);
    }

    public GlyphRenderer getRenderer(RenderableGlyph glyph) {
        GlyphAtlasTexture.Slot slot = this.rootSlot.findSlotFor(glyph);
        if (slot != null) {
            return new GlyphRenderer(((float)slot.x + 0.01F) / 256.0F, ((float)slot.x - 0.01F + (float)glyph.getWidth()) / 256.0F, ((float)slot.y + 0.01F) / 256.0F, ((float)slot.y - 0.01F + (float)glyph.getHeight()) / 256.0F, glyph.getXMin(), glyph.getXMax(), glyph.getYMin(), glyph.getYMax());
        } else {
            return null;
        }
    }

    static class Slot {
        final int x;
        final int y;
        private final int width;
        private final int height;
        @Nullable
        private GlyphAtlasTexture.Slot subSlot1;
        @Nullable
        private GlyphAtlasTexture.Slot subSlot2;
        private boolean occupied;

        Slot(int i, int j, int k, int l) {
            this.x = i;
            this.y = j;
            this.width = k;
            this.height = l;
        }

        @Nullable
        GlyphAtlasTexture.Slot findSlotFor(RenderableGlyph glyph) {
            if (this.subSlot1 != null && this.subSlot2 != null) {
                GlyphAtlasTexture.Slot slot = this.subSlot1.findSlotFor(glyph);
                if (slot == null) {
                    slot = this.subSlot2.findSlotFor(glyph);
                }

                return slot;
            } else if (this.occupied) {
                return null;
            } else {
                int i = glyph.getWidth();
                int j = glyph.getHeight();
                if (i <= this.width && j <= this.height) {
                    if (i == this.width && j == this.height) {
                        this.occupied = true;
                        return this;
                    } else {
                        int k = this.width - i;
                        int l = this.height - j;
                        if (k > l) {
                            this.subSlot1 = new GlyphAtlasTexture.Slot(this.x, this.y, i, this.height);
                            this.subSlot2 = new GlyphAtlasTexture.Slot(this.x + i + 1, this.y, this.width - i - 1, this.height);
                        } else {
                            this.subSlot1 = new GlyphAtlasTexture.Slot(this.x, this.y, this.width, j);
                            this.subSlot2 = new GlyphAtlasTexture.Slot(this.x, this.y + j + 1, this.width, this.height - j - 1);
                        }

                        return this.subSlot1.findSlotFor(glyph);
                    }
                } else {
                    return null;
                }
            }
        }
    }
}
