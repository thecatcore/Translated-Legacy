package fr.catcore.translatedlegacy.babric;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.catcore.translatedlegacy.babric.font.BetterTextRenderer;
import fr.catcore.translatedlegacy.font.TextRenderer;
import fr.catcore.translatedlegacy.font.provider.BitmapGlyphProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TranslatedLegacyBabricClient {
    public static void setGameProvider(Minecraft minecraft) {
        TextRenderer.setGameProvider(new BabricGameProvider(minecraft) {
            @Override
            public InputStream getResource(String path) {
                return FabricLoader.getInstance().getGameInstance().getClass().getResourceAsStream(path);
            }
        });

        registerGlyphProviders();
    }

    private static final Gson GSON = new Gson();

    public static void registerGlyphProviders() {
        InputStream stream = BetterTextRenderer.class.getResourceAsStream("/assets/minecraft/font/default.json");
        InputStreamReader reader = new InputStreamReader(stream);

        JsonObject object = GSON.fromJson(reader, JsonObject.class);
        JsonArray list = object.getAsJsonArray("providers");

        for (JsonElement providers : list) {
            JsonObject obj = providers.getAsJsonObject();

            if (Objects.equals(obj.get("type").getAsString(), "bitmap")) {
                String path = obj.get("file").getAsString();

                List<int[]> chars = new ArrayList<>();

                for (JsonElement element : obj.getAsJsonArray("chars")) {
                    int[] is = element.getAsString().codePoints().toArray();
                    chars.add(is);
                }

                int ascent = obj.get("ascent").getAsInt();
                int height = obj.has("height") ? obj.get("height").getAsInt() : 8;

                if (path.contains(":")) {
                    String[] split = path.split(":");
                    path = "/assets/" + split[0] + "/textures/" + split[1];
                }

                TextRenderer.registerProvider(new BitmapGlyphProvider(path, ascent, height, chars));
            }
        }
    }
}
