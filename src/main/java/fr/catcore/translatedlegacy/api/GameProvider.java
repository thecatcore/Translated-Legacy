package fr.catcore.translatedlegacy.api;

import net.minecraft.client.render.Tessellator;

public interface GameProvider {
    boolean anaglyph3d();

    default void draw(int x, int y, int width, int height, float blitOffset) {
        draw(x, y, width, height, 0, 0, 1, 1, blitOffset);
    }

    void draw(int x, int y, int width, int height, float u0, float v0, float u1, float v1, float blitOffset);

    public static GameProvider getInstance() {
        return new GameProvider() {
            @Override
            public boolean anaglyph3d() {
                return false;
            }

            @Override
            public void draw(int x, int y, int width, int height, float u0, float v0, float u1, float v1, float blitOffset) {
                Tessellator var9;
                (var9 = Tessellator.INSTANCE).startQuads();
                var9.vertex((float)x, (float)(y + height), blitOffset, u0, v1);
                var9.vertex((float)(x + width), (float)(y + height), blitOffset, u1, v1);
                var9.vertex((float)(x + width), (float)y, blitOffset, u1, v0);
                var9.vertex((float)x, (float)y, blitOffset, u0, v0);
                var9.draw();
            }
        };
    }
}
