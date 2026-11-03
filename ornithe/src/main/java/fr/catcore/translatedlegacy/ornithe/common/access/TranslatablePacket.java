package fr.catcore.translatedlegacy.ornithe.common.access;

import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.text.api.TextComponent;

public interface TranslatablePacket {
    void setTexts(TextComponent... text);
    boolean requiresPacket();
    void writeCustomPacket(PacketBuffer buffer);
    default void makeTranslatable() {}
    default void preTranslate() {}
}
