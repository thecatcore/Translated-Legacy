package fr.catcore.translatedlegacy.ornithe.mixin.server;

import fr.catcore.translatedlegacy.ornithe.common.access.TranslatablePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.ChatMessagePacket;
import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.text.api.MessageConsumer;
import net.ornithemc.osl.text.api.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Conditional(minecraftVersion = @Version(">=12w16a"))
@Environment(EnvType.SERVER)
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements MessageConsumer {
    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    @Override
    public void sendMessage(TextComponent message) {
        ChatMessagePacket packet = new ChatMessagePacket(message.buildFormattedString());
        ((TranslatablePacket) packet).setTexts(message);
        this.networkHandler.sendPacket(packet);
    }
}
