package fr.catcore.translatedlegacy.ornithe.common.mixin.server;

import fr.catcore.translatedlegacy.ornithe.common.NetworkingHelper;
import fr.catcore.translatedlegacy.ornithe.common.access.TranslatablePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.OpenInventoryMenuPacket;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Environment(EnvType.SERVER)
@Conditional(minecraftVersion = @Version("<13w41a"))
@Mixin(OpenInventoryMenuPacket.class)
public class OpenInventoryMenuPacketMixin implements TranslatablePacket {

    @Conditional(minecraftVersion = @Version("<13w02a"))
    @Shadow
    public String displayName;

    @Unique
    private TextComponent tl$title;

    @Conditional(minecraftVersion = @Version("<13w02a"))
    @Inject(method = "<init>(IILjava/lang/String;I)V", at = @At("RETURN"))
    private void tl$wrapDisplayName$old(int i, int j, String string, int k, CallbackInfo ci) {
        if (L10n.has(string)) {
            tl$title = TextComponents.translatable(string);
            if (!NetworkingHelper.SCREEN_NAME_IS_KEY) {
                displayName = tl$title.buildFormattedString();
            }
        }
    }

    @Conditional(minecraftVersion = @Version(">=13w02a"))
    @Inject(method = "<init>(IILjava/lang/String;IZ)V", at = @At("RETURN"))
    private void tl$wrapDisplayName$new(int i, int j, String string, int k, boolean l, CallbackInfo ci) {
        if (L10n.has(string)) {
            tl$title = TextComponents.translatable(string);
        }
    }

    @Override
    public void setTexts(TextComponent... text) {
        tl$title = text[0];
    }

    @Override
    public boolean requiresPacket() {
        return tl$title != null;
    }

    @Override
    public void writeCustomPacket(PacketBuffer buffer) {
        buffer.writeCollection(Collections.singleton(this.tl$title), PacketBuffer::writeTextComponent);
    }
}
