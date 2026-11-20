package fr.catcore.translatedlegacy.ornithe;

import com.google.common.collect.BiMap;
import net.minecraft.network.NetworkProtocol;
import net.minecraft.network.packet.Packet;

public class ModernNetworkUtils {
    private static BiMap packets = NetworkProtocol.PLAY.getS2CPackets().inverse();

    public static int getPacketId(Packet packet) {
        return (int) packets.get(packet);
    }
}
