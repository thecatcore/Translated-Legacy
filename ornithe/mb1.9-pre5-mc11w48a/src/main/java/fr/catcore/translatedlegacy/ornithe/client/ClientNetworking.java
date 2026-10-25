package fr.catcore.translatedlegacy.ornithe.client;

import fr.catcore.translatedlegacy.ornithe.NetworkingHelper;
import net.ornithemc.osl.localization.api.L10n;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

import java.util.ArrayList;
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
            int stringNumbers = data.readByte();
            List<TextComponent> texts = new ArrayList<>();

            for (int i = 0; i < stringNumbers; i++) {
                TextComponent text = TextComponents.fromJson(data.readString());
                texts.add(text);
            }

            NetworkingHelper.packetTranslations.put(packetId, texts.toArray(new TextComponent[0]));
        });
    }
}
