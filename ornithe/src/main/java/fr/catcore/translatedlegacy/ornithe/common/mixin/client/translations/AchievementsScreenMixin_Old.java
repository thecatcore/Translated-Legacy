package fr.catcore.translatedlegacy.ornithe.common.mixin.client.translations;

import net.minecraft.client.gui.screen.menu.AchievementsScreen;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Conditional(minecraftVersion = @Version(">=b1.5 <1.4.4"))
@Mixin(AchievementsScreen.class)
public class AchievementsScreenMixin_Old {

    @ModifyArg(method = "setTitle", index = 0, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/TextRenderer;draw(Ljava/lang/String;III)V"
    ))
    public String tl$translateTitle(String string) {
        return L10n.get("gui.achievements");
    }
}
