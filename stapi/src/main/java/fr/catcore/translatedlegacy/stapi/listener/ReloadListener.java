package fr.catcore.translatedlegacy.stapi.listener;

import fr.catcore.translatedlegacy.font.TextRenderer;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.resource.AssetsReloadEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener
public final class ReloadListener {
    @EventListener
    public static void reloadResourceManager(final AssetsReloadEvent event) {
        TextRenderer.reload();
    }
}
