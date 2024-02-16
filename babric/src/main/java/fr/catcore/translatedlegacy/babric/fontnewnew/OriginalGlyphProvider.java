package fr.catcore.translatedlegacy.babric.fontnewnew;

import fr.catcore.translatedlegacy.api.Glyph;
import fr.catcore.translatedlegacy.api.GlyphProvider;
import fr.catcore.translatedlegacy.util.NativeImage;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OriginalGlyphProvider implements GlyphProvider {
    private final Glyph[] glyphs = new Glyph[256];
    private final int[] widths = new int[256];
    private static final String texturePath = "/font/default.png";
    private NativeImage fullTexture;

    @Override
    public boolean provides(Character c) {
        return c < 256;
    }

    @Override
    public Glyph getGlyph(Character c) {
        return glyphs[c];
    }

    @Override
    public String getId() {
        return "minecraft:vanilla";
    }

    @Override
    public List<Glyph> getShowcasedGlyphs() {
        List<Glyph> showcased = new ArrayList<>();
        showcased.add(getGlyph('A'));

        return showcased;
    }

    @Override
    public boolean isLoaded() {
        return fullTexture != null;
    }

    @Override
    public void load() throws IOException {
        fullTexture = NativeImage.read(NativeImage.Format.RGBA, FabricLoader.getInstance().getGameInstance().getClass().getResourceAsStream(texturePath));

        for (int i = 0; i < 256; i++) {
            int column = i % 16;
            int row = i / 16;

            int xOffset;
            for (xOffset = 7; xOffset >= 0; --xOffset) {
                int currentX = column * 8 + xOffset;

                boolean stopper = true;

                for (int yOffset = 0; yOffset < 8; ++yOffset) {
                    int currentY = (row * 8 + yOffset);
                    int opacity = fullTexture.getColor(currentX, currentY) & 255;
                    if (opacity > 0) {
                        stopper = false;
                    }
                }

                if (!stopper) {
                    break;
                }
            }

            if (i == 32) {
                xOffset = 2;
            }

            widths[i] = xOffset + 2;
        }

        for (int i = 0; i < 256; i++) {
            int column = (i % 16) * 8;
            int row = (i / 16) * 8;

            glyphs[i] = new OriginalGlyph(widths[i], column, row, i, this);
        }
    }

    @Override
    public void unload() {
        if (fullTexture != null) {
            fullTexture.close();
            fullTexture = null;
        }
    }

    @Override
    public void upload(Glyph glyph, NativeImage to, int x, int y) {
        glyph.upload(fullTexture, to, x, y);
    }
}
