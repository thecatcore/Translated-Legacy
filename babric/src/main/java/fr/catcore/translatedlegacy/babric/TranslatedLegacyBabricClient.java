package fr.catcore.translatedlegacy.babric;

import fr.catcore.translatedlegacy.font.TextRenderer;
import fr.catcore.translatedlegacy.font.provider.AsciiGlyphProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

import java.io.InputStream;

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

    public static void registerGlyphProviders() {
        TextRenderer.registerProvider(new AsciiGlyphProvider("/font/default.png"));
    }
}
