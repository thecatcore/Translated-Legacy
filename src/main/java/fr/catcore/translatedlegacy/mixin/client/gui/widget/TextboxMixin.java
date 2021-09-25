package fr.catcore.translatedlegacy.mixin.client.gui.widget;

import fr.catcore.translatedlegacy.font.BetterCharacterUtils;
import net.minecraft.client.gui.widgets.Textbox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Textbox.class)
public class TextboxMixin {

    @Shadow private String field_2427;

    @Shadow private int field_2428;

    @Inject(
            method = "method_1877",
            cancellable = true,
            at = @At(
                    value = "INVOKE",
                    remap = false,
                    target = "Ljava/lang/String;indexOf(I)I"
            )
    )
    public void method_1877$fix(char c, int i, CallbackInfo ci) {
        if (BetterCharacterUtils.getId(c) >= 32 && (this.field_2427.length() < this.field_2428 || this.field_2428 == 0)) {
            this.field_2427 = this.field_2427 + c;
        }
        ci.cancel();
    }
}
