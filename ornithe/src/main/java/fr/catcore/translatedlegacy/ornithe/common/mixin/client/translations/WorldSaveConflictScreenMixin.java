package fr.catcore.translatedlegacy.ornithe.common.mixin.client.translations;

import net.minecraft.client.gui.screen.menu.world.WorldSaveConflictScreen;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Conditional(minecraftVersion = @Version("<12w24a"))
@Mixin(WorldSaveConflictScreen.class)
public class WorldSaveConflictScreenMixin {

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V"))
    public String init$lang(String string) {
        return L10n.get("gui.toMenu");
    }
}
