package fr.catcore.translatedlegacy.mixin.client.gui.widget;

import fr.catcore.translatedlegacy.font.BetterCharacterUtils;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextFieldWidget.class)
public class TextboxMixin {

    @Shadow private String text;

    @Shadow private int maxLength;

    @Inject(
            method = "keyPressed",
            cancellable = true,
            at = @At(
                    value = "INVOKE",
                    remap = false,
                    target = "Ljava/lang/String;indexOf(I)I"
            )
    )
    public void method_1877$fix(char c, int i, CallbackInfo ci) {
        if (BetterCharacterUtils.getId(c) >= 32 && (this.text.length() < this.maxLength || this.maxLength == 0)) {
            this.text = this.text + c;
        }
        ci.cancel();
    }
}
