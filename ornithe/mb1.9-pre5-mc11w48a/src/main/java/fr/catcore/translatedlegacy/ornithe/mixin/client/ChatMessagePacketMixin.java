package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.ornithe.NetworkingHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.ChatMessagePacket;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;

@Environment(EnvType.CLIENT)
@Mixin(ChatMessagePacket.class)
public abstract class ChatMessagePacketMixin extends Packet {
    @Shadow
    public String message;

    @Inject(method = "read", at = @At("TAIL"))
    private void tl$translateMessage(DataInputStream par1, CallbackInfo ci) {
        if (NetworkingHelper.packetTranslations.containsKey(this.getId())) this.message = NetworkingHelper.packetTranslations.remove(this.getId())[0].buildFormattedString();
    }
}
