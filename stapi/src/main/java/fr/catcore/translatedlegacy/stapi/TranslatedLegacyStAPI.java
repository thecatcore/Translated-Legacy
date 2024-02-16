package fr.catcore.translatedlegacy.stapi;

import fr.catcore.translatedlegacy.babric.TranslatedLegacyBabricClient;
import net.minecraft.client.Minecraft;

public class TranslatedLegacyStAPI {
    public static void setGameProvider(Minecraft minecraft) {
        TranslatedLegacyBabricClient.setGameProvider(minecraft);
    }
}
