package fr.catcore.translatedlegacy.stapi.mixin.server;

import fr.catcore.translatedlegacy.stapi.networking.TranslatablePacket;
import fr.catcore.translatedlegacy.stapi.networking.TranslationPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Environment(EnvType.SERVER)
@Mixin(OpenScreenS2CPacket.class)
public class OpenScreenS2CPacketMixin implements TranslatablePacket {

    private String titleKey;

    @Inject(method = "<init>(IILjava/lang/String;I)V", at = @At("RETURN"))
    private void tl$wrapDisplayName$old(int i, int j, String string, int k, CallbackInfo ci) {
        if (!Objects.equals(I18n.getTranslation(string), string)) {
            titleKey = string;
        }
    }

    @Override
    public void setTexts(String... text) {
        titleKey = text[0];
    }

    @Override
    public boolean requiresPacket() {
        return titleKey != null;
    }

    @Override
    public void writeCustomPacket(TranslationPacket packet) {
        packet.translations.add(titleKey);
    }
}
