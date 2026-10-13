package fr.catcore.translatedlegacy.stapi.mixin;

import fr.catcore.translatedlegacy.stapi.TranslatedLegacyNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.impl.server.network.ServerVanillaChecker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerVanillaChecker.class)
public class ServerVanillaCheckerMixin {
    @Inject(method = "lambda$registerMessages$2", at = @At(value = "HEAD"))
    private static void setDelegateTranslation(PlayerEntity player, MessagePacket message, CallbackInfo ci) {
        Map<String, String> clientMods = new HashMap<>();
        for (int i = 1; i < message.strings.length; i += 2)
            clientMods.put(message.strings[i], message.strings[i + 1]);
        ((TranslatedLegacyNetworkHandler) ((ServerPlayerEntity) player).networkHandler).setDelegateTranslation(clientMods.containsKey("translated-legacy"));
    }
}
