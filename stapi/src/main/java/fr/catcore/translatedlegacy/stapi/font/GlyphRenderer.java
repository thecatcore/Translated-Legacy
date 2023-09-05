package fr.catcore.translatedlegacy.stapi.font;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.util.math.Matrix4f;
import net.modificationstation.stationapi.api.util.math.Vector4f;

public class GlyphRenderer {
//    private final TextRenderLayerSet textRenderLayers;
    private final float minU;
    private final float maxU;
    private final float minV;
    private final float maxV;
    private final float minX;
    private final float maxX;
    private final float minY;
    private final float maxY;

    public GlyphRenderer(/*TextRenderLayerSet textRenderLayers, */float minU, float maxU, float minV, float maxV, float minX, float maxX, float minY, float maxY) {
//        this.textRenderLayers = textRenderLayers;
        this.minU = minU;
        this.maxU = maxU;
        this.minV = minV;
        this.maxV = maxV;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

//    public void draw(boolean italic, float x, float y, Matrix4f matrix, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int light) {
//        int i = 3;
//        float f = x + this.minX;
//        float g = x + this.maxX;
//        float h = this.minY - 3.0f;
//        float j = this.maxY - 3.0f;
//        float k = y + h;
//        float l = y + j;
//        float m = italic ? 1.0f - 0.25f * h : 0.0f;
//        float n = italic ? 1.0f - 0.25f * j : 0.0f;
//        vertexConsumer.vertex(matrix, f + m, k, 0.0f).color(red, green, blue, alpha).texture(this.minU, this.minV).light(light).next();
//        vertexConsumer.vertex(matrix, f + n, l, 0.0f).color(red, green, blue, alpha).texture(this.minU, this.maxV).light(light).next();
//        vertexConsumer.vertex(matrix, g + n, l, 0.0f).color(red, green, blue, alpha).texture(this.maxU, this.maxV).light(light).next();
//        vertexConsumer.vertex(matrix, g + m, k, 0.0f).color(red, green, blue, alpha).texture(this.maxU, this.minV).light(light).next();
//    }

    public void draw(boolean italic, float x, float y, Matrix4f matrix, Tessellator tessellator, float red, float green, float blue, float alpha, int light) {
        int i = 3;
        float f = x + this.minX;
        float g = x + this.maxX;
        float h = this.minY - i;
        float j = this.maxY - i;
        float k = y + h;
        float l = y + j;
        float m = italic ? 1.0f - 0.25f * h : 0.0f;
        float n = italic ? 1.0f - 0.25f * j : 0.0f;

        class DrawInfo {
            public final float x, y, z;
            public final double texU, texV;

            DrawInfo(float x, float y, float z, double texU, double texV) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.texU = texU;
                this.texV = texV;
            }
        }

        DrawInfo[] drawInfos = new DrawInfo[] {
                new DrawInfo(f + m, k, 0.0f, this.minU, this.minV),
                new DrawInfo(f + n, l, 0.0f, this.minU, this.maxV),
                new DrawInfo(g + n, l, 0.0f, this.maxU, this.maxV),
                new DrawInfo(g + m, k, 0.0f, this.maxU, this.minV)
        };

        tessellator.startQuads();

        tessellator.color(red, green, blue, alpha);

        for (DrawInfo info : drawInfos) {

            Vector4f vec1 = new Vector4f(info.x, info.y, info.z, 1.0f);
            vec1.transform(matrix);

            tessellator.vertex(vec1.getX(), vec1.getY(), vec1.getZ(), info.texU, info.texV);
        }

        tessellator.draw();
    }

//    public void drawRectangle(Rectangle rectangle, Matrix4f matrix, VertexConsumer vertexConsumer, int light) {
//        vertexConsumer.vertex(matrix, rectangle.minX, rectangle.minY, rectangle.zIndex).color(rectangle.red, rectangle.green, rectangle.blue, rectangle.alpha).texture(this.minU, this.minV).light(light).next();
//        vertexConsumer.vertex(matrix, rectangle.maxX, rectangle.minY, rectangle.zIndex).color(rectangle.red, rectangle.green, rectangle.blue, rectangle.alpha).texture(this.minU, this.maxV).light(light).next();
//        vertexConsumer.vertex(matrix, rectangle.maxX, rectangle.maxY, rectangle.zIndex).color(rectangle.red, rectangle.green, rectangle.blue, rectangle.alpha).texture(this.maxU, this.maxV).light(light).next();
//        vertexConsumer.vertex(matrix, rectangle.minX, rectangle.maxY, rectangle.zIndex).color(rectangle.red, rectangle.green, rectangle.blue, rectangle.alpha).texture(this.maxU, this.minV).light(light).next();
//    }

    public void drawRectangle(Rectangle rectangle, Matrix4f matrix, Tessellator tessellator, int light) {
        class DrawInfo {
            public final float x, y, z;
            public final double texU, texV;

            DrawInfo(float x, float y, float z, double texU, double texV) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.texU = texU;
                this.texV = texV;
            }
        }

        DrawInfo[] infos = new DrawInfo[] {
                new DrawInfo(rectangle.minX, rectangle.minY, rectangle.zIndex, this.minU, this.minV),
                new DrawInfo(rectangle.maxX, rectangle.minY, rectangle.zIndex, this.minU, this.maxV),
                new DrawInfo(rectangle.maxX, rectangle.maxY, rectangle.zIndex, this.maxU, this.maxV),
                new DrawInfo(rectangle.minX, rectangle.maxY, rectangle.zIndex, this.maxU, this.minV)
        };

        tessellator.startQuads();

        tessellator.color(rectangle.red, rectangle.green, rectangle.blue, rectangle.alpha);

        for (DrawInfo info : infos) {

            Vector4f vec1 = new Vector4f(info.x, info.y, info.z, 1.0f);
            vec1.transform(matrix);

            tessellator.vertex(vec1.getX(), vec1.getY(), vec1.getZ(), info.texU, info.texV);
        }

        tessellator.draw();
    }

//    public RenderLayer getLayer(TextRenderer.TextLayerType layerType) {
//        return this.textRenderLayers.getRenderLayer(layerType);
//    }

    @Environment(value= EnvType.CLIENT)
    public static class Rectangle {
        protected final float minX;
        protected final float minY;
        protected final float maxX;
        protected final float maxY;
        protected final float zIndex;
        protected final float red;
        protected final float green;
        protected final float blue;
        protected final float alpha;

        public Rectangle(float minX, float minY, float maxX, float maxY, float zIndex, float red, float green, float blue, float alpha) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
            this.zIndex = zIndex;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }
    }
}
