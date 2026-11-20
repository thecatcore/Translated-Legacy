package fr.catcore.translatedlegacy.ornithe.mixin.client.translations;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ServerListEntryWidget;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Environment(EnvType.CLIENT)
@Conditional(minecraftVersion = @Version(">=13w41a"))
@Mixin(ServerListEntryWidget.class)
public class ServerListWidgetMixin {
    @ModifyConstant(method = "render", constant = @Constant(stringValue = "Pinging..."))
    private String tl$translate$pinging(String constant) {
        return L10n.get("backport.multiplayer.status.pinging");
    }

    @ModifyConstant(method = "render", constant = @Constant(stringValue = "(no connection)"))
    private String tl$translate$noConnection(String constant) {
        return L10n.get("backport.multiplayer.status.no_connection");
    }

    @ModifyConstant(method = "render", constant = @Constant(stringValue = "Client out of date!"))
    private String tl$translate$clientOutOfDate(String constant) {
        return L10n.get("backport.multiplayer.status.client_out_of_date");
    }

    @ModifyConstant(method = "render", constant = @Constant(stringValue = "Server out of date!"))
    private String tl$translate$serverOutOfDate(String constant) {
        return L10n.get("backport.multiplayer.status.server_out_of_date");
    }
}
