package fr.catcore.translatedlegacy.stapi.networking;

import fr.catcore.translatedlegacy.babric.NetworkingHelper;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TranslationPacket extends Packet implements ManagedPacket<TranslationPacket> {
    public static final PacketType<TranslationPacket> TYPE = PacketType.builder(true, false, TranslationPacket::new).build();

    public int originalId;
    public List<String> translations = new ArrayList<>();

    public TranslationPacket() {
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            originalId = stream.readByte();
            int size = stream.readByte();
            translations = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                translations.add(stream.readUTF());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeByte(originalId);
            stream.writeByte(translations.size());
            for (String translation : translations) {
                stream.writeUTF(translation);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        NetworkingHelper.packetTranslations.put(originalId, translations.toArray(new String[0]));
    }

    @Override
    public int size() {
        return 2 + translations.stream().mapToInt(String::length).sum();
    }

    @Override
    public @NotNull PacketType<TranslationPacket> getType() {
        return TYPE;
    }
}
