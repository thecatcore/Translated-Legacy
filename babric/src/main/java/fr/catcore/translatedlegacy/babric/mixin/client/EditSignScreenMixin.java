package fr.catcore.translatedlegacy.babric.mixin.client;

import fr.catcore.translatedlegacy.util.TextUtils;
import net.minecraft.class_629;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SignEditScreen.class)
public class EditSignScreenMixin {

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V"))
    public String init$lang(String string) {
        return class_629.method_2049("gui.done");
    }

    @Redirect(method = "keyPressed", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I"))
    public int fixCharInput(String instance, int ch) {
        return TextUtils.isCharacterAllowed(ch);
    }

    @ModifyArg(method = "render", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/SignEditScreen;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    public String render$lang(String string) {
        return class_629.method_2049("sign.edit");
    }
}
