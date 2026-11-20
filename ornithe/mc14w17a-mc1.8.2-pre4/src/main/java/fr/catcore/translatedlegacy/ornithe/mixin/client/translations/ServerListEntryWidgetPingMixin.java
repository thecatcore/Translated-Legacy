package fr.catcore.translatedlegacy.ornithe.mixin.client.translations;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.client.gui.widget.ServerListEntryWidget__23781818")
public class ServerListEntryWidgetPingMixin {
    @ModifyConstant(method = "run", constant = @Constant(stringValue = "Can't resolve hostname"))
    private String tl$translate$hostname(String constant) {
        return L10n.get("backport.multiplayer.status.cannot_resolve");
    }

    @ModifyConstant(method = "run", constant = @Constant(stringValue = "Can't connect to server."))
    private String tl$translate$server(String constant) {
        return L10n.get("backport.multiplayer.status.cannot_connect");
    }
}
