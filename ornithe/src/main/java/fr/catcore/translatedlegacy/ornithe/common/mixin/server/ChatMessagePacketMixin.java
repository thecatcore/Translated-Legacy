package fr.catcore.translatedlegacy.ornithe.common.mixin.server;

import fr.catcore.translatedlegacy.ornithe.common.access.TranslatablePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.ChatMessagePacket;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.text.api.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import java.util.Collections;

@Environment(EnvType.SERVER)
@Mixin(ChatMessagePacket.class)
public class ChatMessagePacketMixin implements TranslatablePacket {
    private TextComponent text;

    @Override
    public void setTexts(TextComponent... text) {
        this.text = text[0];
    }

    @Override
    public boolean requiresPacket() {
        return this.text != null;
    }

    @Override
    public void writeCustomPacket(PacketBuffer buffer) {
        buffer.writeCollection(Collections.singleton(this.text), PacketBuffer::writeTextComponent);
    }
}
