package fr.catcore.translatedlegacy.ornithe.common.mixin.client.translations;

import net.minecraft.client.gui.screen.game.inventory.CreativeInventoryScreen;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Conditional(minecraftVersion = @Version(">=b1.8-pre2 <12w04a"))
@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin {
    @ModifyArg(
            method = "renderLabels",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/TextRenderer;draw(Ljava/lang/String;III)V",
                    ordinal = 0
            )
    )
    public String renderForeground$lang1(String string) {
        return L10n.get("backport.container.creative");
    }
}
