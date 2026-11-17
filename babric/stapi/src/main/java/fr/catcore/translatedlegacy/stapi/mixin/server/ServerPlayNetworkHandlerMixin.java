package fr.catcore.translatedlegacy.stapi.mixin.server;

import fr.catcore.translatedlegacy.language.TranslationRecipient;
import fr.catcore.translatedlegacy.stapi.networking.TranslatablePacket;
import fr.catcore.translatedlegacy.stapi.networking.TranslationPacket;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin implements TranslationRecipient {
    @Shadow
    private ServerPlayerEntity player;
    @Unique
    private String languageCode;

    @Override
    public String tl$getLanguage() {
        return languageCode;
    }

    @Override
    public void tl$setLanguage(String language) {
        this.languageCode = language;
    }

    @Override
    public boolean tl$pretranslate() {
        return languageCode == null;
    }

    @Inject(method = "sendPacket", at = @At(value = "HEAD"))
    private void tl$delegateTranslation(Packet par1, CallbackInfo ci) {
        if (this.tl$pretranslate()) {
            if (par1 instanceof OpenScreenS2CPacket openScreenS2CPacket) {
                openScreenS2CPacket.name = I18n.getTranslation(openScreenS2CPacket.name);
            }
        }

        if (par1 instanceof TranslatablePacket translatablePacket) {
            if (translatablePacket.requiresPacket()) {
                if (!tl$pretranslate()) {
                    var translationPacket = new TranslationPacket();
                    translationPacket.originalId = par1.getRawId();
                    translatablePacket.writeCustomPacket(translationPacket);
                    PacketHelper.sendTo(this.player, translationPacket);
                }
            } else {
                if (tl$pretranslate()) {
                    translatablePacket.preTranslate();
                } else {
                    translatablePacket.makeTranslatable();
                }
            }
        }
    }
}
