package fr.catcore.translatedlegacy.ornithe.mixin.server;

import fr.catcore.translatedlegacy.ornithe.access.TranslatablePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.OpenInventoryMenuPacket;
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
@Mixin(OpenInventoryMenuPacket.class)
public class OpenInventoryMenuPacketMixin implements TranslatablePacket {
    @Shadow
    public String displayName;

    private TextComponent title;

    @Inject(method = "<init>(IILjava/lang/String;I)V", at = @At("RETURN"))
    private void tl$wrapDisplayName(int i, int j, String string, int k, CallbackInfo ci) {
        if (L10n.has(string)) {
            title = TextComponents.translatable(string);
        }
    }

    @Override
    public void setTexts(TextComponent... text) {
        title = text[0];
    }

    @Override
    public boolean requiresPacket() {
        return title != null;
    }

    @Override
    public void writeCustomPacket(PacketBuffer buffer) {
        buffer.writeByte(1);
        buffer.writeString(TextComponents.toJson(title));
    }
}
