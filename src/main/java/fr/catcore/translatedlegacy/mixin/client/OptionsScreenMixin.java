package fr.catcore.translatedlegacy.mixin.client;

import fr.catcore.translatedlegacy.language.LanguageScreen;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {

    @Inject(method = "init", at = @At("RETURN"))
    public void init$langButton(CallbackInfo ci) {
        TranslationStorage storage = TranslationStorage.getInstance();
        this.buttons.add(new Button(102, this.width / 2 - 100, this.height / 6 + 72 + 12, storage.translate("options.language")));
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"))
    protected void buttonClicked$langButton(Button arg, CallbackInfo ci) {
        if (arg.active && arg.id == 102) {
            this.minecraft.options.saveOptions();
            this.minecraft.openScreen(new LanguageScreen(this));
        }
    }
}
