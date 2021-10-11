package fr.catcore.translatedlegacy.mixin.client;

import net.minecraft.client.gui.screen.AchievementsScreen;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AchievementsScreen.class)
public class AchievementsScreenMixin {

    @ModifyArg(method = "method_1999", index = 0, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/TextRenderer;drawText(Ljava/lang/String;III)V"
    ))
    public String method_1999$lang(String string) {
        return I18n.translate("gui.achievements");
    }
}
