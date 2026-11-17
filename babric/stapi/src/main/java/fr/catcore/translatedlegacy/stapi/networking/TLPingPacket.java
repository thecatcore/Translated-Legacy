package fr.catcore.translatedlegacy.stapi.networking;

import fr.catcore.translatedlegacy.babric.NetworkingHelper;
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

public class TLPingPacket extends Packet implements ManagedPacket<TLPingPacket> {
    public static final PacketType<TLPingPacket> TYPE = PacketType.builder(true, false, TLPingPacket::new).build();

    public TLPingPacket() {
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            stream.readBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeBoolean(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        SideUtil.run(() -> handleClient(networkHandler), () -> {});
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(NetworkHandler networkHandler) {
        NetworkingHelper.serverSupportsUnicode = true;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public @NotNull PacketType<TLPingPacket> getType() {
        return TYPE;
    }
}
