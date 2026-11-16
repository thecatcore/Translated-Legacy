package fr.catcore.translatedlegacy.ornithe.common.mixin.client;

import fr.catcore.translatedlegacy.ornithe.common.NetworkingHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.ChatMessagePacket;
import net.minecraft.network.packet.Packet;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInput;
import java.io.DataInputStream;

@Environment(EnvType.CLIENT)
@Conditional(minecraftVersion = @Version("<13w21a"))
@Mixin(ChatMessagePacket.class)
public abstract class ChatMessagePacketMixin extends Packet {
    @Shadow
    public String message;

    @Conditional(minecraftVersion = @Version("<=1.5.2"))
    @Inject(method = "read", at = @At("TAIL"))
    private void tl$translateMessage(DataInputStream par1, CallbackInfo ci) {
        if (NetworkingHelper.packetTranslations.containsKey(this.getId())) this.message = NetworkingHelper.packetTranslations.remove(this.getId())[0].buildFormattedString();
    }

    @Conditional(minecraftVersion = @Version(">=13w17a"))
    @Inject(method = "m_13296744(Ljava/io/DataInput;)V", at = @At("TAIL"))
    private void tl$translateMessage$onsixsnapshot(DataInput par1, CallbackInfo ci) {
        if (NetworkingHelper.packetTranslations.containsKey(this.getId())) this.message = NetworkingHelper.packetTranslations.remove(this.getId())[0].buildFormattedString();
    }
}
