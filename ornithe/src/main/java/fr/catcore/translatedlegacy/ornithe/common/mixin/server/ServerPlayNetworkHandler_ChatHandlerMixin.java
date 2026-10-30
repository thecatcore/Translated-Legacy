package fr.catcore.translatedlegacy.ornithe.common.mixin.server;

import fr.catcore.translatedlegacy.ornithe.common.access.TranslatablePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.ChatMessagePacket;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.text.api.Formatting;
import net.ornithemc.osl.text.api.MessageConsumer;
import net.ornithemc.osl.text.api.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Conditional(minecraftVersion = @Version("<12w16a"))
@Environment(EnvType.SERVER)
@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandler_ChatHandlerMixin implements MessageConsumer {

    @Shadow
    public abstract void sendPacket(Packet packet);

    @Override
    public void sendMessage(TextComponent message) {
        message = message.format(Formatting.GRAY);
        ChatMessagePacket packet = new ChatMessagePacket(message.buildFormattedString());
        ((TranslatablePacket) packet).setTexts(message);
        this.sendPacket(packet);
    }
}
