package fr.catcore.translatedlegacy.ornithe.common.mixin.server;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import fr.catcore.translatedlegacy.ornithe.common.access.TranslatablePacket;
import net.minecraft.network.packet.ChatMessagePacket;
import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.text.api.TextComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Conditional(minecraftVersion = @Version(">=b1.6.5"))
    @Definition(id = "ChatMessagePacket", type = ChatMessagePacket.class)
    @Expression("new ChatMessagePacket(?)")
    @ModifyExpressionValue(method = "addMessage", at = @At("MIXINEXTRAS:EXPRESSION"))
    private ChatMessagePacket tl$markPacketAsTranslatable(ChatMessagePacket original, @Local(ordinal = 0) String message) {
        ((TranslatablePacket) original).setTexts(TextComponents.translatable(message));
        return original;
    }
}
