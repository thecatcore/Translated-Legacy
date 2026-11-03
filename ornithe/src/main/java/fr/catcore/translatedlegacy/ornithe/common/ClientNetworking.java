package fr.catcore.translatedlegacy.ornithe.common;

import net.ornithemc.osl.localization.api.L10n;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import net.ornithemc.osl.text.api.TextComponent;

import java.util.List;

public class ClientNetworking {
    public static void init() {
        NetworkingHelper.init();
        ClientConnectionEvents.PLAY_READY.register(context -> {
            PacketBuffer buffer = PacketBuffers.make();
            buffer.writeString(L10n.getLanguage().code());
            ClientPlayNetworking.send(NetworkingHelper.UPDATE_ID, buffer);
        });

        ClientConnectionEvents.DISCONNECT.register(context -> {
            NetworkingHelper.serverSupportsUnicode = false;
        });

        ClientPlayNetworking.registerListener(NetworkingHelper.PING_ID, (context, data) -> {
            NetworkingHelper.serverSupportsUnicode = true;
        });

        ClientPlayNetworking.registerListener(NetworkingHelper.TRANSLATE_ID, (context, data) -> {
            int packetId = data.readInt();
            List<TextComponent> texts = data.readList(PacketBuffer::readTextComponent);
            NetworkingHelper.packetTranslations.put(packetId, texts.toArray(new TextComponent[0]));
        });
    }
}
