package fr.catcore.translatedlegacy.babric.mixin;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;

@Mixin(OpenScreenS2CPacket.class)
public class OpenScreenS2CPacketMixin {
    @Shadow
    public String name;

    @Inject(method = "read", at = @At("TAIL"))
    private void tl$translateContainerName(DataInputStream par1, CallbackInfo ci) {
        this.name = I18n.getTranslation(this.name);
    }
}
