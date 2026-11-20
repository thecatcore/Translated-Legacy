package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.ornithe.ModernNetworkUtils;
import fr.catcore.translatedlegacy.ornithe.common.NetworkingHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.OpenInventoryMenuS2CPacket;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Conditional(minecraftVersion = @Version(">=13w41a <=1.7.10"))
@Mixin(OpenInventoryMenuS2CPacket.class)
public abstract class OpenInventoryMenuS2CPacketMixin extends Packet {

    @Shadow
    private String displayName;

    @Inject(method = "read", at = @At("TAIL"))
    private void tl$translateContainerName$newer(PacketByteBuf buffer, CallbackInfo ci) {
        if (NetworkingHelper.packetTranslations.containsKey(ModernNetworkUtils.getPacketId(this))) {
            this.displayName = NetworkingHelper.packetTranslations.remove(ModernNetworkUtils.getPacketId(this))[0].buildFormattedString();
        }
    }
}
