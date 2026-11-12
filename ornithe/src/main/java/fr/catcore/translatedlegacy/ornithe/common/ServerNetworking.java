package fr.catcore.translatedlegacy.ornithe.common;

import fr.catcore.translatedlegacy.language.TranslationRecipient;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;

public class ServerNetworking {
    public static void init() {
        NetworkingHelper.init();
        ServerPlayNetworking.registerListener(NetworkingHelper.UPDATE_ID, (context, buffer) -> {
            ServerPlayNetworkHandler networkHandler = context.networkHandler();
            String language = buffer.readString();
            ((TranslationRecipient) networkHandler).setLanguage(language);
        });

        ServerConnectionEvents.PLAY_READY.register(context -> {
            PacketBuffer buffer = PacketBuffers.make();
            buffer.writeByte(0);
            ServerPlayNetworking.send(context.player(), NetworkingHelper.PING_ID, buffer);
        });
    }
}
