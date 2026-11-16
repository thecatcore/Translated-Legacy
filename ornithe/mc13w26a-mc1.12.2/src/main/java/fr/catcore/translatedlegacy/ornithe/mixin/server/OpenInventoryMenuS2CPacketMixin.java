package fr.catcore.translatedlegacy.ornithe.mixin.server;

import fr.catcore.translatedlegacy.ornithe.common.access.TranslatablePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.s2c.play.OpenInventoryMenuS2CPacket;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Environment(EnvType.SERVER)
@Conditional(minecraftVersion = @Version(">=13w41a <=1.7.10"))
@Mixin(OpenInventoryMenuS2CPacket.class)
public class OpenInventoryMenuS2CPacketMixin implements TranslatablePacket {

    private TextComponent title;

    @Inject(method = "<init>(IILjava/lang/String;IZ)V", at = @At("RETURN"))
    private void tl$wrapDisplayName$old(int menuId, int menuType, String displayName, int size, boolean hasCustomName, CallbackInfo ci) {
        if (L10n.has(displayName)) {
            title = TextComponents.translatable(displayName);
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
        buffer.writeCollection(Collections.singleton(this.title), PacketBuffer::writeTextComponent);
    }
}
