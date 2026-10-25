package fr.catcore.translatedlegacy.ornithe.mixin.server;

import fr.catcore.translatedlegacy.ornithe.access.TranslatablePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.DisconnectPacket;
import net.ornithemc.osl.localization.api.L10n;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(DisconnectPacket.class)
public class DisconnectPacketMixin implements TranslatablePacket {
    @Shadow
    public String reason;
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
        buffer.writeByte(1);
        buffer.writeString(TextComponents.toJson(message));
    }
}
