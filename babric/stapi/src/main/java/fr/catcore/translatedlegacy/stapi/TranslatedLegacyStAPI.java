package fr.catcore.translatedlegacy.stapi;

import fr.catcore.translatedlegacy.babric.BabricGameProvider;
import fr.catcore.translatedlegacy.babric.TranslatedLegacyBabricClient;
import fr.catcore.translatedlegacy.babric.language.LanguageManager;
import fr.catcore.translatedlegacy.font.TextRenderer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.resource.ReloadableAssetsManager;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.IOException;
import java.io.InputStream;

public class TranslatedLegacyStAPI {
    public static void setGameProvider(Minecraft minecraft) {
        TextRenderer.setGameProvider(new BabricGameProvider(minecraft) {
            @Override
            public InputStream getResource(String path) {
                ResourceManager resourceManager = ReloadableAssetsManager.INSTANCE;

                try {
                    return resourceManager.getResource(Identifier.of(path)).orElseThrow(IOException::new).getInputStream();
                } catch (IOException e) {
                    System.out.println("Loading resource with java api instead of StAPI ResourceManager");
                    return FabricLoader.getInstance().getGameInstance().getClass().getResourceAsStream(path);
                }
            }
        });

        TranslatedLegacyBabricClient.registerGlyphProviders();

        LanguageManager.registerCallback(net.modificationstation.stationapi.api.resource.language.LanguageManager::changeLanguage);
    }
}
