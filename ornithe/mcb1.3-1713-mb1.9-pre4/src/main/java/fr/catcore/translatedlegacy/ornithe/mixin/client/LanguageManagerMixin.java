package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.ornithe.common.NetworkingHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ornithemc.osl.localization.api.language.Language;
import net.ornithemc.osl.localization.impl.language.LanguageManager;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LanguageManager.class)
public class LanguageManagerMixin {
    @Shadow
    private String selectedLanguage;

    @Inject(method = "setSelectedLanguage(Ljava/lang/String;)V", at = @At("RETURN"))
    private void tl$updateLanguagePacket(String language, CallbackInfo ci) {
        if (ClientPlayNetworking.isPlayReady()) {
            PacketBuffer buffer = PacketBuffers.make();
            buffer.writeString(this.selectedLanguage);

            ClientPlayNetworking.send(NetworkingHelper.UPDATE_ID, buffer);
        }
    }

    @Inject(method = "setSelectedLanguage(Lnet/ornithemc/osl/localization/api/language/Language;)V", at = @At("RETURN"))
    private void tl$updateLanguagePacket(Language language, CallbackInfo ci) {
        if (ClientPlayNetworking.isPlayReady()) {
            PacketBuffer buffer = PacketBuffers.make();
            buffer.writeString(this.selectedLanguage);

            ClientPlayNetworking.send(NetworkingHelper.UPDATE_ID, buffer);
        }
    }
}
