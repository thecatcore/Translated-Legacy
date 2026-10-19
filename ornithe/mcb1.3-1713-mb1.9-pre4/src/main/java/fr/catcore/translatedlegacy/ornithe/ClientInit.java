package fr.catcore.translatedlegacy.ornithe;

import fr.catcore.translatedlegacy.ornithe.client.ClientNetworking;
import fr.catcore.translatedlegacy.ornithe.client.FontReloader;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

public class ClientInit implements ClientModInitializer {
    @Override
    public void initClient() {
        FontReloader.init();
        ClientNetworking.init();
    }
}
