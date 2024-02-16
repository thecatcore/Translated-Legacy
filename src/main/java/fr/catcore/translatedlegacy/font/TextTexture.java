package fr.catcore.translatedlegacy.font;

import fr.catcore.translatedlegacy.util.GLUtils;
import fr.catcore.translatedlegacy.util.NativeImage;
import org.lwjgl.opengl.GL11;

import java.io.Closeable;
import java.io.IOException;

public class TextTexture implements Closeable {
    private final NativeImage image;
    private final int pointer;

    public TextTexture(NativeImage image) {
        this.image = image;
        this.pointer = GLUtils.generateTextureId();
        GLUtils.prepareImage(pointer, image.getWidth(), image.getHeight());
        GL11.glBindTexture(3553, pointer);
        image.upload(0, 0, 0, false);
    }

    public void bind() {
        GL11.glBindTexture(3553, pointer);
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public void close() throws IOException {
        image.close();
        GLUtils.releaseTextureId(pointer);
    }
}
