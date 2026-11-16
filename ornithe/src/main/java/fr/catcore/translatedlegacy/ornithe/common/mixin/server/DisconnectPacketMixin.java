package fr.catcore.translatedlegacy.ornithe.common.mixin.server;

import fr.catcore.translatedlegacy.ornithe.common.access.TranslatablePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.DisconnectPacket;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Environment(EnvType.SERVER)
@Conditional(minecraftVersion = @Version("<13w41a"))
@Mixin(DisconnectPacket.class)
public class DisconnectPacketMixin implements TranslatablePacket {
    @Unique
    private TextComponent message;

    @Inject(method = "<init>(Ljava/lang/String;)V", at = @At("RETURN"))
    private void tl$wrapDisplayName(String reason, CallbackInfo ci) {
        if (L10n.has(reason)) {
            message = TextComponents.translatable(reason);
        }
    }

    @Override
    public void setTexts(TextComponent... text) {
        this.message = text[0];
    }

    @Override
    public boolean requiresPacket() {
        return this.message != null;
    }

    @Override
    public void writeCustomPacket(PacketBuffer buffer) {
        buffer.writeCollection(Collections.singleton(this.message), PacketBuffer::writeTextComponent);
    }
}
