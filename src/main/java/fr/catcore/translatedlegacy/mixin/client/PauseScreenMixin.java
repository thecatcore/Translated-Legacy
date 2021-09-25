package fr.catcore.translatedlegacy.mixin.client;

import net.minecraft.client.gui.screen.PauseScreen;
import net.minecraft.client.resource.language.I18n;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PauseScreen.class)
public class PauseScreenMixin {

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widgets/Button;<init>(IIILjava/lang/String;)V", ordinal = 0))
    public String init$lang1(String string) {
        return I18n.translate("menu.returnToMenu");
    }

    @ModifyVariable(method = "init", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/widgets/Button;text:Ljava/lang/String;", opcode = Opcodes.PUTFIELD))
    public String init$lang2(String string) {
        return I18n.translate("menu.disconnect");
    }

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widgets/Button;<init>(IIILjava/lang/String;)V", ordinal = 1))
    public String init$lang3(String string) {
        return I18n.translate("menu.returnToGame");
    }

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widgets/Button;<init>(IIILjava/lang/String;)V", ordinal = 2))
    public String init$lang4(String string) {
        return I18n.translate("menu.options");
    }
}
