package fr.catcore.translatedlegacy.ornithe.common.mixin.client.translations;

import fr.catcore.translatedlegacy.ornithe.common.TranslationHelper;
import net.minecraft.client.gui.screen.game.inventory.SurvivalInventoryScreen;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Conditional(minecraftVersion = @Version("<12w04a"))
@Mixin(SurvivalInventoryScreen.class)
public abstract class SurvivalInventoryScreenMixin {

    @ModifyArg(
            method = "renderLabels",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/TextRenderer;draw(Ljava/lang/String;III)V"
            )
    )
    public String renderForeground$lang(String string) {
        return TranslationHelper.get("backport.container.crafting", "container.crafting");
    }
}
