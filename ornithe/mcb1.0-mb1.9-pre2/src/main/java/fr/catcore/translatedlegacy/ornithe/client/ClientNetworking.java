package fr.catcore.translatedlegacy.ornithe.client;

import fr.catcore.translatedlegacy.ornithe.NetworkingHelper;
import net.ornithemc.osl.localization.api.L10n;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;

public class ClientNetworking {
    public static void init() {
        ClientConnectionEvents.LOGIN.register(context -> {
            PacketBuffer buffer = PacketBuffers.make();
            buffer.writeString(L10n.getLanguage().code());

            ClientPlayNetworking.send(NetworkingHelper.PACKET_ID, buffer);
        });
    }
}
