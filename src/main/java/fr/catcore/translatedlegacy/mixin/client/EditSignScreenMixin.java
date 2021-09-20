package fr.catcore.translatedlegacy.mixin.client;

import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EditSignScreen.class)
public class EditSignScreenMixin extends Screen {

    /**
     * @author CatCore
     */
    @Overwrite
    public void init() {
        TranslationStorage translationStorage = TranslationStorage.getInstance();

        this.buttons.clear();
        Keyboard.enableRepeatEvents(true);
        this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + 120, translationStorage.translate("gui.done")));
    }
}
