package fr.catcore.translatedlegacy.ornithe;

import fr.catcore.translatedlegacy.ornithe.access.TranslationRecipient;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;

public class ServerNetworking {
    public static void init() {
        ServerPlayNetworking.registerListener(NetworkingHelper.PACKET_ID, (context, buffer) -> {
            ServerPlayNetworkHandler networkHandler = context.networkHandler();
            String language = buffer.readString();
            ((TranslationRecipient) networkHandler).setLanguage(language);
        });
    }
}
