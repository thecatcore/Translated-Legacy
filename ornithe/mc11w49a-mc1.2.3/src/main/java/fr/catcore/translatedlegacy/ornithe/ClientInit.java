package fr.catcore.translatedlegacy.ornithe;

import fr.catcore.translatedlegacy.ornithe.common.ClientNetworking;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

public class ClientInit implements ClientModInitializer {
    @Override
    public void initClient() {
        ClientNetworking.init();
    }
}
