package fr.catcore.translatedlegacy.stapi.mixin.server;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import fr.catcore.translatedlegacy.stapi.networking.TranslatablePacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.play.ChatMessagePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Definition(id = "ChatMessagePacket", type = ChatMessagePacket.class)
    @Expression("new ChatMessagePacket(?)")
    @ModifyExpressionValue(method = "sendMessage", at = @At("MIXINEXTRAS:EXPRESSION"))
    private ChatMessagePacket tl$markPacketAsTranslatable(ChatMessagePacket original, @Local(ordinal = 0) String message) {
        ((TranslatablePacket) original).setTexts(message);
        return original;
    }
}
