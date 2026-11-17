package fr.catcore.translatedlegacy.stapi.mixin.server;

import fr.catcore.translatedlegacy.stapi.networking.TranslatablePacket;
import fr.catcore.translatedlegacy.stapi.networking.TranslationPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.network.packet.play.DisconnectPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Environment(EnvType.SERVER)
@Mixin(DisconnectPacket.class)
public class DisconnectPacketMixin implements TranslatablePacket {
    @Unique
    private String translationKey;

    @Inject(method = "<init>(Ljava/lang/String;)V", at = @At("RETURN"))
    private void tl$wrapDisplayName(String reason, CallbackInfo ci) {
        if (!Objects.equals(I18n.getTranslation(reason), reason)) {
            translationKey = reason;
        }
    }

    @Override
    public void setTexts(String... text) {
        translationKey = text[0];
    }

    @Override
    public boolean requiresPacket() {
        return this.translationKey != null;
    }

    @Override
    public void writeCustomPacket(TranslationPacket packet) {
        packet.translations.add(translationKey);
    }
}
