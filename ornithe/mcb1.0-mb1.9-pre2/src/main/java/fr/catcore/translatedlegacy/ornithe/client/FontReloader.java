package fr.catcore.translatedlegacy.ornithe.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.catcore.translatedlegacy.font.TextRenderer;
import fr.catcore.translatedlegacy.font.provider.BitmapGlyphProvider;
import fr.catcore.translatedlegacy.font.provider.LegacyUnicodeProvider;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.resource.loader.api.client.ClientResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class FontReloader implements ResourceReloadListener {
    public static void init() {
        ClientResourceLoaderEvents.INIT_RESOURCE_MANAGER.register(resourceManager -> {
            TextRenderer.setGameProvider(new OrnitheGameProvider());
            registerGlyphProviders(resourceManager);
            resourceManager.addReloader(new FontReloader());
        });
    }

    @Override
    public void resourcesReloaded(ResourceManager manager) {
        TextRenderer.reload();
    }

    private static final Gson GSON = new Gson();

    public static void registerGlyphProviders(ResourceManager manager) {
        Optional<Resource> resource = manager.getResource(NamespacedIdentifiers.from("minecraft", "font/default.json"));

        try (BufferedReader reader = resource.get().openAsReader()) {
            JsonObject object = GSON.fromJson(reader, JsonObject.class);
            JsonArray list = object.getAsJsonArray("providers");

            for (JsonElement providers : list) {
                JsonObject obj = providers.getAsJsonObject();

                if (Objects.equals(obj.get("type").getAsString(), "bitmap")) {
                    TextRenderer.registerProvider(new BitmapGlyphProvider(obj));
                } else if (Objects.equals(obj.get("type").getAsString(), "legacy_unicode")) {
                    TextRenderer.registerProvider(new LegacyUnicodeProvider(obj));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
