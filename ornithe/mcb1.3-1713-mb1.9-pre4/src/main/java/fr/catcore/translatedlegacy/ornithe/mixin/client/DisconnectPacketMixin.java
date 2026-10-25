package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.ornithe.NetworkingHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.DisconnectPacket;
import net.minecraft.network.packet.Packet;
import net.ornithemc.osl.text.api.TextComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;

@Environment(EnvType.CLIENT)
@Mixin(DisconnectPacket.class)
public abstract class DisconnectPacketMixin extends Packet {

    @Shadow
    public String reason;

    @Inject(method = "read", at = @At("TAIL"))
    private void tl$translateContainerName(DataInputStream par1, CallbackInfo ci) {
        if (NetworkingHelper.packetTranslations.containsKey(this.getId())) this.reason = NetworkingHelper.packetTranslations.remove(this.getId())[0].buildFormattedString();
        else if (!NetworkingHelper.DISCONNECT_PACKET_LIMIT && NetworkingHelper.serverSupportsUnicode) this.reason = TextComponents.fromJson(reason).buildFormattedString();
    }
}
