package fr.catcore.translatedlegacy.stapi.mixin.client;

import fr.catcore.translatedlegacy.babric.NetworkingHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.play.DisconnectPacket;
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
        if (NetworkingHelper.packetTranslations.containsKey(this.getRawId())) this.reason = I18n.getTranslation(NetworkingHelper.packetTranslations.remove(this.getRawId())[0]);
    }
}
