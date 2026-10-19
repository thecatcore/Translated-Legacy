package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.util.TextUtils;
import net.minecraft.client.gui.screen.inventory.menu.SignEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SignEditScreen.class)
public class SignEditScreenMixin {
    @Redirect(method = "keyPressed", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I"))
    public int fixCharInput(String instance, int ch) {
        return TextUtils.isCharacterAllowed(ch);
    }
}
