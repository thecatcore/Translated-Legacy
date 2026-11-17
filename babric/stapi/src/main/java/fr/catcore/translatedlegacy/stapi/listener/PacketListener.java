package fr.catcore.translatedlegacy.stapi.listener;

import fr.catcore.translatedlegacy.babric.NetworkingHelper;
import fr.catcore.translatedlegacy.babric.language.LanguageManager;
import fr.catcore.translatedlegacy.stapi.networking.LanguageUpdatePacket;
import fr.catcore.translatedlegacy.stapi.networking.TLPingPacket;
import fr.catcore.translatedlegacy.stapi.networking.TranslationPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.network.MultiplayerLogoutEvent;
import net.modificationstation.stationapi.api.client.event.network.ServerLoginSuccessEvent;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.server.event.network.PlayerLoginEvent;
import net.modificationstation.stationapi.api.util.Namespace;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener
public final class PacketListener {
    private static Namespace PARENT = Namespace.of("translated-legacy");

    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, PARENT.id("update"), LanguageUpdatePacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, PARENT.id("ping"), TLPingPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, PARENT.id("translate"), TranslationPacket.TYPE);
    }

    private static LanguageManager.LanguageSwitchCallback callback;

    @Environment(EnvType.CLIENT)
    @EventListener
    public void onServerLoginSuccess(ServerLoginSuccessEvent event) {
        if (callback == null) {
            callback = (lang) -> {
                var packet = new LanguageUpdatePacket();
                packet.code = lang;
                PacketHelper.send(packet);
            };
        }
        LanguageManager.registerCallback(callback);

        var packet = new LanguageUpdatePacket();
        packet.code = LanguageManager.CURRENT_LANGUAGE.code;
        PacketHelper.send(packet);
    }

    @Environment(EnvType.CLIENT)
    @EventListener
    public void onServerDisconnect(MultiplayerLogoutEvent event) {
        if (callback != null) LanguageManager.removeCallback(callback);
        NetworkingHelper.serverSupportsUnicode = false;
    }

    @Environment(EnvType.SERVER)
    @EventListener
    public void onPlayerLogin(PlayerLoginEvent event) {
        PacketHelper.sendTo(event.player, new TLPingPacket());
    }
}
