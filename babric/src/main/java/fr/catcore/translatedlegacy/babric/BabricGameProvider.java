package fr.catcore.translatedlegacy.babric;

import fr.catcore.translatedlegacy.font.api.GameProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;

public abstract class BabricGameProvider implements GameProvider {
    private final Minecraft minecraft;

    public BabricGameProvider(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public boolean anaglyph3d() {
        return minecraft.options.anaglyph3d;
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
}
