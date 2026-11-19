package fr.catcore.translatedlegacy.ornithe.common.mixin.client.translations;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.game.SignEditScreen;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Conditional(minecraftVersion = @Version("<13w47a"))
@Mixin(SignEditScreen.class)
public class SignEditScreenMixin extends Screen {

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V"))
    public String init$lang(String string) {
        return L10n.get("gui.done");
    }

    @ModifyArg(method = "render", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/game/SignEditScreen;drawCenteredString(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V"))
    public String render$lang(String string) {
        return L10n.get("backport.sign.edit") + ":";
    }
}
