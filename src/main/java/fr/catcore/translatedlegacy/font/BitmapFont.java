package fr.catcore.translatedlegacy.font;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.texture.TextureManager;

import java.util.ArrayList;
import java.util.List;

public class BitmapFont extends Font {

    private final String fileLocation;
    private final int ascent;
    private int height = 8;
    private final List<int[]> chars = new ArrayList<>();

    protected BitmapFont(JsonObject object) {
        this.fileLocation = object.get("file").getAsString();
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

    }

    @Override
    protected int getImagePointer(char c) {
        return 0;
    }

    @Override
    protected int getOtherPointer(char c) {
        return 0;
    }

    @Override
    protected byte getWidth(char c) {
        return 0;
    }
}
