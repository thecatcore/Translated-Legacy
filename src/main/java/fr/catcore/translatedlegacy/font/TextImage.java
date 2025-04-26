package fr.catcore.translatedlegacy.font;

import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;
import fr.catcore.translatedlegacy.util.GLUtils;
import fr.catcore.translatedlegacy.util.NativeImage;
import org.lwjgl.opengl.GL11;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TextImage implements Closeable {
    private final List<Glyph> glyphs;
    private NativeImage image;
    private Integer pointer = null;

    public TextImage(List<Glyph> glyphs) {
        this.glyphs = glyphs;
        if (!this.glyphs.isEmpty()) this.image = createImage();
    }

    public int getWidth() {
        return glyphs.stream().mapToInt(x -> x == null ? TextRenderer.getSpaceWidth() : x.getFullWidth()).sum();
    }

    public int getHeight() {
        return glyphs.stream().mapToInt(x -> x == null ? TextRenderer.getSpaceWidth() : x.getHeight()).max().orElse(0);
    }

    private NativeImage createImage() {
        int width = getWidth();
        int height = getHeight();

        NativeImage image = new NativeImage(width, height, false);

        int currentX = 0;

        for (Glyph glyph : glyphs) {
            if (glyph == null) {
                currentX += TextRenderer.getSpaceWidth();
                continue;
            }

            GlyphProvider provider = glyph.getProvider();

            int y = 0;
            if (height > 8) {
                int originalY = height - glyph.getHeight() - 1;
                y = Math.max(originalY, 0);
            }

            provider.upload(glyph, image, currentX, y);

            currentX += glyph.getFullWidth();
        }

        return image;
    }

    private void registerPointer() {
        if (this.pointer == null) {
            this.pointer = GLUtils.generateTextureId();
            GLUtils.prepareImage(pointer, image.getWidth(), image.getHeight());
            GL11.glBindTexture(3553, pointer);
            image.upload(0, 0, 0, false);
        }
    }

    public void bind() {
        if (this.pointer == null) {
            registerPointer();
        }

        GL11.glBindTexture(3553, pointer);
    }

    @Override
    public void close() throws IOException {
        if (image != null) image.close();

        if (pointer != null) {
            GLUtils.releaseTextureId(pointer);
        }
    }
}
