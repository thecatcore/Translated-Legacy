package fr.catcore.translatedlegacy.font;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Tessellator;

public class GlyphRenderer {

    private final float minU;
    private final float maxU;
    private final float minV;
    private final float maxV;
    private final float minX;
    private final float maxX;
    private final float minY;
    private final float maxY;

    public GlyphRenderer(float f, float g, float h, float i, float j, float k, float l, float m) {
        this.minU = f;
        this.maxU = g;
        this.minV = h;
        this.maxV = i;
        this.minX = j;
        this.maxX = k;
        this.minY = l;
        this.maxY = m;
    }

    public void draw(boolean italic, float x, float y, Tessellator tessellator) {
//        float minx = x + this.minX;
//        float maxx = x + this.maxX;
//        float h = this.minY - 3.0F;
//        float j = this.maxY - 3.0F;
//        float miny = y + h;
//        float maxy = y + j;
//        float m = italic ? 1.0F - 0.25F * h : 0.0F;
//        float n = italic ? 1.0F - 0.25F * j : 0.0F;
//
////        vertexConsumer.vertex(matrix, minx + m, miny, 0.0F).color(red, green, blue, alpha).texture(this.minU, this.minV).light(light).next();
////        vertexConsumer.vertex(matrix, minx + n, maxy, 0.0F).color(red, green, blue, alpha).texture(this.minU, this.maxV).light(light).next();
////        vertexConsumer.vertex(matrix, maxx + n, maxy, 0.0F).color(red, green, blue, alpha).texture(this.maxU, this.maxV).light(light).next();
////        vertexConsumer.vertex(matrix, maxx + m, miny, 0.0F).color(red, green, blue, alpha).texture(this.maxU, this.minV).light(light).next();
//
//
//
//        tessellator.vertex(
//                this.maxU,
//                this.maxV,
//                maxx + n,
//                maxy, //((float) height + var26) / 128.0F + var28,
//                 0//((float) width + var26) / 128.0F + var30
//        );
//
//        tessellator.vertex(
//                this.minU,
//                this.maxV,
//                minx + n,
//                maxy, //(float) height / 128.0F + var28,
//                0//((float) width + var26) / 128.0F + var30
//        );
//
//        tessellator.vertex(
//                this.maxU,
//                this.minV,
//                maxx + m,
//                miny, //((float) height + var26) / 128.0F + var28,
//                0//(float) width / 128.0F + var30
//        );
//
//        tessellator.vertex(
//                this.minU,
//                this.minV,
//                minx + m,
//                miny, //(float) height / 128.0F + var28,
//                 0//(float) width / 128.0F + var30
//        );


        float var26 = 7.99F;
        float var28 = 0.0F;
        float var30 = 0.0F;

        tessellator.vertex(
                0.0D,
                0.0F + var26,
                0.0D,
                (float) y / 128.0F + var28,
                ((float) x + var26) / 128.0F + var30
        );

        tessellator.vertex(
                0.0F + var26,
                0.0F + var26,
                0.0D,
                ((float) y + var26) / 128.0F + var28,
                ((float) x + var26) / 128.0F + var30
        );

        tessellator.vertex(
                0.0F + var26,
                0.0D,
                0.0D,
                ((float) y + var26) / 128.0F + var28,
                (float) x / 128.0F + var30
        );

        tessellator.vertex(
                0.0D,
                0.0D,
                0.0D,
                (float) y / 128.0F + var28,
                (float) x / 128.0F + var30
        );
    }

//    public void drawRectangle(GlyphRenderer.Rectangle rectangle, Matrix4f matrix, VertexConsumer vertexConsumer, int light) {
//        vertexConsumer.vertex(matrix, rectangle.minX, rectangle.minY, rectangle.zIndex).color(rectangle.red, rectangle.green, rectangle.blue, rectangle.alpha).texture(this.minU, this.minV).light(light).next();
//        vertexConsumer.vertex(matrix, rectangle.maxX, rectangle.minY, rectangle.zIndex).color(rectangle.red, rectangle.green, rectangle.blue, rectangle.alpha).texture(this.minU, this.maxV).light(light).next();
//        vertexConsumer.vertex(matrix, rectangle.maxX, rectangle.maxY, rectangle.zIndex).color(rectangle.red, rectangle.green, rectangle.blue, rectangle.alpha).texture(this.maxU, this.maxV).light(light).next();
//        vertexConsumer.vertex(matrix, rectangle.minX, rectangle.maxY, rectangle.zIndex).color(rectangle.red, rectangle.green, rectangle.blue, rectangle.alpha).texture(this.maxU, this.minV).light(light).next();
//    }

//    public RenderLayer getLayer(TextLayerType layerType) {
//        switch(layerType) {
//            case NORMAL:
//            default:
//                return this.textLayer;
//            case SEE_THROUGH:
//                return this.seeThroughTextLayer;
//            case POLYGON_OFFSET:
//                return this.polygonOffsetTextLayer;
//        }
//    }

    @Environment(EnvType.CLIENT)
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

        public Rectangle(float f, float g, float h, float i, float j, float k, float l, float m, float n) {
            this.minX = f;
            this.minY = g;
            this.maxX = h;
            this.maxY = i;
            this.zIndex = j;
            this.red = k;
            this.green = l;
            this.blue = m;
            this.alpha = n;
        }
    }
}
