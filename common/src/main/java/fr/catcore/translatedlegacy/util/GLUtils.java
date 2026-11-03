package fr.catcore.translatedlegacy.util;

import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class GLUtils {
    public static synchronized ByteBuffer allocateByteBuffer(int i) {
        return ByteBuffer.allocateDirect(i).order(ByteOrder.nativeOrder());
    }

    public static int generateTextureId() {
        return GL11.glGenTextures();
    }

    public static void releaseTextureId(int id) {
        GL11.glDeleteTextures(id);
    }

    public static void prepareImage(int id, int width, int height) {
        prepareImage(NativeImage.InternalFormat.RGBA, id, 0, width, height);
    }

    public static void prepareImage(NativeImage.InternalFormat internalFormat, int id, int width, int height) {
        prepareImage(internalFormat, id, 0, width, height);
    }

    public static void prepareImage(int id, int maxLevel, int width, int height) {
        prepareImage(NativeImage.InternalFormat.RGBA, id, maxLevel, width, height);
    }

    public static void prepareImage(NativeImage.InternalFormat internalFormat, int id, int maxLevel, int width, int height) {
        bind(id);
        if (maxLevel >= 0) {
            GL11.glTexParameteri(3553, 33085, maxLevel);
            GL11.glTexParameteri(3553, 33082, 0);
            GL11.glTexParameteri(3553, 33083, maxLevel);
            GL11.glTexParameterf(3553, 34049, 0.0f);
        }
        for (int i = 0; i <= maxLevel; ++i) {
            GL11.glTexImage2D(3553, i, internalFormat.getValue(), width >> i, height >> i, 0, 6408, 5121, (IntBuffer) null);
        }
    }

    private static void bind(int id) {
        GL11.glBindTexture(3553, id);
    }

    public static void initTexture(IntBuffer imageData, int width, int height) {
        GL11.glPixelStorei(3312, 0);
        GL11.glPixelStorei(3313, 0);
        GL11.glPixelStorei(3314, 0);
        GL11.glPixelStorei(3315, 0);
        GL11.glPixelStorei(3316, 0);
        GL11.glPixelStorei(3317, 4);
        GL11.glTexImage2D(3553, 0, 6408, width, height, 0, 32993, 33639, imageData);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10241, 9729);
    }
}
