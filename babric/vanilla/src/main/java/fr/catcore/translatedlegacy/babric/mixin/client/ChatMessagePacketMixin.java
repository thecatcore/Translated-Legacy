package fr.catcore.translatedlegacy.babric.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.network.packet.play.ChatMessagePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;

@Environment(EnvType.CLIENT)
@Mixin(ChatMessagePacket.class)
public class ChatMessagePacketMixin {
    @Shadow
    public String chatMessage;

    @Inject(method = "read", at = @At("TAIL"))
    private void tl$translateContainerName(DataInputStream par1, CallbackInfo ci) {
        this.chatMessage = I18n.getTranslation(this.chatMessage);
    }
}
