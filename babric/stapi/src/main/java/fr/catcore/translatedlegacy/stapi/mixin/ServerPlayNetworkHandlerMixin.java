package fr.catcore.translatedlegacy.stapi.mixin;

import fr.catcore.translatedlegacy.language.TranslationRecipient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin implements TranslationRecipient {
    @Unique
    private String languageCode;

    @Override
    public String getLanguage() {
        return languageCode;
    }

    @Override
    public void setLanguage(String language) {
        this.languageCode = language;
    }

    @Override
    public boolean pretranslate() {
        return languageCode == null;
    }

    @Inject(method = "sendPacket", at = @At(value = "HEAD"))
    private void tl$delegateTranslation(Packet par1, CallbackInfo ci) {
        if (this.pretranslate()) {
            if (par1 instanceof OpenScreenS2CPacket openScreenS2CPacket) {
                openScreenS2CPacket.name = I18n.getTranslation(openScreenS2CPacket.name);
            }
        }
    }
}
