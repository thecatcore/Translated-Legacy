package fr.catcore.translatedlegacy.mixin.client;

import net.minecraft.class_629;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SignEditScreen.class)
public class EditSignScreenMixin {

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V"))
    public String init$lang(String string) {
        return class_629.method_2049("gui.done");
    }

    @ModifyArg(method = "keyPressed", index = 0, at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I"))
    public int fixCharInput(int ch) {
        return '0';
    }

    @ModifyArg(method = "render", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/SignEditScreen;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    public String render$lang(String string) {
        return class_629.method_2049("sign.edit");
    }
}
