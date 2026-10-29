package fr.catcore.translatedlegacy.ornithe.common.mixin.server;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fr.catcore.translatedlegacy.ornithe.common.NetworkingHelper;
import fr.catcore.translatedlegacy.ornithe.common.access.TranslatablePacket;
import fr.catcore.translatedlegacy.ornithe.common.access.TranslationRecipient;
import fr.catcore.translatedlegacy.util.TextUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.ChatMessagePacket;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;
import net.ornithemc.osl.text.api.Formatting;
import net.ornithemc.osl.text.api.MessageConsumer;
import net.ornithemc.osl.text.api.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin implements MessageConsumer, TranslationRecipient {
    @Shadow
    private ServerPlayerEntity player;

    @Shadow
    public abstract void sendPacket(Packet packet);

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

    @Conditional(minecraftVersion = @Version("<12w04a"))
    @ModifyExpressionValue(method = "handleChatMessage", at = @At(value = "INVOKE", target = "Ljava/lang/String;charAt(I)C", remap = false))
    private char allowMoreCharactersInChat(char original) {
        return TextUtils.isCharacterAllowed(original) == 0 ? 'a' : original;
    }

    @Redirect(method = "handleSignBlockEntityUpdate", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", remap = false))
    private int allowMoreCharactersInSign(String instance, int ch) {
        return TextUtils.isCharacterAllowed(ch);
    }

    @Override
    public void sendMessage(TextComponent message) {
        message = message.format(Formatting.GOLD);
        ChatMessagePacket packet = new ChatMessagePacket(message.buildFormattedString());
        ((TranslatablePacket) packet).setTexts(message);
        this.sendPacket(packet);
    }
}
