package fr.catcore.translatedlegacy.ornithe.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.catcore.translatedlegacy.ornithe.client.language.LanguageScreen;
import fr.catcore.translatedlegacy.ornithe.common.TranslationHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    @Inject(method = "init", at = @At("RETURN"))
    public void init$langButton(CallbackInfo ci) {
        this.buttons.add(new ButtonWidget(999, this.width / 2 - 155, this.height / 6 + 120 + 12, 150, 20, TranslationHelper.get("backport.options.language", "options.language")));
    }

    @Definition(id = "ButtonWidget", type = ButtonWidget.class)
    @Expression("new ButtonWidget(?, ?, ?, ?)")
    @WrapOperation(method = "init", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private ButtonWidget tl$moveControlsButton(int id, int x, int y, String text, Operation<ButtonWidget> original) {
        return new ButtonWidget(id, x + 105, y, 150, 20, text);
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"))
    protected void buttonClicked$langButton(ButtonWidget arg, CallbackInfo ci) {
        if (arg.active && arg.id == 999) {
            this.minecraft.options.save();
            this.minecraft.openScreen(new LanguageScreen(this));
        }
    }
}
