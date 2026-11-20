package fr.catcore.translatedlegacy.ornithe.common.mixin.client.translations;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import net.ornithemc.osl.text.api.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Environment(EnvType.CLIENT)
@Conditional(minecraftVersion = @Version(">=b1.8-pre2 <13w41a"))
@Mixin(targets = "net.minecraft.client.gui.screen.menu.multiplayer.MultiplayerScreen__ServerListWidget__15868918")
public class OldServerListWidgetPingThreadMixin {
    @ModifyConstant(method = "run", constant = @Constant(stringValue = "§8Polling.."))
    private String tl$translate$polling(String constant) {
        return Formatting.DARK_GRAY + L10n.get("multiplayer.status.polling");
    }

    @ModifyConstant(method = "run", constant = @Constant(stringValue = "§4Can't resolve hostname"))
    private String tl$translate$hostname(String constant) {
        return Formatting.DARK_RED + L10n.get("backport.multiplayer.status.cannot_resolve");
    }

    @ModifyConstant(method = "run", constant = @Constant(stringValue = "§4Can't reach server"))
    private String tl$translate$server(String constant) {
        return Formatting.DARK_RED + L10n.get("multiplayer.status.cannot_reach");
    }

    @ModifyConstant(method = "run", constant = @Constant(stringValue = "§4Communication error"))
    private String tl$translate$communication(String constant) {
        return Formatting.DARK_RED + L10n.get("multiplayer.status.communication_error");
    }
}
