package fr.catcore.translatedlegacy.mixin.client;

import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.screen.LevelSaveConflictScreen;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LevelSaveConflictScreen.class)
public class LevelSaveConflictScreenMixin extends Screen {

    /**
     * @author CatCore
     */
    @Overwrite
    public void init() {
        TranslationStorage translationStorage = TranslationStorage.getInstance();

        this.buttons.clear();
        this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + 120 + 12, translationStorage.translate("gui.toMenu")));
    }
}
