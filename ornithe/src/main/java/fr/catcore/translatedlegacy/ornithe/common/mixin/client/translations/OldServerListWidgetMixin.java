package fr.catcore.translatedlegacy.ornithe.common.mixin.client.translations;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Environment(EnvType.CLIENT)
@Conditional(minecraftVersion = @Version(">=b1.8-pre2 <13w41a"))
@Mixin(targets = "net.minecraft.client.gui.screen.menu.multiplayer.MultiplayerScreen__ServerListWidget")
public class OldServerListWidgetMixin {
    @Conditional(minecraftVersion = @Version(">=12w25a"))
    @ModifyConstant(method = "m_59186403(IIIILnet/minecraft/unmapped/C_96603444;)V", constant = @Constant(stringValue = "Polling.."))
    private String tl$translate$polling$nw(String constant) {
        return L10n.get("multiplayer.status.polling");
    }

    @Conditional(minecraftVersion = @Version(">=12w25a"))
    @ModifyConstant(method = "m_59186403(IIIILnet/minecraft/unmapped/C_96603444;)V", constant = @Constant(stringValue = "(no connection)"))
    private String tl$translate$noConnection$nw(String constant) {
        return L10n.get("backport.multiplayer.status.no_connection");
    }

    @Conditional(minecraftVersion = @Version(">=12w42b"))
    @ModifyConstant(method = "m_59186403(IIIILnet/minecraft/unmapped/C_96603444;)V", constant = @Constant(stringValue = "Client out of date!"))
    private String tl$translate$clientOutOfDate(String constant) {
        return L10n.get("backport.multiplayer.status.client_out_of_date");
    }

    @Conditional(minecraftVersion = @Version(">=12w42b"))
    @ModifyConstant(method = "m_59186403(IIIILnet/minecraft/unmapped/C_96603444;)V", constant = @Constant(stringValue = "Server out of date!"))
    private String tl$translate$serverOutOfDate(String constant) {
        return L10n.get("backport.multiplayer.status.server_out_of_date");
    }

    @Conditional(minecraftVersion = @Version(">=1.3.2 <12w42a"))
    @ModifyConstant(method = "*", constant = @Constant(stringValue = "(Hidden)"))
    private String tl$translate$hidden(String constant) {
        return L10n.get("backport.selectServer.hiddenAddress");
    }

    @Conditional(minecraftVersion = @Version("<12w25a"))
    @ModifyConstant(method = "renderEntry", constant = @Constant(stringValue = "Polling.."))
    private String tl$translate$polling$old(String constant) {
        return L10n.get("multiplayer.status.polling");
    }

    @Conditional(minecraftVersion = @Version("<12w25a"))
    @ModifyConstant(method = "renderEntry", constant = @Constant(stringValue = "(no connection)"))
    private String tl$translate$noConnection$old(String constant) {
        return L10n.get("backport.multiplayer.status.no_connection");
    }
}
