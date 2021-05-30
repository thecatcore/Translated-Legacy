package io.github.minecraftcursedlegacy.example.mixin.client;

import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.screen.PauseScreen;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.util.maths.MathsHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {

    @Shadow private int field_2204;

    /**
     * @author CatCore
     */
    @Overwrite
    public void init() {
        TranslationStorage translationStorage = TranslationStorage.getInstance();

        this.field_2204 = 0;
        this.buttons.clear();
        byte var1 = -16;
        this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4 + 120 + var1, translationStorage.translate("menu.returnToMenu")));
        if (this.minecraft.isConnectedToServer()) {
            ((Button)this.buttons.get(0)).text = translationStorage.translate("menu.disconnect");
        }

        this.buttons.add(new Button(4, this.width / 2 - 100, this.height / 4 + 24 + var1, translationStorage.translate("menu.returnToGame")));
        this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + 96 + var1, translationStorage.translate("menu.options")));
        this.buttons.add(new Button(5, this.width / 2 - 100, this.height / 4 + 48 + var1, 98, 20, I18n.translate("gui.achievements")));
        this.buttons.add(new Button(6, this.width / 2 + 2, this.height / 4 + 48 + var1, 98, 20, I18n.translate("gui.stats")));
    }
}
