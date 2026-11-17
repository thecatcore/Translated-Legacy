package fr.catcore.translatedlegacy.stapi.mixin.server;

import fr.catcore.translatedlegacy.stapi.networking.TranslatablePacket;
import fr.catcore.translatedlegacy.stapi.networking.TranslationPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.play.ChatMessagePacket;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.SERVER)
@Mixin(ChatMessagePacket.class)
public class ChatMessagePacketMixin implements TranslatablePacket {
    private String translationKey;

    @Override
    public void setTexts(String... text) {
        this.translationKey = text[0];
    }

    @Override
    public boolean requiresPacket() {
        return translationKey != null;
    }

    @Override
    public void writeCustomPacket(TranslationPacket packet) {
        packet.translations.add(this.translationKey);
    }
}
