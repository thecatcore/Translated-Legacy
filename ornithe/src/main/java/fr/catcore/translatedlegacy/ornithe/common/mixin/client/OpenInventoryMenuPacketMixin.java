package fr.catcore.translatedlegacy.ornithe.common.mixin.client;

import fr.catcore.translatedlegacy.ornithe.common.NetworkingHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.OpenInventoryMenuPacket;
import net.minecraft.network.packet.Packet;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;

@Environment(EnvType.CLIENT)
@Conditional(minecraftVersion = @Version("<13w41a"))
@Mixin(OpenInventoryMenuPacket.class)
public abstract class OpenInventoryMenuPacketMixin extends Packet {

    @Shadow
    public String displayName;

    @Conditional(minecraftVersion = @Version("<=1.5.2"))
    @Inject(method = "read", at = @At("TAIL"))
    private void tl$translateContainerName(DataInputStream par1, CallbackInfo ci) {
        if (NetworkingHelper.packetTranslations.containsKey(this.getId())) this.displayName = NetworkingHelper.packetTranslations.remove(this.getId())[0].buildFormattedString();
    }

    @Conditional(minecraftVersion = @Version(">=13w17a"))
    @Inject(method = "m_13296744(Ljava/io/DataInput;)V", at = @At("TAIL"))
    private void tl$translateContainerName$newer(DataInputStream par1, CallbackInfo ci) {
        if (NetworkingHelper.packetTranslations.containsKey(this.getId())) this.displayName = NetworkingHelper.packetTranslations.remove(this.getId())[0].buildFormattedString();
    }
}
