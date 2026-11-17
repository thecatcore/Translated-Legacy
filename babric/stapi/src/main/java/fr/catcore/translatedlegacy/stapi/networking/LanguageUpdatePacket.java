package fr.catcore.translatedlegacy.stapi.networking;

import fr.catcore.translatedlegacy.language.TranslationRecipient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LanguageUpdatePacket extends Packet implements ManagedPacket<LanguageUpdatePacket> {
    public static final PacketType<LanguageUpdatePacket> TYPE = PacketType.builder(false, true, LanguageUpdatePacket::new).build();

    public LanguageUpdatePacket() {
    }

    public String code;

    @Override
    public void read(DataInputStream stream) {
        try {
            code = stream.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeUTF(code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        SideUtil.run(() -> {}, () -> handleServer(networkHandler));
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
        if (networkHandler instanceof TranslationRecipient recipient) {
            recipient.tl$setLanguage(code);
        }
    }

    @Override
    public int size() {
        return code.length();
    }

    @Override
    public @NotNull PacketType<LanguageUpdatePacket> getType() {
        return TYPE;
    }
}
