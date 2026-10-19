package fr.catcore.translatedlegacy.ornithe.mixin.server;

import fr.catcore.translatedlegacy.ornithe.access.TranslationRecipient;
import fr.catcore.translatedlegacy.util.TextUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.OpenInventoryMenuPacket;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.ornithemc.osl.localization.api.L10n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin implements TranslationRecipient {
    @Unique
    private String language;

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public boolean pretranslate() {
        return language == null;
    }

    @Inject(method = "sendPacket", at = @At("HEAD"))
    private void tl$precomputeTranslationsForVanillaClients(Packet packet, CallbackInfo ci) {
        if (pretranslate()) {
            if (packet instanceof OpenInventoryMenuPacket) {
                ((OpenInventoryMenuPacket) packet).displayName = L10n.get(((OpenInventoryMenuPacket) packet).displayName);
            }
        }
    }

    @Redirect(method = "handleChatMessage", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", remap = false))
    private int allowMoreCharactersInChat(String instance, int ch) {
        return TextUtils.isCharacterAllowed(ch);
    }

    @Redirect(method = "handleSignBlockEntityUpdate", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", remap = false))
    private int allowMoreCharactersOnSigns(String instance, int ch) {
        return TextUtils.isCharacterAllowed(ch);
    }
}
