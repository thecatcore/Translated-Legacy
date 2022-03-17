package fr.catcore.translatedlegacy.mixin.client;

import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EditSignScreen.class)
public class EditSignScreenMixin {

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widgets/Button;<init>(IIILjava/lang/String;)V"))
    public String init$lang(String string) {
        return I18n.translate("gui.done");
    }

    @ModifyArg(method = "keyPressed", index = 0, at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I"))
    public int fixCharInput(int ch) {
        return '0';
    }

    @ModifyArg(method = "render", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/EditSignScreen;drawTextWithShadowCentred(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V"))
    public String render$lang(String string) {
        return I18n.translate("sign.edit");
    }
}
