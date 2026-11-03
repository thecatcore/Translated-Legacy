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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Environment(EnvType.SERVER)
@Mixin(OpenInventoryMenuPacket.class)
public class OpenInventoryMenuPacketMixin implements TranslatablePacket {
    @Conditional(minecraftVersion = @Version("<=b1.7.3"))
    @Shadow
    public String displayName;

    private TextComponent title;

    @Conditional(minecraftVersion = @Version("<13w02a"))
    @Inject(method = "<init>(IILjava/lang/String;I)V", at = @At("RETURN"))
    private void tl$wrapDisplayName$old(int i, int j, String string, int k, CallbackInfo ci) {
        if (L10n.has(string)) {
            title = TextComponents.translatable(string);
        } else if (!NetworkingHelper.INVENTORY_NAME_PACKET_LIMIT) {
            title = TextComponents.literal(string);
        }
    }

    @Conditional(minecraftVersion = @Version(">=13w02a"))
    @Inject(method = "<init>(IILjava/lang/String;IZ)V", at = @At("RETURN"))
    private void tl$wrapDisplayName$new(int i, int j, String string, int k, boolean l, CallbackInfo ci) {
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
        return NetworkingHelper.INVENTORY_NAME_PACKET_LIMIT && title != null;
    }

    @Override
    public void writeCustomPacket(PacketBuffer buffer) {
        buffer.writeCollection(Collections.singleton(this.title), PacketBuffer::writeTextComponent);
    }

    @Conditional(minecraftVersion = @Version("<=b1.7.3"))
    @Override
    public void makeTranslatable() {
        displayName = TextComponents.toJson(this.title);
    }

    @Conditional(minecraftVersion = @Version("<=b1.7.3"))
    @Override
    public void preTranslate() {
        displayName = this.title.buildFormattedString();
    }
}
