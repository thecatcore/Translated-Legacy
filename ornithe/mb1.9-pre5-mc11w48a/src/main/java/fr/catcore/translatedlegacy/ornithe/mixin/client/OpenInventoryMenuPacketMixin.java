package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.ornithe.NetworkingHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.OpenInventoryMenuPacket;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;

@Environment(EnvType.CLIENT)
@Mixin(OpenInventoryMenuPacket.class)
public abstract class OpenInventoryMenuPacketMixin extends Packet {

    @Shadow
    public String displayName;

    @Inject(method = "read", at = @At("TAIL"))
    private void tl$translateContainerName(DataInputStream par1, CallbackInfo ci) {
        if (NetworkingHelper.packetTranslations.containsKey(this.getId())) this.displayName = NetworkingHelper.packetTranslations.remove(this.getId())[0].buildFormattedString();
    }
}
