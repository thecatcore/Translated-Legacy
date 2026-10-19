package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.util.TextUtils;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TextFieldWidget.class)
public class TextFieldWidgetMixin {

    @Redirect(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    remap = false,
                    target = "Ljava/lang/String;indexOf(I)I"
            )
    )
    public int tl$fixInput(String instance, int ch) {
        return TextUtils.isCharacterAllowed(ch);
    }
}
