package fr.catcore.translatedlegacy.babric.mixin.client;

import net.minecraft.client.gui.screen.AchievementsScreen;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AchievementsScreen.class)
public class AchievementsScreenMixin {

    @ModifyArg(method = "setTitle", index = 0, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;III)V"
    ))
    public String method_1999$lang(String string) {
        return I18n.getTranslation("gui.achievements");
    }
}
