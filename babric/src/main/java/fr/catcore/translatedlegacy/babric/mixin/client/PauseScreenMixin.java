package fr.catcore.translatedlegacy.babric.mixin.client;

import fr.catcore.translatedlegacy.babric.mixin.client.screen.ScreenAccessor;
import net.minecraft.class_525;
import net.minecraft.class_629;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_525.class)
public class PauseScreenMixin {

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V", ordinal = 0))
    public String init$lang1(String string) {
        return class_629.method_2049("menu.returnToMenu");
    }

    @Inject(method = "init", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;text:Ljava/lang/String;", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    public void init$lang2(CallbackInfo ci) {
        ((ButtonWidget)((ScreenAccessor)this).getButtons().get(0)).text = class_629.method_2049("menu.disconnect");
    }

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V", ordinal = 1))
    public String init$lang3(String string) {
        return class_629.method_2049("menu.returnToGame");
    }

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V", ordinal = 2))
    public String init$lang4(String string) {
        return class_629.method_2049("menu.options");
    }

    @ModifyArg(method = "render", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_525;drawStringWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    public String render$lang1(String string) {
        return class_629.method_2049("menu.saving");
    }

    @ModifyArg(method = "render", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_525;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    public String render$lang2(String string) {
        return class_629.method_2049("menu.game");
    }
}
