package fr.catcore.translatedlegacy.ornithe.common.mixin.client.translations;

import fr.catcore.translatedlegacy.ornithe.common.TranslationHelper;
import net.minecraft.client.gui.screen.game.inventory.DispenserScreen;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Conditional(minecraftVersion = @Version(">=b1.2 <12w04a"))
@Mixin(DispenserScreen.class)
public abstract class DispenserScreenMixin {

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
        return TranslationHelper.get("backport.container.dispenser", "container.dispenser");
    }

    @ModifyArg(
            method = "renderLabels",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/TextRenderer;draw(Ljava/lang/String;III)V",
                    ordinal = 1
            )
    )
    public String renderForeground$lang2(String string) {
        return TranslationHelper.get("backport.container.inventory", "container.inventory");
    }
}
