package fr.catcore.translatedlegacy.ornithe.mixin.server;

import fr.catcore.translatedlegacy.ornithe.NetworkingHelper;
import fr.catcore.translatedlegacy.ornithe.access.TranslatablePacket;
import fr.catcore.translatedlegacy.ornithe.access.TranslationRecipient;
import fr.catcore.translatedlegacy.util.TextUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin implements TranslationRecipient {
    @Shadow
    private ServerPlayerEntity player;
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
    private void tl$handleTranslatablePackets(Packet packet, CallbackInfo ci) {
        if (packet instanceof TranslatablePacket) {
            TranslatablePacket translatablePacket = (TranslatablePacket) packet;
            if (translatablePacket.requiresPacket()) {
                if (!pretranslate()) {
                    PacketBuffer buffer = PacketBuffers.make();
                    buffer.writeInt(packet.getId());
                    translatablePacket.writeCustomPacket(buffer);

                    ServerPlayNetworking.send(player, NetworkingHelper.TRANSLATE_ID, buffer);
                }
            } else {
                if (pretranslate()) {
                    translatablePacket.preTranslate();
                } else {
                    translatablePacket.makeTranslatable();
                }
            }
        }
    }

    @Redirect(method = {"handleChatMessage", "handleSignBlockEntityUpdate"}, at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", remap = false))
    private int allowMoreCharactersInChat(String instance, int ch) {
        return TextUtils.isCharacterAllowed(ch);
    }
}
