package fr.catcore.translatedlegacy.babric.mixin.client.gui.widget;

import fr.catcore.translatedlegacy.util.TextUtils;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TextFieldWidget.class)
public class TextboxMixin {

    @Redirect(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    remap = false,
                    target = "Ljava/lang/String;indexOf(I)I"
            )
    )
    public int method_1877$fix(String instance, int ch) {
        return TextUtils.isCharacterAllowed(ch);
    }
}
