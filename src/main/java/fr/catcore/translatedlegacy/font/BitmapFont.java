package fr.catcore.translatedlegacy.font;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.class_214;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.texture.TextureManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitmapFont extends Font {

    private final String fileLocation;
    private final int ascent;
    private int height = 8;
    private final List<int[]> chars = new ArrayList<>();
    private final Map<Integer, Glyph> GLYPHS = new HashMap<>();
    private int imagePointer;
    private int anInt;

    protected BitmapFont(JsonObject object) {
        this.fileLocation = "/assets/" + object.get("file").getAsString().replace(":", "/textures/");
        this.ascent = object.get("ascent").getAsInt();

        for (JsonElement element : object.getAsJsonArray("chars")) {
            int[] is = element.getAsString().codePoints().toArray();
            chars.add(is);
        }

        if (object.has("height")) {
            this.height = object.get("height").getAsInt();
        }
    }

    @Override
    public boolean contains(char c) {
        return false;
    }

    @Override
    protected Glyph getGlyph(char c) {
        return null;
    }

    @Override
    protected void loadTextures(GameOptions arg, TextureManager arg1) {
        BufferedImage fontImage;
        try {
            fontImage = ImageIO.read(LegacyUnicodeFont.class.getResourceAsStream(this.fileLocation));
        } catch (IOException var18) {
            throw new RuntimeException(var18);
        } catch (IllegalArgumentException e) {
            return;
        }

        int imageWidth = fontImage.getWidth();
        int imageHeight = fontImage.getHeight();

        this.imagePointer = arg1.glLoadImage(fontImage);
        this.anInt = class_214.method_741(this.getImagePointer());
    }

    private int getImagePointer() {
        return 545 + (
                this.fileLocation.contains("ascii") ? 1 :
                        this.fileLocation.contains("nonlatin_european") ? 2 : 3
                );
    }

    @Override
    protected int getImagePointer(char c) {
        return this.imagePointer;
    }

    @Override
    protected int getOtherPointer(char c) {
        return this.anInt;
    }

    @Override
    protected byte getWidth(char c) {
        return 0;
    }
}
