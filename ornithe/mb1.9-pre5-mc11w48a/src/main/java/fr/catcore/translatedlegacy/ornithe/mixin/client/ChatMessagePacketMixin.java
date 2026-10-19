package fr.catcore.translatedlegacy.ornithe.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.ChatMessagePacket;
import net.ornithemc.osl.localization.api.L10n;
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
    public String message;

    @Inject(method = "read", at = @At("TAIL"))
    private void tl$translateMessage(DataInputStream par1, CallbackInfo ci) {
        this.message = L10n.get(this.message);
    }
}
