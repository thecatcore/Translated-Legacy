package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.ornithe.client.language.LanguageScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.ornithemc.osl.localization.api.L10n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    @Inject(method = "init", at = @At("RETURN"))
    public void init$langButton(CallbackInfo ci) {
        this.buttons.add(new ButtonWidget(999, this.width / 2 - 100, this.height / 6 + 72 + 12, L10n.get("options.language")));
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"))
    protected void buttonClicked$langButton(ButtonWidget arg, CallbackInfo ci) {
        if (arg.active && arg.id == 999) {
            this.minecraft.options.save();
            this.minecraft.openScreen(new LanguageScreen(this));
        }
    }
}
