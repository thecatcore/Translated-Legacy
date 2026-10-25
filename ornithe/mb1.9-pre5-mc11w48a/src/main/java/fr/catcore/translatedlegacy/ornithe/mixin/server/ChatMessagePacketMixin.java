package fr.catcore.translatedlegacy.ornithe.mixin.server;

import fr.catcore.translatedlegacy.ornithe.access.TranslatablePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.ChatMessagePacket;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;
import org.spongepowered.asm.mixin.Mixin;

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
        buffer.writeByte(1);
        buffer.writeString(TextComponents.toJson(this.text));
    }
}
